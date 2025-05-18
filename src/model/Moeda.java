package model;

import dao.DAO;

public abstract class Moeda {

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	private int codigo;
	private String nome;
	private double cambio;
    private double valor;
    private int codigoMoeda;
    
    public Moeda(int codigoMoeda, double valor) {
    	
    	DAO dao = new DAO();
    	
    	TipoMoeda tipoMoedaExiste = dao.buscarTipoMoedaPorID(codigoMoeda);
    	
    	if(tipoMoedaExiste == null) {throw new IllegalArgumentException("A moeda informada não existe.");}
    	
    	this.codigo = tipoMoedaExiste.getCodigo();
        this.valor = valor;
        this.nome = tipoMoedaExiste.getNome();
        this.cambio = tipoMoedaExiste.getCambio();
        this.codigo = tipoMoedaExiste.getCodigo();
    }

    public String info() {   
        return "Você possui: " + String.format("%.2f", valor) + " " + nome.toLowerCase() + ".";
    }

    public double converter() {
        return valor * cambio;
    }

    public int getCodigo() {
		return codigo;
	}

	public double getValor() {
        return valor;
    }


    public double getCambio() {
        return cambio;
    }

    
    public void setValor(double valor) {
        this.valor = valor;
    }

	public int getCodigoMoeda() {
		return codigoMoeda;
	}
}
