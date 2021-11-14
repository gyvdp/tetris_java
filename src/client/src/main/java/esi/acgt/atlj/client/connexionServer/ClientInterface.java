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

package esi.acgt.atlj.client.connexionServer;

import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeListener;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Contains all necessary methods to interact with a client.
 */
public interface ClientInterface extends PropertyChangeListener {

  /**
   * Tries to establish a connexion with server.
   */
  void connect() throws ConnectException;

  /**
   * Request the next color of Tetrimino.
   */
  void requestNextMino();

  /**
   * Sends the mino that has been put into hold by player to server
   */
  void sendHoldMino(Mino m);


  /**
   * Connects hold to client
   *
   * @param hold Lambda to connect.
   */
  void connectHold(Consumer<Mino> hold);

  /**
   * Connect player ready lambda to client
   *
   * @param playerReady Lambda to connect
   */
  void connectPlayerReady(Runnable playerReady);

  /**
   * Connects other player lost lambda to client
   *
   * @param otherPlayerLost Lambda to connect
   */
  void connectOtherPlayerLost(Runnable otherPlayerLost);

  /**
   * Connects player disconnected to client
   *
   * @param playerDisconnected Lambda to connect
   */
  void connectPlayerDisconnected(Runnable playerDisconnected);

  /**
   * Connects a consumer of updateTetriminoOtherPlayer client to model.
   *
   * @param updateNextTetriminoOtherPlayer Lambda function to connect.
   */
  void connectUpdateNextTetriminoOtherPlayer(Consumer<Mino> updateNextTetriminoOtherPlayer);

  /**
   * Sends your name to the server.
   *
   * @param name Name of the current player.
   */
  void sendName(String name);

  /**
   * Connects a consumer of new mino of model to client.
   *
   * @param newMino Lambda function to connect.
   */
  void connectNewMinoFromServer(Consumer<Mino> newMino);

  /**
   * Connect a consumer of  addTetrimino of model to client.
   *
   * @param addTetrimino Lambda function to connect.
   */
  void connectAddTetrimino(Consumer<TetriminoInterface> addTetrimino);

  /**
   * Connect a consumer of sendScore of model to client.
   *
   * @param sendScore Lambda function to connect.
   */
  void connectSendScore(Consumer<Integer> sendScore);


  /**
   * Closes the connection to server
   */
  void closeConnectionToServer();

  /**
   * Connects a receive user name to client
   *
   * @param receiveName Lambda function to connect
   */
  void connectReceiveUserName(Consumer<String> receiveName);

  /**
   * Sends your name to the server
   *
   * @param name Same to send
   */
  void sendNameToServer(String name);

  /**
   * Connect a consumer of remove line of model to client.
   *
   * @param removeLine Lambda function to connect.
   */
  void connectRemoveLine(Consumer<ArrayList<Integer>> removeLine);


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

  /**
   * Sends line that current player just destroyed.
   *
   * @param linesDestroyed Line that current player just destroyed.
   */
  void removeLine(ArrayList<Integer> linesDestroyed);
}
