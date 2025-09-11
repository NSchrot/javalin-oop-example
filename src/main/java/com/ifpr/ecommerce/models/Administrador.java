package com.ifpr.ecommerce.models;

public class Administrador extends Usuario {
    public Administrador(String nome, String email) {
        super(nome, email);
    }
    public Boolean cadastrarProduto(Produto newProduto) {
        if (newProduto == null)
            return false;
        com.ifpr.ecommerce.Main.catalogo.add(newProduto);
        return true;
    }
    public Boolean removerProduto(String nomeProduto){
        if(nomeProduto == null) return false;
        return com.ifpr.ecommerce.Main.catalogo.removeIf(p -> nomeProduto.equalsIgnoreCase(p.getNome()));
    }
}
