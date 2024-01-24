package com.sicredi.desafiovotacao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessaoResponse extends RepresentationModel<SessaoResponse> {

    @JsonProperty(value = "id_sessao")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String idSessao;

    private String descricao;

    @JsonProperty(value = "data_inicio_sessao")
    private LocalDateTime dataInicioSessao;

    @JsonProperty(value = "data_fim_sessao")
    private LocalDateTime dataFimSessao;

    public static SessaoResponse of(String id, String message, LocalDateTime startDate, LocalDateTime endDate) {
        return new SessaoResponse(id, message, startDate, endDate);
    }

    public static SessaoResponse criarResponseVazio() {
        return new SessaoResponse();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SessaoResponseResult {

        @JsonProperty(value = "resultado")
        private String resultado;

        @JsonProperty(value = "contagem_sim")
        private int contagemSim;

        @JsonProperty(value = "contagem_nao")
        private int contagemNao;

    }
}

