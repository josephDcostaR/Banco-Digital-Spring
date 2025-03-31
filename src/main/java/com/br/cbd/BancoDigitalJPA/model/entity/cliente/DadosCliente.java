package com.br.cbd.BancoDigitalJPA.model.entity.cliente;

import java.time.LocalDate;
import java.util.List;

import com.br.cbd.BancoDigitalJPA.model.entity.conta.Conta;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.TipoConta;

public record DadosCliente(
    Long id,
    String nome,
    String cpf,
    LocalDate Data_Nascimento,
    boolean ativo,
    CategoriaCliente categoria,
    
    // List<Conta> contas

    List<TipoConta> tiposContas
) {}
