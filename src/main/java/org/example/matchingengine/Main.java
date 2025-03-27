package org.example.matchingengine;

import org.example.matchingengine.service.OrderBook;
import org.example.matchingengine.service.OrderGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class Main {
   // private static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);


    }



}