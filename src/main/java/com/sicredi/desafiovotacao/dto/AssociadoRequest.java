package com.sicredi.desafiovotacao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sicredi.desafiovotacao.service.exception.VotoInvalidoException;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static com.sicredi.desafiovotacao.utils.MessagesConstants.VOTO_INVALIDO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssociadoRequest {

    @JsonProperty(value = "id_associado")
    @ApiModelProperty(notes = "Id do associado", example = "968c3966c51011edafa10242ac120002")
    private String id;

    @Size(min = 11,  max = 11, message = "CPF somente númerico")
    @ApiModelProperty(notes = "CPF do associado", example = "12345678937")
    private String cpf;

    @NotBlank(message = "id da sessão não deve ser vazio")
    @JsonProperty(value = "id_sessao")
    @ApiModelProperty(notes = "ID da sessão dos votos", example = "968c3966c51011edafa10242ac120002", required = true)
    private String idSessao;

    @JsonProperty(value = "voto")
    @ApiModelProperty(notes = "Votos validos: s, n, sim, não, SIM, NÃO",
            example = "SIM", required = true)
    private String voto;

    @AssertTrue(message = "cpf ou id_associado não deve ser nulo ou vazio")
    private boolean isVoteValid() {
        return !Objects.isNull(cpf) || !Objects.isNull(id);
    }

     private void setVoto(String vote) {

        Predicate<String> isVoteOptionValid = x -> validaVotoValido(x, "s")
                || validaVotoValido(x, "n");

        Optional.of(vote)
                .filter(isVoteOptionValid)
                .ifPresentOrElse(
                        (voteDescription) -> this.voto = voteDescription,
                        () -> { throw new VotoInvalidoException(VOTO_INVALIDO); }
                );
    }

    private boolean validaVotoValido(String vote, String prefix) {
        return StringUtils.startsWithIgnoreCase(vote, prefix);
    }

}
