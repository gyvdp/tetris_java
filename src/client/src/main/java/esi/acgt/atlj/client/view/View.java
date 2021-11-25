package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class View implements ViewInterface {

  private Stage primaryStage;
  private Controller controller;
  private BothPlayerView bothPlayerView;

  /**
   * Constructor of view.
   */
  public View() {
    this.controller = null;
    this.bothPlayerView = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayConnexion() {
    this.primaryStage = new Stage();
    this.primaryStage.getIcons()
        .add(new Image(Objects.requireNonNull(
            Connexion.class.getResourceAsStream("/image/tetris-icon-32.png"))));
    this.primaryStage.setTitle("Tetris connexion");
    // TODO Cest quoi cette connection?
    new Connexion(this.controller, this.primaryStage);
    this.primaryStage.setResizable(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayBoard(String username) {
    this.primaryStage.close();
    this.primaryStage = new Stage();
    this.primaryStage.getIcons()
        .add(new Image(Objects.requireNonNull(
            Connexion.class.getResourceAsStream("/image/tetris-icon-32.png"))));
    this.primaryStage.setTitle("Tetris");
    this.primaryStage.setOnCloseRequest(event -> {
      this.controller.disconnect();
      this.bothPlayerView = null;
    });
    this.bothPlayerView = new BothPlayerView(this.controller, this.primaryStage, username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void show() {
    this.primaryStage.setOnCloseRequest(event -> this.controller.disconnect());
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

  /**
   * {@inheritDoc}
   */
  @Override
  public PropertyChangeListener[] getListeners() {
    return new PropertyChangeListener[]{this.bothPlayerView.getPlayer1(),
        this.bothPlayerView.getPlayer2()};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayStatitics() {
    this.primaryStage = new Stage();
    this.primaryStage.setTitle("Statistique");
    this.primaryStage.setOnCloseRequest(event -> this.controller.disconnect());
    new Statistics(this.primaryStage);
    this.primaryStage.setResizable(false);
  }

}
