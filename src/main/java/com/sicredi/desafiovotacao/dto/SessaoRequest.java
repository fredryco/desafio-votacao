package com.sicredi.desafiovotacao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SessaoRequest {

    @NotBlank(message = "pauta_id não deve ser vazio")
    @JsonProperty(value = "pauta_id")
    @ApiModelProperty(notes = "Id da pauta", example = "968c3966c51011edafa10242ac120002", required = true)
    private String idPauta;

    @JsonProperty(value = "inicio_sessao")
    @ApiModelProperty(notes = "Data de abertuda da sessão", example = "2023-17-20T20:30:55")
    private LocalDateTime inicioSessao;

    @JsonProperty(value = "fim_sessao")
    @ApiModelProperty(notes = "Data de encerramento da sessão", example = "2023-17-20T20:35:55")
    private LocalDateTime fimSessao;
}
