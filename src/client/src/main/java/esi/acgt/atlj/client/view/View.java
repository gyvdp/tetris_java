package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class View implements ViewInterface {

  private Stage primaryStage;
  private Controller controller;
  private BothPlayerView bothPlayerView;

  /**
   * Constructor of view.
   */
  public View() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayConnexion() {
    this.primaryStage = new Stage();
    this.primaryStage.setTitle("Tetris connexion");
    Connexion connexion = new Connexion(this.controller, this.primaryStage);
    this.primaryStage.setResizable(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayBoard() {
    this.primaryStage = new Stage();
    this.bothPlayerView = new BothPlayerView(this.controller, this.primaryStage);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void show() {
    this.primaryStage.setOnCloseRequest(event -> {
      this.controller.disconnect();
    });
    this.primaryStage.centerOnScreen();
    this.primaryStage.show();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setController(Controller controller) {
    this.controller = controller;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayError(Exception e) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setHeaderText("Erreur");
    alert.setContentText(e.getMessage());
    alert.showAndWait();
  }

  @Override
  public PropertyChangeListener[] getListeners() {
    PropertyChangeListener[] listeners = {this.bothPlayerView.getPlayer1(),
        this.bothPlayerView.getPlayer2()};
    return listeners;
  }

}
