package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.model.tetrimino.Mino;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class View implements ViewInterface, PropertyChangeListener{

  private final Stage primaryStage;
  private Controller controller;
  private BothPlayerView bothPlayerView;

  public View(Stage stage) {
    this.primaryStage = stage;
    this.primaryStage.setTitle("Tetris");
  }

  @Override
  public void displayConnexion() {
    Connexion connexion = new Connexion(this.controller, this.primaryStage);
    this.primaryStage.centerOnScreen();
    this.primaryStage.setResizable(false);
  }

  @Override
  public void displayBoard() {
    this.primaryStage.setResizable(true);
   // this.bothPlayerView = new BothPlayerView(this.controller,this.primaryStage);
    this.primaryStage.centerOnScreen();
  }

  @Override
  public void show() {
    this.primaryStage.show();
  }

  @Override
  public void setController(Controller controller) {
    this.controller = controller;
    this.bothPlayerView = new BothPlayerView(this.controller,this.primaryStage);
  }

  @Override
  public void displayError(Exception e) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setHeaderText("Erreur");
    alert.setContentText(e.getMessage());
    alert.show();
  }

  @Override
  public void updateBoard(Mino[][] board, int playerID) {
      this.bothPlayerView.updateBoard(board, playerID);
  }

  @Override
  public void updateScore(int newScore, int playerID) {
      this.bothPlayerView.updateScore(newScore, playerID);
  }

  @Override
  public void updateUsername(String newUsername, int playerID) {
      this.bothPlayerView.updateUsername(newUsername, playerID);
  }

  @Override
  public void updateTimer(int timer, int playerID) {
      this.bothPlayerView.updateTimer(timer, playerID);
  }

  @Override
  public void updateLine(int line, int playerID) {
      this.bothPlayerView.updateLine(line,playerID);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()){
      case "player1Name" -> {
        System.out.println("nom du joueur 1 "+ evt.getNewValue());
  //      this.bothPlayerView.updateUsername(evt.getNewValue().toString(),0);
      }
      default -> System.out.println(evt.getPropertyName() + evt.getNewValue());
    }
  }
}
