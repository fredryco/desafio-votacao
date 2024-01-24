package com.sicredi.desafiovotacao.service;

import com.sicredi.desafiovotacao.dto.PautaRequest;
import com.sicredi.desafiovotacao.utils.DateUtils;
import com.sicredi.desafiovotacao.entity.Pauta;
import com.sicredi.desafiovotacao.service.exception.EntidadeNaoEncontradaException;
import com.sicredi.desafiovotacao.repository.PautaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sicredi.desafiovotacao.utils.MessagesConstants.PAUTA_NAO_ENCONTRADA;

@Service
@Slf4j
public class PautaService {

    private final PautaRepository pautaRepository;

    @Autowired
    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Optional<String> criaPauta(PautaRequest pautaRequest) {
        return Optional.of(pautaRequest)
                .map(this::construirObjetoPauta)
                .map(this::persistirPauta)
                .map(Pauta::getId);
    }

    private Pauta persistirPauta(Pauta pauta) {
        log.info("Persistindo Pauta={}", pauta);

        try {
            return this.pautaRepository.save(pauta);
        } catch (Exception e) {
            throw new RuntimeException("Error ao persistir Pauta", e);
        }
    }

    private Pauta construirObjetoPauta(PautaRequest pautaRequest) {
        return Pauta.builder()
                .assunto(pautaRequest.getAssunto())
                .dataCriacao(DateUtils.obterDataAtual())
                .build();
    }

    public Pauta findById(String idPauta) {
        return this.pautaRepository.findById(idPauta).orElseThrow(() ->
                new EntidadeNaoEncontradaException(String.format(PAUTA_NAO_ENCONTRADA, idPauta)));
    }

}
