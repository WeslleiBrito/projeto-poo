package main;

import models.Cofrinho;
import models.Dolar;
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
		
		System.out.println(cofrinho.getSaldo());
		System.out.println(cofrinho.valorPorMoeda());
		
		cofrinho.retirarValorDeUmaTipoDeMoeda(dolar2, 3);
		
		System.out.println(cofrinho.valorPorMoeda());
		System.out.println(cofrinho.getSaldo());
		
		cofrinho.retirarValor(3);
		
		System.out.println(cofrinho.valorPorMoeda());
		System.out.println(cofrinho.getSaldo());
		
		
	}
}
