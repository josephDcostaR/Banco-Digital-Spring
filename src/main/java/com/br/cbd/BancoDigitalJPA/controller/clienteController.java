package com.br.cbd.BancoDigitalJPA.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.cbd.BancoDigitalJPA.model.entity.cliente.Cliente;
import com.br.cbd.BancoDigitalJPA.model.entity.cliente.DadosCliente;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.Conta;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.TipoConta;
import com.br.cbd.BancoDigitalJPA.services.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class clienteController {


    @Autowired
    private ClienteService clienteService;

    @PostMapping("/add")
    public ResponseEntity<String> addCliente(@RequestBody @Valid DadosCliente dadosCliente) {
        Cliente clienteAdicionado = clienteService.salvarCliente(dadosCliente);

        if (clienteAdicionado != null) {
            return new ResponseEntity<>("Cliente " + clienteAdicionado.getNome() + " adicionado com sucesso!",
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Nome ou CPF do cliente inválido!", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<DadosCliente>> getAllClientes() {
        List<DadosCliente> clientes = clienteService.getClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<DadosCliente> findCliente(@PathVariable Long id) {
       Cliente clienteEncontrado = clienteService.getByIdCliente(id);

       List<TipoConta> tipoContas = clienteEncontrado.getContas()
        .stream()
        .map(Conta::getTipo)
        .toList();

        DadosCliente dadosCliente = new DadosCliente(
            clienteEncontrado.getId(),
            clienteEncontrado.getNome(),
            clienteEncontrado.getCpf(),
            clienteEncontrado.getData_Nascimento(),
            clienteEncontrado.isAtivo(),
            clienteEncontrado.getCategoria(),
            tipoContas
        );

        return ResponseEntity.ok(dadosCliente);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Cliente> updateCliente(@Valid @PathVariable Long id, @RequestBody DadosCliente clienteNovo) {
        try {
            Cliente clienteAtualizado = clienteService.updateCliente(id, clienteNovo);
            return new ResponseEntity<>(clienteAtualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        try {
            clienteService.deleteCliente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
