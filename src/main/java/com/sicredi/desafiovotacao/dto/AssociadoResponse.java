package com.sicredi.desafiovotacao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.sicredi.desafiovotacao.utils.DateUtils;
import com.sicredi.desafiovotacao.service.enums.StatusVotoEnum;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AssociadoResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "id_associado")
    private String idAssociado;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String descricao;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "data_votacao")
    private LocalDateTime dataVotacao;

    private AssociadoResponse(String idAssociado, String voteStatus, String descricao, LocalDateTime dataVotacao) {
        this.idAssociado = idAssociado;
        this.status = voteStatus;
        this.descricao = descricao;
        this.dataVotacao = dataVotacao;
    }

    public static AssociadoResponse of(String idAssociado, StatusVotoEnum statusVotoEnum, String message) {
        return new AssociadoResponse(idAssociado, statusVotoEnum.getDescricao(), message, DateUtils.obterDataAtual());
    }

    public static AssociadoResponse criarResponseFalha() {
        return new AssociadoResponse(null, StatusVotoEnum.UNABLE.getDescricao(), null, null);
    }

}
