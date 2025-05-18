package main;

import database.Database;
import model.Cofrinho;
import model.HistoricoTransacao;
import model.TipoMoeda;

public class App {

	public static void main(String[] args) {
		
		Database.inicializar();
		Cofrinho cofrinho = new Cofrinho();
		
		cofrinho.adicionar(2, 2);
		/**cofrinho.adicionar(TipoMoeda.REAL, 50);
		cofrinho.adicionar(TipoMoeda.DOLAR, 7);
		cofrinho.adicionar(TipoMoeda.EURO, 10);
		
		cofrinho.retirarValorDeUmaTipoDeMoeda(TipoMoeda.DOLAR, 5);
		cofrinho.retirarValor(15);**/

		
		
	}
}
