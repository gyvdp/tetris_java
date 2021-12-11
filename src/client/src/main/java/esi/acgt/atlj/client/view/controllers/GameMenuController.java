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
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class GameMenuController implements Initializable {

  private String username;
  private Controller controller;


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

  public void setRootController(Controller controller) {
    this.controller = controller;
  }

  public void setUsername(String username) {
    this.username = username;
    this.connectedUsername.setText(username);
  }

  public void setStats(HashMap<String, Integer> stats) {
    highestScore.setText(String.valueOf(stats.getOrDefault("SCORE", 0)));
    var lost = stats.getOrDefault("LOST", 0);
    var won = stats.getOrDefault("WON", 0);
    loses.setText(String.valueOf(lost));
    wins.setText(String.valueOf(won));
    winningPercent.setText(String.format("%.2f%%", ((double) won) / (won + lost) * 100));
    singleLabel.setText(String.valueOf(stats.getOrDefault("SINGLE", 0)));
    doubleLabel.setText(String.valueOf(stats.getOrDefault("DOUBLE", 0)));
    tripleLabel.setText(String.valueOf(stats.getOrDefault("TRIPLE", 0)));
    tetrisLabel.setText(String.valueOf(stats.getOrDefault("TETRIS", 0)));
    hardDrop.setText(String.valueOf(stats.getOrDefault("HARD_DROP", 0)));
    destroyedLine.setText(String.valueOf(stats.getOrDefault("BURN", 0)));


  }

  /**
   * Action when you click on the disconnect button
   */
  public void disconnect() {
    this.controller.stop();
  }

  /**
   * Action when you click on the Play button
   */
  public void play() {
    this.controller.startMultiplayerGame(this.username);
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.connectedUsername.setText(this.connectedUsername.getText() + this.username);
  }
}
