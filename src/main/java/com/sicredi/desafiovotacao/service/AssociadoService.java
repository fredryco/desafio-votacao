package com.sicredi.desafiovotacao.service;

import com.sicredi.desafiovotacao.dto.AssociadoRequest;
import com.sicredi.desafiovotacao.entity.Associado;
import com.sicredi.desafiovotacao.entity.Sessao;
import com.sicredi.desafiovotacao.repository.AssociadoRepository;
import com.sicredi.desafiovotacao.service.exception.SessaoEncerradaException;
import com.sicredi.desafiovotacao.service.exception.VotoUnicoException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sicredi.desafiovotacao.utils.MessagesConstants.ERROR_FINALIZAR_SESSAO;
import static com.sicredi.desafiovotacao.utils.MessagesConstants.CHECK_VOTO_UNICO;

@Service
@Slf4j
public class AssociadoService {

    private final SessaoService sessaoService;

    private final AssociadoRepository associadoRepository;

    @Autowired
    public AssociadoService(SessaoService sessaoService, AssociadoRepository associadoRepository) {
        this.sessaoService = sessaoService;
        this.associadoRepository = associadoRepository;
    }

    public String realizarVoto(AssociadoRequest associadoRequest) {

        Sessao sessao = this.sessaoService.findById(associadoRequest.getIdSessao());
        String idAssociado;

        if (sessao.isOpen()) {
            idAssociado = realizarAssociacao(associadoRequest, sessao);
            atualizarSessao(sessao, associadoRequest);
        } else {
            throw new SessaoEncerradaException(String.format(ERROR_FINALIZAR_SESSAO, associadoRequest.getIdSessao()));
        }

        return idAssociado;
    }

    private String realizarAssociacao(AssociadoRequest associadoRequest, Sessao session) {
        Optional<Associado> associado = this.obterAssociado(associadoRequest, session);

        if (associado.isEmpty()) {
            return inserirNovoAssociado(associadoRequest, session);
        } else {
            associado
                    .filter(associate -> associate.verificaVotoUnico(session))
                    .orElseThrow(
                            () -> new VotoUnicoException(String.format(CHECK_VOTO_UNICO, session.getId())));

            return associado.get().getId();
        }
    }

    private Optional<Associado> obterAssociado(AssociadoRequest associadoRequest, Sessao session) {
        return Optional.ofNullable(associadoRequest.getId())
                .map(this.associadoRepository::findById)
                .orElse(this.associadoRepository.findByCpf(associadoRequest.getCpf()));
    }

    private String inserirNovoAssociado(AssociadoRequest associadoRequest, Sessao session) {
        Associado associado = Associado.of(associadoRequest, session);
        persistirAssociado(associado);
        return associado.getId();
    }

    private void persistirAssociado(Associado associado) {
        log.info("realizando persistencia do associado={}", associado);
        this.associadoRepository.save(associado);
    }

    private void atualizarSessao(Sessao sessao, AssociadoRequest associadoRequest) {
        log.info("realizando atualizacao da sessao de id={}", sessao.getId());

        sessao.appendVote(associadoRequest.getVoto());
        this.sessaoService.atualizarSessao(sessao);
    }

}
