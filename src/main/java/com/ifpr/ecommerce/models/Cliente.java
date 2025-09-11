package com.ifpr.ecommerce.models;

public class Cliente extends Usuario {

    private Carrinho carrinho;

    public Cliente(String nome, String email) {
        super(nome, email);
    this.carrinho = new Carrinho(java.time.LocalDateTime.now());
    }

    public Boolean adicionarProduto(Produto newProduto) {
        if (newProduto == null)
            return false;
        this.carrinho.adicionarProduto(newProduto);
        return true;
    }                     
    
    public String listarCarrinho(){
        return this.carrinho.listarItens();
    }
    
    public Carrinho getCarrinho(){
        return this.carrinho;
    }
    
    public void setCarrinho(Carrinho carrinho){
        this.carrinho = carrinho;
    }
    
}