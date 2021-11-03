package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class View implements ViewInterface {

  private final Stage primaryStage;
  private Controller controller;

  public View(Stage stage) {
    this.primaryStage = stage;
    this.primaryStage.setTitle("Tetris");
  }

  @Override
  public void displayConnexion() {
    Connexion connexion = new Connexion(this.controller, this.primaryStage);
    this.primaryStage.centerOnScreen();
  }

  @Override
  public void displayBoard() {
    BothPlayerView bothPlayerView = new BothPlayerView(this.controller, this.primaryStage);
    this.primaryStage.centerOnScreen();
  }

  @Override
  public void show() {
    this.primaryStage.setResizable(false);
    this.primaryStage.show();
  }

  @Override
  public void setController(Controller controller) {
    this.controller = controller;
  }

  @Override
  public void displayError(Exception e) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setHeaderText("Erreur");
    alert.setContentText(e.getMessage());
    alert.show();
  }

}
