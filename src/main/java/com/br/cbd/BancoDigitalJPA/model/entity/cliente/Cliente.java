package com.br.cbd.BancoDigitalJPA.model.entity.cliente;

import java.util.List;

import com.br.cbd.BancoDigitalJPA.model.entity.conta.Conta;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;

    private LocalDate Data_Nascimento;
    private boolean ativo;

    @Enumerated(EnumType.STRING)
    private CategoriaCliente categoria;


    @OneToMany(mappedBy = "cliente")
    private List<Conta> contas;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getCpf() {
        return cpf;
    }


    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public LocalDate getData_Nascimento() {
        return Data_Nascimento;
    }


    public void setData_Nascimento(LocalDate data_Nascimento) {
        Data_Nascimento = data_Nascimento;
    }


    public boolean isAtivo() {
        return ativo;
    }


    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }


    public CategoriaCliente getCategoria() {
        return categoria;
    }


    public void setCategoria(CategoriaCliente categoria) {
        this.categoria = categoria;
    }


    public List<Conta> getContas() {
        return contas;
    }


    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }


   
    
    
}
