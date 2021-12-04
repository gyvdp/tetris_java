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

import esi.acgt.atlj.client.model.ClientModel;
import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.message.PlayerAction;
import esi.acgt.atlj.model.game.Direction;
import esi.acgt.atlj.model.game.GameStatus;
import java.util.Objects;
import javafx.scene.input.KeyCode;

/**
 * Controller of the Client application
 */
public class Controller {

  private final ClientModel model;

  private final ViewInterface view;

  /**
   * Constructor of Controller
   *
   * @param model Model of the Client
   * @param view  View of the Client
   */
  public Controller(ClientModel model, ViewInterface view) {
    Objects.requireNonNull(model, "model can not be null");
    Objects.requireNonNull(view, "view can not be null");
    this.model = model;
    this.view = view;
  }


  /**
   * Launches the view
   */
  public void start() {
    view.setController(this);
    view.displayConnexion();
    view.show();
  }

  /**
   * End the Programme
   */
  public void disconnect() {
    view.displayConnexion();
    model.closeConnection();
    view.show();
  }


  /**
   * Forward inputs from the view to the model
   *
   * @param input keyboard input from the view
   */
  public void keyBoardInput(KeyCode input) {
    if (this.model.getStatus() != GameStatus.NOT_STARTED
        && this.model.getStatus() != GameStatus.LOCK_OUT) {
      switch (input) {
        case LEFT, NUMPAD4 -> this.model.move(Direction.LEFT);
        case RIGHT, NUMPAD6 -> this.model.move(Direction.RIGHT);
        case DOWN, NUMPAD2 -> this.model.softDrop();
        case UP, X, NUMPAD3, NUMPAD5 -> this.model.rotate(true);
        case SHIFT, C, NUMPAD0 -> this.model.hold();
        case SPACE, NUMPAD8 -> this.model.hardDrop();
        case CONTROL, NUMPAD1, NUMPAD9 -> this.model.rotate(false);
      }
    }

  }

  /**
   * Connexion to the server and when it's done start the board view
   *
   * @param ip       ip of the server to connect to
   * @param port     port of the server to connect to
   * @param username username of the player
   */
  public void connexion(String ip, int port, String username) {
    try {
      this.model.connect(port, ip);
      this.startMenu(username);
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion();
    }
    this.view.show();
  }

  /**
   * Start the menu window and link it to the client
   *
   * @param username username of the player
   */
  public void startMenu(String username) {
    this.view.displayMenu(username);
    this.model.addPropertyChangeListenerToClient(this.view.getMenuListener());
    //DEMANDER AU SERVER LES INFOS AU MENU.
  }

  /**
   * Start the game window and link the boards to the model
   *
   * @param username username of the player
   */
  public void startPlay(String username) {
    this.model.initManagedBoard(username); // todo factory class
    this.view.displayBoard(username);
    this.model.addPropertyChangeListenerToBoards(this.view.getBoardListeners());
    this.model.sendAction(PlayerAction.PLAY_ONLINE);
  }

  public void solo(String username) {
    try {
      this.model.initManagedBoard(username);
      this.view.displayBoard(username);
      this.model.addPropertyChangeListenerToBoards(this.view.getBoardListeners());
      this.model.start();
    } catch (Exception e) {
      e.printStackTrace();
      this.view.displayError(e);
      this.view.displayConnexion();
    }
    this.view.show();
  }

  public void leaveMatch() {
    //TODO Quitter le match et "stop" le model.
  }
}
