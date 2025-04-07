package com.br.cbd.BancoDigitalJPA.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.cbd.BancoDigitalJPA.model.entity.conta.DadosConta;
import com.br.cbd.BancoDigitalJPA.services.ContaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/conta")
public class contaController {

    @Autowired
    private ContaService contaService;

   @PostMapping("/corrente")
    public ResponseEntity<String> cadastraContaCorrente(@RequestBody @Valid DadosConta dadosConta) {
        contaService.salvarContaCorrente(dadosConta);
        return ResponseEntity.status(HttpStatus.CREATED).body("Conta Corrente Cadastrado!");
    }

    @PostMapping("/poupanca")
    public ResponseEntity<String> cadastraContaPoupanca(@RequestBody @Valid DadosConta dadosConta) {
        contaService.salvarContaPoupanca(dadosConta);
        return ResponseEntity.status(HttpStatus.CREATED).body("Conta Poupança Cadastrado!");
    }


    @GetMapping("/listar")
    public ResponseEntity<List<DadosConta>> listarContas() {
        return ResponseEntity.ok(contaService.getContas());
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.getSaldo(id));
    }

    @PostMapping("/transferencia")
    public ResponseEntity<String> transferencia(@RequestParam  @Valid Long origem, @RequestParam Long destino, @RequestParam BigDecimal valor) {
        contaService.pagamentoViaPix(origem, destino, valor);
        return ResponseEntity.ok("Transferência via Pix realizada com sucesso!");
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<String> deposito(@PathVariable @Valid Long id, @RequestParam BigDecimal valor) throws IllegalAccessException {
        contaService.deposito(id, valor);
        return ResponseEntity.ok("Depósito realizado com sucesso!");
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<String> saque(@PathVariable @Valid Long id, @RequestParam BigDecimal valor) {
        contaService.saque(id, valor);
        return ResponseEntity.ok("Saque realizado com sucesso!");
    }

    @PostMapping("/{id}/taxa-manutencao") //Conta Corrente
    public ResponseEntity<String> aplicarTaxaManutencao(@PathVariable @Valid Long id) {
        contaService.aplicarTaxaManutencao(id);
        return ResponseEntity.ok("Taxa de manutenção aplicada!");
    }

    @PostMapping("/{id}/rendimento") //Conta Poupança
    public ResponseEntity<String> aplicarRendimento(@PathVariable @Valid Long id) {
        contaService.aplicarRendimento(id);
        return ResponseEntity.ok("Rendimento aplicado!");
    }


    @GetMapping("/buscar/{id}")
    public ResponseEntity<DadosConta> obterConta(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.getByIdConta(id));
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<String> deletarConta(@PathVariable Long id) {
        contaService.deleteConta(id);
        return ResponseEntity.ok("Conta excluída com sucesso!");
    }

}
