package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class View implements ViewInterface, PropertyChangeListener {

  private Stage primaryStage;
  private Controller controller;
  private BothPlayerView bothPlayerView;

  /**
   * Constructor of view.
   *
   * @param stage Stage that the view will use.
   */
  public View(Stage stage) {
    this.primaryStage = stage;
    this.primaryStage.setTitle("Tetris");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayConnexion() {
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
    alert.show();
  }

  /**
   * Update the board.
   *
   * @param oldBoard old board
   * @param newBoard new board
   * @param playerID player that we need to update
   */
  public void updateBoard(Mino[][] oldBoard, Mino[][] newBoard, int playerID) {
    this.bothPlayerView.updateBoard(oldBoard, newBoard, playerID);
  }

  /**
   * Update the score
   *
   * @param newScore new score
   * @param playerID player that we need to update
   */
  public void updateScore(int newScore, int playerID) {
    this.bothPlayerView.updateScore(newScore, playerID);
  }

  /**
   * Update the username
   *
   * @param newUsername new username
   * @param playerID    player that we need to update
   */
  public void updateUsername(String newUsername, int playerID) {
    this.bothPlayerView.updateUsername(newUsername, playerID);
  }

  /**
   * Update the timer
   *
   * @param timer    new timer
   * @param playerID player that we need to update
   */
  public void updateTimer(int timer, int playerID) {
    this.bothPlayerView.updateTimer(timer, playerID);
  }

  /**
   * Update the line
   *
   * @param line     new Line
   * @param playerID player that we need to update
   */
  public void updateLine(int line, int playerID) {
    this.bothPlayerView.updateLine(line, playerID);
  }

  /**
   * Update the hold
   *
   * @param hold     new hold
   * @param playerID player that we need to update
   */
  public void updateHold(TetriminoInterface hold, int playerID) {
    this.bothPlayerView.updateHold(hold, playerID);
  }

  /**
   * update the next
   *
   * @param next     new next
   * @param playerID player that we need to update
   */
  public void updateNext(TetriminoInterface next, int playerID) {
    this.bothPlayerView.updateNext(next, playerID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {

    switch (evt.getPropertyName()) {
      case "player1Board" -> {
        updateBoard((Mino[][]) evt.getOldValue(), (Mino[][]) evt.getNewValue(),
            0);
      }
      case "player2Board" -> {
        updateBoard((Mino[][]) evt.getOldValue(), (Mino[][]) evt.getNewValue(),
            1);
      }
      case "player1NbLine" -> {
        updateLine((int) evt.getNewValue(), 0);
      }
      case "player2NbLine" -> {
        updateLine((int) evt.getNewValue(), 1);
      }
      case "player1Score" -> {
        updateScore((int) evt.getNewValue(), 0);
      }
      case "player2Score" -> {
        updateScore((int) evt.getNewValue(), 1);
      }
      case "player1Name" -> {
        updateUsername(evt.getNewValue().toString(), 0);
      }
      case "player2Name" -> {
        updateUsername(evt.getNewValue().toString(), 1);
      }

      case "player1Hold" -> {
        updateHold((TetriminoInterface) evt.getNewValue(), 0);
      }
      case "player2Hold" -> {
        updateHold((TetriminoInterface) evt.getNewValue(), 1);
      }
      case "player1Next" -> {
        this.bothPlayerView.updateNext((TetriminoInterface) evt.getNewValue(), 0);
      }
      case "player2Next" -> {
        this.bothPlayerView.updateNext((TetriminoInterface) evt.getNewValue(), 1);
      }

      default -> System.out.println(evt.getPropertyName() + evt.getNewValue());
    }

  }

}
