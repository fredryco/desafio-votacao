package com.sicredi.desafiovotacao.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessagesConstants {

    private static final List<String> opcoesVoto = List.of("SIM", "NÃO", "sim", "não", "s", "n");

    public static final String INICIO_SESSAO_DEVE_SER_INFERIOR_A_FIM_SESSAO = "inicio_sessao deve ser inferior a fim_sessao";
    public static final String DATA_INICIAL_INVALIDA = "inicio_sessao deve ser superior a data atual";
    public static final String PAUTA_NAO_ENCONTRADA = "Pauta %s não encontrada na base";
    public static final String SESSAO_NAO_ENCOTRADA = "Sessão não encontrada na base";
    public static final String ERROR_FINALIZAR_SESSAO = "Sessão {%s} encontra-se encerrada";
    public static final String CHECK_VOTO_UNICO = "Associado já possui voto registrado para a sessão {%s}";
    public static final String VOTO_INVALIDO = String.format("Voto deve ser contabilizado como: %s", opcoesVoto);


    public static final String PAUTA_CRIADA_SUCESSO = "Pauta %s criado com sucesso!";
    public static final String SESSAO_CRIADA_SUCESSO = "Sessão criada com sucesso!";
    public static final String VOTO_SUCESSO = "Voto realizado com sucesso!";

}
