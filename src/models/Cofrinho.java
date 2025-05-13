package models;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;



public class Cofrinho {
	
	private ArrayList<Moeda> cofre;
	private double saldo = 0.0;
	private ArrayList<HistoricoTransacao> historicoTransacoes;
	
	public Cofrinho() {
		cofre = new ArrayList<>();
		atualizarSaldo();
		this.historicoTransacoes = new ArrayList<>();
	}
	
	public void adicionar(Moeda moeda) {
		this.cofre.add(moeda);
		atualizarSaldo();
		
		historicoTransacoes.add(new HistoricoTransacao(moeda, true, saldo));
	}
	
	private boolean retiradaValida (String nomeMoeda, double valor, boolean paraEstaMoeda) {
		
		Map<String, Double> saldoPorMoeda = agruparValorPorMoeda();
		
	    String chave = normalizaChave(nomeMoeda);
	    
	    if(paraEstaMoeda) {
	    	
	    	if(saldoPorMoeda.containsKey(chave)) {
				
				if(valor > saldoPorMoeda.get(chave)) {
					throw new IllegalArgumentException("Você não tem saldo suficiente para retirada dessa moeda.");
				}	
				
			}else {
				throw new IllegalArgumentException("A moeda informada não existe.");
			}
	    	
	    }else {
			
	        if (valor > saldo) {
	            throw new IllegalArgumentException("Saldo total insuficiente para a retirada.");
	        }
	        
		}
		
		return true;
	}
	
	private static String normalizaChave(String chave) {
		String normalizada = Normalizer.normalize(chave.toLowerCase(), Normalizer.Form.NFD);
		return normalizada.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	private Map<String, Double> agruparValorPorMoeda() {
	    Map<String, Double> mapaAgrupado = new HashMap<>();

	    cofre.forEach(moeda -> {
	        String chave = normalizaChave(moeda.getNomeMoedaSingular());
	        double valor = moeda.getValor();

	        mapaAgrupado.merge(chave, valor, Double::sum);
	    });

	    return mapaAgrupado;
	}
	
	private void atualizarSaldo() {
		saldo = cofre.stream().mapToDouble(m -> m.converter()).sum();
	}
	
	private void registrarTransacao(Moeda moeda, boolean tipoTransacao) {
	    historicoTransacoes.add(new HistoricoTransacao(moeda, tipoTransacao, saldo));
	}
	
	public Map<String, Double> valorPorMoeda(){
		return agruparValorPorMoeda();
	}
	
	public void retirarValorDeUmaTipoDeMoeda(Moeda moeda, double valor) {

	    if (retiradaValida(moeda.getNomeMoedaSingular(), valor, true)) {
	        String chave = normalizaChave(moeda.getNomeMoedaSingular());
	        double restante = valor;

	        for (int i = 0; i < cofre.size(); i++) {
	            Moeda m = cofre.get(i);

	            if (normalizaChave(m.getNomeMoedaSingular()).equals(chave)) {
	                double valMoeda = m.getValor();

	                if (valMoeda <= restante) {
	                    // Remove moeda inteira
	                    restante -= valMoeda;
	                    cofre.remove(i);
	                    i--; // Corrige índice após remoção
	                } else {
	                    
	                    m.setValor(valMoeda - restante);
	                    restante = 0;
	                    break;
	                }

	                if (restante <= 0) break;
	            }
	        }

	        // Atualiza saldo
	        saldo -= valor;
	        if (saldo < 0) saldo = 0;
	    }
	    
	    atualizarSaldo();
	    registrarTransacao(moeda, false);
	}

	public void retirarValor(double valor) {
	    if (retiradaValida("", valor, false)) {
	        double restante = valor;

	        // Ordena do menor para o maior valor
	        cofre.sort(Comparator.comparingDouble(Moeda::converter));

	        for (int i = 0; i < cofre.size(); i++) {
	            Moeda m = cofre.get(i);
	            double valMoeda = m.getValor();

	            if (m.converter() <= restante) {
	                restante -= m.converter();
	                cofre.remove(i);
	                i--; // Corrige o índice após a remoção
	            } else {
	                // Moeda maior que o valor restante: reduz parcialmente
	            	
	                m.setValor(valMoeda - restante / m.getCambio());
	                restante = 0;
	                break;
	            }

	            if (restante <= 0) break;
	        }

	        // Atualiza o saldo
	        saldo -= valor;
	        if (saldo < 0) saldo = 0;
	    }
	    
	    atualizarSaldo();
	    registrarTransacao(new Real(valor, 1), false);
	}

	public double getSaldo() {return saldo;}
	
	public ArrayList<HistoricoTransacao> getHistorico() {return historicoTransacoes;}
}
