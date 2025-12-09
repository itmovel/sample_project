package com.copel.sample.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import com.copel.pcm.model.UcElegivel;
import com.copel.sample.domain.Uc;

@Component
public class UcApiMapper {

    public UcElegivel toUcElegivel(Uc uc) {
        if ( uc == null ) {
            return null;
        }

        UcElegivel ucElegivel = new UcElegivel();

        ucElegivel.setCodigo( uc.getCodigo() );
        ucElegivel.setEndereco( uc.getEndereco() );

        ucElegivel.setContratoMLAtivo( uc.temContratoMLAtivo() );

        return ucElegivel;
    }
}