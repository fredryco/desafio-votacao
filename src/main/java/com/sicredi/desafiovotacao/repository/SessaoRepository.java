package com.sicredi.desafiovotacao.repository;

import com.sicredi.desafiovotacao.entity.Sessao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends CrudRepository<Sessao, String> {
}
