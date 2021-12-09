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

import static esi.acgt.atlj.message.PlayerAction.QUIT;

import esi.acgt.atlj.client.model.Client;
import esi.acgt.atlj.client.model.ClientInterface;
import esi.acgt.atlj.client.view.View;
import esi.acgt.atlj.client.view.ViewInterface;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Client Application
 */
public class Controller extends Application {

  private ViewInterface view;

  private ClientInterface client;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    this.view = new View();
    view.setController(this);
    view.displayConnexion();

    final Parameters params = getParameters();
    for (var param : params.getRaw()) {
      switch (param) {
        case "--localhost" -> this.connexion("127.0.0.1", 6969, "Pacliclown");
        // todo : case "--client" -> this.solo("Andrew");
      }
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
      this.client = new Client(port, host);
      this.client.connect();
      this.client.sendNameToServer(username);
      this.startMenu(username);
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion();
    }
    this.view.show();
  }

  /**
   * End the Programme
   */
  public void disconnect() {
    view.displayConnexion();
    view.show();
  }

  /**
   * Start the menu window and link it to the client
   *
   * @param username username of the player
   */
  public void startMenu(String username) {
    this.view.displayMenu(username);
    // todo : this.model.addPropertyChangeListenerToClient(this.view.getMenuListener());
    this.client.askAllStatistics();
  }

  public void leaveMatch() {
    //todo stop real game.
    client.sendAction(QUIT);
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
    this.client.startMultiplayerGame(username);
    this.view.displayBoard(username);
    this.client.addPCSToBoard(this.view.getBoardListeners());
  }

}
