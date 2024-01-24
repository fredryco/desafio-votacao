package com.sicredi.desafiovotacao.entity;

import com.sicredi.desafiovotacao.dto.SessaoRequest;
import com.sicredi.desafiovotacao.utils.DateUtils;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "SESSAO")
public class Sessao {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "PAUTA_ID")
    private Pauta pauta;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "DATA_INICIAL", nullable = false)
    private LocalDateTime dataInicial;

    @Column(name = "DATA_FINAL", nullable = false)
    private LocalDateTime dataFinal;

    @Column(name = "CONTAGEM_VOTOS_SIM", nullable = false)
    @Min(0)
    private int contagemSim;

    @Column(name = "CONTAGEM_VOTOS_NAO", nullable = false)
    @Min(0)
    private int contagemNao;

    public static Sessao of(Pauta pauta, SessaoRequest sessaoRequest) {
        return Sessao.builder()
                .pauta(pauta)
                .dataCriacao(DateUtils.obterDataAtual())
                .dataInicial(sessaoRequest.getInicioSessao())
                .dataFinal(sessaoRequest.getFimSessao())
                .build();
    }

    public boolean isOpen() {
        var dataAtual = DateUtils.obterDataAtual();
        return dataInicial.isBefore(dataAtual) && dataFinal.isAfter(dataAtual);
    }

    public void appendVote(String voto) {
        if (verificaTipoVoto(voto)) {
            this.contagemSim++;
        } else {
            this.contagemNao++;
        }
    }

    private boolean verificaTipoVoto(String voto) {
        return voto.toLowerCase().startsWith("s");
    }

}
