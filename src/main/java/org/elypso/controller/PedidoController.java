package org.elypso.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elypso.dto.PedidoDTO;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

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

    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> gerarExcelPedidos(PedidoFilter pedidoFilter, Pageable pageable) {

        List<Pedido> pedidos = pedidoServiceImpl.filterForExcel(pedidoFilter, pageable);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clientes");

            // Estilo para o cabeçalho com fundo azul e cor da fonte preta
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFont(headerFont);

            // Alinhamento centralizado para as células do cabeçalho
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            Cell cellNome = headerRow.createCell(0);
            cellNome.setCellValue("Nome no Cartão");
            cellNome.setCellStyle(headerStyle);

            Cell cellNumeroClientel = headerRow.createCell(1);
            cellNumeroClientel.setCellValue("Nº do Cliente");
            cellNumeroClientel.setCellStyle(headerStyle);

            Cell cellNumeroApolice = headerRow.createCell(2);
            cellNumeroApolice.setCellValue("APÓLICE Nº");
            cellNumeroApolice.setCellStyle(headerStyle);

            Cell cellImpressora = headerRow.createCell(3);
            cellImpressora.setCellValue("Impressora");
            cellImpressora.setCellStyle(headerStyle);

            Cell cellFita = headerRow.createCell(4);
            cellFita.setCellValue("Fita");
            cellFita.setCellStyle(headerStyle);

            Cell cellLad = headerRow.createCell(5);
            cellLad.setCellValue("Lado");
            cellLad.setCellStyle(headerStyle);

            Cell cellData = headerRow.createCell(6);
            cellData.setCellValue("Data");
            cellData.setCellStyle(headerStyle);

            // Definir a altura do cabeçalho
            headerRow.setHeightInPoints(30); // A altura é definida em pontos (1 ponto = 1/72 polegadas)

            // Dados dos funcionários
            int rowNum = 1;
            for (Pedido pedido : pedidos) {
                Row row = sheet.createRow(rowNum++);

                // Formatar data
                // Define o formato desejado
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                // Formata a data
                String dataFormatada = formato.format(pedido.getDate());

                row.createCell(0).setCellValue(pedido.getNome());
                row.createCell(1).setCellValue(pedido.getNumeroCliente());
                row.createCell(2).setCellValue(pedido.getNumeroApolice());
                row.createCell(3).setCellValue(pedido.getImpressora());
                row.createCell(4).setCellValue(pedidoServiceImpl.getTipoFita(pedido.getFita().toString()));
                row.createCell(5).setCellValue(pedido.getLado().getDescription());
                row.createCell(6).setCellValue(dataFormatada);
            }

            // Configura o tamanho automático das colunas
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Cria o arquivo Excel em um array de bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Configura a resposta HTTP com o arquivo Excel
            byte[] excelBytes = outputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(excelBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=spc-impressoes-safeline.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);

        } catch (IOException e) {
            // Tratamento de erro, caso ocorra alguma exceção
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
