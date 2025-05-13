package models;


public abstract class Moeda {
	
	private double valor;
	private double cambio; 
	private String nomeMoedaSingular;
	private String nomeMoedaPlural;
	
	public Moeda(String nomeMoedaSingular, String nomeMoedaPlural, double valor, double cambio) {
		
		if (valor < 0) {
			
			throw new  IllegalArgumentException("Informe um valor maior que zero.");
		}
		
		if (nomeMoedaSingular == null || nomeMoedaSingular.trim().isEmpty()) {
	            throw new IllegalArgumentException("O nome da moeda não pode ser nulo ou vazio.");
        }
		
		if (nomeMoedaPlural == null || nomeMoedaPlural.trim().isEmpty()) {
			throw new IllegalArgumentException("O nome da moeda não pode ser nulo ou vazio.");
		}
		
		if(cambio < 0) {
			throw new  IllegalArgumentException("O câmbio não pode ser negativo.");
		}
		
		
		this.valor = valor;
		this.nomeMoedaSingular = nomeMoedaSingular;
		this.nomeMoedaPlural = nomeMoedaPlural;
		this.cambio = (cambio == 0) ? 1 : cambio;
		
	}
	
	public String info() {
		return "Você possui: " + String.format("%.2f", this.getValor()) +  (this.getValor() > 1 ? " " + nomeMoedaPlural.toLowerCase() : (this.getValor() > 0 && this.getValor() < 1) ? String.join(" centavos de ", nomeMoedaSingular.toLowerCase()): "" + String.join(" ", nomeMoedaSingular)) + ".";
	}
	
	public double converter() { return valor * cambio;};
	
	public double getValor() {
		return this.valor;
	}
	
	public String getNomeMoedaSingular() {
		return this.nomeMoedaSingular;
	}
	
	public String getNomeMoedaPlural() {
		return this.nomeMoedaPlural;
	}

	public double getCambio() {return cambio;}
	
	public void setValor(double valor) {
	    this.valor = valor;
	}
	
	
}


