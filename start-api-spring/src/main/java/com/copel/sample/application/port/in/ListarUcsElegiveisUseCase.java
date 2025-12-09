package com.copel.sample.application.port.in;

import com.copel.pcm.model.ListaUcsResponse;

public interface ListarUcsElegiveisUseCase {

    ListaUcsResponse execute(String documento, int limiteUcs);
    
}