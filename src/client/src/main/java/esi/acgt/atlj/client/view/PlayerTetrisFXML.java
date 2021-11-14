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

import static esi.acgt.atlj.model.board.GameInterface.HEIGHT;
import static esi.acgt.atlj.model.board.GameInterface.WIDTH;

import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * Scene that contains a player's board and informations
 */
public class PlayerTetrisFXML implements Initializable, PropertyChangeListener {

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
  private ImageView[][] minosGrid;

  /**
   * load all Images
   */
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

  /**
   * Constructor of PlayerTetrisFXML
   *
   * @param motherbox Box wich we will bind everything
   */
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

  /**
   * Initialize for TetrisBoard.fxml
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    doBindings();

    if (this.minosGrid == null) {
      this.minosGrid = new ImageView[HEIGHT][WIDTH];
      for (int i = 0; i < this.minosGrid.length; i++) {
        for (int j = 0; j < this.minosGrid[i].length; j++) {
          this.minosGrid[i][j] = new ImageView();
          this.minosGrid[i][j].setPreserveRatio(true);
          this.minosGrid[i][j].fitWidthProperty()
              .bind(Bindings.min(this.boardPane.widthProperty().subtract(0.5 * 11).divide(11),
                  this.boardPane.heightProperty().subtract(0.5 * 23).divide(23)));
          this.minosGrid[i][j].fitHeightProperty()
              .bind(Bindings.min(this.boardPane.widthProperty().subtract(0.5 * 11).divide(11),
                  this.boardPane.heightProperty().subtract(0.5 * 23).divide(23)));
          this.minosGrid[i][j].setImage(image_NOMino);

          this.boardPane.add(this.minosGrid[i][j], j, i);
        }
      }
    }

  }

  /**
   * Update the board of this player
   *
   * @param oldBoard oldBoard of this player
   * @param newBoard newBoard of this player
   */
  public void updateBoard(Mino[][] oldBoard, Mino[][] newBoard) {
    for (int i = 0; i < this.minosGrid.length; i++) {
      for (int j = 0; j < this.minosGrid[i].length; j++) {
        if (oldBoard[i][j] != newBoard[i][j]) {
          this.minosGrid[i][j].setImage(cubeColor(newBoard[i][j]));
        }
      }
    }
  }

  /**
   * Update score of this player
   *
   * @param newScore new Score of this player
   */
  public void updateScore(int newScore) {
    this.scoreLabel.setText(Integer.toString(newScore));
  }

  /**
   * Update username of this player
   *
   * @param newUsername new Username of this player
   */
  public void updateUsername(String newUsername) {
    Platform.runLater(() -> this.usernameLabel.setText(newUsername));
  }

  /**
   * Update Line of this player
   *
   * @param line new Line of this player
   */
  public void updateLine(int line) {
    this.linesLabel.setText(Integer.toString(line));
  }

  /**
   * Update Hold Image of this player
   *
   * @param hold new hold Tetrimino of this player
   */
  public void updateHold(TetriminoInterface hold) {
    this.holdTetrimino.setImage(this.getTetriminoImage(hold.getType()));
  }

  /**
   * Update NextPiece of this player
   *
   * @param tetrimino new next Tetrimino of this player
   */
  public void updateNextPiece(TetriminoInterface tetrimino) {
    this.nextTetrimino.setImage(this.getTetriminoImage(tetrimino.getType()));
  }

  /**
   * Return a Tetrimino Image relatide to the input Mino
   *
   * @param tetrimino Mino of a Tetrimino
   * @return Tetrimino Image
   */
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

  /**
   * Return a Image is determine from the input color
   *
   * @param color Mino that will determine the image
   * @return Image that is a Mino
   */
  private Image cubeColor(Mino color) {
    if (color == null) {
      return this.image_NOMino;
    }
    return switch (color) {
      case S_MINO -> image_SMino;
      case T_MINO -> image_TMino;
      case I_MINO -> image_IMino;
      case J_MINO -> image_JMino;
      case L_MINO -> image_LMino;
      case Z_MINO -> image_ZMino;
      case O_MINO -> image_OMino;
    };
  }

  /**
   * Do all the Binding for this scene and item in this class
   */
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
        Bindings.min(this.stackPaneHold.widthProperty().multiply(0.6),
            this.stackPaneHold.heightProperty().multiply(0.6)));
    this.holdTetrimino.fitHeightProperty().bind(
        Bindings.min(this.stackPaneHold.widthProperty().multiply(0.6),
            this.stackPaneHold.heightProperty().multiply(0.6)));

    // Binding next
    this.nextTetrimino.fitWidthProperty()
        .bind(Bindings.min(this.stackPaneNext.widthProperty().multiply(0.6),
            this.stackPaneNext.heightProperty().multiply(0.6)));
    this.nextTetrimino.fitHeightProperty()
        .bind(Bindings.min(this.stackPaneNext.widthProperty().multiply(0.6),
            this.stackPaneNext.heightProperty().multiply(0.6)));

  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case "board" -> {
        updateBoard((Mino[][]) evt.getOldValue(), (Mino[][]) evt.getNewValue());
      }
      case "line" -> {
        updateLine((int) evt.getNewValue());
      }

      case "score" -> {
        updateScore((int) evt.getNewValue());
      }

      case "username" -> {
        updateUsername(evt.getNewValue().toString());
      }

      case "hold" -> {
        updateHold((TetriminoInterface) evt.getNewValue());
      }

      case "next" -> {
        updateNextPiece((TetriminoInterface) evt.getNewValue());
      }
    }
  }
}
