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

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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

  private final Image whiteCube;
  private final Image greenCube;
  private final Image lightBlueCube;
  private final Image darkBlueCube;
  private final Image orangeCube;
  private final Image purpleCube;
  private final Image redCube;
  private final Image yellowCube;


  public PlayerTetrisFXML(Stage stage) {
    this.whiteCube = new Image(getClass().getResourceAsStream("/image/WhiteCube.png"));
    this.greenCube = new Image(getClass().getResourceAsStream("/image/GreenCube.png"));
    this.lightBlueCube = new Image(getClass().getResourceAsStream("/image/LightBlueCube.png"));
    this.darkBlueCube = new Image(getClass().getResourceAsStream("/image/DarkBlueCube.png"));
    this.orangeCube = new Image(getClass().getResourceAsStream("/image/OrangeCube.png"));
    this.purpleCube = new Image(getClass().getResourceAsStream("/image/PurpleCube.png"));
    this.redCube = new Image(getClass().getResourceAsStream("/image/RedCube.png"));
    this.yellowCube = new Image(getClass().getResourceAsStream("/image/YellowCube.png"));

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
    int[][] board = new int[10][20];
    for (int i = 0; i < boardPane.getColumnCount(); i++) {
      for (int j = 0; j < boardPane.getRowCount(); j++) {
        board[i][j] = (int) (Math.random() * 8);
        ImageView imageView = new ImageView();
        imageView.setFitHeight(46);
        imageView.setFitWidth(46);
        this.boardPane.add(imageView, i, j);
      }
    }
    updateBoard(board);
  }

  private void updateBoard(int[][] board) {
    int i = 0;
    var list = this.boardPane.getChildren();
    for (Node node : list) {
      if (node instanceof ImageView) {
        ((ImageView) node).setImage(
            cubeColor(
                board[i / this.boardPane.getRowCount()][i % this.boardPane.getColumnCount()]));
        i++;
      }
    }
  }


  private Image cubeColor(int color) {
    switch (color) {
      case 0:
        return this.whiteCube;
      case 1:
        return this.greenCube;
      case 2:
        return this.purpleCube;
      case 3:
        return this.lightBlueCube;
      case 4:
        return this.darkBlueCube;
      case 5:
        return this.orangeCube;
      case 6:
        return this.redCube;
      case 7:
        return this.yellowCube;
      default:
        throw new IllegalArgumentException("Color doesn't exists");
    }
  }

}
