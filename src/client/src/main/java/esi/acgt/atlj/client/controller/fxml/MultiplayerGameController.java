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

import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

public class MultiplayerGameController implements Initializable {

  public static final int H = BoardController.H;
  public static final int L = BoardMainController.L * 2;

  public HBox container;

  public HBox left;
  public BoardController leftController;

  public HBox right;
  public BoardController rightController;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    container.prefWidthProperty().bind(container.heightProperty().divide(H).multiply(L));
    container.prefHeightProperty().bind(container.widthProperty().divide(L).multiply(H));

    left.maxWidthProperty().bind(container.widthProperty().divide(2));
    left.maxHeightProperty().bind(container.heightProperty());

    right.maxWidthProperty().bind(container.widthProperty().divide(2));
    right.maxHeightProperty().bind(container.heightProperty());
  }

  public void setMainPlayerUsername(String username) {
    leftController.setUsername(username);
  }

  public PropertyChangeListener getPlayer1() {
    return leftController;
  }

  public PropertyChangeListener getPlayer2() {
    return rightController;
  }
}
