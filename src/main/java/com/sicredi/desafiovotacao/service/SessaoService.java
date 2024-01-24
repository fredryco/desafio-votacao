package com.sicredi.desafiovotacao.service;

import com.sicredi.desafiovotacao.dto.SessaoRequest;
import com.sicredi.desafiovotacao.dto.SessaoResponse;
import com.sicredi.desafiovotacao.utils.DateUtils;
import com.sicredi.desafiovotacao.entity.Sessao;
import com.sicredi.desafiovotacao.entity.Pauta;
import com.sicredi.desafiovotacao.service.exception.EntidadeNaoEncontradaException;
import com.sicredi.desafiovotacao.service.exception.DataInvalidaException;
import com.sicredi.desafiovotacao.repository.SessaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import static com.sicredi.desafiovotacao.utils.MessagesConstants.*;
import static com.sicredi.desafiovotacao.service.enums.ResultadoSessaoEnum.*;

@Service
@Slf4j
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    @Autowired
    public SessaoService(SessaoRepository sessaoRepository, PautaService pautaService) {
        this.sessaoRepository = sessaoRepository;
        this.pautaService = pautaService;
    }

    public Optional<SessaoResponse> criarSessao(SessaoRequest sessaoRequest) {
        return Optional.of(sessaoRequest)
                .map(this::construirSessao)
                .map(this::persistirSessao)
                .map(this::construirSessaoResponse);
    }

    private Sessao persistirSessao(Sessao sessao) {
        log.info("Persistindo Sessao={}", sessao);
        return this.sessaoRepository.save(sessao);
    }

    private Sessao construirSessao(SessaoRequest sessaoRequest) {

        checarDataSessao(sessaoRequest);
        Pauta pauta = findPautaId(sessaoRequest.getIdPauta());

        return Sessao.of(pauta, sessaoRequest);
    }

    private SessaoResponse construirSessaoResponse(Sessao sessao) {
        return SessaoResponse.of(sessao.getId(), SESSAO_CRIADA_SUCESSO, sessao.getDataInicial(),
                sessao.getDataFinal());
    }

    private Pauta findPautaId(String topicId) {
        return this.pautaService.findById(topicId);
    }

    private void checarDataSessao(SessaoRequest sessaoRequest) {
        LocalDateTime dataInicial = sessaoRequest.getInicioSessao();
        LocalDateTime dataFinal = sessaoRequest.getFimSessao();

        if (Objects.isNull(dataInicial)) {
            dataInicial = DateUtils.obterDataAtual().plus(1, ChronoUnit.MINUTES);
        }

        if (Objects.isNull(dataFinal)) {
            dataFinal = dataInicial.plus(1, ChronoUnit.MINUTES);
        }

        if (dataFinal.isBefore(dataInicial)) {
            throw new DataInvalidaException(INICIO_SESSAO_DEVE_SER_INFERIOR_A_FIM_SESSAO, dataInicial, dataFinal);
        }

        if (dataInicial.isBefore(DateUtils.obterDataAtual())) {
            throw new DataInvalidaException(DATA_INICIAL_INVALIDA, dataInicial, dataFinal);
        }

        sessaoRequest.setInicioSessao(dataInicial);
        sessaoRequest.setFimSessao(dataFinal);
    }

    public SessaoResponse.SessaoResponseResult obterResultado(String sessionId) {
        Sessao sessao = findById(sessionId);

        int countYes = sessao.getContagemSim();
        int countNo = sessao.getContagemNao();

        return SessaoResponse.SessaoResponseResult.builder()
                .contagemSim(countYes)
                .contagemNao(countNo)
                .resultado(obterResultadoSessao(countYes, countNo))
                .build();
    }

    private String obterResultadoSessao(int countYes, int countNo) {
        if (countYes > countNo) {
            return APROVADO.getDescricao();
        } else if (countYes == countNo) {
            return EMPATE.getDescricao();
        }
        return NEGADO.getDescricao();
    }

    public Sessao findById(String sessionId) {
        return this.sessaoRepository.findById(sessionId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format(SESSAO_NAO_ENCOTRADA, sessionId)));
    }

    public void atualizarSessao(Sessao sessao) {
        this.persistirSessao(sessao);
    }
}
