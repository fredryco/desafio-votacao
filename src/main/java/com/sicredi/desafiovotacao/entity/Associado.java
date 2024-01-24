package com.sicredi.desafiovotacao.entity;

import com.sicredi.desafiovotacao.dto.AssociadoRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ASSOCIADO")
public class Associado {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "SESSAO_ID")
    private Sessao sessao;

    @Column(name = "CPF")
    private String cpf;

    @Column(name = "VOTO", nullable = false)
    private String voto;

    public static Associado of(AssociadoRequest associadoRequest, Sessao sessao) {
        return Associado.builder()
                .sessao(sessao)
                .cpf(associadoRequest.getCpf())
                .voto(associadoRequest.getVoto())
                .build();
    }

    public boolean verificaVotoUnico(Sessao session) {
        return !this.sessao.getId().equals(session.getId());
    }
}
