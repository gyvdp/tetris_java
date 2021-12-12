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
package esi.acgt.atlj.client.controller;

import esi.acgt.atlj.client.model.Client;
import esi.acgt.atlj.client.model.ClientInterface;
import esi.acgt.atlj.client.model.game.MultiplayerGame;
import esi.acgt.atlj.client.view.View;
import esi.acgt.atlj.client.view.ViewInterface;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Client Application
 */
public class Controller extends Application {

  private ViewInterface view;

  private ClientInterface client;

  /**
   * Main of the client application
   *
   * @param args args passed at launch
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Start the application
   *
   * @param stage stage of the application
   */
  @Override
  public void start(Stage stage) {
    this.view = new View(stage);
    view.setController(this);

    String host = null;
    String port = null;
    String username = null;
    Level logLevel = Level.WARNING;

    final Parameters params = getParameters();
    for (Map.Entry<String, String> entry : params.getNamed().entrySet()) {
      switch (entry.getKey()) {
        case "host" -> host = entry.getValue();
        case "port" -> port = entry.getValue();
        case "username" -> username = entry.getValue();
        case "log" -> logLevel = Level.parse(entry.getValue());
      }
    }

    setLogLevel(logLevel);
    view.displayConnexion(host, port, username);
    view.show();
  }

  public static void setLogLevel(Level targetLevel) {
    Logger root = Logger.getLogger("");
    root.setLevel(targetLevel);
    for (Handler handler : root.getHandlers()) {
      handler.setLevel(targetLevel);
    }
  }

  /**
   * Connexion to the server and when it's done start the board view
   *
   * @param host     host of the server to connect to
   * @param port     port of the server to connect to
   * @param username username of the player
   */
  public void connexion(String host, int port, String username) {
    try {
      this.client = new Client(port, host, username);
      this.startMenu(username);
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion(host, String.valueOf(port), username);
    }
    this.view.show();
  }

  /**
   * End the Programme
   */
  public void stop() {
    view.quit();
    if (client != null) {
      client.closeConnectionToServer();
      client = null;
    }

  }

  /**
   * Start the menu window and link it to the client
   *
   * @param username username of the player
   */
  public void startMenu(String username) {
    this.view.displayMenu(username, client.getStats());
  }

  public void leaveMatch() {
    ((MultiplayerGame) client.getActualGame()).getPlayer().stop();
    startMenu(((MultiplayerGame) client.getActualGame()).getPlayer().getUsername());
  }

  /**
   * Forward inputs from the view to the model
   *
   * @param input keyboard input from the view
   */
  public void keyBoardInput(KeyCode input) {
    this.client.keyBoardInput(input);
  }

  /**
   * Start the game window and link the boards to the model
   *
   * @param username username of the player
   */
  public void startMultiplayerGame(String username) {
    this.view.displayBoard(username);
    this.client.startMultiplayerGame(username, this.view.getBoardListeners());
  }

}
