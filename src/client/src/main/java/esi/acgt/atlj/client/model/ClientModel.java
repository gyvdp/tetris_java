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
import esi.acgt.atlj.client.model.utils.MessagesFromServerHandler;
import esi.acgt.atlj.client.model.utils.MessagesToServerHandler;
import esi.acgt.atlj.message.PlayerAction;
import esi.acgt.atlj.model.Model;
import esi.acgt.atlj.model.game.Direction;
import esi.acgt.atlj.model.game.GameStatus;
import esi.acgt.atlj.model.game.ManagedGame;
import esi.acgt.atlj.model.game.UnmanagedGame;
import java.beans.PropertyChangeListener;
import java.net.ConnectException;

public class ClientModel extends Model {

  private ManagedGame player;
  private UnmanagedGame otherPlayer;
  private ClientInterface client;

  public ClientModel() {
    super();
  }

  /**
   * Sends action to server.
   *
   * @param a Action to send to server.
   */
  public void sendAction(PlayerAction a) {
    this.client.sendAction(a);
  }

  /**
   * Instantiates a new client with port and host to connect to. Connects all lambda methods in
   * client.
   *
   * @param port Port client must connect to.
   * @param host Hostname of server.
   */
  public void connect(int port, String host) throws ConnectException {
    this.client = new Client(port, host);
    this.client.connect();
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
    this.player.start();
    this.otherPlayer.playerStatus("", 0);
  }


  /**
   * Initializes a managed game.
   *
   * @param username Username of player.
   */
  public void initManagedBoard(String username) {
    player = new ManagedGame(username);
    otherPlayer = new UnmanagedGame();
    if (client != null) {
      client.sendNameToServer(player.getUsername());
    }
    new MessagesToServerHandler(player, client);
    new MessagesFromServerHandler(otherPlayer, player, client, players);
    players.add(player);
    players.add(otherPlayer);
  }

  /**
   * Closes connection to server.
   */
  public void closeConnection() {
    if (client != null) {
      this.client.closeConnectionToServer();
    }
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
   * Soft drops a player's mino.
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
