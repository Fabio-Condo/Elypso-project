package org.elypso.domain;

public class PrinterCenterResponse {

    private Long id;
    private String jsonrpc;
    private String result;

    private PrinterCenterErrorResponse error;

    public PrinterCenterResponse() {
    }

    public PrinterCenterResponse(Long id, String jsonrpc, String result, PrinterCenterErrorResponse error) {
        this.id = id;
        this.jsonrpc = jsonrpc;
        this.result = result;
        this.error = error;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public PrinterCenterErrorResponse getError() {
        return error;
    }

    public void setError(PrinterCenterErrorResponse error) {
        this.error = error;
    }
}
