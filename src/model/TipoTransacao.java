package model;

public class TipoTransacao {

    private int codigo;
    private String nome;

    public TipoTransacao(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TipoTransacao)) return false;
        TipoTransacao outro = (TipoTransacao) obj;
        return this.codigo == outro.codigo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(codigo);
    }
}
