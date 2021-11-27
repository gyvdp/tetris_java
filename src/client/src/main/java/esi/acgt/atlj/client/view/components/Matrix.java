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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Matrix implements Initializable {

  public final static int H = 174;

  public final static int L = 93;

  public final static int P = 7;

  @FXML
  public Pane container;

  @FXML
  public GridPane grid;

  public Matrix() {
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    container.prefHeightProperty().bind(container.widthProperty().multiply(H / L));
    container.prefWidthProperty().bind(container.heightProperty().multiply(L / H));

    grid.paddingProperty().bind(Bindings.createObjectBinding(
        () -> new Insets(container.widthProperty().divide(93).multiply(6).doubleValue()),
        container.widthProperty()));
    grid.prefWidthProperty().bind(container.widthProperty().divide(93).multiply(81));
    grid.prefHeightProperty().bind(container.heightProperty().divide(174).multiply(162));

    for (int i = 0; i < 10; ++i) {
      for (int j = 0; j < 20; ++j) {
        var mino = new Mino();
        grid.add(new Mino(), i, j);
        mino.fitHeightProperty().bind(grid.heightProperty().divide(10));
      }
    }
  }
}
