package com.ifpr.ecommerce.models.abstracts;

public abstract class Pagamento{

    private Double valor;

    public Pagamento(Double valor){
        this.valor = valor;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public abstract void processarPagamento();
}