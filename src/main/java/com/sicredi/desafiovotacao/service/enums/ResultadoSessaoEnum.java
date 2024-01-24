package com.sicredi.desafiovotacao.service.enums;

import lombok.Getter;

public enum ResultadoSessaoEnum {

    EMPATE("Empate"),
    APROVADO("Aprovado"),
    NEGADO("Negado");

    @Getter
    private String descricao;

    ResultadoSessaoEnum(String descricao) {
        this.descricao = descricao;
    }

}
