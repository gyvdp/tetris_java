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

import esi.acgt.atlj.model.tetrimino.Mino;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class TetriminoHolderController implements Initializable {

  public static final int WEIGHT = 45;

  public static final int HEIGHT = 54;

  public static final int PADDING = 7;

  public static final Map<Mino, Image> imageMap = Map.of(
      Mino.L_MINO,
      new Image(Objects.requireNonNull(
          TetriminoHolderController.class.getResourceAsStream("/image/tetrimino/L0.png"))),

      Mino.J_MINO,
      new Image(Objects.requireNonNull(
          TetriminoHolderController.class.getResourceAsStream("/image/tetrimino/J0.png"))),

      Mino.Z_MINO, new Image(Objects.requireNonNull(
          TetriminoHolderController.class.getResourceAsStream("/image/tetrimino/Z0.png"))),

      Mino.S_MINO,
      new Image(Objects.requireNonNull(
          TetriminoHolderController.class.getResourceAsStream("/image/tetrimino/S0.png"))),

      Mino.O_MINO,
      new Image(Objects.requireNonNull(
          TetriminoHolderController.class.getResourceAsStream("/image/tetrimino/O0.png"))),

      Mino.I_MINO,
      new Image(Objects.requireNonNull(
          TetriminoHolderController.class.getResourceAsStream("/image/tetrimino/I0.png"))),

      Mino.T_MINO,
      new Image(Objects.requireNonNull(
          TetriminoHolderController.class.getResourceAsStream("/image/tetrimino/T0.png")))
  );

  public StackPane container;

  public ImageView background;

  public Label typeText;

  public ImageView tetrimino;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    background.fitWidthProperty().bind(container.widthProperty());
    background.fitHeightProperty().bind(background.fitWidthProperty().divide(WEIGHT).multiply(
        HEIGHT));

    background.fitWidthProperty().bind(container.widthProperty());

    typeText.fontProperty()
        .bind(Bindings.createObjectBinding(
            () -> Font.loadFont(
                Objects.requireNonNull(getClass().getResource("/fonts/Pixel_NES.otf"))
                    .openStream(),
                container.heightProperty().divide(HEIGHT).multiply(7).doubleValue()),
            container.heightProperty()));

    typeText.paddingProperty().bind(Bindings.createObjectBinding(
        () -> new Insets(container.heightProperty().divide(HEIGHT).multiply(7).doubleValue()),
        container.heightProperty()));

    tetrimino.fitWidthProperty().bind(container.widthProperty().divide(WEIGHT).multiply(
        WEIGHT - (2 * PADDING)));
    tetrimino.fitHeightProperty().bind(tetrimino.fitWidthProperty());

    container.heightProperty().addListener((observable) ->
        StackPane.setMargin(tetrimino,
            new Insets(container.heightProperty().divide(HEIGHT).multiply(7).doubleValue())));
  }

  public void setTetrimino(Mino mino) {
    tetrimino.setImage(mino != null ? imageMap.get(mino) : null);
  }

  public void setType(String type) {
    typeText.setText(type.toUpperCase());
  }

}
