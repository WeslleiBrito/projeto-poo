package model;

import java.time.ZonedDateTime;

import dao.DAO;

public class HistoricoTransacao {
	
	private Integer idTrasacao;
    private String nomeMoeda;
    private int codigoMoeda;
    private double valorTransacao;
    private ZonedDateTime dataTransacao;
    private int codigoTransacao;
    private double saldoAnterior;
    private double saldoAtual;
    private DAO dao = new DAO();
  
    
    public HistoricoTransacao(Integer id, int codigoMoeda, int codigoTransacao, String nomeMoeda, double valorTransacao,
    		 double saldoAtual, double saldoAnterior, ZonedDateTime dataTransacao) {
    	
    	this.idTrasacao = id;
    	this.codigoMoeda = codigoMoeda;
    	this.codigoTransacao = codigoTransacao;
        this.nomeMoeda = nomeMoeda;
        this.valorTransacao = valorTransacao;
        this.saldoAtual = saldoAtual;
        this.saldoAnterior = saldoAnterior;
        this.dataTransacao = dataTransacao;
    }

    public Integer getIdTrasacao() {
		return idTrasacao;
	}

	public String getNomeMoeda() {
		return nomeMoeda;
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


    public ZonedDateTime getDataTransacao() {
        return dataTransacao;
    }

    public TipoTransacao getTipoTransacao() {
        return dao.buscarTipoTransacaoPorID(codigoTransacao);
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
                "Tipo: " + getTipoTransacao().getNome() +
                ", Moeda: " + nomeMoeda +
                ", Valor original: " + valorTransacao +
                ", Data: " + dataTransacao +
                ", Saldo anterior: " + saldoAnterior +
                ", Saldo atual: " + saldoAtual +
                ']';
    }

	
}
