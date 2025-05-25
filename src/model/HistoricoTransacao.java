package model;

import java.time.ZonedDateTime;

import dao.DAO;
import error.ErroBancoDeDadosException;

/**
 * Representa uma transação realizada em uma moeda específica,
 * armazenando dados como valores, data e tipo de transação.
 * @author Wesllei Brito
 */
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

    /**
     * Construtor da classe HistoricoTransacao.
     *
     * @param id                Identificador da transação
     * @param codigoMoeda       Código da moeda
     * @param codigoTransacao   Código do tipo de transação
     * @param nomeMoeda         Nome da moeda
     * @param valorTransacao    Valor da transação
     * @param saldoAtual        Saldo da moeda após a transação
     * @param saldoAnterior     Saldo da moeda antes da transação
     * @param dataTransacao     Data e hora da transação
     */
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

    /**
     * @return Identificador da transação
     */
    public Integer getIdTrasacao() {
		return idTrasacao;
	}

    /**
     * @return Nome da moeda
     */
	public String getNomeMoeda() {
		return nomeMoeda;
	}

    /**
     * @return Código da moeda
     */
	public int getCodigoMoeda() {
		return codigoMoeda;
	}

    /**
     * @return Tipo (nome) da moeda
     */
	public String getTipoMoeda() {
        return nomeMoeda;
    }

    /**
     * @return Valor da transação
     */
    public double getValorTransacao() {
        return valorTransacao;
    }

    /**
     * @return Data e hora em que a transação foi realizada
     */
    public ZonedDateTime getDataTransacao() {
        return dataTransacao;
    }

    /**
     * Retorna o tipo de transação associado ao código armazenado.
     *
     * @return TipoTransacao referente ao código
     * @throws ErroBancoDeDadosException se houver falha ao buscar no banco de dados
     */
    public TipoTransacao getTipoTransacao() throws ErroBancoDeDadosException {
        return dao.buscarTipoTransacaoPorID(codigoTransacao);
    }

    /**
     * @return Saldo da moeda antes da transação
     */
    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    /**
     * @return Saldo da moeda após a transação
     */
    public double getSaldoAtual() {
        return saldoAtual;
    }

    /**
     * @return Código do tipo de transação
     */
    public int getCodigoTransacao() {
		return codigoTransacao;
	}
}
