package com.br.cbd.BancoDigitalJPA.model.entity.cartao;

import java.math.BigDecimal;
import java.util.List;

import com.br.cbd.BancoDigitalJPA.model.entity.seguro.DadosSeguro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCartao(
    Long id, // Opcional, sem validação explícita

    @NotNull(message = "O ID da conta é obrigatório")
    Long conta,

    @NotBlank(message = "O número do cartão é obrigatório")
    // @Pattern(regexp = "^[0-9]{16}$", message = "O número do cartão deve conter exatamente 16 dígitos")
    String numeroCartao,

    @NotBlank(message = "A senha do cartão é obrigatória")
    // @Pattern(regexp = "^[0-9]{4,6}$", message = "A senha do cartão deve conter entre 4 e 6 dígitos")
    String senha,

    boolean ativo,

    // @NotNull(message = "O limite do cartão é obrigatório")
    // @DecimalMin(value = "0.0", message = "O limite do cartão não pode ser negativo")
    BigDecimal limite,

    // Opcional: ajuste conforme necessário
    // @DecimalMin(value = "0.0", message = "O valor da fatura não pode ser negativo")
    BigDecimal fatura,

    @NotNull(message = "O tipo do cartão é obrigatório")
    TipoCartao tipoCartao,

    // @NotNull(message = "A lista de seguros é obrigatória")
    // @NotEmpty(message = "O cartão deve ter pelo menos um seguro associado")
    List<DadosSeguro> seguros
) {}