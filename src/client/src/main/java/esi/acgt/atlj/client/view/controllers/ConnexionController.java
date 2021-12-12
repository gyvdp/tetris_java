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

import esi.acgt.atlj.client.controller.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

/**
 * Connexion Scene
 */
public class ConnexionController implements Initializable {

  /**
   * Pattern of an address IP
   */
  private static final Pattern regex = Pattern.compile("^(?:[0-9]{1,3}.){3}[0-9]{1,3}$");

  private Controller controller;

  @FXML
  public TextField ip;

  @FXML
  public TextField username;

  @FXML
  public TextField port;

  public void setController(Controller controller) {
    this.controller = controller;
  }

  public void setHost(String host) {
    this.ip.setText(host);
  }

  public void setPort(String port) {
    this.port.setText(port);
  }

  public void setUsername(String username) {
    this.username.setText(username);
  }

  /**
   * Verify if the information of the connection are complete and if it's correct information
   *
   * @return true if all information are complete and if the ip is in good format. False otherwise.
   */
  private boolean informationComplete() {
    Matcher m = regex.matcher(this.ip.getText());
    if (!(!m.matches() || this.username.getText().isEmpty() || this.port.getText()
        .isEmpty())) {
      return true;
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setContentText("Les informations entrées sont incorrects.");
      alert.setHeaderText("Information Erroné");
      alert.showAndWait();
      return false;
    }
  }

  /**
   * Action when you press on the button for connexion
   */
  public void connexion() {
    if (informationComplete()) {
      this.controller.connexion(ip.getText(), Integer.parseInt(port.getText()),
          username.getText());
    }
  }

  /**
   * Action when you press on the button to leave the application
   */
  public void leavePressed() {
    this.controller.stop();
  }

  //todo methode vide

  /**
   * Initiale of the Connexion.fxml
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }

}
