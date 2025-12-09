package com.copel.sample.infrastructure.wsc.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UcsContainer {

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty("uc")
    private List<UcSimpleDTO> listaUcs = new ArrayList<>();

    public List<UcSimpleDTO> getListaUcs() {
        return listaUcs;
    }

    public void setListaUcs(List<UcSimpleDTO> listaUcs) {
        this.listaUcs = listaUcs;
    }
}
