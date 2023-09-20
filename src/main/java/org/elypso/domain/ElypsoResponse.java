package org.elypso.domain;

public class ElypsoResponse {

    private Long id;
    private String jsonrpc;
    private String result;

    private ElypsoError error;

    public ElypsoResponse() {
    }

    public ElypsoResponse(Long id, String jsonrpc, String result, ElypsoError error) {
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

    public ElypsoError getError() {
        return error;
    }

    public void setError(ElypsoError error) {
        this.error = error;
    }
}
