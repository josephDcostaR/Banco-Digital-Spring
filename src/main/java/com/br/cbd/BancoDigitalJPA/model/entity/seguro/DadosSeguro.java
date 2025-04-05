package com.br.cbd.BancoDigitalJPA.model.entity.seguro;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public record DadosSeguro(
    Long id, // Opcional, sem validação explícita

    @NotNull(message = "O ID do cartão é obrigatório")
    Long cartao,

    @NotBlank(message = "O número da apólice é obrigatório")
    // @Pattern(regexp = "^[0-9]{8,12}$", message = "O número da apólice deve conter entre 8 e 12 dígitos") // Ajuste o padrão conforme necessário
    String numeroAPolice,

    @NotNull(message = "A data de contratação é obrigatória")
    @JsonFormat(pattern = "dd-MM-yyyy")
    // @PastOrPresent(message = "A data de contratação deve ser no passado ou no presente")
    LocalDate dataContratacao,

    @NotNull(message = "O valor da apólice é obrigatório")
    @DecimalMin(value = "0.0", message = "O valor da apólice não pode ser negativo")
    BigDecimal valorApolice,

    boolean ativo
) {}