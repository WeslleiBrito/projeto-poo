package view;

import controller.MainGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import model.Cofrinho;

public class MainGUI extends Application {

    private static Cofrinho cofrinho;

    public static void setCofrinho(Cofrinho c) {
        cofrinho = c;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainGUI.fxml"));
        Parent root = loader.load();

        // Passar o cofrinho para o controller
        MainGUIController controller = loader.getController();
        controller.setCofrinho(cofrinho);

        Scene scene = new Scene(root);
        stage.setTitle("Cofrinho Digital");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
