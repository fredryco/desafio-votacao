package com.sicredi.desafiovotacao.service.sessao;

import com.sicredi.desafiovotacao.BaseContextConfigurationTest;
import com.sicredi.desafiovotacao.dto.SessaoRequest;
import com.sicredi.desafiovotacao.dto.SessaoResponse;
import com.sicredi.desafiovotacao.utils.DateUtils;
import com.sicredi.desafiovotacao.entity.Sessao;
import com.sicredi.desafiovotacao.entity.Pauta;
import com.sicredi.desafiovotacao.service.SessaoService;
import com.sicredi.desafiovotacao.service.exception.EntidadeNaoEncontradaException;
import com.sicredi.desafiovotacao.service.exception.DataInvalidaException;
import com.sicredi.desafiovotacao.repository.SessaoRepository;
import com.sicredi.desafiovotacao.repository.PautaRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.sicredi.desafiovotacao.utils.MessagesConstants.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mockStatic;

public class SessaoServiceTest extends BaseContextConfigurationTest {

    @SpyBean
    private SessaoResponse sessaoResponse;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    private Pauta pauta;

    private boolean isSessionOpen;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void before() {
        pauta = pautaRepository.save(Pauta.builder()
                .dataCriacao(DateUtils.obterDataAtual())
                .assunto("assunto")
                .build());
    }

    @Test
    public void deveCriarSessaoSucesso() {

        LocalDateTime dataAtual = LocalDateTime.of(2023, 3, 20, 12, 30, 30);
        LocalDateTime dataInicial = dataAtual.plus(1, ChronoUnit.MINUTES);
        LocalDateTime dataFinal = dataInicial.plus(1, ChronoUnit.MINUTES);

        executarCriacaoSessao(dataAtual, dataInicial, dataFinal);

        assertNotNull(sessaoResponse);
        assertEquals(SESSAO_CRIADA_SUCESSO, sessaoResponse.getDescricao());
    }

    @Test
    public void deveCriarSessaoSemDataInicial() {
        LocalDateTime dataAtual = LocalDateTime.of(2023, 3, 20, 12, 30, 30);
        LocalDateTime dataFinal = dataAtual.plus(5, ChronoUnit.MINUTES);

        executarCriacaoSessao(dataAtual, null, dataFinal);

        assertNotNull(sessaoResponse);
        assertEquals(SESSAO_CRIADA_SUCESSO, sessaoResponse.getDescricao());
        assertEquals(dataAtual.plus(1, ChronoUnit.MINUTES), sessaoResponse.getDataInicioSessao());
    }

    @Test
    public void deveCriarSessaoSemDataFinal() {
        LocalDateTime dataAtual = LocalDateTime.of(2023, 3, 20, 12, 30, 30);
        LocalDateTime dataInicla = dataAtual.plus(1, ChronoUnit.MINUTES);

        executarCriacaoSessao(dataAtual, dataInicla, null);

        assertNotNull(sessaoResponse);
        assertEquals(SESSAO_CRIADA_SUCESSO, sessaoResponse.getDescricao());
        assertEquals(dataInicla.plus(1, ChronoUnit.MINUTES), sessaoResponse.getDataFimSessao());
    }

    @Test
    public void deveCriarSessaoSemDatas() {
        LocalDateTime dataATual = LocalDateTime.of(2023, 3, 20, 12, 30, 30);

        executarCriacaoSessao(dataATual, null, null);

        assertNotNull(sessaoResponse);
        assertEquals(SESSAO_CRIADA_SUCESSO, sessaoResponse.getDescricao());
        assertEquals(dataATual.plus(1, ChronoUnit.MINUTES), sessaoResponse.getDataInicioSessao());
        assertEquals(sessaoResponse.getDataInicioSessao().plus(1, ChronoUnit.MINUTES), sessaoResponse.getDataFimSessao());
    }

    @Test
    public void deveRetornarExcecaoDataInicialMenorDataAtual() {

        expectedException.expect(DataInvalidaException.class);
        expectedException.expectMessage(DATA_INICIAL_INVALIDA);

        LocalDateTime dataAtual = LocalDateTime.of(2023, 3, 20, 12, 30, 30);
        LocalDateTime dataInicial = dataAtual.minus(1, ChronoUnit.MINUTES);

        executarCriacaoSessao(dataAtual, dataInicial, null);

        assertNull(sessaoResponse);
    }

