package com.sicredi.desafiovotacao.service.enums;

import lombok.Getter;

public enum StatusVotoEnum {

    ABLE("ABLE_TO_VOTE"),
    UNABLE("UNABLE_TO_VOTE");

    @Getter
    private String descricao;

    StatusVotoEnum(String descricao) {
        this.descricao = descricao;
    }


}
