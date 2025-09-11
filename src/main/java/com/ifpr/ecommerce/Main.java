package com.ifpr.ecommerce;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.*;
import com.ifpr.ecommerce.models.*;

public class Main {
    public static List<Produto> catalogo = new ArrayList<>();
    public static Cliente clienteDemo = new Cliente("Cliente Demo", "cliente@demo.com");
    public static Administrador adminDemo = new Administrador("Admin", "admin@demo.com");
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });
        }).start(7575);

        app.get("/tads24", ctx -> ctx.result("TADS24 EH TOP"));
        app.get("/", ctx -> ctx.redirect("index.html"));
        app.get("/produtos", ctx -> ctx.redirect("produtos.html"));
        app.get("/carrinho", ctx -> ctx.redirect("carrinho.html"));
        app.get("/admin", ctx -> ctx.redirect("admin.html"));

        app.get("/api/produtos", ctx -> {
            ctx.contentType("application/json");
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(int i=0; i<catalogo.size(); i++){
                Produto p = catalogo.get(i);
                sb.append("{\"nome\":\"").append(p.getNome()).append("\"")
                  .append(",\"preco\":").append(p.getPreco())
                  .append(",\"estoque\":").append(p.getEstoque())
                  .append("}");
                if(i < catalogo.size()-1) sb.append(",");
            }
            sb.append("]");
            ctx.result(sb.toString());
        });

        app.get("/admin/produtos", ctx -> {
            StringBuilder sb = new StringBuilder();
            for(Produto p : catalogo){
                sb.append(p.exibirInformacoes()).append(System.lineSeparator());
            }
            if(sb.length() == 0){
                ctx.result("Catálogo vazio");
            } else {
                ctx.result(sb.toString());
            }
        });

        app.post("/admin/produtos", ctx -> {
            String nome = ctx.queryParam("nome");
            String precoStr = ctx.queryParam("preco");
            String estoqueStr = ctx.queryParam("estoque");
            if(nome == null || precoStr == null || estoqueStr == null){
                ctx.status(400).result("Parâmetros obrigatórios: nome, preco, estoque");
                return;
            }
            double preco;
            int estoque;
            try {
                preco = Double.parseDouble(precoStr);
                estoque = Integer.parseInt(estoqueStr);
            } catch (NumberFormatException e){
                ctx.status(400).result("preco/estoque inválidos");
                return;
            }
            Produto novo = new Produto(nome, preco, estoque);
            boolean ok = adminDemo.cadastrarProduto(novo);
            ctx.result(ok ? "Produto cadastrado" : "Falha ao cadastrar");
        });

        app.delete("/admin/produtos", ctx -> {
            String nome = ctx.queryParam("nome");
            if(nome == null){
                ctx.status(400).result("Parâmetro obrigatório: nome");
                return;
            }
            boolean ok = adminDemo.removerProduto(nome);
            ctx.result(ok ? "Produto removido" : "Produto não encontrado");
        });

        app.post("/api/carrinho/adicionar", ctx -> {
            String nome = ctx.queryParam("nome");
            if(nome == null){
                ctx.status(400).result("Parâmetro obrigatório: nome");
                return;
            }
            Optional<Produto> prod = catalogo.stream().filter(p -> nome.equalsIgnoreCase(p.getNome())).findFirst();
            if(prod.isEmpty()){
                ctx.status(404).result("Produto não encontrado");
                return;
            }
            boolean ok = clienteDemo.adicionarProduto(prod.get());
            ctx.result(ok ? "Adicionado ao carrinho" : "Falha ao adicionar");
        });

        app.get("/api/carrinho", ctx -> {
            ctx.contentType("application/json");
            var carrinho = clienteDemo.getCarrinho();
            var produtos = carrinho.getProdutos();
            StringBuilder sb = new StringBuilder();
            sb.append("{\"itens\":[");
            for(int i=0; i<produtos.size(); i++){
                var p = produtos.get(i);
                sb.append("{\"nome\":\"").append(p.getNome()).append("\"")
                  .append(",\"preco\":").append(p.getPreco())
                  .append(",\"estoque\":").append(p.getEstoque())
                  .append("}");
                if(i < produtos.size()-1) sb.append(",");
            }
            sb.append("],\"total\":").append(carrinho.getTotal()).append("}");
            ctx.result(sb.toString());
        });

        app.post("/api/checkout", ctx -> {
            String tipo = ctx.queryParam("tipo");
            double total = clienteDemo.getCarrinho().getTotal();
            if(total <= 0){
                ctx.status(400).contentType("application/json").result("{\"status\":\"erro\",\"mensagem\":\"Carrinho vazio\"}");
                return;
            }
            com.ifpr.ecommerce.models.abstracts.Pagamento pagamento;
            String pagamentoTipo;
            if("cartao".equalsIgnoreCase(tipo)){
                pagamento = new PagamentoCartao(total);
                pagamentoTipo = "Cartão";
            } else if("boleto".equalsIgnoreCase(tipo)){
                pagamento = new PagamentoBoleto(total);
                pagamentoTipo = "Boleto";
            } else {
                ctx.status(400).contentType("application/json").result("{\"status\":\"erro\",\"mensagem\":\"Tipo inválido. Use cartao ou boleto\"}");
                return;
            }
            pagamento.processarPagamento();
            var carrinho = clienteDemo.getCarrinho();
            var produtos = carrinho.getProdutos();
            StringBuilder sb = new StringBuilder();
            sb.append("{\"status\":\"sucesso\",\"cliente\":{\"nome\":\"")
              .append(clienteDemo.getNome()).append("\",\"email\":\"").append(clienteDemo.getEmail()).append("\"},");
            sb.append("\"pagamento\":\"").append(pagamentoTipo).append("\",");
            sb.append("\"itens\":[");
            for(int i=0; i<produtos.size(); i++){
                var p = produtos.get(i);
                sb.append("{\"nome\":\"").append(p.getNome()).append("\"")
                  .append(",\"preco\":").append(p.getPreco())
                  .append(",\"estoque\":").append(p.getEstoque())
                  .append("}");
                if(i < produtos.size()-1) sb.append(",");
            }
            sb.append("],\"total\":").append(carrinho.getTotal()).append("}");
            ctx.contentType("application/json").result(sb.toString());
        });
    }
}
