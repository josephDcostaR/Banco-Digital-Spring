package com.br.cbd.BancoDigitalJPA.services;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.cbd.BancoDigitalJPA.model.entity.cartao.Cartao;
import com.br.cbd.BancoDigitalJPA.model.entity.cartao.DadosCartao;
import com.br.cbd.BancoDigitalJPA.model.entity.cartao.TipoCartao;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.Conta;
import com.br.cbd.BancoDigitalJPA.model.entity.seguro.DadosSeguro;
import com.br.cbd.BancoDigitalJPA.repository.CartaoRepository;
import com.br.cbd.BancoDigitalJPA.repository.ContaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ContaRepository contaRepository;


    //Criar novo Cartao
    // public Cartao salvarCartao(DadosCartao dadosCartao) {
    //     //Verifica se existe uma conta 
    //     Conta conta = contaRepository.findById(dadosCartao.conta())
    //         .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada!"));
    //     // Verifica se a conta já possui o número máximo de cartões (por exemplo, 2)
    //     if (conta.getCartoes() != null && conta.getCartoes().size() >= 2) {
    //         throw new RuntimeException("A conta já possui o número máximo de cartões permitidos.");
    //     }
    //     Cartao cartao = new Cartao();
    //     cartao.setConta(conta);
    //     cartao.setNumeroCartao(dadosCartao.numeroCartao());
    //     cartao.setSenha(dadosCartao.senha());
    //     cartao.setTipoCartao(dadosCartao.tipoCartao());
    //     cartao.setAtivo(true); 
    //     if (dadosCartao.tipoCartao() == TipoCartao.CREDITO) {
    //         // Definir limite com base na categoria do cliente
    //         BigDecimal limite;
    //         switch (conta.getCliente().getCategoria()) {
    //             case COMUM:
    //                 limite = new BigDecimal("1000.00");
    //                 break;
    //             case SUPER:
    //                 limite = new BigDecimal("5000.00");
    //                 break;
    //             case PREMIUM:
    //                 limite = new BigDecimal("10000.00");
    //                 break;
    //             default:
    //                 throw new IllegalArgumentException("Categoria de cliente inválida.");
    //         }
    //         cartao.setLimite(limite);
    //     } else if (dadosCartao.tipoCartao() == TipoCartao.DEBITO) {
    //         // O limite do débito será o saldo da conta
    //         cartao.setLimite(conta.getSaldo());
    //     } else {
    //         throw new IllegalArgumentException("Tipo de cartão inválido.");
    //     }
    //     return cartaoRepository.save(cartao);
    // }


    public Cartao salvarCartaoCredito(DadosCartao dadosCartao) {
        Conta conta = buscaConta(dadosCartao.conta());
        validarMaximoCartoes(conta);

        Cartao cartao = new Cartao();
        cartao.setConta(conta);
        cartao.setNumeroCartao(dadosCartao.numeroCartao());
        cartao.setSenha(dadosCartao.senha());
        cartao.setTipoCartao(TipoCartao.CREDITO);
        cartao.setAtivo(true);

        BigDecimal limite = switch (conta.getCliente().getCategoria()) {
            case COMUM -> new BigDecimal("1000.00");
            case SUPER -> new BigDecimal("5000.00");
            case PREMIUM -> new BigDecimal("10000.00");
        };

        cartao.setLimite(limite);

        return cartaoRepository.save(cartao);
    }

    public Cartao salvarCartaoDebito(DadosCartao dadosCartao){
        Conta conta = buscaConta(dadosCartao.conta());
        validarMaximoCartoes(conta);

        Cartao cartao = new Cartao();
        cartao.setConta(conta);
        cartao.setNumeroCartao(dadosCartao.numeroCartao());
        cartao.setSenha(dadosCartao.senha());
        cartao.setTipoCartao(TipoCartao.DEBITO);
        cartao.setAtivo(true);

        // Limite = saldo da conta
        cartao.setLimite(conta.getSaldo());

        return cartaoRepository.save(cartao);
    }

    private Conta buscaConta (Long idConta) {
        return contaRepository.findById(idConta)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada!"));
    }
    
    private void validarMaximoCartoes(Conta conta) {
        if (conta.getCartoes() != null && conta.getCartoes().size() >= 2) {
            throw new RuntimeException("A conta já possui o número máximo de cartões permitidos");
        }
    }
    
    //Consulta um cartão pelo ID
    public DadosCartao getByIdCartao(Long id) {
        Cartao cartao = cartaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado!"));
    
        List<DadosSeguro> seguros = Optional.ofNullable(cartao.getSeguros())
            .orElse(Collections.emptyList()) // Se for null, retorna uma lista vazia
            .stream()
            .map(seguro -> new DadosSeguro(
                seguro.getId(),
                seguro.getCartao().getId(), // Adiciona o ID do cartão, igual ao outro método
                seguro.getNumeroAPolice(),
                seguro.getDataContratacao(),
                seguro.getValorApolice(),
                seguro.isAtivo()
            ))
            .collect(Collectors.toList());
    
        return new DadosCartao(
            cartao.getId(),
            cartao.getConta().getId(),
            cartao.getNumeroCartao(),
            cartao.getSenha(),
            cartao.isAtivo(),
            cartao.getLimite(),
            cartao.getFatura(),
            cartao.getTipoCartao(),
            seguros // Agora passa uma lista padronizada de seguros!
        );
    }
    


    //Realizar um pagamento com o cartão 
    @Transactional
    public void pagamento(Long id, BigDecimal valor) {
        Cartao cartao = cartaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado!"));

        // Verificar se o cartão está ativo
        if (!cartao.isAtivo()) {
            throw new IllegalStateException("O cartão não está ativo!");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da compra deve ser maior que zero!");
        }

       switch (cartao.getTipoCartao()) {
            case DEBITO -> pagamentoComDebito(cartao, valor);
            case CREDITO -> pagamentoComCredito(cartao, valor);
            default -> throw new IllegalArgumentException("Tipo de cartão inválido");
       }

        cartaoRepository.save(cartao);
    }

    private void pagamentoComDebito(Cartao cartao, BigDecimal valor) {
        BigDecimal saldoConta = cartao.getConta().getSaldo();
        if (valor.compareTo(saldoConta) > 0) {
            throw new IllegalStateException("Saldo insuficiente!");
        }
        cartao.getConta().setSaldo(saldoConta.subtract(valor));
    }

    private void pagamentoComCredito(Cartao cartao, BigDecimal valor) {
        BigDecimal faturaAtual = cartao.getFatura() != null ? cartao.getFatura() : BigDecimal.ZERO;
        BigDecimal limiteDisponivel = cartao.getLimite().subtract(faturaAtual);

        if (valor.compareTo(limiteDisponivel) > 0) {
            throw new IllegalStateException("Limite insuficiente!");
        }

        cartao.setFatura(faturaAtual.add(valor));
    }


    //Alterar limite do cartão
    @Transactional
    public void alterarLimite(Long id, BigDecimal novoLimite) {
        // 1. Buscar o cartão no banco de dados
        Cartao cartao = cartaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado!"));
    
        // 2. Verificar se o cartão está ativo
        if (!cartao.isAtivo()) {
            throw new IllegalStateException("O cartão não está ativo!");
        }
    
        // 3. O novo limite não pode ser negativo
        if (novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O limite do cartão não pode ser negativo!");
        }
    
        // 4. Verificar se o novo limite é menor que o saldo já utilizado
        BigDecimal limiteUtilizado = cartao.getLimite(); 
        // Exemplo: Se o limite original era 1000 e o limite atual é 600, já foram usados 400
    
        if (novoLimite.compareTo(limiteUtilizado) < 0) {
            throw new IllegalStateException("O novo limite não pode ser menor que o valor já utilizado!");
        }
    
        // 5. Atualizar o limite do cartão
        cartao.setLimite(novoLimite);
    
        // 6. Salvar a alteração no banco de dados
        cartaoRepository.save(cartao);
    }

    //Ativar ou desativar um cartão 
    @Transactional
    public void AtivarDesativarCartao(Long id) {
         // 1. Buscar o cartão no banco de dados
         Cartao cartao = cartaoRepository.findById(id)
         .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado!"));
 
        
        // Alternar o status do cartão (ativo/inativo)
        cartao.setAtivo(!cartao.isAtivo());

        cartaoRepository.save(cartao);
    }

    //Alterar senha do cartão 
    @Transactional
    public void AlterarSenha(Long id, String novaSenha) {
        // 1. Buscar o cartão no banco de dados
        Cartao cartao = cartaoRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado!"));
  
        // 2. Verificar se o cartão está ativo
        if (!cartao.isAtivo()) {
            throw new IllegalStateException("O cartão não está ativo!");
        }

        //3. validar a nova senha 
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new IllegalArgumentException("A nova senha não pode estar vazia!");
        }

        cartao.setSenha(novaSenha);
        cartaoRepository.save(cartao);
    }

    // Consultar fatura do cartão de crédito 
    @Transactional
    public BigDecimal ConsultarFatura(Long id) {
        Cartao cartao = cartaoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado"));

        return cartao.getFatura();
    }

    //Realizar pagamento da fatura do cartão de crédito
    @Transactional
    public void PagarFatura(Long id, BigDecimal valorPagamento) {
        Cartao cartao = cartaoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado!"));

        if (!cartao.isAtivo()) {
            throw new IllegalStateException("O cartão não está ativo!");
        }

        if (valorPagamento.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero!");
        }

        BigDecimal faturaAtual = cartao.getFatura();

            
        if (valorPagamento.compareTo(faturaAtual) > 0) {
            throw new IllegalStateException("O pagamento não pode ser maior que o valor da fatura!");
        }

        cartao.setFatura(faturaAtual.subtract(valorPagamento));
        cartaoRepository.save(cartao);
    }

    //Alterar limite diário do cartão de débito 
    @Transactional
    public void AlterarLimiteDiario(Long id, BigDecimal novoLimite) {
        Cartao cartao = cartaoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado!"));

        if (!cartao.isAtivo()) {
            throw new IllegalStateException("O cartão não está ativo!");
        }

        if(cartao.getTipoCartao() != TipoCartao.DEBITO) {
            throw new IllegalArgumentException("Essa ação só pode ser realizada no cartão do tipo debito");
        }

        if (novoLimite.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O novo valor do limite deve ser maior que zero!");
        }

        cartao.setLimite(novoLimite);
        cartaoRepository.save(cartao);
    }


    



}
