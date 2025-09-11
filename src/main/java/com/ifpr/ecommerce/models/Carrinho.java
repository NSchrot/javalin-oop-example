package com.ifpr.ecommerce.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Carrinho {
    private ArrayList<Produto> produtos;
    private Double total;
    private String descricao;
    private LocalDateTime criadoEm;
    private LocalDateTime finalizadoEm;

    public Carrinho(ArrayList<Produto> produtos, String descricao, Double total, LocalDateTime criadoEm, LocalDateTime finalizadoEm) {
    this.produtos = produtos != null ? produtos : new ArrayList<>();
    this.descricao = descricao;
    this.total = total != null ? total : 0.0;
    this.criadoEm = criadoEm;
    this.finalizadoEm = finalizadoEm;
    }

    public Carrinho(LocalDateTime criadoEm){
        this.produtos = new ArrayList<>();
        this.total = 0.0;
        this.descricao = "Pendente";
        this.criadoEm = criadoEm;
        this.finalizadoEm = null;

    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getFinalizadoEm() {
        return finalizadoEm;
    }

    public void setFinalizadoEm(LocalDateTime finalizadoEm) {
        this.finalizadoEm = finalizadoEm;
    }

    public void adicionarProduto(Produto produto){
        if(produto == null) return;
        this.produtos.add(produto);
        if(produto.getPreco() != null){
            this.total = (this.total == null ? 0.0 : this.total) + produto.getPreco();
        }
    }

    public String listarItens(){
        StringBuilder sb = new StringBuilder();
        for(Produto p : produtos){
            sb.append(p.exibirInformacoes()).append(System.lineSeparator());
        }
        sb.append("Total: ").append(total);
        return sb.toString();
    }
}