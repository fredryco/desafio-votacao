package com.sicredi.desafiovotacao.controller;

import com.sicredi.desafiovotacao.dto.SessaoRequest;
import com.sicredi.desafiovotacao.dto.SessaoResponse;
import com.sicredi.desafiovotacao.service.SessaoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.function.Predicate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/sessao")
@ApiOperation("API para operações da Sessão")
public class SessaoController {

    private final SessaoService sessaoService;

    @Autowired
    public SessaoController(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    @ApiOperation(value = "Criar sessão de votos", notes = "ID da Pauta deve ser informado")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "NOT FOUND - Pauta não encontrada"),
            @ApiResponse(code = 400, message = "BAD REQUEST - Alguma data invalida"),
            @ApiResponse(code = 201, message = "CREATED - Sessão criada com sucesso")
    })
    @PostMapping
    public ResponseEntity<SessaoResponse> criarSessao(@RequestBody @Valid SessaoRequest sessaoRequest) {
        SessaoResponse sessaoResponse = this.sessaoService.criarSessao(sessaoRequest)
                .orElse(SessaoResponse.criarResponseVazio());

        Optional.of(sessaoResponse.getIdSessao())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> {
                    sessaoResponse.add(linkTo(methodOn(this.getClass())
                            .obterResultadoSessao(id)).withRel("resultado"));
                });

        return new ResponseEntity<>(sessaoResponse, CREATED);
    }

    @GetMapping("/resultado/{id}")
    public ResponseEntity<SessaoResponse.SessaoResponseResult> obterResultadoSessao(@PathVariable(value = "id") String id) {

        SessaoResponse.SessaoResponseResult sessaoResponseResult =
                this.sessaoService.obterResultado(id);

        return new ResponseEntity<>(sessaoResponseResult, HttpStatus.OK);
    }
}
