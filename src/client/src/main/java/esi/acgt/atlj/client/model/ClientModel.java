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

import esi.acgt.atlj.client.connexionServer.Client;
import esi.acgt.atlj.client.connexionServer.ClientInterface;
import esi.acgt.atlj.model.Model;
import esi.acgt.atlj.model.game.Direction;
import esi.acgt.atlj.model.game.GameStatus;
import esi.acgt.atlj.model.game.ManagedGame;
import esi.acgt.atlj.model.game.UnmanagedGame;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeListener;
import java.net.ConnectException;
import java.util.List;
import java.util.function.Consumer;

public class ClientModel extends Model {

  private ManagedGame player;
  private UnmanagedGame otherPlayer;
  private ClientInterface client;

  public ClientModel() {
    super();
  }

  /**
   * Lambda expression to connect ask new mino from managed board to server.
   */
  Runnable askNextMino = () ->
  {
    if (client != null) {
      this.client.requestNextMino();
    } else {
      var minos = Mino.values();
      player.setNextTetrimino(
          Tetrimino.createTetrimino(minos[(int) (Math.random() * (minos.length))]));
    }
  };

  /**
   * Lambda expression to connect new mino from server to update from managed board.
   */
  Consumer<Mino> newMinoFromServer = (Mino nextMino) ->
      player.setNextTetrimino(Tetrimino.createTetrimino(nextMino));

  /**
   * Sets the name of the player given from the server
   */
  Consumer<String> receiveName = (String name) -> otherPlayer.setUsername(name);

  /**
   * Lambda expression to connect remove line to update from unmanaged board
   */
  Consumer<List<Integer>> removeLine = (List<Integer> line) ->
  {
    otherPlayer.removeLines(line);
    otherPlayer.setNbLine(line.size());
  };

  /**
   * Lambda to execute when locking other player.
   */
  Consumer<TetriminoInterface> lockTetrimino = (TetriminoInterface tetriminoInterface) ->
      otherPlayer.placeTetrimino(tetriminoInterface);


  /**
   * Send which line has been destroyed by the managed game to the server
   */
  Consumer<List<Integer>> lineDestroyed = (List<Integer> lineDestroyed) -> {
    if (client != null) {
      client.removeLine(lineDestroyed);
    }
  };

  Runnable iLost = () ->
  {
    if (client != null) {
      this.client.notifyLoss();
    }
  };

  /**
   * Lambda expression to connect add tetrimino to update from unmanaged board. Behaviour for when a
   * player send you his placed pawn.
   */
  Consumer<TetriminoInterface> addTetrimino = (TetriminoInterface tetriminoInterface) ->
      otherPlayer.setActualTetrimino(tetriminoInterface);

  public void closeConnection() {
    if (client != null) {
      this.client.closeConnectionToServer();
    }
  }

  /**
   * Send a pawn to another player.
   */
  Consumer<TetriminoInterface> addTetriminoToOtherPlayer = (TetriminoInterface tetriminoInterface) ->
  {
    if (client != null) {
      this.client.sendTetriminoToOtherPlayer(tetriminoInterface);
    }
  };

  Consumer<Mino> setHold = (Mino m) ->
  {
    if (client != null) {
      this.client.sendHoldMino(m);
    }
  };

  /**
   * Lambda expression to connect send score to update from unmanaged board
   */
  Consumer<Integer> sendScore = (Integer score) ->
  {
    if (client != null) {
      otherPlayer.setScore(score);
    }
  };


  /**
   * Lambda expression to connect hold with client
   */
  Consumer<Mino> hold = (Mino mino) ->
      otherPlayer.setHold(mino);

  /**
   * Updates next mino of other player.
   */
  Consumer<Mino> updateNextTetriminoOtherPlayer = (Mino m) ->
      otherPlayer.setNextTetrimino(Tetrimino.createTetrimino(m));

  /**
   * Sends a tetrimino to lock to server.
   */
  Consumer<TetriminoInterface> lockMyTetrimino = (TetriminoInterface m) -> {
    client.lockTetrimino(m);
  };

