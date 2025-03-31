package com.br.cbd.BancoDigitalJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.cbd.BancoDigitalJPA.model.entity.cliente.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {

    boolean existsByCpf(String cpf);

}
