package main;

import database.Database;
import model.Cofrinho;
import model.HistoricoTransacao;
import model.Moeda;
import model.TipoMoeda;

public class App {

	public static void main(String[] args) {
		
		Database.inicializar();
		Cofrinho cofrinho = new Cofrinho();
		
		for(Moeda moeda: cofrinho.getCofre()) {
			System.out.println(moeda.info());
		}
		
	}
}
