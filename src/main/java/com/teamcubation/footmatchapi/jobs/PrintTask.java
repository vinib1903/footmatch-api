package com.teamcubation.footmatchapi.jobs;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PrintTask {

    @Scheduled(fixedRate = 5000)
    @Async
    public void printMessageRate() {
        System.out.println("Essa mensagem sera exibida a cada 5 segundos - " + LocalDateTime.now());
    }

    @Scheduled(fixedDelay = 10000)
    public void printMessageDelay() throws InterruptedException {
        System.out.println("Essa mensagem sera exibida a cada 10 segundos, após a execução da anterior - " + LocalDateTime.now());
        Thread.sleep(5000);
    }

    @Scheduled(cron = "0 0/2 16 * * *")
    public void printMessageCron() {
        System.out.println("Essa mensagem sera exibida a cada 2 minutos das 16:00 - " + LocalDateTime.now());
    }


}
