package model;

public class TipoMoeda {

    private int codigo;
    private String nome;
    private double cambio;

    public TipoMoeda(int codigo, String nome, double cambio) {
        this.codigo = codigo;
        this.nome = nome;
        this.cambio = cambio;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public double getCambio() {
        return cambio;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TipoMoeda)) return false;
        TipoMoeda outra = (TipoMoeda) obj;
        return this.codigo == outra.codigo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(codigo);
    }

    @Override
    public String toString() {
        return nome + " (câmbio: " + cambio + ")";
    }
}
