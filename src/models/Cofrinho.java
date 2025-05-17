package models;

import java.text.Normalizer;
import java.util.*;

public class Cofrinho {

    private final List<Moeda> cofre;
    private final List<HistoricoTransacao> historicoTransacoes;

    public Cofrinho() {
        this.cofre = new ArrayList<>();
        this.historicoTransacoes = new ArrayList<>();
    }

    public void adicionar(TipoMoeda tipo, double valor) {
    	if (tipo == null) throw new IllegalArgumentException("O tipo de moeda não pode ser nulo.");
    	
    	Moeda moeda = instanciarMoeda(tipo, valor);
    	
        this.cofre.add(moeda);
        registrarTransacao(moeda, true);
    }

    public void retirarValorDeUmaTipoDeMoeda(TipoMoeda tipo, double valor) {
        if (tipo == null) throw new IllegalArgumentException("O tipo de moeda não pode ser nulo.");

        double saldoTipo = cofre.stream()
                .filter(m -> m.getTipo() == tipo)
                .mapToDouble(Moeda::getValor)
                .sum();

        if (valor > saldoTipo) {
            throw new IllegalArgumentException("Saldo insuficiente da moeda " + tipo.getNomePlural());
        }

        double restante = valor;
        Iterator<Moeda> iterator = cofre.iterator();

        while (iterator.hasNext() && restante > 0) {
            Moeda m = iterator.next();
            if (m.getTipo() == tipo) {
                double valMoeda = m.getValor();
                if (valMoeda <= restante) {
                    restante -= valMoeda;
                    iterator.remove();
                } else {
                    m.setValor(valMoeda - restante);
                    restante = 0;
                }
            }
        }

        registrarTransacao(instanciarMoeda(tipo, valor), false);
    }

    public void retirarValor(double valor) {
        if (valor > getSaldo()) {
            throw new IllegalArgumentException("Saldo total insuficiente para a retirada.");
        }

        double restante = valor;

        cofre.sort(Comparator.comparingDouble(Moeda::converter));

        Iterator<Moeda> iterator = cofre.iterator();

        while (iterator.hasNext() && restante > 0) {
            Moeda m = iterator.next();
            double valorConvertido = m.converter();

            if (valorConvertido <= restante) {
                restante -= valorConvertido;
                iterator.remove();
            } else {
                double novoValor = m.getValor() - (restante / m.getCambio());
                m.setValor(novoValor);
                restante = 0;
            }
        }

        double valorRetirado = valor - restante;
        registrarTransacao(new Real(valorRetirado), false); 
    }

    public double getSaldo() {
        return cofre.stream().mapToDouble(Moeda::converter).sum();
    }

    public Map<String, Double> valorPorMoeda() {
        Map<String, Double> mapa = new HashMap<>();
        for (Moeda moeda : cofre) {
            String chave = normalizaChave(moeda.getNomeMoedaSingular());
            mapa.merge(chave, moeda.getValor(), Double::sum);
        }
        return mapa;
    }

    public List<HistoricoTransacao> getHistorico() {
        return historicoTransacoes;
    }

    // ========== AUXILIARES ==========

    private void registrarTransacao(Moeda moeda, boolean tipoTransacao) {
        historicoTransacoes.add(new HistoricoTransacao(moeda, tipoTransacao, getSaldo()));
    }

    private String normalizaChave(String chave) {
        String normalizada = Normalizer.normalize(chave.toLowerCase(), Normalizer.Form.NFD);
        return normalizada.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    private Moeda instanciarMoeda(TipoMoeda tipo, double valor) {
        return switch (tipo) {
            case REAL -> new Real(valor);
            case DOLAR -> new Dolar(valor);
            case EURO -> new Euro(valor);
        };
    }

}
