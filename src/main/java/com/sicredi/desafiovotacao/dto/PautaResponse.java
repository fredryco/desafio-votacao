package com.sicredi.desafiovotacao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PautaResponse {

    @JsonProperty(value = "id_pauta")
    private String idPauta;

    @JsonProperty(value = "data_criacao")
    private LocalDateTime dataCriacao;

    private String descricao;

    public static PautaResponse of(String idPauta, LocalDateTime dataCriacao, String descricao) {
        return new PautaResponse(idPauta, dataCriacao, descricao);
    }
}
