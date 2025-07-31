# Footmatch API

Bem-vindo à Footmatch API, uma aplicação Spring Boot para gerenciar dados de partidas de futebol, incluindo clubes, estádios e estatísticas de confrontos.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **Kafka**
- **MySQL**
- **Docker**
- **SpringDoc OpenAPI** para documentação da API

## Como Começar

Esta seção guiará você na configuração e execução do projeto em seu ambiente local.

### Pré-requisitos

- **Java 21**
- **Maven**
- **Docker** e **Docker Compose**

### Passos para Execução

1.  **Clone o repositório:**
    ```sh
    git clone https://github.com/vinib1903/footmatch-api.git
    cd footmatch-api
    ```

2.  **Inicie a infraestrutura com Docker Compose:**

    O arquivo `docker-compose.yml` na raiz do projeto irá configurar e iniciar todos os serviços necessários (MySQL, Kafka e Zookeeper). Execute o seguinte comando:

    ```sh
    docker-compose up -d
    ```
    O `-d` executa os contêineres em modo "detached" (em segundo plano).

3.  **Configure a aplicação:**

    As configurações da aplicação em `src/main/resources/application.properties` devem apontar para os serviços que estão rodando no Docker. O arquivo já deve estar configurado, mas verifique se as propriedades abaixo estão corretas:

    ```properties
    # MySQL
    spring.datasource.url=jdbc:mysql://localhost:3306/footmatchdb
    spring.datasource.username=root
    spring.datasource.password=root

    # Kafka
    spring.kafka.bootstrap-servers=localhost:9092
    ```

4.  **Execute a aplicação Spring Boot:**

    ```sh
    ./mvnw spring-boot:run
    ```

    A aplicação estará disponível em `http://localhost:8080`.

### Parando os Serviços

Para parar todos os contêineres da infraestrutura, execute:

```sh
docker-compose down
```

## Testes

Para rodar a suíte de testes automatizados, execute o seguinte comando:

```sh
./mvnw test
```

## Endpoints da API

A API é documentada usando SpringDoc OpenAPI. Com a aplicação em execução, você pode acessar a interface do Swagger em:

`http://localhost:8080/swagger-ui.html`

Abaixo está um resumo dos endpoints disponíveis:

### Clubes (`/api/v1/clubes`)

-   `POST /`: Cria um novo clube.
-   `GET /`: Retorna uma lista paginada de clubes. Pode ser filtrada por `nome`, `siglaEstado` e `ativo`.
-   `GET /{id}`: Retorna um clube específico pelo seu ID.
-   `PUT /{id}`: Atualiza um clube existente.
-   `DELETE /{id}`: Inativa um clube.

### Estádios (`/api/v1/estadios`)

-   `POST /`: Cria um novo estádio.
-   `GET /`: Retorna uma lista paginada de estádios. Pode ser filtrada por `nome`.
-   `GET /{id}`: Retorna um estádio específico pelo seu ID.
-   `PUT /{id}`: Atualiza um estádio existente.

### Partidas (`/api/v1/partidas`)

-   `POST /`: Cria uma nova partida.
-   `GET /`: Retorna uma lista paginada de partidas. Pode ser filtrada por `clubeId`, `estadioId`, `goleada` e `papel` (ex: mandante/visitante).
-   `GET /{id}`: Retorna uma partida específica pelo seu ID.
-   `PUT /{id}`: Atualiza uma partida existente.
-   `DELETE /{id}`: Deleta uma partida.

### Retrospectos (`/api/v1/retrospectos`)

-   `GET /{id}`: Retorna as estatísticas gerais de um clube.
-   `GET /{id}/contra-adversarios`: Retorna o retrospecto de um clube contra todos os seus adversários.
-   `GET /{clubeId}/confrontos-diretos/{adversarioId}`: Retorna o confronto direto entre dois clubes.
-   `GET /ranking`: Retorna um ranking de clubes com base em diferentes critérios (`pontos`, `vitorias`, etc.).

## Comunicação com Kafka

A aplicação utiliza o Kafka para comunicação assíncrona, notificando outros sistemas sobre a criação, atualização e exclusão de entidades. Abaixo estão os tópicos utilizados:

### Tópicos de Clubes
-   `clubes-criacao`: Notifica a criação de um novo clube.
-   `clubes-atualizacao`: Notifica a atualização de um clube existente.
-   `clubes-exclusao`: Notifica a inativação de um clube.

### Tópicos de Estádios
-   `estadios-criacao`: Notifica a criação de um novo estádio.
-   `estadios-atualizacao`: Notifica a atualização de um estádio existente.

### Tópicos de Partidas
-   `partidas-criacao`: Notifica a criação de uma nova partida.
-   `partidas-atualizacao`: Notifica a atualização de uma partida existente.
-   `partidas-exclusao`: Notifica a exclusão de uma partida.
