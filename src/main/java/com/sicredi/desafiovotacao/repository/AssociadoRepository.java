package com.sicredi.desafiovotacao.repository;

import com.sicredi.desafiovotacao.entity.Associado;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssociadoRepository extends CrudRepository<Associado, String> {

    Optional<Associado> findByCpf(String cpf);

}
