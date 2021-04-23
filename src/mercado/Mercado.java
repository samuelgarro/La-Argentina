package mercado;

import controller.PrincipalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Mercado extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Mercado.class.getResource("Principal.fxml"));
		AnchorPane root = loader.load();

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("La argentina - Forrajeria v1.0.0");
		stage.getIcons().setAll(new Image("file:icono/icono.png"));
		stage.setResizable(true);
		stage.setMaximized(true);
		stage.show();

		PrincipalController controller = loader.getController();
		controller.redimencionar(scene.getWidth(), scene.getHeight());
		controller.loadInventario();
		controller.setIcon();
		controller.setBackground();
	}

}
