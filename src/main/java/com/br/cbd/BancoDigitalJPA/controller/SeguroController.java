package com.br.cbd.BancoDigitalJPA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.cbd.BancoDigitalJPA.model.entity.seguro.DadosSeguro;
import com.br.cbd.BancoDigitalJPA.model.entity.seguro.Seguro;
import com.br.cbd.BancoDigitalJPA.services.SeguroService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/seguro")
public class SeguroController {

    @Autowired
    private SeguroService seguroService;

    @PostMapping("/contratarSeguro")
    public ResponseEntity<Seguro> contratarSeguro(@RequestBody @Valid DadosSeguro dadosSeguro) {
        Seguro novSeguro = seguroService.salvarSeguro(dadosSeguro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novSeguro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosSeguro> encontrarApolice(@PathVariable Long id) {
        DadosSeguro seguro = seguroService.encontrarApolice(id);
        return ResponseEntity.ok(seguro);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<DadosSeguro>> listarSeguros() {
        List<DadosSeguro> seguros = seguroService.retornaTodosSeguros();
        return ResponseEntity.ok(seguros);
    }

     @PutMapping("/{id}/alternar-status")
    public ResponseEntity<DadosSeguro> alternarStatusApolice(@PathVariable @Valid Long id) {
        DadosSeguro seguroAtualizado = seguroService.alternarStatusApolice(id);
        return ResponseEntity.ok(seguroAtualizado);
    }

}
