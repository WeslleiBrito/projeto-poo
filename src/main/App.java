package main;

import database.Database;
import model.Cofrinho;
import model.Euro;
import model.HistoricoTransacao;
import model.Moeda;
import model.Real;


public class App {

	public static void main(String[] args) {
		
		Database.inicializar();
		Cofrinho cofrinho = new Cofrinho();
		Moeda mEuro = new Euro();
		mEuro.setCodigoMoeda(3);
		mEuro.setValor(15);
		
		Moeda mReal = new Real();
		
		mReal.setCodigoMoeda(1);
		mReal.setCambio(1);
		mReal.setValor(100);
		
		cofrinho.adicionar(mReal);
		cofrinho.retirarValorDeUmaTipoDeMoeda(1, 50);
		
		for (HistoricoTransacao historico : cofrinho.getHistorico()) {
			System.out.println(historico.toString());
		}

	}
}
