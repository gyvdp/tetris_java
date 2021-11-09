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

import esi.acgt.atlj.model.Mino;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class PlayerTetrisFXML implements Initializable {

  // Texts
  @FXML
  public Label scoreLabel;
  @FXML
  public Label timeLabel;
  @FXML
  public Label linesLabel;
  @FXML
  public Label usernameLabel;

  // Hold
  @FXML
  public Circle circleHold;
  @FXML
  public ImageView holdTetromino;
  @FXML
  public StackPane stackPaneHold;

  // Next
  @FXML
  public ImageView nextTetromino;
  @FXML
  public Circle circleNext;
  @FXML
  public StackPane stackPaneNext;

  //Board
  @FXML
  public GridPane boardPane;

  // Scene
  @FXML
  public GridPane scene;

  private final Image image_NOMino;
  private final Image image_SMino;
  private final Image image_IMino;
  private final Image image_JMino;
  private final Image image_LMino;
  private final Image image_TMino;
  private final Image image_ZMino;
  private final Image image_OMino;

  private final Image tetro_J;
  private final Image tetro_S;
  private final Image tetro_O;
  private final Image tetro_Z;
  private final Image tetro_L;
  private final Image tetro_I;
  private final Image tetro_T;


  public PlayerTetrisFXML(Stage stage) {
    //Load all Images
    this.image_NOMino = new Image(getClass().getResourceAsStream("/image/WhiteCube.png"));
    this.image_SMino = new Image(getClass().getResourceAsStream("/image/GreenCube.png"));
    this.image_IMino = new Image(getClass().getResourceAsStream("/image/LightBlueCube.png"));
    this.image_JMino = new Image(getClass().getResourceAsStream("/image/DarkBlueCube.png"));
    this.image_LMino = new Image(getClass().getResourceAsStream("/image/OrangeCube.png"));
    this.image_TMino = new Image(getClass().getResourceAsStream("/image/PurpleCube.png"));
    this.image_ZMino = new Image(getClass().getResourceAsStream("/image/RedCube.png"));
    this.image_OMino = new Image(getClass().getResourceAsStream("/image/YellowCube.png"));

    this.tetro_I = new Image(getClass().getResourceAsStream("/image/I.png"));
    this.tetro_S = new Image(getClass().getResourceAsStream("/image/S.png"));
    this.tetro_T = new Image(getClass().getResourceAsStream("/image/T.png"));
    this.tetro_O = new Image(getClass().getResourceAsStream("/image/O.png"));
    this.tetro_Z = new Image(getClass().getResourceAsStream("/image/Z.png"));
    this.tetro_J = new Image(getClass().getResourceAsStream("/image/J.png"));
    this.tetro_L = new Image(getClass().getResourceAsStream("/image/L.png"));

    // FXML loading
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/fxml/TetrisBoard.fxml"));
    loader.setController(this);
    try {
      stage.setScene(new Scene(loader.load()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Bindings of this.scene 
    this.scene.prefWidthProperty().bind(stage.getScene().widthProperty());
    this.scene.prefHeightProperty().bind(stage.getScene().heightProperty());
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    doBindings();
    for (int i = 0; i < boardPane.getColumnCount(); i++) {
      for (int j = 0; j < boardPane.getRowCount(); j++) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty()
            .bind(this.boardPane.widthProperty().divide(11.5));
        imageView.fitHeightProperty()
            .bind(this.boardPane.heightProperty().divide(21.5));
        imageView.setImage(this.image_NOMino);
        this.boardPane.add(imageView, i, j);
      }
    }
  }

  public void updateBoard(Mino[][] board) {
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

  public void updateScore(int newScore) {
    this.scoreLabel.setText(Integer.toString(newScore));
  }

  public void updateUsername(String newUsername) {
    this.usernameLabel.setText(newUsername);
  }

  public void updateTimer(int timer) {
    int hours, minutes, seconds;
    hours = timer / 3600;
    minutes = (timer % 3600) / 60;
    seconds = timer % 60;
    this.timeLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
  }

  public void updateLine(int line) {
    this.linesLabel.setText(Integer.toString(line));
  }

//  public void updateHold(Tetromino hold) {
//    this.holdTetromino.setImage(getTetrominoImage(hold));
//  }
//
//  public void updateNextPiece(Tetromino tetromino) {
//    this.nextTetromino.setImage(this.getTetrominoImage(tetromino));
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

  private Image cubeColor(Mino color) {
    switch (color) {
      case S_MINO:
        return this.image_SMino;
      case T_MINO:
        return this.image_TMino;
      case I_MINO:
        return this.image_IMino;
      case J_MINO:
        return this.image_JMino;
      case L_MINO:
        return this.image_LMino;
      case Z_MINO:
        return this.image_ZMino;
      case O_MINO:
        return this.image_OMino;
      default:
        return this.image_NOMino;
    }
  }

  private void doBindings() {
    // Bindings circles
    this.circleHold.centerXProperty().bind(this.stackPaneHold.widthProperty().divide(2));
    this.circleHold.centerYProperty().bind(this.stackPaneHold.heightProperty().divide(2));
    this.circleHold.radiusProperty().bind(
        Bindings.min(this.stackPaneHold.widthProperty(), this.stackPaneHold.heightProperty())
            .divide(2));

    this.circleNext.centerXProperty().bind(this.stackPaneNext.widthProperty().divide(2));
    this.circleNext.centerYProperty().bind(this.stackPaneNext.heightProperty().divide(2));
    this.circleNext.radiusProperty().bind(
        Bindings.min(this.stackPaneNext.widthProperty(), this.stackPaneNext.heightProperty())
            .divide(2));

    // Bindings hold
    this.holdTetromino.fitWidthProperty().bind(this.stackPaneHold.widthProperty().divide(1.3));
    this.holdTetromino.fitHeightProperty().bind(this.stackPaneHold.heightProperty().divide(1.3));

    // Binding next
    this.nextTetromino.fitWidthProperty().bind(this.stackPaneNext.widthProperty().divide(1.3));
    this.nextTetromino.fitHeightProperty().bind(this.stackPaneNext.heightProperty().divide(1.3));

    // Board
    this.boardPane.prefHeightProperty().bind(this.scene.heightProperty());
    this.boardPane.prefWidthProperty().bind(this.scene.widthProperty());
  }
}
