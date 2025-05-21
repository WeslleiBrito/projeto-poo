package model;
import java.text.Normalizer;
import java.util.*;
import dao.DAO;
import util.InstanciarMoeda;

public class Cofrinho {

    private List<Moeda> cofre;
    private final List<HistoricoTransacao> historicoTransacoes;
    private final DAO dao = new DAO();
    private InstanciarMoeda instanciar = new InstanciarMoeda();
    
    public Cofrinho() {
        this.atualizarCofre();
        this.historicoTransacoes = new ArrayList<>();
    }

    public void adicionar(Moeda moeda) {
    	
    	if(moeda.getValor() < 0.01) {
    		throw new IllegalArgumentException("O valor de depósito mínimo é de 0.01.");
    	}
    	
    	TipoMoeda tipoMoeda = validarMoeda(moeda.getCodigoMoeda());	
    	
    	moeda.setCambio(tipoMoeda.getCambio());
    	moeda.setNome(tipoMoeda.getNome());
    	
        this.cofre.add(moeda);
        dao.salvarMoeda(moeda);
        
        HistoricoTransacao transacao = new HistoricoTransacao(moeda, 1, getSaldo());
        dao.salvarTransacao(transacao);
    }

    public void retirarValorDeUmaTipoDeMoeda(int codigoTipoMoeda, double valor) {
    	
    	TipoMoeda tipoMoeda = validarMoeda(codigoTipoMoeda);	
    	
    	if(!moedaExisteNoCofre(codigoTipoMoeda)) {
    		throw new IllegalArgumentException("O tipo de moeda não existe.");
    	}
    	
    	List<Moeda> listaMoeda = new ArrayList<>(buscarMoedaPorId(codigoTipoMoeda));
    	
        double saldoDisponivel = listaMoeda.stream().map(m -> m.getValor()).reduce(0.0, (a, b) -> a + b);
        
        listaMoeda.sort((a, b) -> Double.compare(a.getValor(), b.getValor()));

        if (valor > saldoDisponivel) {
            throw new IllegalArgumentException("Saldo insuficiente da moeda " + tipoMoeda.getNome());
        }

        double restante = valor;
        
        
        
        Iterator<Moeda> iterator = listaMoeda.iterator();
        
        List<Integer> deletar = new ArrayList<>();
        
        EditarMoeda editar = new EditarMoeda(-1, 0);
        
        while (iterator.hasNext() && restante > 0) {
            Moeda m = iterator.next();
            
            double valMoeda = m.getValor();
            
            if (valMoeda <= restante) {
                restante -= valMoeda;
                deletar.add(m.getCodigo());
            } else {
   
            	editar.setId(m.getCodigo());
            	editar.setValor(valMoeda - restante);
                restante = 0;
            }
           
        }
        
        Moeda moedaRegistro = this.instanciar.instanciar(tipoMoeda.getCodigo());
        
        
        moedaRegistro.setNome(tipoMoeda.getNome());
        moedaRegistro.setCambio(tipoMoeda.getCambio());
        moedaRegistro.setValor(valor);
        
        HistoricoTransacao transacao = new HistoricoTransacao(moedaRegistro, 2, getSaldo());

        dao.removerMoedasPorId(deletar);
        dao.atualizarValorMoeda(editar);
        dao.salvarTransacao(transacao);
    }

    public void retirarValorSaldo(double valor) {
    	
        if (valor > getSaldo()) {
            throw new IllegalArgumentException("Saldo total insuficiente para a retirada.");
        }

        double restante = valor;
        
        List<Moeda> listaMoedas = new ArrayList<>(dao.buscarMoedas());
        
        listaMoedas.sort((a, b) -> Double.compare(a.getValor(), b.getValor()));
        

        Iterator<Moeda> iterator = listaMoedas.iterator();
        
        List<Integer> deletar = new ArrayList<>();
        
        EditarMoeda editar = new EditarMoeda(-1, 0);

        while (iterator.hasNext() && restante > 0) {
            Moeda m = iterator.next();
       
            if (m.converter() <= restante) {
                restante -= m.converter();
                deletar.add(m.getCodigo());
            } else {
            	
                double novoValor = m.getValor() - (restante / m.getCambio());
                editar.setId(m.getCodigo());
            	editar.setValor(novoValor);
                restante = 0;
            }
        }

        Moeda moedaRegistro = new Real();
        
        moedaRegistro.setCodigoMoeda(1);
        moedaRegistro.setCambio(1);
        moedaRegistro.setValor(valor);
        
        HistoricoTransacao transacao = new HistoricoTransacao(moedaRegistro, 2, getSaldo());
        
        dao.removerMoedasPorId(deletar);
        dao.atualizarValorMoeda(editar);
        dao.salvarTransacao(transacao);
    }

    public double getSaldo() {
    	
        return cofre.stream().map(m -> m.converter()).reduce(0.0, (a, b) -> a + b);
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
    
    private String normalizaChave(String chave) {
        String normalizada = Normalizer.normalize(chave.toLowerCase(), Normalizer.Form.NFD);
        return normalizada.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    public List<Moeda> classificaMoeda(){
    	
    	List<Moeda> categoriaMoeda = new ArrayList<>();
    	

    	for(Moeda moeda: cofre) {
    		
    		boolean moedaExist = false;
    		
    		for(Moeda m: categoriaMoeda) {
    			
    			if(moeda.getCodigoMoeda() == m.getCodigoMoeda()) {
    				m.setValor(m.getValor() + moeda.getValor());
    				moedaExist = true;
    			}
    		}
    		
    		if(!moedaExist) {
    			categoriaMoeda.add(moeda);
    		}
    	}
    	
    	return categoriaMoeda;
    	
    }
    
    public List<Moeda> buscarMoedaPorId(int codigoMoeda){
    	
    	return cofre.stream().filter(moeda -> moeda.getCodigoMoeda() == codigoMoeda).toList();
    }
    
    private boolean moedaExisteNoCofre(int codigo) {
    	
    	boolean teste = false;
    	
    	for(Moeda m : cofre) {
    		
    		if(m.getCodigoMoeda() == codigo) {
    			
    			teste = true;
    		}
    	}
    	
    	return teste;
    }
    
    private void atualizarCofre() {
    	this.cofre = dao.buscarMoedas();
    }
    
}
