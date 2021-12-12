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
package esi.acgt.atlj.client.view.components;

import esi.acgt.atlj.model.tetrimino.Mino;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MinoView extends Pane {

  public static final int HEIGHT = 7;

  public static final int WEIGHT = 7;

  private final static Image A1;

  private final static Image B1;

  private final static Image C1;

  static {
    A1 = new Image(
        Objects.requireNonNull(MinoView.class.getResourceAsStream("/image/mino/A0.png")));
    B1 = new Image(
        Objects.requireNonNull(MinoView.class.getResourceAsStream("/image/mino/B0.png")));
    C1 = new Image(
        Objects.requireNonNull(MinoView.class.getResourceAsStream("/image/mino/C0.png")));

  }

  private final ImageView image;

  public MinoView() {
    image = new ImageView();
    this.getChildren().add(image);
    image.fitHeightProperty().bind(this.heightProperty());
    image.fitWidthProperty().bind(this.widthProperty());
  }

  /**
   * Set the image of the right mino.
   *
   * @param mino the right image of null if mino is null.
   */
  public void set(Mino mino) {
    if (mino == null) {
      image.setImage(null);
      return;
    }

    switch (Objects.requireNonNull(mino)) {
      case T_MINO, O_MINO, I_MINO -> image.setImage(A1);
      case L_MINO, Z_MINO -> image.setImage(B1);
      case J_MINO, S_MINO -> image.setImage(C1);
      default -> image.setImage(null);
    }
  }

}
