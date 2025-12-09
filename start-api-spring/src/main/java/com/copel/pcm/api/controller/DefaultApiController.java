package com.copel.pcm.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.copel.pcm.controller.DefaultApi;
import com.copel.pcm.model.ListaUcsResponse;
import com.copel.sample.application.port.in.ListarUcsElegiveisUseCase;

import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-15T10:38:25.320651600-03:00[America/Sao_Paulo]", comments = "Generator version: 7.15.0")
@Controller
public class DefaultApiController implements DefaultApi {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ListarUcsElegiveisUseCase listarUcsElegiveisUseCase;

    public DefaultApiController(ListarUcsElegiveisUseCase listarUcsElegiveisUseCase) {
        this.listarUcsElegiveisUseCase = listarUcsElegiveisUseCase;
    }


    @Override
    public ResponseEntity<ListaUcsResponse> listarUcsElegiveisParaContratacao(@NotNull String documento,
            @NotNull @Min(1) @Valid Integer limiteUcs) {            
        log.info("Início listarUcsElegiveisParaContratacao");

        ListaUcsResponse response = listarUcsElegiveisUseCase.execute(documento, limiteUcs);
        
       log.info("Fim listarUcsElegiveisParaContratacao - {} UCs encontradas", response.getDados().size());
        return ResponseEntity.ok(response);
    }

}
