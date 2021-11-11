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

import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;


public class PlayerTetrisFXML implements Initializable {

  private final static Image image_NOMino;
  private final static Image image_SMino;
  private final static Image image_IMino;
  private final static Image image_JMino;
  private final static Image image_LMino;
  private final static Image image_TMino;
  private final static Image image_ZMino;
  private final static Image image_OMino;
  private final static Image tetri_J;
  private final static Image tetri_S;
  private final static Image tetri_O;
  private final static Image tetri_Z;
  private final static Image tetri_L;
  private final static Image tetri_I;
  private final static Image tetri_T;

  static {
    image_NOMino = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/WhiteCube.png")));
    image_SMino = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/GreenCube.png")));
    image_IMino = new Image(
        Objects.requireNonNull(
            PlayerTetrisFXML.class.getResourceAsStream("/image/LightBlueCube.png")));
    image_JMino = new Image(
        Objects.requireNonNull(
            PlayerTetrisFXML.class.getResourceAsStream("/image/DarkBlueCube.png")));
    image_LMino = new Image(
        Objects.requireNonNull(
            PlayerTetrisFXML.class.getResourceAsStream("/image/OrangeCube.png")));
    image_TMino = new Image(
        Objects.requireNonNull(
            PlayerTetrisFXML.class.getResourceAsStream("/image/PurpleCube.png")));
    image_ZMino = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/RedCube.png")));
    image_OMino = new Image(
        Objects.requireNonNull(
            PlayerTetrisFXML.class.getResourceAsStream("/image/YellowCube.png")));

    tetri_I = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/I.png")));
    tetri_S = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/S.png")));
    tetri_T = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/T.png")));
    tetri_O = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/O.png")));
    tetri_Z = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/Z.png")));
    tetri_J = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/J.png")));
    tetri_L = new Image(
        Objects.requireNonNull(PlayerTetrisFXML.class.getResourceAsStream("/image/L.png")));
  }

  // Variable
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
  public ImageView holdTetrimino;
  @FXML
  public StackPane stackPaneHold;
  // Next
  @FXML
  public ImageView nextTetrimino;
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


  public PlayerTetrisFXML(HBox motherbox) {
    // FXML loading
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/fxml/TetrisBoard.fxml"));
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Bindings of this.scene
    this.scene.prefWidthProperty().bind(motherbox.widthProperty().divide(2));
    this.scene.prefHeightProperty().bind(motherbox.heightProperty());
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    doBindings();
    for (int i = 0; i < boardPane.getColumnCount(); i++) {
      for (int j = 0; j < boardPane.getRowCount(); j++) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty()
            .bind(Bindings.min(this.boardPane.widthProperty().subtract(0.5 * 11).divide(11),
                this.boardPane.heightProperty().subtract(0.5 * 23).divide(23)));
        imageView.fitHeightProperty()
            .bind(Bindings.min(this.boardPane.widthProperty().subtract(0.5 * 11).divide(11),
                this.boardPane.heightProperty().subtract(0.5 * 23).divide(23)));
        imageView.setImage(image_NOMino);
        this.boardPane.add(imageView, i, j);
      }
    }
  }

  public void updateBoard(Mino[][] oldboard, Mino[][] newBoard) {
    int i = 0;
    var list = this.boardPane.getChildren();
    for (Node node : list) {
      if (node instanceof ImageView) {
        if (newBoard[i / 22][i % 22] != oldboard[i / 22][i % 22]) {
          ((ImageView) node).setImage(
              cubeColor(newBoard[i / 22][i % 22]));
        }
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

  public void updateHold(TetriminoInterface hold) {
    this.holdTetrimino.setImage(this.getTetriminoImage(hold.getType()));
  }

  public void updateNextPiece(TetriminoInterface tetrimino) {
    this.nextTetrimino.setImage(this.getTetriminoImage(tetrimino.getType()));
  }

  private Image getTetriminoImage(Mino tetrimino) {
    return switch (tetrimino) {
      case I_MINO -> tetri_I;
      case J_MINO -> tetri_J;
      case L_MINO -> tetri_L;
      case O_MINO -> tetri_O;
      case S_MINO -> tetri_S;
      case T_MINO -> tetri_T;
      case Z_MINO -> tetri_Z;
    };
  }

  private Image cubeColor(Mino color) {
    switch (color) {
      case S_MINO:
        return image_SMino;
      case T_MINO:
        return image_TMino;
      case I_MINO:
        return image_IMino;
      case J_MINO:
        return image_JMino;
      case L_MINO:
        return image_LMino;
      case Z_MINO:
        return image_ZMino;
      case O_MINO:
        return image_OMino;
      default:
        return image_NOMino;
    }
  }

  private void doBindings() {
    // Bindings circles
    this.circleHold.radiusProperty().bind(
        Bindings.min(this.stackPaneHold.widthProperty().multiply(0.35),
            this.stackPaneHold.heightProperty().multiply(0.35)));

    this.circleNext.radiusProperty().bind(
        Bindings.min(this.stackPaneNext.widthProperty().multiply(0.35),
            this.stackPaneNext.heightProperty().multiply(0.35)));

    // Bindings hold
    this.holdTetrimino.fitWidthProperty().bind(
        Bindings.min(this.stackPaneHold.widthProperty().divide(1.3),
            this.stackPaneHold.heightProperty().divide(1.3)));
    this.holdTetrimino.fitHeightProperty().bind(
        Bindings.min(this.stackPaneHold.widthProperty().divide(1.3),
            this.stackPaneHold.heightProperty().divide(1.3)));

    // Binding next
    this.nextTetrimino.fitWidthProperty()
        .bind(Bindings.min(this.stackPaneNext.widthProperty().divide(1.3),
            this.stackPaneNext.heightProperty().divide(1.3)));
    this.nextTetrimino.fitHeightProperty()
        .bind(Bindings.min(this.stackPaneNext.widthProperty().divide(1.3),
            this.stackPaneNext.heightProperty().divide(1.3)));

  }
}
