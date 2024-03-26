package org.elypso.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

@Component
public class PrinterCenterConnector {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${printer.ip}")
    private String printerIp;

    @Value("${printer.port}")
    private int printerPort;

    public Socket iniciarSocket() throws IOException {
        try {
            Socket socket = new Socket(printerIp, printerPort);
            LOGGER.info("URL do centro de impressão Evolis: {}:{} [conectado]", printerIp, printerPort);
            return socket;
        } catch (ConnectException e) {
            LOGGER.error("Conexão recusada ao tentar se conectar ao centro de impressão Evolis", e);
            throw new ConnectException("Conexão recusada. Por favor, verifique se o serviço da impressora está ativado e verifique seu endereço IP e porta.");
        } catch (IOException e) {
            LOGGER.error("Falha ao estabelecer comunicação com o centro de impressão Evolis", e);
            LOGGER.error("Possíveis razões:");
            LOGGER.error("- Verifique se a comunicação TCP está ativada");
            LOGGER.error("- Verifique se o serviço está ativado");
            LOGGER.error("- Verifique seu endereço IP e porta");
            throw e;
        }
    }
}
