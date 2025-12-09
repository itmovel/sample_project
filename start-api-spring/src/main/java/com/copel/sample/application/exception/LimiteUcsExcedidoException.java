package com.copel.sample.application.exception;

public class LimiteUcsExcedidoException extends RuntimeException {

    private final int limite;

    public LimiteUcsExcedidoException(String message, int limite) {
        super(message);
        this.limite = limite;
    }

    public int getLimite() {
        return limite;
    }
}