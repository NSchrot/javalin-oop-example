package com.ifpr.ecommerce.models;


public class PagamentoBoleto extends com.ifpr.ecommerce.models.abstracts.Pagamento {
    public PagamentoBoleto(Double valor){
        super(valor);
    }
    @Override
    public void processarPagamento() {
        System.out.println("Pagamento com boleto processado. Valor: " + getValor());
    }
}
