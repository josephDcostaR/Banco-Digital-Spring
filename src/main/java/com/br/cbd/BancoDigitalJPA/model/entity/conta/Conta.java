package com.br.cbd.BancoDigitalJPA.model.entity.conta;

import java.math.BigDecimal;

import com.br.cbd.BancoDigitalJPA.model.entity.cliente.Cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String codigo;

    @Column(precision = 19, scale = 2)
    private BigDecimal saldo;
    private boolean ativa;

    @Enumerated(EnumType.STRING)
    private TipoConta tipo; //ENUM CORRENTE OU POUPANCA

    @Column(precision = 5, scale = 2)
    private BigDecimal taxaManutencaoRendimento;

    public Conta() {
        // Inicializando com zero para evitar NullPointerException
        this.saldo = BigDecimal.ZERO;
        this.taxaManutencaoRendimento = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getTaxaManutencaoRendimento() {
        return taxaManutencaoRendimento;
    }

    public void setTaxaManutencaoRendimento(BigDecimal taxaManutencaoRendimento) {
        this.taxaManutencaoRendimento = taxaManutencaoRendimento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
