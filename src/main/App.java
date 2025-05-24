package main;

import database.Database;
import model.Cofrinho;
import model.TipoMoeda;
import view.MainGUI;
import view.MenuConsole;

public class App {

    public static void main(String[] args) {
        Database.inicializar();
        Cofrinho cofrinho = new Cofrinho();
        
        boolean console = true;
  
        if(console) {
        	MenuConsole menu = new MenuConsole(cofrinho);
        	menu.exibirMenuPrincipal();
        }
        // Passa o cofrinho para a interface
        //MainGUI.setCofrinho(cofrinho);
        //MainGUI.main(args);  // inicia a GUI
        
        
    }
}
