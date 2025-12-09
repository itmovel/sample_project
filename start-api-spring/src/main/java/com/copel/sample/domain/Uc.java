package com.copel.sample.domain;

public class Uc {
    private final String codigo;
    private final String endereco;

    public Uc(String codigo, String endereco) {
        this.codigo = codigo;
        this.endereco = endereco;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEndereco() {
        return endereco;
    }

    public boolean temContratoMLAtivo() {        
        return false;
    }
}