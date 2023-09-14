package org.elypso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;


@SpringBootApplication
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
        System.out.println("Hello elypso!");

        /*
        try {
            String ip = "127.0.0.1";
            int port = 18000;
            char[] data = new char[1024];
            String request = "{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"CMD.SendCommand\",\"params\":{\"command\":\"Rfv\", \"device\":\"Evolis ElypsoCommandsService\", \"timeout\":\"5000\"}}";
            String answer = "";

            Socket socket = new Socket(ip,port);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            byte[] byteRequest = request.getBytes("UTF-8");

            out.write(byteRequest);

            out.flush();

            while( (br.read(data)) != -1 )
            {
                answer = new String(data);
                System.out.println(answer);
            }

            socket.close();
        }
        catch (Exception e)
        {
            System.out.println("Communication failed :\n");
            System.out.println(" - check TCP communication is activated\n");
            System.out.println(" - check the service is activated\n");
            System.out.println(" - check your IP address and port\n");
        }
        */
    }
}