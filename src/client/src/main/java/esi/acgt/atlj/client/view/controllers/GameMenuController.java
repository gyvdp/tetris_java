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
import java.text.DecimalFormat;

public class GameMenuController implements Initializable, PropertyChangeListener {

  private String username;
  private Controller controller;
  private Stage stage;

  @FXML
  public Label connectedUsername;
  @FXML
  public Label singleLabel;
  @FXML
  public Label doubleLabel;
  @FXML
  public Label tripleLabel;
  @FXML
  public Label tetrisLabel;
  @FXML
  public Label destroyedLine;
  @FXML
  public Label hardDrop;
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

    this.modifyStage();

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
   * Modify the action when we close the stage, modify the icon of the stage and the title
   */
  private void modifyStage() {
    this.stage.setOnCloseRequest(windowEvent -> {
      this.stage.close();
      this.controller.disconnect();
    });
    this.stage.getIcons()
        .add(new Image(Objects.requireNonNull(
            ConnexionController.class.getResourceAsStream("/image/tetris-icon-32.png"))));
    this.stage.setTitle("Tetris");
  }

  /**
   * Close the window and disconnect the client from the server.
   */
  public void disconnect() {
    this.stage.close();
    this.controller.disconnect();
  }

  /**
   * Send to the server that the client is ready to play and will launch the MultiplayerGame.
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
        case "BURN" -> updateLineLabel((int) evt.getNewValue());
        case "HARD" -> updateHardDropLabel((int) evt.getNewValue());
        case "WON" -> updateWinsLabel((int) evt.getNewValue());
        case "LOST" -> updateLosesLabel((int) evt.getNewValue());
        case "SCORE" -> updateHighestScoreLabel((int) evt.getNewValue());
        case "PERCENT" -> updateWinsPercentLabel((double) evt.getNewValue());
        case "SINGLE" -> updateSingleLabel((int) evt.getNewValue());
        case "DOUBLE" -> updateDoubleLabel((int) evt.getNewValue());
        case "TRIPLE" -> updateTripleLabel((int) evt.getNewValue());
        case "TETRIS" -> updateTetrisLabel((int) evt.getNewValue());
      }
    });
  }

  /**
   * @param score new value of highestScore
   */
  private void updateHighestScoreLabel(int score) {
    this.highestScore.setText(Integer.toString(score));
  }

  /**
   * @param loses new value of loses
   */
  private void updateLosesLabel(int loses) {
    this.loses.setText(Integer.toString(loses));
  }

  /**
   * @param percent new value of winningPercent
   */
  private void updateWinsPercentLabel(double percent) {
    if (percent >= 0) {
      var temp = new DecimalFormat("#.##").format(percent);
      this.winningPercent.setText(temp.toString() + "%");
    } else {
      this.winningPercent.setText("N/A");
    }
  }

  /**
   * @param wins new value of wins
   */
  private void updateWinsLabel(int wins) {
    this.wins.setText(Integer.toString(wins));
  }

  /**
   * @param hardD new value of hardDrop
   */
  private void updateHardDropLabel(int hardD) {
    this.hardDrop.setText(Integer.toString(hardD));
  }

  /**
   * @param single new value of hardDrop
   */
  private void updateSingleLabel(int single) {
    this.singleLabel.setText(Integer.toString(single));
  }

  /**
   * @param doubleL new value of hardDrop
   */
  private void updateDoubleLabel(int doubleL) {
    this.doubleLabel.setText(Integer.toString(doubleL));
  }

  /**
   * @param triple new value of hardDrop
   */
  private void updateTripleLabel(int triple) {
    this.tripleLabel.setText(Integer.toString(triple));
  }

  /**
   * @param tetris new value of hardDrop
   */
  private void updateTetrisLabel(int tetris) {
    this.tetrisLabel.setText(Integer.toString(tetris));
  }

  /**
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
