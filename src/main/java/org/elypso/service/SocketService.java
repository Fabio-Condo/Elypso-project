package org.elypso.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
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
            LOGGER.info("Evolis Printer center URL: {}:{} [connected]", ip, port);
            return socket;

        } catch (Exception e) {
            LOGGER.error("Failed to establish communication", e);
            LOGGER.error("Possible reasons:");
            LOGGER.error("- Check if TCP communication is activated");
            LOGGER.error("- Check if the service is activated");
            LOGGER.error("- Check your IP address and port");
            return null;
        }

    }
}
