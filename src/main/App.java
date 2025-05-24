package main;

import database.Database;
import model.Cofrinho;
import view.MainGUI;

public class App {

    public static void main(String[] args) {
        Database.inicializar();
        Cofrinho cofrinho = new Cofrinho();

        // Passa o cofrinho para a interface
        MainGUI.setCofrinho(cofrinho);
        MainGUI.main(args);  // inicia a GUI
    }
}
