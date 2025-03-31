package com.br.cbd.BancoDigitalJPA.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.cbd.BancoDigitalJPA.model.entity.cliente.Cliente;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta,Long> {

    @Query("SELECT c.saldo FROM Conta c WHERE c.id = :id")
    BigDecimal findSaldoById(@Param("id") Long id);


    // List<Conta> findByIdCliente(Long clienteId);

    List<Conta> findByCliente(Cliente cliente);


}
