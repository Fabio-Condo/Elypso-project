package org.elypso.commandsService;

import org.elypso.enumerations.Fita;
import org.springframework.stereotype.Service;

@Service
public class ElypsoCommandsService {

    String timeout = "5000";

    public String gerarComandoIniciarSequencia(String impressora) {
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

    public String gerarComandoInicializarProcessoImpressao(String impressora, String sessao) {
        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.Begin\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"device\":\"" + impressora + "\",\n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}\n";
    }

    public String gerarComandoConfigurarProcessoImpressao(String impressora, Fita fitaSelecionada, String sessao) {
        if (impressora.contains("Primacy")) {
            return "{\n" +
                    "\t\"jsonrpc\":\"2.0\",\n" +
                    "\t\"id\":\"1\",\n" +
                    "\t\"method\":\"PRINT.Set\",\n" +
                    "\t\"params\":\n" +
                    "\t{\n" +
                    "\t\t\"data\":\"FColorBrightness=VAL20;FColorContrast=VAL20;GRibbonType=" + fitaSelecionada + ";Duplex=NONE;GDuplexType=DUPLEX_MM;IGIQLACC=VAL10;IGIQLACM=VAL10;IGIQLACY=VAL10;GSmoothing=ADVSMOOTH;Resolution=DPI600300\",\n" +
                    "\t\t\"session\":\"" + sessao + "\",\n" +
                    "\t\t\"timeout\":\"" + timeout + "\"\n" +
                    "\t}\n" +
                    "}";
        } else {
            return "{\n" +
                    "\t\"jsonrpc\":\"2.0\",\n" +
                    "\t\"id\":\"1\",\n" +
                    "\t\"method\":\"PRINT.Set\",\n" +
                    "\t\"params\":\n" +
                    "\t{\n" +
                    "\t\t\"data\":\"FColorBrightness=VAL20;FColorContrast=VAL20;GRibbonType=" + fitaSelecionada + ";Duplex=NONE;IGIQLACC=VAL10;IGIQLACM=VAL10;IGIQLACY=VAL10;GSmoothing=ADVSMOOTH;Resolution=DPI600300\",\n" +
                    "\t\t\"timeout\":\"" + timeout + "\",\n" +
                    "\t\t\"session\":\"" + sessao + "\"\n" +
                    "\t}\n" +
                    "}";
        }
    }

    public String gerarComandoDefinirBitmapImpressaoFrontal(String dadosBase64, String sessao) {

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
                "                \"panel\":\"color\",\n" +
                "                \"data\":\"" + dadosBase64 + "\"\n" +
                "    }\n" +
                "}";

    }

    public String gerarComandoDefinirBitmapImpressaoTrazeiro(String dadosBase64, String sessao) {

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

    public String gerarComandoRealizarImpressao(String sessao) {
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

    public String gerarComandoFinalizarImpressao(String sessao) {
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

    public String gerarComandoFinalizarSequencia(String impressora) {
        String com = "Se";

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

    public String gerarComandosLigarOuReinicializarHardwareImpressora(String impressora) {
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

    public String gerarComandoVerificarStatusImpressora(String impressora) {

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

    public String gerarComandoReinicializarComunicacoesComAImpressora(String impressora) {
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

    public String gerarComandoVerificarFita(int opcao, String impressora) {

        String comando1 = "Rkt;type;";
        String comando2 = "Rkt;label;";

        if (opcao == 1) {
            return "{\n" +
                    "\t\"jsonrpc\":\"2.0\",\n" +
                    "\t\"id\":\"1\",\n" +
                    "\t\"method\":\"CMD.SendCommand\",\n" +
                    "\t\"params\":\n" +
                    "\t{\n" +
                    "\t\t\"command\":\"" + comando1 + "\",\n" +
                    "\t\t\"device\":\"" + impressora + "\", \n" +
                    "\t\t\"timeout\":\"" + timeout + "\"\n" +
                    "\t}\n" +
                    "}";
        } else {
            return "{\n" +
                    "\t\"jsonrpc\":\"2.0\",\n" +
                    "\t\"id\":\"1\",\n" +
                    "\t\"method\":\"CMD.SendCommand\",\n" +
                    "\t\"params\":\n" +
                    "\t{\n" +
                    "\t\t\"command\":\"" + comando2 + "\",\n" +
                    "\t\t\"device\":\"" + impressora + "\", \n" +
                    "\t\t\"timeout\":\"" + timeout + "\"\n" +
                    "\t}\n" +
                    "}";
        }
    }

    /**
     * Realiza a verificacao da impressora, caso haja um erro ou aviso relacionado a uma impressora especifica apos a realizacao do fluxo de impressao(Print.Begin ate Print.Print)
     */
    public String criarComandoGetEvent(String impressora) {
        return "{\n" +
                " \"id\": \"1\",\n" +
                " \"jsonrpc\": \"2.0\",\n" +
                " \"method\": \"SUPERVISION.GetEvent\",\n" +
                " \"params\": {\n" +
                " \"device\": \"" + impressora + "\",\n" +
                " \"timeout\":\"" + timeout + "\"\n" +
                " }\n" +
                "}";
    }


    /**
     * Realiza o cancelamento do erro, caso a impressora tenha algum erro associada a mesma naquele momento, +erro ou aviso relacionados a uma impressora especifica apos a realizacao do fluxo de impressao(Print.Begin ate Print.Print)
     */
    public String criarComandoSetEvent(String erro, String impressora) {
        return "{\n" +
                " \"id\": \"1\",\n" +
                " \"jsonrpc\": \"2.0\",\n" +
                " \"method\": \"SUPERVISION.SetEvent\",\n" +
                " \"params\": {\n" +
                " \"action\": \"" + erro + ":CANCEL\",\n" +
                " \"device\": \"" + impressora + "\",\n" +
                " \"timeout\":\"" + timeout + "\"\n" +
                " }\n" +
                "}";
    }


}
