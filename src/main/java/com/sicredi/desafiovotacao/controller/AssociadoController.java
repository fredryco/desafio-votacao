package com.sicredi.desafiovotacao.controller;

import com.sicredi.desafiovotacao.dto.AssociadoRequest;
import com.sicredi.desafiovotacao.dto.AssociadoResponse;
import com.sicredi.desafiovotacao.driver.cpfapi.CPFApiService;
import com.sicredi.desafiovotacao.service.AssociadoService;
import com.sicredi.desafiovotacao.service.enums.StatusVotoEnum;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

import static com.sicredi.desafiovotacao.utils.MessagesConstants.VOTO_SUCESSO;

@RestController
@RequestMapping("/v1/associado")
@ApiOperation("API para operações do Associado")
public class AssociadoController {

    private final AssociadoService associadoService;

    private final CPFApiService CPFApiService;

    @Autowired
    public AssociadoController(AssociadoService associadoService, CPFApiService CPFApiService) {
        this.associadoService = associadoService;
        this.CPFApiService = CPFApiService;
    }

    @ApiOperation(value = "Registrar voto do associado", notes = "Deve ser informado o Cpf ou o ID,  primeiro voto do associado é cadastrado na base.")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE - Caso a sessão esteja encerrada ou o voto viole a politica de voto único ou caso a validação de CPF falhe."),
            @ApiResponse(code = 404, message = "NOT FOUND - Caso a sessão informada não seja encontrada."),
            @ApiResponse(code = 400, message = "BAD REQUEST - Caso a opção de voto seja diferente de {s, n, sim, não, SIM, NÃO}"),
            @ApiResponse(code = 200, message = "OK - Voto computado com sucesso.")
            })
    @PostMapping
    public ResponseEntity<AssociadoResponse> realizarVoto(@RequestBody @Valid AssociadoRequest associadoRequest) {

        if (Objects.isNull(associadoRequest.getId())) {

            String idAssociado = associadoService.realizarVoto(associadoRequest);
            return new ResponseEntity<>(
                    AssociadoResponse.of(idAssociado, StatusVotoEnum.ABLE, VOTO_SUCESSO), HttpStatus.OK);

        } else if (this.CPFApiService.verificaCPFValido(associadoRequest.getCpf())) {

            String idAssociado = associadoService.realizarVoto(associadoRequest);
            return new ResponseEntity<>(
                    AssociadoResponse.of(idAssociado, StatusVotoEnum.ABLE, VOTO_SUCESSO), HttpStatus.OK);

        }
        return new ResponseEntity<>(AssociadoResponse.criarResponseFalha(), HttpStatus.NOT_ACCEPTABLE);
    }
}
