package com.sicredi.desafiovotacao.repository;

import com.sicredi.desafiovotacao.entity.Pauta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends CrudRepository<Pauta, String> {
}
