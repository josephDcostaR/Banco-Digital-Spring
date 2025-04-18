package com.br.cbd.BancoDigitalJPA.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.cbd.BancoDigitalJPA.model.entity.cartao.Cartao;
import com.br.cbd.BancoDigitalJPA.model.entity.cartao.TipoCartao;
import com.br.cbd.BancoDigitalJPA.model.entity.cliente.Cliente;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.Conta;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.DadosConta;
import com.br.cbd.BancoDigitalJPA.model.entity.conta.TipoConta;
import com.br.cbd.BancoDigitalJPA.repository.ClienteRepository;
import com.br.cbd.BancoDigitalJPA.repository.ContaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ContaService {
    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public ContaService(ContaRepository contaRepository,ClienteRepository clienteRepository){
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;

    }
   

    public Conta salvarContaCorrente(DadosConta dadosConta) {
        Cliente cliente = getClienteValido(dadosConta.cliente());

        Conta conta = new Conta();
        conta.setCliente(cliente);
        conta.setCodigo(dadosConta.codigo());
        conta.setSaldo(dadosConta.saldo());
        conta.setAtiva(true);
        conta.setTipo(TipoConta.CORRENTE);
        conta.setTaxaManutencaoRendimento(getTaxaManutencao(cliente));

        return contaRepository.save(conta);
    }

    public Conta salvarContaPoupanca(DadosConta dadosConta) {
        Cliente cliente = getClienteValido(dadosConta.cliente());

        Conta conta = new Conta();
        conta.setCliente(cliente);
        conta.setCodigo(dadosConta.codigo());
        conta.setSaldo(dadosConta.saldo());
        conta.setAtiva(true);
        conta.setTipo(TipoConta.POUPANCA);
        conta.setTaxaManutencaoRendimento(getTaxaRendimento(cliente));

        return contaRepository.save(conta);
    } 

    private BigDecimal getTaxaRendimento(Cliente cliente) {
        return switch (cliente.getCategoria()){
            case COMUM -> new BigDecimal("0.005"); //0,5%
            case SUPER -> new BigDecimal("0.007"); //0,7%
            case PREMIUM -> new BigDecimal("0.009"); //0,9%
        };
    }

    private BigDecimal getTaxaManutencao(Cliente cliente) {
        return switch (cliente.getCategoria()) {
            case COMUM -> new BigDecimal("12.00");
            case SUPER -> new BigDecimal("8.00");
            case PREMIUM -> BigDecimal.ZERO;
        };
    }

    private Cliente getClienteValido(Long idCliente){
        return  clienteRepository.findById(idCliente)
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado!"));
    }

 

    public List<DadosConta> getContas() { 
    if (contaRepository.count() == 0) {
        throw new RuntimeException("Nenhum conta cadastrado no sistema!");
    }
    return contaRepository.findAll().stream()
        .map(conta -> {
            // Mapeia os cartões para extrair os tipos
            List<TipoCartao> tiposCartao = conta.getCartoes()
                .stream()
                .map(Cartao::getTipoCartao)  // Certifique-se de que o Cartao tem um método getTipoCartao()
                .distinct()
                .collect(Collectors.toList());

            return new DadosConta(
                conta.getId(),
                conta.getCliente().getId(),
                conta.getCodigo(),
                conta.getSaldo(),
                conta.isAtiva(),
                conta.getTipo(),
                conta.getTaxaManutencaoRendimento(),
                tiposCartao   // Aqui você passa a lista de tipos dos cartões
            );
        })
        .collect(Collectors.toList());
    }


    //Consulta saldo da conta
    @Transactional
    public BigDecimal getSaldo(Long id) {
        BigDecimal saldo = contaRepository.findSaldoById(id);
        if (saldo == null) {
            throw new EntityNotFoundException("Conta não encontrada!");
        }
        return saldo;
    }

    // Realizar um pagamento via Pix 
    @Transactional
    public void pagamentoViaPix(Long idContaOrigem, Long idContaDestino, BigDecimal valor) {
        Conta contaOrigem = contaRepository.findById(idContaOrigem)
            .orElseThrow(() -> new EntityNotFoundException("Conta de origem não encontrada!"));

        Conta contaDestino = contaRepository.findById(idContaDestino)
            .orElseThrow(() -> new EntityNotFoundException("Conta de destino não encontrada!"));

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente para a transferência!");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
    }

    //Realizar um depósito na conta 
    @Transactional
    public void deposito(Long id, BigDecimal valor) throws IllegalAccessException {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalAccessException("O valor do depósito deve ser maior que zero!");
        }

        Conta contaId = contaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada!"));

        contaId.setSaldo(contaId.getSaldo().add(valor));

        contaRepository.save(contaId);
    }
   
    //Realizar um saque na conta
    @Transactional
    public void saque(Long id, BigDecimal valor) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero!");
        }

        Conta contaId = contaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada!"));

        contaId.setSaldo(contaId.getSaldo().subtract(valor));

        contaRepository.save(contaId);
    }

    //Aplicar taxa de manutenção mensal (para conta corrente) 
    @Transactional
    public void aplicarTaxaManutencao(Long id) {
        Conta conta = contaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada!"));

        if (conta.getTipo() != TipoConta.CORRENTE) {
            throw new IllegalArgumentException("A taxa de manutenção só pode ser aplicada a contas correntes.");
        }

        BigDecimal taxaManutencao = getTaxaManutencao(conta.getCliente());

        if (conta.getSaldo().compareTo(taxaManutencao) < 0) {
            throw new RuntimeException("Saldo insuficiente para aplicar a taxa de manutenção.");
        }

        //Subtrai a taxa do saldo
        conta.setSaldo(conta.getSaldo().subtract(taxaManutencao));

        contaRepository.save(conta);
    }

    //Aplicar taxa de rendimento mensal (para conta poupança)
    @Transactional
    public void aplicarRendimento(Long id){
        Conta conta = contaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada!"));

        if (conta.getTipo() != TipoConta.POUPANCA) {
            throw new IllegalArgumentException("O rendimento só pode ser aplicado a contas poupança.");
        }

        BigDecimal taxaRendimento = getTaxaRendimento(conta.getCliente());
        BigDecimal rendimento = conta.getSaldo().multiply(taxaRendimento);
        conta.setSaldo(conta.getSaldo().add(rendimento));

        contaRepository.save(conta);

    }

    //Obtem detalhes de uma conta
    public DadosConta getByIdConta(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada!"));
        
        // Mapeia os cartões para extrair os tipos, removendo duplicatas, se necessário
        List<TipoCartao> tiposCartao = conta.getCartoes()
                .stream()
                .map(Cartao::getTipoCartao)  // Certifique-se de que o Cartao possui o método getTipoCartao()
                .distinct()
                .collect(Collectors.toList());
    
        return new DadosConta(
                conta.getId(),
                conta.getCliente().getId(),
                conta.getCodigo(),
                conta.getSaldo(),
                conta.isAtiva(),
                conta.getTipo(),
                conta.getTaxaManutencaoRendimento(),
                tiposCartao
        );
    }

    //Deletar conta
    public void deleteConta(Long id) {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada!"));
    
        contaRepository.delete(conta); // Deleta diretamente a conta
    }
}
