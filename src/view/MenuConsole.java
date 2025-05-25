package view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import error.ErroBancoDeDadosException;
import model.TipoMoeda;
import util.InstanciarMoeda;
import model.Cofrinho;
import model.HistoricoTransacao;
import model.Moeda;
import java.time.format.DateTimeFormatter;


/**
 * Classe responsável por exibir e controlar o menu do cofrinho digital via console.
 * Permite interações como depósito, saque e visualização de histórico.
 * @author Wesllei Brito
 */
public class MenuConsole {
	private Cofrinho cofrinho;
	private Scanner scanner = new Scanner(System.in);
	
	/**
     * Construtor do menu console.
     * 
     * @param cofrinho Instância do cofrinho a ser manipulada pelo menu.
     */
	public MenuConsole(Cofrinho cofrinho) {
		this.cofrinho = cofrinho;
	}
	
	/**
     * Exibe o menu principal com as opções de interação disponíveis.
     * Controla a entrada do usuário e redireciona para os menus correspondentes.
     */
	public void exibirMenuPrincipal() throws ErroBancoDeDadosException{
		
		while (true) {
			try {
				exibirTitulo("COFRINHO DIGITAL", 60);
		    	
				System.out.printf("Saldo atual: R$ %.2f\n\n", cofrinho.getSaldo());
				System.out.println("1 - Depositar");
				System.out.println("2 - Sacar");
				System.out.println("3 - Ver Histórico");
				System.out.println("4 - Sair\n");
				System.out.printf("Escolha uma opção: ");
				
				int opcao = scanner.nextInt();
				
				switch (opcao) {
					case 1: menuDeposito(); break;
					case 2: menuSaque(); break;
					case 3: mostrarHistorico(); break;
					case 4: return;
					default: throw new IllegalArgumentException("Opção inválida.");
				}
			} catch (IllegalArgumentException e) {
        		System.out.println(e.getMessage());
        	} catch (InputMismatchException e) {
        		scanner.nextLine();
        	    System.out.println("Opção inválida.");
        	}	
		}
	}
	
	 /**
     * Exibe o menu de depósito, onde o usuário pode selecionar uma moeda
     * e informar o valor a ser depositado.
     */
	private void menuDeposito() throws ErroBancoDeDadosException {
		
		while(true) {
			try {
				exibirTitulo("DEPÓSITO", 60);
				Integer moedaEscolhida = exibirTiposMoedas();
		        
				if(moedaEscolhida == 4) {
					return;
				}
				
				System.out.printf("\nInforme o valor do depósito: ");
				double valor = scanner.nextDouble();
				Moeda moeda = new InstanciarMoeda().instanciar(moedaEscolhida, valor);
			
				cofrinho.adicionar(moeda);
				System.out.println("Moeda adicionada com sucesso!");
				return;
			} catch (IllegalArgumentException e) {
        		System.out.println(e.getMessage());
        	} catch (InputMismatchException e) {
        		scanner.nextLine();
        	    System.out.println("Opção inválida.");
        	}	
		}
		
       
    }
	
	/**
     * Exibe o menu de saque, permitindo ao usuário sacar por valor total
     * ou por tipo específico de moeda.
     */
    private void menuSaque() throws ErroBancoDeDadosException{
    	while(true) {
    		try {
        		
        		exibirTitulo("SAQUE", 60);
            	
            	System.out.println("\n1 - Por valor");
            	System.out.println("2 - Por moeda");
            	System.out.println("3 - Voltar\n");
            	
            	System.out.println("Qual o tipo de saque?\n");
            	
            	int opcao = scanner.nextInt();
            	
            	if(opcao == 3) {
            		return;
            	}
            	
            	if(opcao < 1 || opcao > 2) {
            		throw new IllegalArgumentException("Opção inválida.");
            	}
            	
            	if(opcao == 1) {
        			System.out.printf("Qual o valor do saque? ");
        			double valor = scanner.nextDouble();
        			cofrinho.retirarValorSaldo(valor);
        			return;
        		}else {
        			Integer moedaEscolhida = exibirTiposMoedas();
        			
        			if(moedaEscolhida != null) {
        				System.out.printf("\nInforme o valor do saque: ");
        				double valor = scanner.nextDouble();
        				
        				cofrinho.retirarValorDeUmaTipoDeMoeda(moedaEscolhida, valor);
        			}
        		}
            
        	} catch (IllegalArgumentException e) {
        		System.out.println(e.getMessage());
        	} catch (InputMismatchException e) {
        		scanner.nextLine();
        	    System.out.println("Opção inválida.");
        	}	
    	}
    	
    	
    }
    
