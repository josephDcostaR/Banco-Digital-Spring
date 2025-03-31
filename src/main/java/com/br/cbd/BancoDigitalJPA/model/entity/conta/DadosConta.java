package com.br.cbd.BancoDigitalJPA.model.entity.conta;

import java.math.BigDecimal;

import com.br.cbd.BancoDigitalJPA.model.entity.cliente.Cliente;

public record DadosConta(
    Long id,
    Long cliente,
    String codigo,
    BigDecimal saldo,
    boolean ativa,
    TipoConta tipo, //ENUM CORRENTE OU POUPANCA
    BigDecimal taxaManutencaoRendimento
) {}
