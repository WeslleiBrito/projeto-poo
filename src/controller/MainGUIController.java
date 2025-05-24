package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Cofrinho;

public class MainGUIController {

    private Cofrinho cofrinho;

    public void setCofrinho(Cofrinho c) {
        this.cofrinho = c;
    }

    @FXML
    private Label labelStatus;

    @FXML
    private void mostrarSaldo() {
        double saldo = cofrinho.getSaldo();
        labelStatus.setText("Saldo total: R$ " + saldo);
    }
}
