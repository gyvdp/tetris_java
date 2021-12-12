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
import esi.acgt.atlj.client.view.scenes.ConnectionScene;
import esi.acgt.atlj.client.view.scenes.GameMenuScene;
import esi.acgt.atlj.client.view.scenes.MultiplayerScene;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javafx.beans.property.MapProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class View implements ViewInterface {

  private final Stage primaryStage;

  private Controller controller;

  /**
   * Constructor of view.
   */
  public View(Stage stage) {
    this.controller = null;
    this.primaryStage = stage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayConnexion(String host, String port, String username) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Connexion.fxml"));
    try {
      this.primaryStage.setScene(
          new ConnectionScene(loader.load(), loader.getController(), this.controller, host, port,
              username));
    } catch (IOException e) {
      e.printStackTrace();
    }

    primaryStage.setOnCloseRequest(event -> {
      event.consume();
      if (primaryStage.getScene() instanceof MultiplayerScene) {
        controller.leaveMatch();
      } else if (primaryStage.getScene() instanceof GameMenuScene
          || primaryStage.getScene() instanceof ConnectionScene) {
        controller.stop();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayBoard(String username) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MultiplayerGame.fxml"));
    try {
      this.primaryStage.setScene(
          new MultiplayerScene(loader.load(), loader.getController(), username));
    } catch (IOException e) {
      e.printStackTrace();
    }

    primaryStage.getScene()
        .addEventFilter(KeyEvent.KEY_PRESSED, (key) -> controller.keyBoardInput(key.getCode()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void show() {
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
  public PropertyChangeListener[] getBoardListeners() {
    var scene = this.primaryStage.getScene();
    return scene instanceof MultiplayerScene ? ((MultiplayerScene) scene).getBoardListeners()
        : null;
  }

  @Override
  public void displayMenu(String username, MapProperty<String, Integer> stats) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameMenu.fxml"));
    try {
      this.primaryStage.setScene(
          new GameMenuScene(loader.load(), loader.getController(), this.controller, username,
              stats));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void quit() {
    this.primaryStage.setScene(null);
    this.primaryStage.close();
  }
}
