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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PlayerTetrisFXML implements Initializable {

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
  public GridPane boardPane;
  @FXML
  public ImageView holdTetromino;
  @FXML
  public ImageView nextTetrominos;
  @FXML
  public AnchorPane anchorPanePlayer;

  public PlayerTetrisFXML(Stage stage) {
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
    int[][] board = new int[10][20];
    for (int i = 0; i < boardPane.getColumnCount(); i++) {
      for (int j = 0; j < boardPane.getRowCount(); j++) {
        board[i][j] = (int) (Math.random() * 8);
        //ImageView imageView = new ImageView();
        //imageView.setFitHeight(50);
        //imageView.setFitWidth(50);
        Label label = new Label();
        this.boardPane.add(label, i, j);
      }
    }
    updateBoard(board);
  }

  private void updateBoard(int[][] board) {
    int i = 0;
    var list = this.boardPane.getChildren();
    for (Node node : list) {
      ((Label) node).setText(
          cubeColor(board[i / this.boardPane.getRowCount()][i % this.boardPane.getColumnCount()]));
      i++;
    }
    //imageView.setImage("...");
  }


  private String cubeColor(int color) {
    switch (color) {
      case 0:
        return "white";
      case 1:
        return "blue";
      case 2:
        return "green";
      case 3:
        return "yellow";
      case 4:
        return "red";
      case 5:
        return "darkblue";
      case 6:
        return "purple";
      case 7:
        return "orange";
      default:
        throw new IllegalArgumentException("Color doesn't exists");
    }
  }

}
