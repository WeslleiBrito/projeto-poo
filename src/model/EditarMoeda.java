package model;

public class EditarMoeda {

	private int id;
	private double valor;
	
	public EditarMoeda(int id, double valor) {
		this.id = id;
		this.valor = valor;
	}

	public int getId() {
		return id;
	}

	public double getValor() {
		return valor;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
	

}
