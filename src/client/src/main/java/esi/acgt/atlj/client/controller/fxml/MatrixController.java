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

package esi.acgt.atlj.client.controller.fxml;

import esi.acgt.atlj.client.view.components.MinoView;
import esi.acgt.atlj.model.tetrimino.Mino;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MatrixController implements Initializable {

  public final static int H = 174;
  public final static int L = 93;
  public final static int P = 7;

  @FXML
  public StackPane container;

  @FXML
  public GridPane grid;

  public MinoView[][] minos;

  public ImageView background;

  public MatrixController() {
    minos = new MinoView[20][10];
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    grid.prefHeightProperty().bind(grid.widthProperty().divide(L).multiply(H));
    grid.prefWidthProperty().bind(container.widthProperty());

    background.fitHeightProperty().bind(grid.heightProperty());
    background.fitWidthProperty().bind(grid.widthProperty());

    grid.paddingProperty().bind(Bindings.createObjectBinding(
        () -> new Insets(grid.widthProperty().divide(L).multiply(P).doubleValue()),
        grid.widthProperty()));

    grid.hgapProperty().bind(grid.widthProperty().divide(L));
    grid.vgapProperty().bind(grid.heightProperty().divide(H));

    for (int i = 0; i < 10; ++i) {
      for (int j = 0; j < 20; ++j) {
        var mino = new MinoView();
        grid.add(mino, i, j);
        minos[j][i] = mino;
      }
    }
  }

  public void set(Mino[][] grid) {
    if (grid.length != 20) {
      throw new IllegalArgumentException("The height of the board must be 20");
    }

    for (int i = 0; i < grid.length; ++i) {
      if (grid[i].length != 10) {
        throw new IllegalArgumentException("The width of the board must be 10");
      }
      for (int j = 0; j < grid[i].length; ++j) {
        minos[i][j].set(grid[i][j]);
      }
    }
  }
}
