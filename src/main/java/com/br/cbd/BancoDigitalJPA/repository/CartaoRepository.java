package com.br.cbd.BancoDigitalJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.cbd.BancoDigitalJPA.model.entity.cartao.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao,Long> {

}
