package org.elypso.Dto;

import org.elypso.enumerations.Lado;

public class PedidoDTO {

    private String nome;

    private String numeroCliente;

    private String numeroApolice;

    private String impressora;

    private Lado lado;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(String numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public String getNumeroApolice() {
        return numeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        this.numeroApolice = numeroApolice;
    }

    public String getImpressora() {
        return impressora;
    }

    public void setImpressora(String impressora) {
        this.impressora = impressora;
    }

    public Lado getLado() {
        return lado;
    }

    public void setLado(Lado lado) {
        this.lado = lado;
    }
}
