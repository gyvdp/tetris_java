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

package esi.acgt.atlj.client.model;

import esi.acgt.atlj.message.PlayerAction;
import esi.acgt.atlj.model.Game;
import esi.acgt.atlj.model.player.PlayerStatInterface;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeListener;
import java.net.ConnectException;
import java.util.List;
import javafx.scene.input.KeyCode;

/**
 * Contains all necessary methods to interact with a client.
 */
public interface ClientInterface {

  /**
   * Sends all game stats to user.
   *
   * @param gameStats Game stats interface to send to model.
   */
  void sendAllGameStats(PlayerStatInterface gameStats);

  /**
   * Tries to establish a connexion with server.
   */
  void connect() throws ConnectException;


  /**
   *
   */
  void askAllStatistics();

  /**
   * Request the next color of Tetrimino.
   */
  void requestNextMino();

  /**
   * Sends player action to server.
   *
   * @param a Action to send.
   */
  void sendAction(PlayerAction a);

  /**
   * Sends the mino that has been put into hold by player to server
   */
  void sendHoldMino(Mino m);


  /**
   * Sends a locked tetrimino message to server.
   *
   * @param m Tetrimino to lock.
   */
  void lockTetrimino(TetriminoInterface m);

  /**
   * Closes the connection to server
   */
  void closeConnectionToServer();

  void sendNameToServer(String name);

  /**
   * Notifies server of player loss
   */
  void notifyLoss();


  /**
   * Sends a tetrimino to the opposing player
   *
   * @param tetriminoInterface Tetrimino to send
   */
  void sendTetriminoToOtherPlayer(TetriminoInterface tetriminoInterface);

  /**
   * Sends your score to the server.
   *
   * @param Score Score of the current player.
   */
  void sendScore(int Score);

//  /**
//   * Add a propertyChangeListener to the propertyChangerSupport that's used for the Menu's data
//   *
//   * @param propertyChangeListener new Listenner
//   */
//  void addPropertyChangeListenerToClient(PropertyChangeListener propertyChangeListener);

//  /**
//   * Use the 2 hashmap that is recieve from the server's database to display data on the menu.
//   *
//   * @param gameStats      data from the player game data
//   * @param tetriminoStats data from player tetrimo data
//   */
//  void fireDataToMenu(HashMap<String, Integer> gameStats, HashMap<String, Integer> tetriminoStats);

  void startMultiplayerGame(String username);

  void addPCSToBoard(PropertyChangeListener[] listeners);

  Game getActualGame();

  ClientStatus getStatus();

  public void keyBoardInput(KeyCode input);
}
