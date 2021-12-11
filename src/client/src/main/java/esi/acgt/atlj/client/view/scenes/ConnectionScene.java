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

package esi.acgt.atlj.client.view.scenes;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.client.view.controllers.ConnexionController;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ConnectionScene extends Scene {

  private final ConnexionController controller;

  public ConnectionScene(Parent root, ConnexionController controller, Controller rootController,
      String host, String port, String username) {
    super(root);

    Objects.requireNonNull(controller);
    this.controller = controller;
    this.controller.setController(rootController);
    setFields(host, port, username);
  }

  public void setFields(String host, String port, String username) {
    this.controller.setHost(host);
    this.controller.setPort(port);
    this.controller.setUsername(username);
  }
}
