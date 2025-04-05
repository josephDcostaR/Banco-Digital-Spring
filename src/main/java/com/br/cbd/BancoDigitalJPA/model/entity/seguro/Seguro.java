package com.br.cbd.BancoDigitalJPA.model.entity.seguro;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.br.cbd.BancoDigitalJPA.model.entity.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguros") 
public class Seguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    private String numeroAPolice;
    private LocalDate dataContratacao;
    private BigDecimal valorApolice;
    private boolean ativo;

    public Seguro() {
        this.valorApolice = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public String getNumeroAPolice() {
        return numeroAPolice;
    }

    public void setNumeroAPolice(String numeroAPolice) {
        this.numeroAPolice = numeroAPolice;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public BigDecimal getValorApolice() {
        return valorApolice;
    }

    public void setValorApolice(BigDecimal valorApolice) {
        this.valorApolice = valorApolice;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    





}
