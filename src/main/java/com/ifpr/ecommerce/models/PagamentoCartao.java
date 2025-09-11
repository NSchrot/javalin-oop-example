package com.ifpr.ecommerce.models;


public class PagamentoCartao extends com.ifpr.ecommerce.models.abstracts.Pagamento {
    public PagamentoCartao(Double valor){
        super(valor);
    }
    @Override
    public void processarPagamento() {
        System.out.println("Pagamento com cartão processado. Valor: " + getValor());
    }
}
