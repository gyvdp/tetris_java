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
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Connexion Scene
 */
public class ConnexionController implements Initializable {

  /**
   * Pattern of a adress IP
   */
  private static final Pattern regex = Pattern.compile("^(?:[0-9]{1,3}.){3}[0-9]{1,3}$");

  private final Controller controller;
  private final Stage stage;
  @FXML
  public TextField ip;
  @FXML
  public TextField username;
  @FXML
  public TextField port;

  /**
   * Constructor of Connexion
   *
   * @param controller controller wich we interact with
   * @param stage      stage of the window for this scene
   */
  public ConnexionController(Controller controller, Stage stage) {
    this.controller = controller;
    this.stage = stage;

    this.customizeStage();

    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/fxml/Connexion.fxml"));
    loader.setController(this);
    try {
      stage.setScene(new Scene(loader.load()));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Add a icon on the stage, a action and a title.
   */
  private void customizeStage() {
    this.stage.getIcons()
        .add(new Image(Objects.requireNonNull(
            ConnexionController.class.getResourceAsStream("/image/tetris-icon-32.png"))));
    this.stage.setTitle("Tetris connexion");
    this.stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
      if (key.getCode() == KeyCode.ENTER) {
        this.connexion();
      }
    });
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
   * Verify if all the textfield are complete by using informationComplete and try to connect the
   * client to the server and close the stage.
   */
  public void connexion() {
    if (informationComplete()) {
      this.controller.connexion(ip.getText(), Integer.parseInt(port.getText()),
          username.getText());
      this.stage.close();
    }
  }

  /**
   * Close this stage and should stop the program.
   */
  public void leavePressed() {
    this.stage.close();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }
}
