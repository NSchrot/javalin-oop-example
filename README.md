# Mini E-commerce (POO - 4 Pilares)

Projeto exemplo em Java que reforça Encapsulamento, Herança, Polimorfismo e Abstração usando um mini e-commerce com Javalin.

## Como executar

Pré-requisitos: Java 17+ e Maven.

```powershell
mvn exec:java
```

Servidor: http://localhost:7575

Páginas (frontend):
- / -> redireciona para `index.html`
- /produtos -> `produtos.html`
- /carrinho -> `carrinho.html`
- /admin -> `admin.html`

## API (JSON)

- GET /api/produtos — Lista produtos do catálogo
- POST /admin/produtos?nome=Mouse&preco=99.9&estoque=10 — Cadastrar produto (admin)
- DELETE /admin/produtos?nome=Mouse — Remover produto (admin)
- POST /api/carrinho/adicionar?nome=Mouse — Adicionar ao carrinho (cliente demo)
- GET /api/carrinho — Detalhes do carrinho (itens e total)
- POST /api/checkout?tipo=cartao — Finalizar compra (tipo: cartao|boleto)

## Exemplos de uso

Observação: rotas de escrita (POST/DELETE) não funcionam abrindo direto no navegador. Use a UI (`/admin`, `/produtos`, `/carrinho`) ou as chamadas abaixo via PowerShell.

### Via páginas (navegador)
1) Abra a página de admin e cadastre um produto: `http://localhost:7575/admin`
2) Veja produtos: `http://localhost:7575/produtos`
3) Adicione ao carrinho pela UI de produtos
4) Veja o carrinho: `http://localhost:7575/carrinho`
5) Faça checkout pela página do carrinho

### Via API (PowerShell)

1) Cadastrar produto (POST):
```powershell
Invoke-RestMethod -Uri "http://localhost:7575/admin/produtos?nome=Mouse&preco=99.9&estoque=10" -Method Post -ContentType "application/x-www-form-urlencoded"
```

2) Listar produtos (GET JSON):
```powershell
Invoke-RestMethod -Uri "http://localhost:7575/api/produtos" -Method Get
```

3) Adicionar ao carrinho (POST):
```powershell
Invoke-RestMethod -Uri "http://localhost:7575/api/carrinho/adicionar?nome=Mouse" -Method Post -ContentType "application/x-www-form-urlencoded"
```

4) Ver carrinho (GET JSON):
```powershell
Invoke-RestMethod -Uri "http://localhost:7575/api/carrinho" -Method Get
```

5) Checkout (POST; tipo: cartao|boleto):
```powershell
Invoke-RestMethod -Uri "http://localhost:7575/api/checkout?tipo=cartao" -Method Post -ContentType "application/x-www-form-urlencoded"
```

As respostas de API são JSON (onde aplicável) com cliente, itens, tipo de pagamento e total.

## Pilares de POO atendidos

- Encapsulamento: atributos privados + getters/setters em `Produto`, `Usuario`, `Carrinho`, `Pagamento`.
- Herança: `Cliente` e `Administrador` herdam de `Usuario`; `PagamentoCartao` e `PagamentoBoleto` herdam de `Pagamento`.
- Polimorfismo: `processarPagamento()` se comporta diferente em `PagamentoCartao` e `PagamentoBoleto`.
- Abstração: `Pagamento` é classe abstrata e define o contrato `processarPagamento()`.
