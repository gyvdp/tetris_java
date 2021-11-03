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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PlayerTetrisFXML implements Initializable {

  private Controller controller;

  @FXML
  public Label scoreLabel;
  @FXML
  public Label timeLabel;
  @FXML
  public Label linesLabel;
  @FXML
  public Label levelLabel;
  @FXML
  public Label goalLabel;
  @FXML
  public VBox boardVBox;
  @FXML
  public ImageView holdTetromino;
  @FXML
  public ImageView nextTetrominos;
  @FXML
  public AnchorPane anchorPanePlayer;

  public PlayerTetrisFXML(Controller controller, Stage stage) {
    this.controller = controller;
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/fxml/TetrisBoard.fxml"));
    loader.setController(this);
    try {
      stage.setScene(new Scene(loader.load()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // A VOIR COMMENT ON VA FAIRE.
  }

  public void update(Object o) {
    //TO DO
  }


}
