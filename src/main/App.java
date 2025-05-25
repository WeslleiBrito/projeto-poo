package main;

import database.Database;
import model.Cofrinho;
import view.MenuConsole;

/**
 * Classe principal do programa Cofrinho.
 * 
 * <p><strong>AVISO:</strong> Antes de executar este programa, é 
 * <strong>OBRIGATÓRIA</strong> a leitura do arquivo <code>README.md</code>, 
 * especialmente para configurar corretamente a biblioteca do SQLite.
 * </p>
 *
 * <p>Responsável por iniciar a aplicação, inicializar o banco de dados, 
 * criar uma instância do cofrinho e exibir o menu principal para o usuário.
 * </p>
 * RU 
 * @author Wesllei Brito
 * @version 1.0
 */
public class App {

    /**
     * Método principal que inicializa o banco de dados, 
     * cria o cofrinho e inicia o menu principal da aplicação.
     *
     * @param args argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Database.inicializar();

        Cofrinho cofrinho = new Cofrinho();
        MenuConsole menu = new MenuConsole(cofrinho);

        try {
            menu.exibirMenuPrincipal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
