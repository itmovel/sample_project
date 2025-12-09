package com.copel.sample.infrastructure.wsc;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.copel.sample.application.port.out.UcRepository;
import com.copel.sample.domain.Uc;
import com.copel.sample.infrastructure.wsc.dto.RetornoUcsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WSCService implements UcRepository {
    private static final Logger log = LoggerFactory.getLogger(WSCService.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public WSCService(RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${copel.api.wsc.base-url}") String baseUrl,
            @Value("${copel.api.wsc.username}") String username,
            @Value("${copel.api.wsc.password}") String password) {

        // Configuração isolada do Apache HttpClient 5 para permitir chamada GET
        // enviando conteúdo no body devido serviço exposto pelo WSC
        var httpClient = HttpClients.custom()
                .build();

        var factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(Duration.ofSeconds(10)); // Exemplo de timeout

        // Aplica a factory APENAS nesta instância do RestClient
        this.restClient = restClientBuilder
                .requestFactory(factory)
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();

        this.objectMapper = objectMapper;
    }

    private record ClienteUCRequest(String numDocumento, String codUnCons, boolean detUnCons) {
    }

    @Override
    public List<Uc> buscarPorDocumento(String documento) {
        log.info("Iniciando busca síncrona de UCs para o documento: {}", documento);
        var requestBody = new ClienteUCRequest(documento, "", false);

        try {
            // Utilizando method(HttpMethod.GET) permitimos passar o body() mesmo sendo GET
            RetornoUcsDTO retorno = restClient.method(HttpMethod.GET)
                    .uri("/consulta/cliente/ucs/consultar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()                    
                    .onStatus(status -> status.value() == 444, (request, response) -> {                        
                        String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                        log.error("Erro ao buscar UCs. Status: 444, Body: {}", responseBody);
                        try {
                            JsonNode root = objectMapper.readTree(responseBody);
                            String mensagemRetorno = root.path("mensagemRetorno")
                                    .asText("Erro de negócio não especificado.");                            
                            throw new WSCBusinessException(mensagemRetorno);
                        } catch (JsonProcessingException e) {
                            log.error("Falha ao fazer parse do corpo da resposta de erro 444.", e);
                            throw new WSCBusinessException(
                                    "Erro de negócio na API (444), mas o JSON de resposta era inválido.");
                        }
                    })
                    .body(RetornoUcsDTO.class);

            log.info("Busca de UCs concluída com sucesso.");
            if (retorno != null && retorno.getUcs() != null && retorno.getUcs().getListaUcs() != null) {
                return retorno.getUcs().getListaUcs().stream()
                        .map(ucSimpleDTO -> new Uc(ucSimpleDTO.getCodigo(), ucSimpleDTO.getEndereco()))
                        .toList();
            }

            return Collections.emptyList();

        } catch (WSCBusinessException e) {            
            throw e;
        } catch (Exception ex) {            
            log.error("Erro na comunicação síncrona com a API de UCs.", ex);
            throw new WSCServiceException("Erro na comunicação com a API de UCs.", ex);
        }
    }
}