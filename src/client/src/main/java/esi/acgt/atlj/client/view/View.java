/*
 * MIT License
 *
 * Copyright (c) 2021 Andrew SASSOYE, Constantin GUNDUZ, Gregory VAN DER PLUIJM, Thomas LEUTSCHER
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.client.controller.fxml.MultiplayerGameController;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class View implements ViewInterface {

  private Stage primaryStage;
  private Controller controller;
  private HBox layout;
  private MultiplayerGameController mpgController;

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
   *
   * @param username
   */
  @Override
  public void displayBoard(String username) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MultiplayerGame.fxml"));

    this.primaryStage.close();
    this.primaryStage = new Stage();
    this.primaryStage.getIcons()
        .add(new Image(Objects.requireNonNull(
            Connexion.class.getResourceAsStream("/image/tetris-icon-32.png"))));
    this.primaryStage.setTitle("Tetris");
    this.primaryStage.setOnCloseRequest(event -> this.controller.disconnect());
    try {
      this.layout = loader.load();
      this.mpgController = loader.getController();
      this.mpgController.setMainPlayerUsername(username);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.primaryStage.setScene(new Scene(this.layout));
    this.primaryStage.show();
    primaryStage.getScene()
        .addEventFilter(KeyEvent.KEY_PRESSED, (key) -> controller.keyBoardInput(key.getCode()));
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
    return new PropertyChangeListener[]{this.mpgController.getPlayer1(),
        this.mpgController.getPlayer2()};
  }

}
