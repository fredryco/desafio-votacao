package com.sicredi.desafiovotacao.service.associado;

import com.sicredi.desafiovotacao.BaseContextConfigurationTest;
import com.sicredi.desafiovotacao.dto.AssociadoRequest;
import com.sicredi.desafiovotacao.entity.Associado;
import com.sicredi.desafiovotacao.entity.Sessao;
import com.sicredi.desafiovotacao.repository.AssociadoRepository;
import com.sicredi.desafiovotacao.service.AssociadoService;
import com.sicredi.desafiovotacao.service.SessaoService;
import com.sicredi.desafiovotacao.service.exception.SessaoEncerradaException;
import org.junit.Test;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class AssociadoServiceTest extends BaseContextConfigurationTest {

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private SessaoService sessaoService;

    @SpyBean
    private AssociadoRepository associadoRepository;

    @Test
    public void realizarVotoSessaoAbertaAssociadoCadastrado() {
        Sessao sessao = new Sessao();
        sessao.setDataInicial(LocalDateTime.of(2024, 01, 23, 0, 0));
        sessao.setDataFinal(LocalDateTime.of(2024, 01, 25, 0, 0));
        Mockito.when(sessaoService.findById(String.valueOf(Mockito.anyInt()))).thenReturn(sessao);

        AssociadoRequest associadoRequest = new AssociadoRequest();
        associadoRequest.setIdSessao("1");

        Mockito.when(associadoRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.of(new Associado()));

        String result = associadoService.realizarVoto(associadoRequest);

        assertNotNull(result);
    }

    @Test
    public void realizarVotoSessaoFechadaExceptionLancada() {
        Sessao sessao = new Sessao();
        sessao.setDataFinal(LocalDateTime.of(2024, 01, 23, 0, 0));
        sessao.setDataInicial(LocalDateTime.of(2024, 01, 25, 0, 0));
        Mockito.when(sessaoService.findById(String.valueOf(Mockito.anyInt()))).thenReturn(sessao);

        AssociadoRequest associadoRequest = new AssociadoRequest();
        associadoRequest.setIdSessao("1");

        assertThrows(SessaoEncerradaException.class, () -> associadoService.realizarVoto(associadoRequest));
    }

}
