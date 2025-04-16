package com.br.cbd.BancoDigitalJPA.model.entity.cliente;

import java.time.LocalDate;
import java.util.List;

import com.br.cbd.BancoDigitalJPA.model.entity.conta.TipoConta;
import com.br.cbd.BancoDigitalJPA.validation.MinAge;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record DadosCliente(
    Long id,

    @NotBlank(message = "O nome é obrigatório nome")
    String nome,

    @NotBlank(message = "O CPF é obrigatório")
    // @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
    String cpf,

    @NotNull(message = "A data de nascimento é obrigatoria")
    @Past(message = "A data de nascimento deve ser no passado")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @MinAge(18)
    LocalDate Data_Nascimento,

    boolean ativo,

    @NotNull(message = "A categoria do cliente é obrigatória")
    CategoriaCliente categoria,

    // @NotEmpty(message = "O cliente deve ter ao menos um tipo de conta")
    List<TipoConta> tiposContas
) {}
