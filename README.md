# ⚽ Footmatch API

Bem-vindo à **Footmatch API**, uma robusta aplicação Spring Boot projetada para gerenciar dados de partidas de futebol, incluindo clubes, estádios e estatísticas de confrontos. A API utiliza uma arquitetura moderna e desacoplada com Apache Kafka para garantir alta performance e resiliência.

---

## ✨ Principais Funcionalidades

- **Gerenciamento de Entidades**: CRUD completo para Clubes, Estádios e Partidas.
- **Processamento Assíncrono**: Utiliza Kafka para processar a criação e atualização de entidades em segundo plano, proporcionando uma resposta de API mais rápida.
- **Sistema de Alertas com DLT**: Monitora tópicos de Dead Letter (DLT) no Kafka e envia notificações por e-mail quando um número configurável de mensagens falha, permitindo uma análise proativa de erros.
- **Validação Robusta**: Regras de negócio e validação de dados para garantir a integridade das informações.
- **Documentação Interativa**: API totalmente documentada com Swagger (OpenAPI 3).

---

## 💻 Tecnologias Utilizadas

| Categoria         | Tecnologia                                                                                                 |
| ----------------- | ---------------------------------------------------------------------------------------------------------- |
| **Backend**       | Java 21, Spring Boot 3, Spring Data JPA, Hibernate                                                         |
| **Mensageria**    | Apache Kafka                                                                                               |
| **Banco de Dados**| MySQL                                                                                                      |
| **Testes**        | JUnit 5, Mockito                                                                                           |
| **Documentação**  | SpringDoc OpenAPI (Swagger)                                                                                |
| **Container**     | Docker, Docker Compose                                                                                     |
| **Build**         | Maven                                                                                                      |

---

## 🚀 Como Executar (Ambiente de Desenvolvimento)

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

### ✅ Pré-requisitos

- **Java 21** (ou superior)
- **Docker** e **Docker Compose**
- **Maven**

### 🛠️ Passos para Execução

1.  **Clone o repositório:**
    ```sh
    git clone https://github.com/vinib1903/footmatch-api.git
    cd footmatch-api
    ```

2.  **Configure as Variáveis de Ambiente:**
    O projeto utiliza um arquivo `.env` para gerenciar segredos e configurações locais. Crie o seu a partir do template fornecido:

    ```sh
    cp .env.example .env
    ```

    Agora, **edite o arquivo `.env`** e preencha os valores, especialmente `MAIL_USERNAME` e `MAIL_PASSWORD`.

3.  **Inicie a Infraestrutura com Docker Compose:**
    O `docker-compose.yml` na raiz do projeto irá configurar e iniciar todos os serviços necessários (MySQL e Kafka).

    ```sh
    docker-compose up -d
    ```
    O `-d` executa os contêineres em modo "detached" (em segundo plano).

4.  **Execute a Aplicação Spring Boot:**
    Você pode executar a aplicação diretamente pelo seu IDE (como IntelliJ IDEA) ou via Maven:

    ```sh
    ./mvnw spring-boot:run
    ```

    A aplicação estará disponível em `http://localhost:8080`.

### 🛑 Parando os Serviços

Para parar todos os contêineres da infraestrutura, execute:

```sh
docker-compose down
```

---

## ⚙️ Configuração de Ambiente

As seguintes variáveis de ambiente são utilizadas pelo projeto e devem ser definidas no arquivo `.env` para o ambiente de desenvolvimento.

| Variável                  | Descrição                                                                                             |
| ------------------------- | ----------------------------------------------------------------------------------------------------- |
| `DB_URL`                  | URL de conexão JDBC para o banco de dados MySQL.                                                      |
| `DB_USERNAME`             | Nome de usuário para o banco de dados.                                                                |
| `DB_PASSWORD`             | Senha para o banco de dados.                                                                          |
| `MAIL_USERNAME`           | Seu endereço de e-mail do Gmail usado para enviar notificações.                                       |
| `MAIL_PASSWORD`           | **Senha de App** gerada na sua conta Google para permitir o envio de e-mails. **Não use sua senha principal.** |
| `NOTIFICATION_EMAIL_TO`   | Endereço de e-mail que receberá os alertas de falhas da DLT.                                          |

---

## 🏛️ Arquitetura

A Footmatch API adota uma arquitetura de microsserviços orientada a eventos, utilizando o Apache Kafka como broker de mensagens para desacoplar as operações.

### Fluxo de Processamento Assíncrono

1.  **Requisição**: O cliente envia uma requisição para um endpoint (ex: `POST /api/v1/clubes`).
2.  **Validação e Produção**: O Controller valida a requisição e, em vez de salvar diretamente no banco, produz uma mensagem com os dados para um tópico Kafka (ex: `clubes-criacao`).
3.  **Consumo e Persistência**: Um `Consumer` Kafka escuta o tópico, processa a mensagem e persiste a entidade no banco de dados.

Este padrão melhora a latência da API e aumenta a resiliência do sistema.

### Sistema de Dead Letter Topic (DLT)

- **Detecção de Falhas**: Se um consumidor não consegue processar uma mensagem (devido a um erro de validação, bug, etc.), a mensagem é automaticamente enviada para um tópico de "cartas mortas" (DLT) correspondente.
- **Monitoramento e Alerta**: Uma tarefa agendada (`KafkaTopicMonitorTask`) verifica periodicamente os tópicos DLT. Se o número de mensagens em uma DLT atinge um limite configurado (`kafka.dlt.message-threshold`), um relatório detalhado é gerado e enviado por e-mail para o endereço em `NOTIFICATION_EMAIL_TO`.

---

## 📚 Documentação da API (Swagger)

A API é totalmente documentada usando SpringDoc OpenAPI. Com a aplicação em execução, você pode acessar a interface interativa do Swagger em:

[**http://localhost:8080/swagger-ui.html**](http://localhost:8080/swagger-ui.html)

Nesta interface, você pode visualizar todos os endpoints, seus parâmetros, e testá-los diretamente.

