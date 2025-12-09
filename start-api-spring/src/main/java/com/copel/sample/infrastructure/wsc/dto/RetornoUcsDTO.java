package com.copel.sample.infrastructure.wsc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RetornoUcsDTO {
    
    private UcsContainer ucs;
    private String mensagemRetorno;
    private boolean retornoOK;

    public UcsContainer getUcs() {
        return ucs;
    }

    public void setUcs(UcsContainer ucs) {
        this.ucs = ucs;
    }

    public String getMensagemRetorno() {
        return mensagemRetorno;
    }

    public void setMensagemRetorno(String mensagemRetorno) {
        this.mensagemRetorno = mensagemRetorno;
    }

    public boolean isRetornoOK() {
        return retornoOK;
    }

    public void setRetornoOK(boolean retornoOK) {
        this.retornoOK = retornoOK;
    }

}


