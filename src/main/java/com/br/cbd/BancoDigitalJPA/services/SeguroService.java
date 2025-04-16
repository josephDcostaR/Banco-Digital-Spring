package com.br.cbd.BancoDigitalJPA.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.cbd.BancoDigitalJPA.model.entity.cartao.Cartao;
import com.br.cbd.BancoDigitalJPA.model.entity.seguro.DadosSeguro;
import com.br.cbd.BancoDigitalJPA.model.entity.seguro.Seguro;
import com.br.cbd.BancoDigitalJPA.repository.CartaoRepository;
import com.br.cbd.BancoDigitalJPA.repository.SeguroRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SeguroService {
    private final SeguroRepository seguroRepository;
    private final CartaoRepository cartaoRepository;

    @Autowired
    public SeguroService(SeguroRepository seguroRepository,  CartaoRepository cartaoRepository) {
        this.seguroRepository = seguroRepository;
        this.cartaoRepository = cartaoRepository;
    }

    //Contratar um seguro
    public Seguro salvarSeguro(DadosSeguro dadosSeguro) {
        Cartao cartao = cartaoRepository.findById(dadosSeguro.cartao())
            .orElseThrow(() ->  new EntityNotFoundException("Cartao não encontrada!" ));
     
        if (seguroRepository.existsByNumeroAPolice(dadosSeguro.numeroAPolice())) {
                throw new RuntimeException("Já existe um cliente cadastrado com este CPF!");
        }

        Seguro seguro = new Seguro();

        seguro.setCartao(cartao);
        seguro.setNumeroAPolice(dadosSeguro.numeroAPolice());
        seguro.setDataContratacao(dadosSeguro.dataContratacao());
        seguro.setValorApolice(dadosSeguro.valorApolice());
        seguro.setAtivo(true);

        return seguroRepository.save(seguro);
    }


    //Obter detalhes de uma apólice de seguro 
    public DadosSeguro encontrarApolice(Long id) {
        Seguro seguro = seguroRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Seguro não encontrado"));

        // Verificar se o seguro está ativo
        if (!seguro.isAtivo()) {
            throw new IllegalStateException("O seguro não está ativo!");
        }

        // Retorna o seguro convertido para DadosSeguro
        return new DadosSeguro(
            seguro.getId(),
            seguro.getCartao().getId(),
            seguro.getNumeroAPolice(),
            seguro.getDataContratacao(),
            seguro.getValorApolice(),
            seguro.isAtivo()
        );
    }


    //Listar todos os seguros disponíveis 
    public List<DadosSeguro> retornaTodosSeguros() {
        List<Seguro> seguros = seguroRepository.findAll();

        if (seguros.isEmpty()) {
            throw new RuntimeException("Nenhum seguro cadastrado no sistema");
        }

        return seguros.stream()
        .filter(Seguro::isAtivo)
        .map(seguro -> new DadosSeguro(
            seguro.getId(),
            seguro.getCartao().getId(),
            seguro.getNumeroAPolice(),
            seguro.getDataContratacao(),
            seguro.getValorApolice(),
            seguro.isAtivo()
        ))
        .collect(Collectors.toList());
    }


    @Transactional
    public DadosSeguro alternarStatusApolice(Long id) {
        Seguro seguro = seguroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Seguro não encontrado!"));

        // Alterna o status da apólice (ativo/inativo)
        seguro.setAtivo(!seguro.isAtivo());

        Seguro seguroAtualizado = seguroRepository.save(seguro);

        // Retorna os dados atualizados
        return new DadosSeguro(
            seguroAtualizado.getId(),
            seguroAtualizado.getCartao().getId(),
            seguroAtualizado.getNumeroAPolice(),
            seguroAtualizado.getDataContratacao(),
            seguroAtualizado.getValorApolice(),
            seguroAtualizado.isAtivo()
        );
    }
}

