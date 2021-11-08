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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
  public GridPane main;

  private final Image whiteCube;
  private final Image greenCube;
  private final Image lightBlueCube;
  private final Image darkBlueCube;
  private final Image orangeCube;
  private final Image purpleCube;
  private final Image redCube;
  private final Image yellowCube;

  private final Image tetro_J;
  private final Image tetro_S;
  private final Image tetro_O;
  private final Image tetro_Z;
  private final Image tetro_L;
  private final Image tetro_I;
  private final Image tetro_T;


  public PlayerTetrisFXML(Stage stage) {
    this.whiteCube = new Image(getClass().getResourceAsStream("/image/WhiteCube.png"));
    this.greenCube = new Image(getClass().getResourceAsStream("/image/GreenCube.png"));
    this.lightBlueCube = new Image(getClass().getResourceAsStream("/image/LightBlueCube.png"));
    this.darkBlueCube = new Image(getClass().getResourceAsStream("/image/DarkBlueCube.png"));
    this.orangeCube = new Image(getClass().getResourceAsStream("/image/OrangeCube.png"));
    this.purpleCube = new Image(getClass().getResourceAsStream("/image/PurpleCube.png"));
    this.redCube = new Image(getClass().getResourceAsStream("/image/RedCube.png"));
    this.yellowCube = new Image(getClass().getResourceAsStream("/image/YellowCube.png"));

    this.tetro_I = new Image(getClass().getResourceAsStream("/image/I.png"));
    this.tetro_S = new Image(getClass().getResourceAsStream("/image/S.png"));
    this.tetro_T = new Image(getClass().getResourceAsStream("/image/T.png"));
    this.tetro_O = new Image(getClass().getResourceAsStream("/image/O.png"));
    this.tetro_Z = new Image(getClass().getResourceAsStream("/image/Z.png"));
    this.tetro_J = new Image(getClass().getResourceAsStream("/image/J.png"));
    this.tetro_L = new Image(getClass().getResourceAsStream("/image/L.png"));

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
//    updateHold(Tetromino.T);
//    updateNextPiece(Tetromino.L);
  }

  private void updateBoard(int[][] board) {
    int i = 0;
    var list = this.boardPane.getChildren();
    for (Node node : list) {
      if (node instanceof ImageView) {
        ((ImageView) node).setImage(
            cubeColor(
                board[i / this.boardPane.getRowCount()][i % this.boardPane.getRowCount()]));
        i++;
      }
    }
  }

//  public void updateHold(Tetromino hold) {
//    this.holdTetromino.setImage(getTetrominoImage(hold));
//  }
//
//  public void updateNextPiece(Tetromino tetromino) {
//    this.nextTetrominos.setImage(this.getTetrominoImage(tetromino));
//  }
//
//  private Image getTetrominoImage(Tetromino tetromino) {
//    switch (tetromino) {
//      case I:
//        return this.tetro_I;
//      case J:
//        return this.tetro_J;
//      case L:
//        return this.tetro_L;
//      case O:
//        return this.tetro_O;
//      case S:
//        return this.tetro_S;
//      case T:
//        return this.tetro_T;
//      case Z:
//        return this.tetro_Z;
//      default:
//        throw new IllegalArgumentException("Tetro doesn't exists.");
//    }
//  }

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
