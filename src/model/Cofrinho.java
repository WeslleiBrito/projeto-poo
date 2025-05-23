package model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import dao.DAO;
import util.InstanciarMoeda;

public class Cofrinho {

    private final DAO dao = new DAO();
    private InstanciarMoeda instanciar = new InstanciarMoeda();
    
    
    public void adicionar(Moeda moeda) {
    	
    	if(moeda.getValor() < 0.01) {
    		throw new IllegalArgumentException("O valor de depósito mínimo é de 0.01.");
    	}
    	
    	TipoMoeda tipoMoeda = validarMoeda(moeda.getCodigoMoeda());	
    	
    	moeda.setCambio(tipoMoeda.getCambio());
    	moeda.setNome(tipoMoeda.getNome());
    	
        
        
        int idTipoMoeda = moeda.getCodigoMoeda();
        String nomeMoeda = moeda.getNome();
        double valorTransacao = moeda.getValor();
        ZonedDateTime dataTrasacao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        double saldoAnterior = getSaldo();
        double saldoAtual = getSaldo() + moeda.converter();
        int tipoTrasacao = 1;
        
        HistoricoTransacao transacao = new HistoricoTransacao(null, idTipoMoeda, tipoTrasacao, nomeMoeda, 
        		valorTransacao, saldoAtual, saldoAnterior, dataTrasacao);
        
        dao.salvarMoeda(moeda);
        dao.salvarTransacao(transacao);
    }

    public void retirarValorDeUmaTipoDeMoeda(int codigoTipoMoeda, double valor) {
    	
    	TipoMoeda tipoMoeda = validarMoeda(codigoTipoMoeda);	
    	
    	if(!moedaExiste(codigoTipoMoeda)) {
    		throw new IllegalArgumentException("O tipo de moeda não existe.");
    	}
    	
    	List<Moeda> listaMoeda = new ArrayList<>(selecionarMoedasPorId(codigoTipoMoeda));
    	
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
        
        moedaRegistro.setCodigoMoeda(codigoTipoMoeda);
        moedaRegistro.setNome(tipoMoeda.getNome());
        moedaRegistro.setCambio(tipoMoeda.getCambio());
        moedaRegistro.setValor(valor);
        
        int idTipoMoeda = moedaRegistro.getCodigoMoeda();
        String nomeMoeda = moedaRegistro.getNome();
        double valorTransacao = moedaRegistro.getValor();
        ZonedDateTime dataTrasacao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        double saldoAnterior = getSaldo();
        double saldoAtual = getSaldo() - moedaRegistro.converter();
        int tipoTrasacao = 2;
        
        HistoricoTransacao transacao = new HistoricoTransacao(null, idTipoMoeda, tipoTrasacao, nomeMoeda, 
        		valorTransacao, saldoAtual, saldoAnterior, dataTrasacao);

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
        
        
        Moeda moedaRegistro = selecionarMoedasPorId(1).getFirst();
        
        
        int idTipoMoeda = moedaRegistro.getCodigoMoeda();
        String nomeMoeda = moedaRegistro.getNome();
        double valorTransacao = moedaRegistro.getValor();
        ZonedDateTime dataTrasacao = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        double saldoAnterior = getSaldo();
        double saldoAtual = getSaldo() - moedaRegistro.converter();
        int tipoTrasacao = 2;
        
        HistoricoTransacao transacao = new HistoricoTransacao(null, idTipoMoeda, tipoTrasacao, nomeMoeda, 
        		valorTransacao, saldoAtual, saldoAnterior, dataTrasacao);
        
        dao.removerMoedasPorId(deletar);
        dao.atualizarValorMoeda(editar);
        dao.salvarTransacao(transacao);
    }

    public double getSaldo() {
    	
        return dao.buscarMoedas().stream().map(m -> m.converter()).reduce(0.0, (a, b) -> a + b);
    }
    
    public List<HistoricoTransacao> getHistorico() {
        return dao.buscarHistoricoTransacoes();
    }
    
    
    public List<Moeda> selecionarMoedasPorId(int codigoMoeda){
    	
    	return dao.buscarMoedas().stream().filter(moeda -> moeda.getCodigoMoeda() == codigoMoeda).toList();
    }
    
    private boolean moedaExiste(int codigo) {
    	
    	boolean teste = false;
    	
    	for(Moeda m : dao.buscarMoedas()) {
    		
    		if(m.getCodigoMoeda() == codigo) {
    			
    			teste = true;
    		}
    	}
    	
    	return teste;
    }
    
    private TipoMoeda validarMoeda(int codigo) {
    	
    	TipoMoeda tipoMoedaExiste = this.dao.buscarTipoMoedaPorID(codigo);
    	
    	if (tipoMoedaExiste == null) throw new IllegalArgumentException("O tipo de moeda não existe.");
    	
    	return tipoMoedaExiste;
    }

    
}
