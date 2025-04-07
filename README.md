# 💳 Projeto Banco Digital

Este é um projeto de **backend** desenvolvido em **Java 21**, utilizando o **Spring Boot** e banco de dados em memória **H2 Database**. Ele simula um sistema de banco digital com funcionalidades de **contas bancárias**, **cartões**, **clientes** e **seguros**, oferecendo endpoints REST para integração.

---

## 🚀 Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/banco-digital.git
   ```
2. Importe o projeto na sua IDE (recomenda-se o IntelliJ ou VS Code com suporte a Java).
3. Execute a aplicação via classe `BancoDigitalApplication`.
4. Acesse o H2 Database Console (se configurado):  
   ```
   http://localhost:8080/h2-console
   ```
5. Teste os endpoints via Postman, Insomnia ou similar.

---

## 📦 Dependências

- `Spring Web`
- `Spring Data JPA`
- `Spring Boot Starter Validation`
- `H2 Database`
- `JDBC`

---

## 🧱 Estrutura do Projeto

```
BancoDigital
├── controller
├── exception
├── model
│   └── entity
│       ├── Cartao
│       ├── Cliente
│       ├── Conta
│       └── Seguro
├── repository
└── services
└── validation
```

---

## 📡 Endpoints disponíveis

### 🔐 Cartão
- `POST /cartao/credito` - Cadastrar cartão de crédito  
- `POST /cartao/debito` - Cadastrar cartão de débito  
- `GET /cartao/{id}` - Buscar cartão por ID  
- `POST /cartao/{id}/pagamento/debito` - Realizar pagamento com debito  
- `POST /cartao/{id}/pagamento/credito` - Realizar pagamento com credito 
- `PUT /cartao/{id}/limite` - Alterar limite do cartão  
- `PUT /cartao/{id}/ativar-desativar` - Ativar ou desativar cartão  
- `PUT /cartao/{id}/alterar-senha` - Alterar senha do cartão  
- `GET /cartao/{id}/fatura` - Consultar fatura  
- `POST /cartao/{id}/pagar-fatura` - Pagar fatura  
- `PUT /cartao/{id}/alterar-limite-diario` - Alterar limite diário (débito)

### 👤 Cliente
- `POST /cliente/add` - Cadastrar cliente  
- `GET /cliente/listAll` - Listar todos os clientes  
- `GET /cliente/buscar/{id}` - Buscar cliente por ID  
- `PUT /cliente/update/{id}` - Atualizar cliente  
- `DELETE /cliente/del/{id}` - Deletar cliente

### 💰 Conta
- `POST /conta/corrente` - Cadastrar conta corrente  
- `POST /conta/poupanca` - Cadastrar conta poupança  
- `GET /conta/listar` - Listar contas  
- `GET /conta/{id}/saldo` - exibir saldo  
- `POST /conta/transferencia` - Realizar transferência  
- `POST /conta/{id}/deposito` - Realizar depósito  
- `POST /conta/{id}/saque` - Realizar saque  
- `PATCH /conta/{id}/taxa-manutencao` - Aplicar taxa de manutenção  
- `PATCH /conta/{id}/rendimento` - Aplicar rendimento  
- `GET /conta/buscar/{id}` - Buscar conta por ID  
- `DELETE /conta/del/{id}` - Deletar conta

### 🛡️ Seguro
- `POST /seguro/contratarSeguro` - Cadastrar seguro  
- `GET /seguro/{id}` - Buscar apólice por ID  
- `GET /seguro/ativos` - Listar todos os seguros  
- `PATCH /seguro/{id}/alternar-status` - Alterar status da apólice

---

## 🧪 Testes

Você pode testar os endpoints utilizando ferramentas como:
- [Postman](https://www.postman.com/)
- [Insomnia](https://insomnia.rest/)
- [Swagger (se configurado)](https://springdoc.org/)

---

## 🛠️ Futuras melhorias

- Autenticação com Spring Security
- Integração com bancos reais
- Dashboard com relatórios
- API de validação de CPF

