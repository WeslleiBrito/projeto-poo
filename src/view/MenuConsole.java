package view;

import java.util.List;
import java.util.Scanner;
import model.TipoMoeda;
import util.InstanciarMoeda;
import model.Cofrinho;
import model.HistoricoTransacao;
import model.Moeda;
import java.time.format.DateTimeFormatter;


public class MenuConsole {
	private Cofrinho cofrinho;
	private Scanner scanner = new Scanner(System.in);
	
	public MenuConsole(Cofrinho cofrinho) {
		this.cofrinho = cofrinho;
	}
	
	public void exibirMenuPrincipal() {
		
		while (true) {
			exibirTitulo("COFRINHO DIGITAL", 60);
	    	
			System.out.printf("Saldo atual: R$ %.2f\n\n", cofrinho.getSaldo());
			System.out.println("1 - Depositar");
			System.out.println("2 - Sacar");
			System.out.println("3 - Ver Histórico");
			System.out.println("4 - Sair");
			System.out.printf("Escolha uma opção: ");
			
			int opcao = scanner.nextInt();
			
			switch (opcao) {
				case 1: menuDeposito(); break;
				case 2: menuSaque(); break;
				case 3: mostrarHistorico(); break;
				case 4: return;
				default: System.out.println("Opção inválida.");
			}
		}
	}
	
	private void menuDeposito() {
		exibirTitulo("DEPÓSITO", 60);
		Integer moedaEscolhida = exibirTiposMoedas();
        
		if(moedaEscolhida != null) {
			
			System.out.printf("\nInforme o valor do depósito: ");
			double valor = scanner.nextDouble();
			Moeda moeda = new InstanciarMoeda().instanciar(moedaEscolhida);
			moeda.setValor(valor);
		
			cofrinho.adicionar(moeda);
		}
		

    }

    private void menuSaque() {
    	
    	exibirTitulo("SAQUE", 60);
    	
    	System.out.println("\n1 - Por valor");
    	System.out.println("2 - Por moeda\n");
    	
    	System.out.println("Qual o tipo de saque?\n");
    	
    	int opcao = scanner.nextInt();
    	
    	if(opcao < 1 || opcao > 2) {
    		System.out.println("Opção inválida.");
    	}else {
    		if(opcao == 1) {
    			System.out.printf("Qual o valor do saque? ");
    			double valor = scanner.nextDouble();
    			cofrinho.retirarValorSaldo(valor);
    		}else {
    			
    			Integer moedaEscolhida = exibirTiposMoedas();
    			
    			if(moedaEscolhida != null) {
    				System.out.printf("\nInforme o valor do saque: ");
    				double valor = scanner.nextDouble();
    				
    				cofrinho.retirarValorDeUmaTipoDeMoeda(moedaEscolhida, valor);
    			}
    		}
    	}
    }

    private void mostrarHistorico() {

    	exibirTitulo("HISTÓRICO DE TRANSAÇÕES", 75);
    	exibirCabecalhoTabela();
    	
    	
    	List<HistoricoTransacao> historicos = cofrinho.getHistorico();
    	
    	historicos.sort((a, b) -> b.getDataTransacao().compareTo(a.getDataTransacao()));
    	
    	for(HistoricoTransacao historico : historicos) {
    		String data = historico.getDataTransacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    		String valor = String.format("%.2f", historico.getValorTransacao());
    		String saldoAnterior = String.format("%.2f", historico.getSaldoAnterior());
    		String saldoPos = String.format("%.2f", historico.getSaldoAtual());
    		
    		linhaTabela(data, historico.getNomeMoeda(), historico.getTipoTransacao().getNome(), valor, saldoAnterior, saldoPos);
    	}
    }
    
    private Integer exibirTiposMoedas() {
    	
    	List<TipoMoeda> tipoMoeda = cofrinho.getTipoMoedas();

		for(int i = 0; i < tipoMoeda.size(); i++) {
			System.out.println(i + 1 + " - " + tipoMoeda.get(i).getNome());
		}
		
		System.out.printf("\nEscolha uma moeda: ");
		
		int moedaEscolhida = scanner.nextInt();
		if(moedaEscolhida > tipoMoeda.size() || moedaEscolhida < 0) {
			System.out.println("Opção inválida, tente novamente.");
			return null;
		}
		
		return moedaEscolhida;
    }
    
    private void exibirTitulo(String titulo, int largura, boolean linhaSuperior) {
    	
    	int espaco = (largura - titulo.length()) / 2;
    	
    	if(linhaSuperior) {
    		System.out.println("=".repeat(largura));    		
    	}
    	
    	System.out.printf("%" + espaco + "s%s\n", "", titulo);
    	System.out.println("=".repeat(largura));
    	
    }
    
    private void exibirTitulo(String titulo, int largura) {
    	exibirTitulo(titulo, largura, true);
    }
    
    private void exibirCabecalhoTabela() {
    	System.out.println();
    	exibirTitulo("HISTÓRICO", 75, false);
        System.out.printf("%-10s | %-6s | %-9s | %-10s | %-15s | %-15s\n", "Data", "Moeda", "Tipo", "Valor", "Saldo Anterior", "Saldo Pós");
        System.out.println("-".repeat(75));
    }
    
    private void linhaTabela(String data, String nomeMoeda, String tipoTransacao, String valor, String saldoAnterior, String saldoPos) {
    	System.out.printf("%-10s | %-6s | %-9s | %-10s | %-15s | %-15s\n", data, nomeMoeda, tipoTransacao, valor, saldoAnterior, saldoPos);
    	System.out.println("-".repeat(75));
    }
}
