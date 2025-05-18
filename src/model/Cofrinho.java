package model;

import java.text.Normalizer;
import java.util.*;

import dao.DAO;

public class Cofrinho {

    private final List<Moeda> cofre;
    private final List<HistoricoTransacao> historicoTransacoes;
    private final DAO dao = new DAO();

    public Cofrinho() {
        this.cofre = dao.buscarMoedas();
        this.historicoTransacoes = new ArrayList<>();
    }

    public void adicionar(int codigoMoeda, double valor) {
    	
    	TipoMoeda tipoMoeda = validarMoeda(codigoMoeda);
    	
    	Moeda moeda = instanciarMoeda(tipoMoeda.getCodigo(), valor);   	
    	
        this.cofre.add(moeda);
        dao.salvarMoeda(moeda);
        
        HistoricoTransacao transacao = new HistoricoTransacao(moeda, 1, getSaldo());
        
        dao.salvarTransacao(transacao);
        registrarTransacao(moeda, 1);
    }

    public void retirarValorDeUmaTipoDeMoeda(int codigoMoeda, double valor) {
    	
    	TipoMoeda tipoMoeda = validarMoeda(codigoMoeda);


        double saldoTipo = cofre.stream()
                .filter(m -> m.getCodigo() == tipoMoeda.getCodigo())
                .mapToDouble(Moeda::getValor)
                .sum();

        if (valor > saldoTipo) {
            throw new IllegalArgumentException("Saldo insuficiente da moeda " + tipoMoeda.getNome());
        }

        double restante = valor;
        Iterator<Moeda> iterator = cofre.iterator();

        while (iterator.hasNext() && restante > 0) {
            Moeda m = iterator.next();
            if (m.getCodigo() == tipoMoeda.getCodigo()) {
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

        registrarTransacao(instanciarMoeda(tipoMoeda.getCodigo(), valor), 2);
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
        registrarTransacao(new Real(1, valorRetirado), 2); 
    }

    public double getSaldo() {
        return cofre.stream().mapToDouble(Moeda::converter).sum();
    }
    
    public List<Moeda> getCofre() {
		return cofre;
	}

	public Map<String, Double> valorPorMoeda() {
        Map<String, Double> mapa = new HashMap<>();
        for (Moeda moeda : cofre) {
            String chave = normalizaChave(moeda.getNome());
            mapa.merge(chave, moeda.getValor(), Double::sum);
        }
        return mapa;
    }

    public List<HistoricoTransacao> getHistorico() {
        return historicoTransacoes;
    }

    
    private TipoMoeda validarMoeda(int codigo) {
    	
    	TipoMoeda tipoMoedaExiste = this.dao.buscarTipoMoedaPorID(codigo);
    	
    	if (tipoMoedaExiste == null) throw new IllegalArgumentException("O tipo de moeda não existe.");
    	
    	return tipoMoedaExiste;
    }
    
    private void registrarTransacao(Moeda moeda, int tipoTransacao) {
    	HistoricoTransacao trasacao = new HistoricoTransacao(moeda, tipoTransacao, getSaldo());
        historicoTransacoes.add(trasacao);
    }

    private String normalizaChave(String chave) {
        String normalizada = Normalizer.normalize(chave.toLowerCase(), Normalizer.Form.NFD);
        return normalizada.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    private Moeda instanciarMoeda(int codigoMoeda, double valor) {
        return switch (codigoMoeda) {
            case 1 -> new Real(1, valor);
            case 2 -> new Dolar(2, valor);
            case 3 -> new Euro(2, valor);
		default -> throw new IllegalArgumentException("Unexpected value: " + codigoMoeda);
        };
    }

}
