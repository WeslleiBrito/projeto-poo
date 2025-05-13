package main;

import models.Cofrinho;
import models.Dolar;
import models.HistoricoTransacao;
import models.Real;

public class App {

	public static void main(String[] args) {
		
		Cofrinho cofrinho = new Cofrinho();
		
		Real real = new Real(50, 1);
		Dolar dolar = new Dolar(2, 5);
		Dolar dolar2 = new Dolar(7, 5);
		
		cofrinho.adicionar(dolar);
		cofrinho.adicionar(real);
		cofrinho.adicionar(dolar2);
		cofrinho.retirarValorDeUmaTipoDeMoeda(dolar, 5);
		cofrinho.retirarValor(15);
		
		for(HistoricoTransacao historico : cofrinho.getHistorico()) {
			System.out.println(historico.toString());
		}
		
		
	}
}