  /**
   * Lambda expression to connect game state from server to model.
   */
  Runnable playerReady = this::start;

  /**
   * Lambda expression to connect game state from server to model. Runs if other player has lost
   */
  Runnable otherPlayerLost = () -> {
    this.otherPlayer.playerStatus("LOCK OUT", 0.9);
  };

  /**
   * Sends the score to the server to be updated in other board
   */
  Consumer<Integer> sendScoreServer = (Integer score) -> {
    if (client != null) {
      this.client.sendScore(score);
    }
  };

  Runnable playerLost = () -> this.client.notifyLoss();

  /**
   * Lambda expression to connect game state from server to model. Runs if other player has been
   * disconnected
   */
  Runnable playerDisconnected = () ->
      this.otherPlayer.playerStatus("Disconnected", 1);

  /**
   * Instantiates a new client with port and host to connect to. Connects all lambda methods in
   * client.
   *
   * @param port Port client must connect to.
   * @param host Hostname of server.
   */
  public void connect(int port, String host) throws ConnectException {
    this.client = new Client(port, host);
    connectLambdaClient();
    this.client.connect();
  }

  /**
   * All necessary lambda connection with client
   */
  private void connectLambdaClient() {
    client.connectNewMinoFromServer(this.newMinoFromServer);
    client.connectRemoveLine(this.removeLine);
    client.connectAddTetrimino(this.addTetrimino);
    client.connectSendScore(this.sendScore);
    client.connectUpdateNextTetriminoOtherPlayer(this.updateNextTetriminoOtherPlayer);
    client.connectPlayerReady(this.playerReady);
    client.connectOtherPlayerLost(this.otherPlayerLost);
    client.connectPlayerDisconnected(this.playerDisconnected);
    client.connectReceiveUserName(this.receiveName);
    client.connectHold(this.hold);
    client.connectlockTetrimino(this.lockTetrimino);
  }

  /**
   * Getter for client interface
   *
   * @return The current client.
   */
  public ClientInterface getClient() {
    return client;
  }

  /**
   * Starts a new game.
   */
  public void start() {
    askNextMino.run();
    this.player.start();
    this.otherPlayer.playerStatus("", 0);
    if (client != null) {
      client.sendNameToServer(player.getUsername());
    }
  }

  /**
   * Initializes a managed game.
   *
   * @param username Username of player.
   */
  public void initManagedBoard(String username) {
    player = new ManagedGame(username);
    otherPlayer = new UnmanagedGame();
    connectLambdaPlayer(this.player);
  }

  /**
   * All necessary connection with player
   *
   * @param player Player to connect lambdas too
   */
  public void connectLambdaPlayer(ManagedGame player) {
    player.connectAskNewMino(askNextMino);
    player.connectAddTetrimino(addTetriminoToOtherPlayer);
    player.connectHoldMino(this.setHold);
    player.connectLineDestroyed(this.lineDestroyed);
    player.connectSendScoreServer(sendScoreServer);
    player.connectLost(this.iLost);
    player.connectTetriminoLock(this.lockMyTetrimino);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener[] listener) {
    this.player.addPropertyChangeListener(listener[0]);
    this.otherPlayer.addPropertyChangeListener(listener[1]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    this.player.removePropertyChangeListener(listener);
    this.otherPlayer.removePropertyChangeListener(listener);
  }

  /**
   * Moves a player mino
   *
   * @param direction Direction to move mino in.
   */
  public void move(Direction direction) {
    this.player.move(direction);
  }

  /**
   * Holds a player's mino.
   */
  public void hold() {
    this.player.hold();
  }

  /**
   * Hard drops a player's mino
   */
  public void hardDrop() {
    this.player.hardDrop();
  }

  /**
   * Soft drops a players mino.
   */
  public void softDrop() {
    this.player.softDrop();
  }

  /**
   * Rotates a players mino
   *
   * @param clockwise Direction of rotation
   */
  public void rotate(boolean clockwise) {
    player.rotate(clockwise);
  }

  /**
   * Gets status of the game
   *
   * @return Current status of the game.
   */
  public GameStatus getStatus() {
    return this.player.getStatus();
  }
}
