package com.alee.gera;

import com.alee.gera.websocket.VoiceCommunicationServer;
import com.alee.gera.websocket.WebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeraApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeraApplication.class, args);
    }

}
