package com.sicredi.desafiovotacao.controller;

import com.sicredi.desafiovotacao.dto.PautaRequest;
import com.sicredi.desafiovotacao.dto.PautaResponse;
import com.sicredi.desafiovotacao.utils.DateUtils;
import com.sicredi.desafiovotacao.service.PautaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.sicredi.desafiovotacao.utils.MessagesConstants.PAUTA_CRIADA_SUCESSO;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/pauta")
@ApiOperation("API para operações da Pauta")
public class PautaController {

    private final PautaService pautaService;

    private static final String VAZIO = "";

    @Autowired
    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @ApiOperation(value = "Criar uma pauta de votação", notes = "Deve ser informado o assunto")
    @ApiResponse(code = 201, message = "CREATED - Pauta criada com sucesso")
    @PostMapping
    public ResponseEntity<PautaResponse> criarPauta(@RequestBody @Valid PautaRequest pautaRequest) {

        String id = pautaService.criaPauta(pautaRequest).orElse(VAZIO);

        return new ResponseEntity<>(
                PautaResponse.of(id, DateUtils.obterDataAtual(), String.format(PAUTA_CRIADA_SUCESSO, pautaRequest.getAssunto())),
                CREATED);

    }

}
