package main;

import models.Cofrinho;
import models.HistoricoTransacao;
import models.TipoMoeda;

public class App {

	public static void main(String[] args) {
		
		Cofrinho cofrinho = new Cofrinho();
		
		cofrinho.adicionar(TipoMoeda.DOLAR, 2);
		cofrinho.adicionar(TipoMoeda.REAL, 50);
		cofrinho.adicionar(TipoMoeda.DOLAR, 7);
		cofrinho.adicionar(TipoMoeda.EURO, 10);
		
		cofrinho.retirarValorDeUmaTipoDeMoeda(TipoMoeda.DOLAR, 5);
		cofrinho.retirarValor(15);
		
		for(HistoricoTransacao historico : cofrinho.getHistorico()) {
			System.out.println(historico.toString());
		}
		
		
	}
}
