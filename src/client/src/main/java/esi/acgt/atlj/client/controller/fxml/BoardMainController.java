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

import esi.acgt.atlj.model.tetrimino.Mino;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BoardMainController implements Initializable {

  public final static int H = (InfoBoxController.H * 2) + MatrixController.H;
  public final static int L = MatrixController.L;

  @FXML
  public VBox container;

  @FXML
  public Pane topBox;

  @FXML
  public InfoBoxController topBoxController;

  @FXML
  public Pane matrix;

  @FXML
  public MatrixController matrixController;

  @FXML
  public Pane bottomBox;

  @FXML
  public InfoBoxController bottomBoxController;

  private int lines;

  public static String linesToText(int lines) {
    StringBuilder sb = new StringBuilder();
    sb.append("0".repeat(3));

    var a = Integer.toString(lines < 1000 ? lines : 999);
    return sb.substring(a.length()) + a;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    container.prefWidthProperty().bind(container.heightProperty().divide(H).multiply(L));
    container.prefHeightProperty().bind(container.widthProperty().divide(L).multiply(H));

    topBox.maxWidthProperty().bind(container.widthProperty());
    topBox.maxHeightProperty()
        .bind(container.heightProperty().divide(H).multiply(InfoBoxController.H));

    matrix.maxWidthProperty().bind(container.widthProperty());
    matrix.maxHeightProperty()
        .bind(container.heightProperty().divide(H).multiply(MatrixController.H));

    bottomBox.maxWidthProperty().bind(container.widthProperty());
    bottomBox.maxHeightProperty()
        .bind(container.heightProperty().divide(H).multiply(InfoBoxController.H));

    setUsername("Recherche");
    setLines(0);
  }

  public void setUsername(String username) {
    if (username.length() > 12) {
      throw new IllegalArgumentException("Username must be maximum 12 char long.");
    }
    bottomBoxController.setText(username);
  }

  public void setLines(int i) {
    lines = i;
    topBoxController.setText(String.format("lines - %s", linesToText(i)));
  }

  public void setMatrix(Mino[][] minos) {
    matrixController.set(minos);
  }
}
