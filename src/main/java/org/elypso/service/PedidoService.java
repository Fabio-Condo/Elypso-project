package org.elypso.service;

import org.elypso.domain.Pedido;
import org.elypso.domain.PrinterCenterResponse;
import org.elypso.enumerations.Fita;
import org.elypso.enumerations.Lado;
import org.elypso.exception.domain.FileNotFoundException;
import org.elypso.exception.domain.NomeOuNumeroVazioException;
import org.elypso.exception.domain.PedidoComandoException;
import org.elypso.repository.filter.PedidoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PedidoService {
    void imprimirEmBulk(MultipartFile file, String impressora, Lado lado) throws IOException;

    Pedido executarOperacaoUnica(Pedido pedido) throws IOException, PedidoComandoException, NomeOuNumeroVazioException, FileNotFoundException;

    Pedido salvarPedido(Pedido pedido);

    Page<Pedido> filter(PedidoFilter pedidoFilter, Pageable pageable);

    List<Pedido> filterForExcel(PedidoFilter pedidoFilter, Pageable pageable);

    PrinterCenterResponse iniciarSequencia(String impressora) throws IOException;

    PrinterCenterResponse inicializarProcessoImpressao(String impressora, String sessao) throws IOException;

    PrinterCenterResponse configurarProcessoImpressao(String impressora, Fita fita, String sessao) throws IOException;

    PrinterCenterResponse definirBitmapImpressaoFrontal(Pedido pedido) throws NomeOuNumeroVazioException, IOException, FileNotFoundException;

    PrinterCenterResponse definirBitmapImpressaoFrontalBrancaUsadaCasoNaoSejaEscolhidoLadoFrontal(String sessao) throws IOException, FileNotFoundException;

    PrinterCenterResponse definirBitmapImpressaoTrazeiro(String sessao) throws IOException, FileNotFoundException;

    PrinterCenterResponse realizarImpressao(String sessao) throws IOException;

    PrinterCenterResponse finalizarImpressao(String sessao) throws IOException;

    PrinterCenterResponse finalizarSequencia(String impressora) throws IOException;

    PrinterCenterResponse verificarStatus(String impressora) throws IOException;

    PrinterCenterResponse verificarFita(String impressora) throws IOException;

    PrinterCenterResponse getEvent(String impressora) throws IOException;

    PrinterCenterResponse setEvent(String erro, String impressora) throws IOException;

    PrinterCenterResponse ligarOuReinicializarHardwareImpressora(String impressora) throws IOException;

    PrinterCenterResponse reinicializarComunicacoesComAImpressora(String impressora) throws IOException;

    PrinterCenterResponse enviarPedidoViaSocket(String request) throws IOException;

    void verificarEventoImpressoraELimparErro(Pedido pedido);

    void analisarErroOuRespostaRetornadaPeloPrinterCenter(PrinterCenterResponse printerCenterResponse, int indiceComando, Pedido pedido) throws PedidoComandoException, IOException;
}
