package main;

import database.Database;
import model.Cofrinho;
import model.Euro;
import model.Moeda;


public class App {

	public static void main(String[] args) {
		
		Database.inicializar();
		Cofrinho cofrinho = new Cofrinho();
		Moeda mEuro = new Euro();
		mEuro.setCodigoMoeda(3);
		mEuro.setValor(15);
		
		cofrinho.retirarValorSaldo(76);;

	}
}
