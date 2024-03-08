package org.elypso.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.net.Socket;

@Configuration
public class SocketService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public Socket iniciarSocket(){
        try {
            String ip = "localhost";
            //String ip = "192.168.11.44";
            int port = 18000;
            Socket socket = new Socket(ip, port);
            //socket.setSoTimeout(5000);
            LOGGER.info("Evolis Printer center URL: " + ip + ":" + port + " [connected]");
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
