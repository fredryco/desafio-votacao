package com.sicredi.desafiovotacao.service.pauta;

import com.sicredi.desafiovotacao.BaseContextConfigurationTest;
import com.sicredi.desafiovotacao.dto.PautaRequest;
import com.sicredi.desafiovotacao.utils.DateUtils;
import com.sicredi.desafiovotacao.entity.Pauta;
import com.sicredi.desafiovotacao.service.PautaService;
import com.sicredi.desafiovotacao.repository.PautaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PautaServiceTest extends BaseContextConfigurationTest {

    @SpyBean
    private PautaRepository pautaRepository;

    @Autowired
    private PautaService pautaService;
    @Test
    public void deveCriarPauta() {
        executarCenario(montarPautaRequest());

        List<Pauta> topicList = (List<Pauta>) this.pautaRepository.findAll();

        assertFalse(topicList.isEmpty());
        assertEquals("subject", topicList.get(0).getAssunto());
    }

    @Test(expected = RuntimeException.class)
    public void naoDeveCriarPautaQuandoRetornarExcecao() {
        doThrow(new RuntimeException())
                .when(pautaRepository).save(any(Pauta.class));

        Optional<String> result = pautaService.criaPauta(montarPautaRequest());

        assertTrue(result.isEmpty());
        verify(this.pautaRepository, never()).save(any(Pauta.class));
    }

    private void executarCenario(PautaRequest pautaRequest) {
        this.pautaService.criaPauta(pautaRequest);
    }

    public PautaRequest montarPautaRequest() {
        return new PautaRequest("subject", null);
    }

}
