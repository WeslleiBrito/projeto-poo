package model;

import java.time.LocalDateTime;

import dao.DAO;

public class HistoricoTransacao {

    private String nomeMoeda;
    private int codigoMoeda;
    private double valorTransacao;
    private double valorConvertido;
    private LocalDateTime dataTransacao;
    private TipoTransacao tipoTransacao;
    private int codigoTransacao;
    private double saldoAnterior;
    private double saldoAtual;

    public HistoricoTransacao(Moeda moeda, int codigoTransacao, double saldoAtual) {
    	
    	DAO dao = new DAO();
    	
    	TipoTransacao tipoTransacaoExise = dao.buscarTipoTransacaoPorID(codigoTransacao);
    	
    	if(tipoTransacaoExise == null) { throw new IllegalArgumentException("O tipo transação informado não existe."); }
    	
        this.nomeMoeda = moeda.getNome();
        this.valorTransacao = moeda.getValor();
        this.valorConvertido = moeda.converter();
        this.dataTransacao = LocalDateTime.now();
        this.saldoAtual = saldoAtual;
        this.tipoTransacao = tipoTransacaoExise;
        
        this.codigoTransacao = this.tipoTransacao.getCodigo();
        this.codigoMoeda = moeda.getCodigoMoeda();

        if (this.tipoTransacao.getCodigo() == 1) {
        	
            this.saldoAnterior = saldoAtual - valorConvertido;
        } else {
            this.saldoAnterior = saldoAtual + valorConvertido;
        }
        
    }

    public int getCodigoMoeda() {
		return codigoMoeda;
	}

	public String getTipoMoeda() {
        return nomeMoeda;
    }

    public double getValorTransacao() {
        return valorTransacao;
    }

    public double getValorConvertido() {
        return valorConvertido;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }
    
    public int getCodigoTransacao() {
		return codigoTransacao;
	}
    
    @Override
    public String toString() {
        return "Transação [" +
                "Tipo: " + tipoTransacao.getNome() +
                ", Moeda: " + nomeMoeda +
                ", Valor original: " + valorTransacao +
                ", Valor convertido: " + valorConvertido +
                ", Data: " + dataTransacao +
                ", Saldo anterior: " + saldoAnterior +
                ", Saldo atual: " + saldoAtual +
                ']';
    }

	
}