    /**
     * Exibe o histórico de transações do cofrinho em formato tabular,
     * ordenado da mais recente para a mais antiga.
     */
    private void mostrarHistorico() throws ErroBancoDeDadosException{

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
    
    /**
     * Exibe a lista de tipos de moedas disponíveis para o usuário selecionar.
     * 
     * @return O número correspondente à moeda escolhida ou null se o usuário optar por voltar.
     * @throws IllegalArgumentException Se o valor escolhido for inválido.
     */
    private Integer exibirTiposMoedas() throws ErroBancoDeDadosException{
    	
    	List<TipoMoeda> tipoMoeda = cofrinho.getTipoMoedas();

		for(int i = 0; i < tipoMoeda.size(); i++) {
			System.out.println(i + 1 + " - " + tipoMoeda.get(i).getNome());
		}
		
		System.out.println(tipoMoeda.size() + 1 + " - Voltar\n");
		
		System.out.printf("\nEscolha uma moeda: ");
		
		int moedaEscolhida = scanner.nextInt();
		if(moedaEscolhida != 4 && (moedaEscolhida > tipoMoeda.size() || moedaEscolhida < 0)) {
			throw new IllegalArgumentException("Opção inválida, tente novamente.");
		}
		
		return moedaEscolhida;
    }
    
    /**
     * Exibe um título centralizado com linha superior opcional.
     * 
     * @param titulo Texto do título.
     * @param largura Largura total da linha.
     * @param linhaSuperior Se deve exibir uma linha superior de separação.
     */
    private void exibirTitulo(String titulo, int largura, boolean linhaSuperior) {
    	
    	int espaco = (largura - titulo.length()) / 2;
    	
    	if(linhaSuperior) {
    		System.out.println("=".repeat(largura));    		
    	}
    	
    	System.out.printf("%" + espaco + "s%s\n", "", titulo);
    	System.out.println("=".repeat(largura));
    	
    }
    
    /**
     * Exibe um título centralizado com linha superior por padrão.
     * 
     * @param titulo Texto do título.
     * @param largura Largura total da linha.
     */
    private void exibirTitulo(String titulo, int largura) {
    	exibirTitulo(titulo, largura, true);
    }
    
    /**
     * Exibe o cabeçalho da tabela de histórico de transações.
     */
    private void exibirCabecalhoTabela() {
    	System.out.println();
    	exibirTitulo("HISTÓRICO", 75, false);
        System.out.printf("%-10s | %-6s | %-9s | %-10s | %-15s | %-15s\n", "Data", "Moeda", "Tipo", "Valor", "Saldo Anterior", "Saldo Pós");
        System.out.println("-".repeat(75));
    }
    
    /**
     * Exibe uma linha formatada da tabela de histórico de transações.
     * 
     * @param data Data da transação.
     * @param nomeMoeda Nome da moeda utilizada.
     * @param tipoTransacao Tipo da transação (Depósito ou Saque).
     * @param valor Valor movimentado.
     * @param saldoAnterior Saldo antes da transação.
     * @param saldoPos Saldo após a transação.
     */
    private void linhaTabela(String data, String nomeMoeda, String tipoTransacao, String valor, String saldoAnterior, String saldoPos) {
    	System.out.printf("%-10s | %-6s | %-9s | %-10s | %-15s | %-15s\n", data, nomeMoeda, tipoTransacao, valor, saldoAnterior, saldoPos);
    	System.out.println("-".repeat(75));
    }
}
