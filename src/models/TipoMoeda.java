package models;

public enum TipoMoeda {
    REAL("Real", "Reais", 1.0),
    DOLAR("Dólar", "Dólares", 5.0),
    EURO("Euro", "Euros", 6.0);

    private final String nomeSingular;
    private final String nomePlural;
    private final double cambio;

    TipoMoeda(String singular, String plural, double cambio) {
        this.nomeSingular = singular;
        this.nomePlural = plural;
        this.cambio = cambio;
    }

    public String getNomeSingular() {
        return nomeSingular;
    }

    public String getNomePlural() {
        return nomePlural;
    }

    public double getCambio() {
        return cambio;
    }

    public static TipoMoeda fromString(String nome) {
        for (TipoMoeda tipo : TipoMoeda.values()) {
            if (tipo.getNomeSingular().equalsIgnoreCase(nome)
             || tipo.getNomePlural().equalsIgnoreCase(nome)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de moeda inválido: " + nome);
    }
}