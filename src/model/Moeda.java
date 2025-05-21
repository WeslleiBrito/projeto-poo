package model;


public abstract class Moeda {

	private int codigo;
	private String nome;
	private double cambio;
    private double valor;
    private int codigoMoeda;
    
    
 
	public Moeda() {

    }

    public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public void setCambio(double cambio) {
		this.cambio = cambio;
	}

	public void setCodigoMoeda(int codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}

	public String info() {   
        return "Você possui: " + String.format("%.2f", valor) + " em " + nome.toLowerCase() + ".";
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
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
