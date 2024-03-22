package org.elypso.controller;

import org.elypso.Dto.PedidoDTO;
import org.elypso.domain.PrinterCenterResponse;
import org.elypso.domain.Pedido;
import org.elypso.enumerations.Fita;
import org.elypso.enumerations.Lado;
import org.elypso.exception.domain.FileNotFoundException;
import org.elypso.exception.domain.NomeOuNumeroVazioException;
import org.elypso.exception.domain.PedidoComandoException;
import org.elypso.service.ElypsoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = {"/elypso"})
public class ElypsoController {

    private final String sessao = "JOB000001"; // Usar esta sessao para testar os endpoints/sequencia dos endpoints

    ElypsoService elypsoService;

    public ElypsoController(ElypsoService elypsoService) {
        this.elypsoService = elypsoService;
    }


    @PostMapping
    public ResponseEntity<Pedido> salvarPedido(@RequestBody Pedido pedido){
        return ResponseEntity.status(HttpStatus.CREATED).body(elypsoService.salvarPedido(pedido));
    }

    @GetMapping
    public Page<Pedido> listarPedidos(@RequestParam(required = false, defaultValue = "") String name, Pageable pageable){
        return elypsoService.listarPedidos(name, pageable);
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> imprimirEmBulk(@RequestParam("file") MultipartFile file, @RequestParam("impressora") String impressora, @RequestParam("lado") Lado lado) throws IOException {
        elypsoService.imprimirEmBulk(file, impressora, lado);
        return ResponseEntity.status(HttpStatus.OK).body("Dados dos clientes impressos com sucesso!");
    }

    @PostMapping("/executarOperacaoUnica")
    public ResponseEntity<Pedido> executarOperacaoUnica(@RequestBody PedidoDTO pedidoDTO) throws IOException, PedidoComandoException, NomeOuNumeroVazioException, FileNotFoundException {
        Pedido pedido = new Pedido(pedidoDTO.getNome(), pedidoDTO.getNumeroCliente(), pedidoDTO.getNumeroApolice(), pedidoDTO.getImpressora(), pedidoDTO.getLado());
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.executarOperacaoUnica(pedido));
    }

    @GetMapping("/iniciarSequencia/{impressora}")
    public ResponseEntity<PrinterCenterResponse> iniciarSequencia(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.iniciarSequencia(impressora));
    }

    @GetMapping("/inicializarProcessoImpressao/{impressora}")
    public ResponseEntity<PrinterCenterResponse> inicializarProcessoImpressao(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.inicializarProcessoImpressao(impressora, sessao));
    }

    @GetMapping("/configurarProcessoImpressao/{impressora}/{fita}")
    public ResponseEntity<PrinterCenterResponse> configurarProcessoImpressao(@PathVariable("impressora") String impressora, @PathVariable("fita") Fita fita) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.configurarProcessoImpressao(impressora, fita, sessao));
    }

    @GetMapping("/definirBitmapImpressaoFrontal")
    public ResponseEntity<PrinterCenterResponse> definirBitmapImpressaoFrontal(@RequestBody Pedido pedido) throws IOException, NomeOuNumeroVazioException, FileNotFoundException {
        pedido.setSessao(sessao); // Usar a sessao criada
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoFrontal(pedido));
    }

    @GetMapping("/definirBitmapImpressaoTrazeiro")
    public ResponseEntity<PrinterCenterResponse> definirBitmapImpressaoTrazeiro() throws IOException, FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoTrazeiro(sessao));
    }

    @GetMapping("/realizarImpressao")
    public ResponseEntity<PrinterCenterResponse> realizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.realizarImpressao(sessao));
    }

    @GetMapping("/finalizarImpressao")
    public ResponseEntity<PrinterCenterResponse> finalizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarImpressao(sessao));
    }

    @GetMapping("/finalizarSequencia/{impressora}")
    public ResponseEntity<PrinterCenterResponse> finalizarSequencia(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarSequencia(impressora));
    }

    @GetMapping("/ligarOuReinicializarHardwareImpressora/{impressora}")
    public ResponseEntity<PrinterCenterResponse> ligarOuReinicializarHardwareImpressora(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.ligarOuReinicializarHardwareImpressora(impressora));
    }

    @GetMapping("/status/{impressora}")
    public ResponseEntity<PrinterCenterResponse> verificarStatus(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.verificarStatus(impressora));
    }

    @GetMapping("/reinicializarComunicacao/{impressora}")
    public ResponseEntity<PrinterCenterResponse> reinicializarComunicacoesComAImpressora(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.reinicializarComunicacoesComAImpressora(impressora));
    }

    @GetMapping("/verificarFita/{impressora}")
    public ResponseEntity<PrinterCenterResponse> verificarFita(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.verificarFita(impressora));
    }

    @GetMapping("/getEvent/{impressora}")
    public ResponseEntity<PrinterCenterResponse> getEvent(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.getEvent(impressora));
    }
}
