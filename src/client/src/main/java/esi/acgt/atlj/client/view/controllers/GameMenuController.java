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

package esi.acgt.atlj.client.view.controllers;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.client.view.controllers.ConnexionController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameMenuController implements Initializable, PropertyChangeListener {

  private String username;
  private Controller controller;
  private Stage stage;

  @FXML
  public Label connectedUsername;
  @FXML
  public Label destroyedLine;
  @FXML
  public Label tetriminoPlaced;
  @FXML
  public Label wins;
  @FXML
  public Label loses;
  @FXML
  public Label highestScore;
  @FXML
  public Label winningPercent;

  /**
   * Constructor of GameMenuController
   *
   * @param username   username of the connected player
   * @param stage      stage of this controller
   * @param controller controller that this class will interact with to contact with model
   */
  public GameMenuController(String username, Stage stage, Controller controller) {
    this.username = username;
    this.controller = controller;
    this.stage = stage;

    this.stage.setOnCloseRequest(windowEvent -> {
      this.stage.close();
      this.controller.disconnect();
    });
    this.stage.getIcons()
        .add(new Image(Objects.requireNonNull(
            ConnexionController.class.getResourceAsStream("/image/tetris-icon-32.png"))));
    this.stage.setTitle("Tetris");

    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/fxml/GameMenu.fxml"));
    loader.setController(this);
    try {
      this.stage.setScene(new Scene(loader.load()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Action when you click on the disconnect button
   */
  public void disconnect() {
    this.stage.close();
    this.controller.disconnect();
  }

  /**
   * Action when you click on the Play button
   */
  public void play() {
    this.controller.startPlay(this.username);
  }

  /**
   * Change the information on the view
   *
   * @param evt event of the change
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(() -> {
      switch (evt.getPropertyName()) {
        case "line" -> updateLineLabel((int) evt.getNewValue());
        case "placed" -> updateTetriminoPlacedLabel((int) evt.getNewValue());
        case "wins" -> updateWinsLabel((int) evt.getNewValue());
        case "lose" -> updateLosesLabel((int) evt.getNewValue());
        case "highest" -> updateHighestScoreLabel((int) evt.getNewValue());
        case "percent" -> updateWinsPercentLabel((double) evt.getNewValue());
      }
    });
  }

  /**
   * Update highestScore label
   *
   * @param score new value of highestScore
   */
  private void updateHighestScoreLabel(int score) {
    this.highestScore.setText(Integer.toString(score));
  }

  /**
   * Update loses
   *
   * @param loses new value of loses
   */
  private void updateLosesLabel(int loses) {
    this.loses.setText(Integer.toString(loses));
  }

  /**
   * Update winningPercent
   *
   * @param percent new value of winningPercent
   */
  private void updateWinsPercentLabel(double percent) {
    if (percent >= 0) {
      this.winningPercent.setText(Double.toString(percent));
    } else {
      this.winningPercent.setText("N/A");
    }
  }

  /**
   * Update wins
   *
   * @param wins new value of wins
   */
  private void updateWinsLabel(int wins) {
    this.wins.setText(Integer.toString(wins));
  }

  /**
   * Update TetriminoPlaced
   *
   * @param placed new value of tetriminoPlaced
   */
  private void updateTetriminoPlacedLabel(int placed) {
    this.tetriminoPlaced.setText(Integer.toString(placed));
  }

  /**
   * Update destrouedLine
   *
   * @param destroyedLine new value of destroyedLine
   */
  private void updateLineLabel(int destroyedLine) {
    this.destroyedLine.setText(Integer.toString(destroyedLine));
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.connectedUsername.setText(this.connectedUsername.getText() + this.username);
  }
}
