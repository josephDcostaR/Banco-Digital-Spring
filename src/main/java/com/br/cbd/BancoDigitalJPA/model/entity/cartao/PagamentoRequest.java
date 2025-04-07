package com.br.cbd.BancoDigitalJPA.model.entity.cartao;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(
    @NotNull
    BigDecimal valor
) {
   

}
