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

package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Connexion implements Initializable {

  private static final Pattern regex = Pattern.compile("^(?:[0-9]{1,3}.){3}[0-9]{1,3}$");
  @FXML
  public TextField ip;
  @FXML
  public TextField username;
  @FXML
  public TextField port;
  private final Controller controller;
  private final Stage stage;

  public Connexion(Controller controller, Stage stage) {
    this.controller = controller;
    this.stage = stage;
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/fxml/Connexion.fxml"));
    loader.setController(this);
    try {
      stage.setScene(new Scene(loader.load()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
      if (key.getCode() == KeyCode.ENTER) {
        this.connexionPressed();
      }
    });
  }

  public void connexionPressed() {
    try {
      Matcher m = regex.matcher(this.ip.getText());
      if (m.matches()) {
        this.controller.connexion(ip.getText(), Integer.parseInt(port.getText()),
            username.getText());
      } else {
        throw new IllegalArgumentException();
      }
      this.stage.close();
    } catch (Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setContentText("Les informations entrées sont incorrects.");
      alert.setHeaderText("Information Erroné");
      alert.show();
    }
  }

  public void leavePressed() {
    this.stage.close();
  }

  //TODO retirer button test de la version finale
  public void testPressed() {
    this.controller.connexion("1.1.1.1", 1, "usertest");
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}
