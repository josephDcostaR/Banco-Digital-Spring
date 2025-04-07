package com.br.cbd.BancoDigitalJPA.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.cbd.BancoDigitalJPA.model.entity.cartao.DadosCartao;
import com.br.cbd.BancoDigitalJPA.model.entity.cartao.PagamentoRequest;
import com.br.cbd.BancoDigitalJPA.services.CartaoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartao")
public class CartaoController {
    
    @Autowired
    private CartaoService cartaoService;

    @PostMapping("/credito")
    public ResponseEntity<String> cadastraCartaoCredito(@RequestBody @Valid DadosCartao dadosCartao) {
        cartaoService.salvarCartaoCredito(dadosCartao);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cartão de crédito cadastrado!");
    }
    
    @PostMapping("/debito")
    public ResponseEntity<String> cadastraCartaoDebito(@RequestBody @Valid DadosCartao dadosCartao) {
        cartaoService.salvarCartaoDebito(dadosCartao);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cartão de débito cadastrado!");
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<DadosCartao> buscarCartaoById(@PathVariable Long id) {
        DadosCartao cartao = cartaoService.getByIdCartao(id);
        return ResponseEntity.ok(cartao);
    }

    @PostMapping("/{id}/pagamento/debito")
    public ResponseEntity<String> pagamentoDebito(@PathVariable Long id, @RequestBody @Valid PagamentoRequest request) {
        cartaoService.pagamentoDebito(id, request.valor());
        return ResponseEntity.ok("Pagamento com débito realizado com sucesso!");
    }

    @PostMapping("/{id}/pagamento/credito")
    public ResponseEntity<String> pagamentoCredito(@PathVariable Long id, @RequestBody @Valid PagamentoRequest request) {
        cartaoService.pagamentoCredito(id, request.valor());
        return ResponseEntity.ok("Pagamento com crédito realizado com sucesso!");
    }


    @PutMapping("/{id}/limite")
    public ResponseEntity<String> alterarLimite(@PathVariable @Valid Long id, @RequestBody BigDecimal novoLimite) {
        try {
            cartaoService.alterarLimite(id, novoLimite);
            return ResponseEntity.ok("Limite alterado com sucesso!");
        } catch (EntityNotFoundException | IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/ativar-desativar")
    public ResponseEntity<String> ativarDesativarCartao(@PathVariable @Valid Long id) {
        try {
            cartaoService.AtivarDesativarCartao(id);
            return ResponseEntity.ok("Cartão alterado com sucesso!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para alterar a senha de um cartão
    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<String> alterarSenha(@PathVariable @Valid Long id, @RequestBody String novaSenha) {
        try {
            cartaoService.AlterarSenha(id, novaSenha);
            return ResponseEntity.ok("Senha alterada com sucesso!");
        } catch (EntityNotFoundException | IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/fatura")
    public ResponseEntity<BigDecimal> consultarFatura(@PathVariable Long id) {
        try {
            BigDecimal fatura = cartaoService.ConsultarFatura(id);
            return ResponseEntity.ok(fatura);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/pagar-fatura")
    public ResponseEntity<String> pagarFatura(@PathVariable @Valid Long id, @RequestBody BigDecimal valorPagamento) {
        try {
            cartaoService.PagarFatura(id, valorPagamento);
            return ResponseEntity.ok("Fatura paga com sucesso!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cartão não encontrado!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/alterar-limite-diario")
    public ResponseEntity<String> alterarLimiteDiario(@PathVariable @Valid Long id, @RequestBody BigDecimal novoLimite) {
        try {
            cartaoService.AlterarLimiteDiario(id, novoLimite);
            return ResponseEntity.ok("Limite diário alterado com sucesso!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cartão não encontrado!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
