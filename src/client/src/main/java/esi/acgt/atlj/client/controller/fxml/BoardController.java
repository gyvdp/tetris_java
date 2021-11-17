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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BoardController implements Initializable {

  public final static int H = BoardMainController.H;
  public final static int L = BoardMainController.L;


  @FXML
  public HBox container;

  @FXML
  public VBox boardMain;

  @FXML
  public BoardMainController boardMainController;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
//    container.minHeightProperty().bind(container.widthProperty().multiply(H / L));
//    container.maxHeightProperty().bind(container.widthProperty().multiply(H / L));
//    container.minWidthProperty().bind(container.heightProperty().multiply(L / H));
//    container.maxWidthProperty().bind(container.heightProperty().multiply(L / H));
//    boardMain.prefHeightProperty().bind(container.heightProperty());
//    boardMain.prefWidthProperty().bind(container.widthProperty());

//    System.out.printf("BoardController: containerWidth = %f%n",
//        container.minWidthProperty().doubleValue());
//    System.out.printf("BoardController: containerHeight = %f%n",
//        container.minHeightProperty().doubleValue());
//
//    System.out.printf("BoardController: boardMainWidth = %f%n",
//        boardMain.minWidthProperty().doubleValue());
//    System.out.printf("BoardController: boardMainHeight = %f%n",
//        boardMain.minHeightProperty().doubleValue());

  }

  public void setMatrix(Mino[][] minos) {

  }
}
