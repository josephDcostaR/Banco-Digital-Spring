package com.br.cbd.BancoDigitalJPA.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.cbd.BancoDigitalJPA.model.entity.cliente.Cliente;
import com.br.cbd.BancoDigitalJPA.model.entity.cliente.DadosCliente;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.TipoConta;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.Conta;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.DadosConta;
import com.br.cbd.BancoDigitalJPA.repository.ClienteRepository;
import com.br.cbd.BancoDigitalJPA.repository.ContaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

     // Cadastra cliente
     public Cliente salvarCliente(DadosCliente dadosCliente) {
        if (clienteRepository.existsByCpf(dadosCliente.cpf())) {
            throw new RuntimeException("Já existe um cliente cadastrado com este CPF!");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dadosCliente.nome());
        cliente.setCpf(dadosCliente.cpf());
        cliente.setData_Nascimento(dadosCliente.Data_Nascimento());
        cliente.setAtivo(true);
        cliente.setCategoria(dadosCliente.categoria());

        return clienteRepository.save(cliente);
    }

    public List<DadosCliente> getClientes() {
        if (clienteRepository.count() == 0) {
            throw new RuntimeException("Nenhum cliente cadastrado no sistema!");
        }
    
        return clienteRepository.findAll().stream()
                .map(cliente -> {
                    // Mapeia as contas para apenas os tipos
                    List<TipoConta> tiposContas = cliente.getContas()
                            .stream()
                            .map(Conta::getTipo) // Pega apenas o tipo da conta
                            .toList(); // Converte para lista
    
                    return new DadosCliente(
                            cliente.getId(),
                            cliente.getNome(),
                            cliente.getCpf(),
                            cliente.getData_Nascimento(),
                            cliente.isAtivo(),
                            cliente.getCategoria(),
                            tiposContas // Passa a lista de tipos das contas
                    );
                })
                .collect(Collectors.toList());
    }


     // Retorna um cliente pelo ID
     public Cliente getByIdCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado!"));
    }

     // Atualiza os dados de um cliente
     public Cliente updateCliente(Long id, DadosCliente clienteNovo) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado!"));

        if (clienteNovo.nome() != null) cliente.setNome(clienteNovo.nome());
        if (clienteNovo.cpf() != null) cliente.setCpf(clienteNovo.cpf());
        if (clienteNovo.Data_Nascimento() != null) cliente.setData_Nascimento(clienteNovo.Data_Nascimento());
        if (clienteNovo.categoria() != null) cliente.setCategoria(clienteNovo.categoria());

        return clienteRepository.save(cliente);
    }
    

    // Deleta um cliente escolhido
    @Transactional
    public void deleteCliente(Long id) {
        // Verifica se há clientes cadastrados na base
        if (clienteRepository.count() == 0) {
            throw new RuntimeException("Nenhum cliente cadastrado no sistema!");
        }

        // Busca o cliente no banco
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado!"));

        // Inativa todas as contas associadas ao cliente
        List<Conta> contas = contaRepository.findByCliente(cliente);
        for (Conta conta : contas) {
            conta.setAtiva(false);
            contaRepository.save(conta);
        }

        // Deleta o cliente sem precisar buscá-lo novamente
        clienteRepository.delete(cliente);
    }

}
