package com.copel.sample.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.copel.pcm.model.ListaUcsResponse;
import com.copel.pcm.model.UcElegivel;
import com.copel.sample.adapter.in.web.mapper.UcApiMapper;
import com.copel.sample.application.exception.ClienteNaoEncontradoException;
import com.copel.sample.application.exception.LimiteUcsExcedidoException;
import com.copel.sample.application.port.in.ListarUcsElegiveisUseCase;
import com.copel.sample.application.port.out.UcRepository;

@Service
public class ListarUcsElegiveisService implements ListarUcsElegiveisUseCase {

    private final UcRepository ucRepository;
    private final UcApiMapper ucApiMapper;

    public ListarUcsElegiveisService(UcRepository ucRepository, UcApiMapper ucApiMapper) {
        this.ucRepository = ucRepository;
        this.ucApiMapper = ucApiMapper;
    }

    @Override
    public ListaUcsResponse execute(String documento, int limiteUcs) {
        var ucsEncontradas = ucRepository.buscarPorDocumento(documento);

        List<UcElegivel> ucsElegiveis = ucsEncontradas.stream()
                .map(ucApiMapper::toUcElegivel)
                .toList();
        
        if (ucsElegiveis.isEmpty()) {
            throw new ClienteNaoEncontradoException("Nenhuma UC encontrada para o documento " + documento);
        }
        
        if (ucsElegiveis.size() > limiteUcs) {
            throw new LimiteUcsExcedidoException("O cliente possui " + ucsElegiveis.size() + " UCs, excedendo o limite de " + limiteUcs, limiteUcs);
        }

        return new ListaUcsResponse().dados(ucsElegiveis);
    }
}