    @Test
    public void deveRetornarExcecaoDataFinalMenorDataInicial() {

        expectedException.expect(DataInvalidaException.class);
        expectedException.expectMessage(INICIO_SESSAO_DEVE_SER_INFERIOR_A_FIM_SESSAO);

        LocalDateTime dataAtual = LocalDateTime.of(2023, 3, 20, 12, 30, 30);
        LocalDateTime dataInicial = dataAtual.plus(1, ChronoUnit.MINUTES);
        LocalDateTime dataFinal = dataInicial.minus(1, ChronoUnit.MINUTES);

        executarCriacaoSessao(dataAtual, dataInicial, dataFinal);

        assertNull(sessaoResponse);
    }


    @Test
    public void deveRegistrarVotosAFavor() {
        Sessao sessao = montarSessao(DateUtils.obterDataAtual(), DateUtils.obterDataAtual());
        sessaoRepository.save(sessao);

        sessao.appendVote("s");
        sessao.appendVote("S");
        sessao.appendVote("sim");
        sessao.appendVote("SIM");

        sessaoService.atualizarSessao(sessao);

        assertEquals(4, sessao.getContagemSim());
        assertEquals(0, sessao.getContagemNao());
    }

    @Test
    public void deveRegistrarVotosContrarios() {
        Sessao sessao = montarSessao(DateUtils.obterDataAtual(), DateUtils.obterDataAtual());
        sessaoRepository.save(sessao);

        sessao.appendVote("n");
        sessao.appendVote("N");
        sessao.appendVote("não");
        sessao.appendVote("NÃO");

        sessaoService.atualizarSessao(sessao);

        assertEquals(0, sessao.getContagemSim());
        assertEquals(4, sessao.getContagemNao());
    }

    @Test
    public void deveAbrirSessao() {
        LocalDateTime currentDate = LocalDateTime.of(2023, 3, 20, 12, 30, 30);
        LocalDateTime startDate = currentDate.minus(30, ChronoUnit.SECONDS);
        LocalDateTime endDate = startDate.plus(1, ChronoUnit.MINUTES);

        Sessao sessao = montarSessao(startDate, endDate);

        simularAberturaSessao(sessao, currentDate);

        assertTrue(isSessionOpen);
    }

    @Test
    public void deveFecharSessao() {
        LocalDateTime currentDate = LocalDateTime.of(2023, 3, 20, 12, 30, 30);
        LocalDateTime startDate = LocalDateTime.of(2023, 3, 20, 12, 20, 30);
        LocalDateTime endDate = startDate.plus(5, ChronoUnit.MINUTES);

        Sessao sessao = montarSessao(startDate, endDate);

        simularAberturaSessao(sessao, currentDate);

        assertFalse(isSessionOpen);
    }

    @Test
    public void deveRetornarSessaoAposBusca() {
        Sessao sessao = montarSessao(DateUtils.obterDataAtual(), DateUtils.obterDataAtual());
        sessaoRepository.save(sessao);

        Sessao sessionById = simularFindId(sessao.getId());

        assertEquals(sessao.getId(), sessionById.getId());
    }

    @Test
    public void deveRetornarExcecaoAoNaoEncontrarSessao() {

        String sessionId = "asdfa113a9";

        expectedException.expect(EntidadeNaoEncontradaException.class);
        expectedException.expectMessage(String.format(SESSAO_NAO_ENCOTRADA, sessionId));

        Sessao sessionById = simularFindId(sessionId);

        assertNull(sessionById);
    }

    private void executarCriacaoSessao(LocalDateTime dataAtual, LocalDateTime dataInical, LocalDateTime dataFinal) {
        // Necessário mockar a hora do sistema
        try (MockedStatic<DateUtils> dateMock = mockStatic(DateUtils.class)) {
            dateMock.when(DateUtils::obterDataAtual)
                    .thenReturn(dataAtual);

            var sessionRequest = montarSessaoRequest(dataInical, dataFinal);

            this.sessaoService.criarSessao(sessionRequest)
                    .ifPresent(response -> this.sessaoResponse = response);
        }
    }

    private void simularAberturaSessao(Sessao sessao, LocalDateTime currentDate) {
        try (MockedStatic<DateUtils> dateMock = mockStatic(DateUtils.class)) {
            dateMock.when(DateUtils::obterDataAtual)
                    .thenReturn(currentDate);
            isSessionOpen = sessao.isOpen();
        }
    }

    private Sessao simularFindId(String id) {
        return this.sessaoService.findById(id);
    }

    private SessaoRequest montarSessaoRequest(LocalDateTime startDate, LocalDateTime endDate) {
        return new SessaoRequest(this.pauta.getId(), startDate, endDate);
    }

    private Sessao montarSessao(LocalDateTime startDate, LocalDateTime endDate) {
        return Sessao.builder()
                .dataCriacao(DateUtils.obterDataAtual())
                .pauta(this.pauta)
                .dataInicial(startDate)
                .dataFinal(endDate)
                .build();
    }
}
