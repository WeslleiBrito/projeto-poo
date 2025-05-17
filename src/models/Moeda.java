package models;

public abstract class Moeda {

    private double valor;
    private TipoMoeda tipo;

    public Moeda(TipoMoeda tipo, double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Informe um valor maior que zero.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de moeda não pode ser nulo.");
        }

        this.valor = valor;
        this.tipo = tipo;
    }

    public String info() {
        String nome = valor > 1 ? tipo.getNomePlural() : tipo.getNomeSingular();
        return "Você possui: " + String.format("%.2f", valor) + " " + nome.toLowerCase() + ".";
    }

    public double converter() {
        return valor * tipo.getCambio();
    }

    public double getValor() {
        return this.valor;
    }

    public String getNomeMoedaSingular() {
        return tipo.getNomeSingular();
    }

    public String getNomeMoedaPlural() {
        return tipo.getNomePlural();
    }

    public double getCambio() {
        return tipo.getCambio();
    }

    public TipoMoeda getTipo() {
        return tipo;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
