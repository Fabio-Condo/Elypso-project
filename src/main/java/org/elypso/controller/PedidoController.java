package org.elypso.controller;

import org.elypso.Dto.PedidoDTO;
import org.elypso.domain.HttpResponse;
import org.elypso.domain.PrinterCenterResponse;
import org.elypso.domain.Pedido;
import org.elypso.enumerations.Fita;
import org.elypso.enumerations.Lado;
import org.elypso.exception.domain.FileNotFoundException;
import org.elypso.exception.domain.NomeOuNumeroVazioException;
import org.elypso.exception.domain.PedidoComandoException;
import org.elypso.repository.filter.PedidoFilter;
import org.elypso.service.PedidoServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = {"/elypso"})
public class PedidoController {

    private final String sessao = "JOB000001"; // Usar esta sessao para testar os endpoints/sequencia dos endpoints

    PedidoServiceImpl pedidoServiceImpl;

    public PedidoController(PedidoServiceImpl pedidoServiceImpl) {
        this.pedidoServiceImpl = pedidoServiceImpl;
    }

    @GetMapping("/filter")
    public Page<Pedido> filter(PedidoFilter pedidoFilter, Pageable pageable) {
        return pedidoServiceImpl.filter(pedidoFilter, pageable);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> imprimirEmBulk(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("impressora") String impressora,
                                                 @RequestParam("lado") Lado lado) throws IOException {

        pedidoServiceImpl.imprimirEmBulk(file, impressora, lado);
        return response(HttpStatus.OK, "Dados dos clientes impressos com sucesso!");
    }

    @PostMapping("/executarOperacaoUnica")
    public ResponseEntity<Pedido> executarOperacaoUnica(@RequestBody PedidoDTO pedidoDTO) throws IOException, PedidoComandoException, NomeOuNumeroVazioException, FileNotFoundException {
        Pedido pedido = new Pedido(pedidoDTO.getNome(), pedidoDTO.getNumeroCliente(), pedidoDTO.getNumeroApolice(), pedidoDTO.getImpressora(), pedidoDTO.getLado());
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.executarOperacaoUnica(pedido));
    }

    @GetMapping("/iniciarSequencia/{impressora}")
    public ResponseEntity<PrinterCenterResponse> iniciarSequencia(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.iniciarSequencia(impressora));
    }

    @GetMapping("/inicializarProcessoImpressao/{impressora}")
    public ResponseEntity<PrinterCenterResponse> inicializarProcessoImpressao(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.inicializarProcessoImpressao(impressora, sessao));
    }

    @GetMapping("/configurarProcessoImpressao/{impressora}/{fita}")
    public ResponseEntity<PrinterCenterResponse> configurarProcessoImpressao(@PathVariable("impressora") String impressora, @PathVariable("fita") Fita fita) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.configurarProcessoImpressao(impressora, fita, sessao));
    }

    @GetMapping("/definirBitmapImpressaoFrontal")
    public ResponseEntity<PrinterCenterResponse> definirBitmapImpressaoFrontal(@RequestBody Pedido pedido) throws IOException, NomeOuNumeroVazioException, FileNotFoundException {
        pedido.setSessao(sessao); // Usar a sessao criada
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.definirBitmapImpressaoFrontal(pedido));
    }

    @GetMapping("/definirBitmapImpressaoTrazeiro")
    public ResponseEntity<PrinterCenterResponse> definirBitmapImpressaoTrazeiro() throws IOException, FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.definirBitmapImpressaoTrazeiro(sessao));
    }

    @GetMapping("/realizarImpressao")
    public ResponseEntity<PrinterCenterResponse> realizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.realizarImpressao(sessao));
    }

    @GetMapping("/finalizarImpressao")
    public ResponseEntity<PrinterCenterResponse> finalizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.finalizarImpressao(sessao));
    }

    @GetMapping("/finalizarSequencia/{impressora}")
    public ResponseEntity<PrinterCenterResponse> finalizarSequencia(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.finalizarSequencia(impressora));
    }

    @GetMapping("/ligarOuReinicializarHardwareImpressora/{impressora}")
    public ResponseEntity<PrinterCenterResponse> ligarOuReinicializarHardwareImpressora(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.ligarOuReinicializarHardwareImpressora(impressora));
    }

    @GetMapping("/status/{impressora}")
    public ResponseEntity<PrinterCenterResponse> verificarStatus(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.verificarStatus(impressora));
    }

    @GetMapping("/reinicializarComunicacao/{impressora}")
    public ResponseEntity<PrinterCenterResponse> reinicializarComunicacoesComAImpressora(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.reinicializarComunicacoesComAImpressora(impressora));
    }

    @GetMapping("/verificarFita/{impressora}")
    public ResponseEntity<PrinterCenterResponse> verificarFita(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.verificarFita(impressora));
    }

    @GetMapping("/getEvent/{impressora}")
    public ResponseEntity<PrinterCenterResponse> getEvent(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoServiceImpl.getEvent(impressora));
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
                httpStatus);
    }
}
