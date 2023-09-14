package org.elypso.commands;

import org.springframework.stereotype.Service;

@Service
public class ElypsoCommandsService {

    String impressora = "Evolis Elypso";
    String timeout = "5000";
    //String fitaSelecionada = "RM_KO";
    String fitaSelecionada = "RC_YMCKO"; // Colorida
    String sessao = "JOB000001";

    public String gerarComandoIniciarSequencia() {
        String com = "Ss";

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"CMD.SendCommand\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"command\":\"" + com + "\",\n" +
                "\t\t\"device\":\"" + impressora + "\", \n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";
    }

    public String gerarComandoInicializarProcessoImpressao() {
        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.Begin\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"device\":\"" + impressora + "\",\n" +
                //"\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}\n";
    }

    public String gerarComandoConfigurarProcessoImpressao() {

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.Set\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"data\":\"GRibbonType=" + fitaSelecionada + ";Duplex=NONE;\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\",\n" +
                "\t\t\"session\":\"" + sessao + "\"\n" +
                "\t}\n" +
                "}";

    }

    public String gerarComandoDefinirBitmapImpressaoFrontal(String dadosBase64) {

        dadosBase64 = "base64:" + dadosBase64;

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.SetBitmap\",\n" +
                "\t\"params\":\n" +
                "\t{ \n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\",\n" +
                "                \"face\":\"front\",\n" +
                "                \"panel\":\"resin\",\n" +
                "                \"data\":\"" + dadosBase64 + "\"\n" +
                "    }\n" +
                "}";

    }

    public String gerarComandoDefinirBitmapImpressaoTrazeiro(String dadosBase64) {

        dadosBase64 = "base64:" + dadosBase64;

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.SetBitmap\",\n" +
                "\t\"params\":\n" +
                "\t{ \n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\",\n" +
                "                \"face\":\"back\",\n" +
                "                \"panel\":\"color\",\n" +
                "                \"data\":\"" + dadosBase64 + "\"\n" +
                "    }\n" +
                "}";

    }

    public String gerarComandoRealizarImpressao() {
        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.Print\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";
    }

    public String gerarComandoFinalizarImpressao() {
        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.End\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";
    }

    public String gerarComandosLigarOuReinicializarHardwareImpressora() {
        String com = "Srs";

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"CMD.SendCommand\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"command\":\"" + com + "\",\n" +
                "\t\t\"device\":\"" + impressora + "\", \n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";

    }

    public String gerarComandoVerificarStatusImpressora() {

        return "{\r\n" +
                "	\"jsonrpc\":\"2.0\",\r\n" +
                "	\"id\":\"1\",\r\n" +
                "	\"method\":\"SUPERVISION.List\",\r\n" +
                "	\"params\":\r\n" +
                "	{\r\n" +
                "		\"device\":\"" + impressora + "\", \n" +
                "       \"timeout\":\"" + timeout + "\",\n" +
                "        \"level\":\"2\"\r\n" +
                "	}\r\n" +
                "}\r\n" +
                "";
    }

    public String gerarComandoReinicializarComunicacoesComAImpressora() {
        return "{\n" +
                " \"id\": \"1\",\n" +
                " \"jsonrpc\": \"2.0\",\n" +
                " \"method\": \"CMD.ResetCom\",\n" +
                " \"params\": {\n" +
                " \"device\": \"" + impressora + "\"\n" +
                " \"timeout\": \"" + timeout + "\",\n" +
                " }\n" +
                "}\n";
    }
}