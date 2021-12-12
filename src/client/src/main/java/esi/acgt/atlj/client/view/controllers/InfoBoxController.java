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

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class InfoBoxController implements Initializable {

  public final static int HEIGHT = 24;

  public final static int WIDTH = 93;

  //todo never used
  public final static int PADDING = 8;

  public Label infoBoxText;

  public ImageView background;

  public Pane container;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    background.fitWidthProperty().bind(container.widthProperty());
    background.fitHeightProperty()
        .bind(background.fitWidthProperty().divide(WIDTH).multiply(HEIGHT));

    infoBoxText.prefWidthProperty().bind(container.widthProperty());
    infoBoxText.prefHeightProperty()
        .bind(infoBoxText.widthProperty().divide(WIDTH).multiply(HEIGHT));

    infoBoxText.fontProperty()
        .bind(Bindings.createObjectBinding(
            () -> Font.loadFont(
                Objects.requireNonNull(getClass().getResource("/fonts/Pixel_NES.otf")).openStream(),
                container.heightProperty().divide(HEIGHT).multiply(7).doubleValue()),
            container.heightProperty()));
  }

  public void setText(String text) {
    infoBoxText.setText(text.toUpperCase());
  }

}
