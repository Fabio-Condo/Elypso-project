package org.elypso.service;

import org.springframework.context.annotation.Configuration;

import java.net.Socket;

@Configuration
public class SocketService {

    public Socket iniciarSocket(){
        try {
            String ip = "localhost";
            int port = 18000;
            Socket socket = new Socket(ip,port);
            //socket.setSoTimeout(5000);
            System.out.println("Connected");
            //socket.close();
            return socket; // Analizar

        } catch (Exception e) {
            System.out.println("Communication failed :\n");
            System.out.println(" - check TCP communication is activated\n");
            System.out.println(" - check the service is activated\n");
            System.out.println(" - check your IP address and port\n");
            return null; // Analizar
        }

    }
}
