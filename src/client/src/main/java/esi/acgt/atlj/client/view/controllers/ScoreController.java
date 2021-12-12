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
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class ScoreController implements Initializable {

  public static final int WIDTH = 64;

  public static final int HEIGHT = 72;

  public static final int PADDING = 8;

  public StackPane container;

  public ImageView background;

  public Label topTitle;

  public Label topValue;

  public Label scoreTitle;

  public Label scoreValue;

  public static String scoreToText(int score) {
    StringBuilder sb = new StringBuilder();
    sb.append("0".repeat(6));

    var a = Integer.toString(score < 1000000 ? score : 999999);
    return sb.substring(a.length()) + a;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    container.prefWidthProperty().bind(container.heightProperty().divide(HEIGHT).multiply(WIDTH));
    container.prefHeightProperty().bind(container.widthProperty().divide(WIDTH).multiply(HEIGHT));

    background.fitWidthProperty().bind(container.widthProperty());
    background.fitHeightProperty()
        .bind(background.fitWidthProperty().divide(WIDTH).multiply(HEIGHT));

    for (Label label : Arrays.asList(topTitle, topValue, scoreTitle, scoreValue)) {
      label.fontProperty()
          .bind(Bindings.createObjectBinding(
              () -> Font.loadFont(
                  Objects.requireNonNull(getClass().getResource("/fonts/Pixel_NES.otf"))
                      .openStream(),
                  container.heightProperty().divide(HEIGHT).multiply(7).doubleValue()),
              container.heightProperty()));
    }

    topTitle.paddingProperty().bind(Bindings.createObjectBinding(
        () -> {
          var padding = container.widthProperty().divide(WIDTH).multiply(PADDING).doubleValue();
          return new Insets(container.heightProperty().divide(HEIGHT).multiply(16).doubleValue(),
              padding, padding,
              padding);
        },
        container.heightProperty()));

    topValue.paddingProperty().bind(Bindings.createObjectBinding(
        () -> {
          var padding = container.widthProperty().divide(WIDTH).multiply(PADDING).doubleValue();
          return new Insets(container.heightProperty().divide(HEIGHT).multiply(24).doubleValue(),
              padding, padding,
              padding);
        },
        container.heightProperty()));

    scoreTitle.paddingProperty().bind(Bindings.createObjectBinding(
        () -> {
          var padding = container.widthProperty().divide(WIDTH).multiply(PADDING).doubleValue();
          return new Insets(container.heightProperty().divide(HEIGHT).multiply(40).doubleValue(),
              padding, padding,
              padding);
        },
        container.heightProperty()));

    scoreValue.paddingProperty().bind(Bindings.createObjectBinding(
        () -> {
          var padding = container.widthProperty().divide(WIDTH).multiply(PADDING).doubleValue();
          return new Insets(container.heightProperty().divide(HEIGHT).multiply(48).doubleValue(),
              padding, padding,
              padding);
        },
        container.heightProperty()));
  }

  public void setScore(int score) {
    scoreValue.setText(scoreToText(score));
  }

  public void setHighScore(int highScore) {
    topValue.setText(scoreToText(highScore));
  }

}
