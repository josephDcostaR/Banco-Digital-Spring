package com.br.cbd.BancoDigitalJPA.model.entity.conta;

import java.math.BigDecimal;
import java.util.List;

import com.br.cbd.BancoDigitalJPA.model.entity.cartao.TipoCartao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record DadosConta(
    Long id,

    @NotNull(message = "O ID do cliente é obrigatório")
    Long cliente,

    @NotBlank(message = "O código da conta é obrigatório")
    // @Pattern(regexp = "^CONT-[0-9]{4}$", message = "O código da conta deve seguir o formato CONT-XXXX")
    String codigo,

    @NotNull(message = "O saldo é obrigatório")
    @DecimalMin(value = "0.0", message = "O saldo não pode ser negativo")
    BigDecimal saldo,

    boolean ativa,

    @NotNull(message = "O tipo da conta é obrigatório")
    TipoConta tipo, //ENUM CORRENTE OU POUPANCA

    // @NotNull(message = "A taxa de manutenção/rendimento é obrigatória")
    // @DecimalMin(value = "0.0", message = "A taxa de manutenção/rendimento não pode ser negativa")
    BigDecimal taxaManutencaoRendimento,

    // @NotNull(message = "A lista de tipos de cartão é obrigatória")
    // @NotEmpty(message = "A conta deve ter pelo menos um tipo de cartão")
    List<TipoCartao> tiposCartao
) {}
