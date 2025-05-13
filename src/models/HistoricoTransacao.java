package models;

import java.time.LocalDateTime;

public class HistoricoTransacao {
	
	private String nomeMoeda;
    private double valorTrasacao;
    private LocalDateTime dataTransacao;
    private boolean tipoTransacao;  // true é entrada, false é saída
    private double saldoAnterior;
    private double saldoAtual;
  
	public HistoricoTransacao(Moeda moeda, boolean tipoTransacao, double saldo) {
		this.nomeMoeda = moeda.getNomeMoedaSingular();
        this.valorTrasacao = moeda.getValor();
        this.dataTransacao = LocalDateTime.now(); 
        this.tipoTransacao = tipoTransacao;
        this.saldoAtual = saldo;
        
        if(tipoTransacao) {
        	this.saldoAnterior = saldo - valorTrasacao;
        }else {
        	this.saldoAnterior = saldo + valorTrasacao;
        }
	}
	
	public String getNomeMoeda() {
        return nomeMoeda;
    }

    public double getValor() {
        return valorTrasacao;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public boolean isTipoTransacao() {
        return tipoTransacao;
    }
    
    public double getSaldoAnterior() {
    	return saldoAnterior;
    }
    
    public double getSaldoAtual() {
    	return saldoAtual;
    }
    
    @Override
    public String toString() {
        return "Transação [Tipo: " + (tipoTransacao ? "Entrada" : "Saída") + 
               ", Moeda: " + nomeMoeda + 
               ", Valor: " + valorTrasacao + 
               ", Data: " + dataTransacao + 
               ", Saldo anterior: " + saldoAnterior +
               ". Saldo atual: " + saldoAtual + "]";
    }
}
