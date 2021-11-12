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

package esi.acgt.atlj.server;

import esi.acgt.atlj.message.messageTypes.*;
import esi.acgt.atlj.model.tetrimino.ITetrimino;
import esi.acgt.atlj.model.tetrimino.Mino;
import java.util.HashMap;
import java.util.Random;

public class Server extends AbstractServer {

  /**
   * First random bag of tetriminos of game.
   */
  private final Mino[] firstBag;
  /**
   * Hash map of all members in function of their clientId.
   */
  private final HashMap<Integer, CustomClientThread> members;
  /**
   * current taly of client id.
   */
  private int clientId;

  /**
   * Constructor for a server.
   *
   * @param port Port for server to listen on.
   */
  public Server(int port) {
    super(port);
    firstBag = regenBag();
    clientId = 0;
    members = new HashMap<Integer, CustomClientThread>();
    this.startServer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void exceptionHook(Exception e) {
    super.exceptionHook(e);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleMessageClient(Object msg, CustomClientThread client) {
    if (client.getClientStatus().equals(PlayerStatus.READY)) { //If player is not ready discard msg
      CustomClientThread opPlayer = members.get(0).equals(client) ? members.get(1) : members.get(0);
      switch (msg.toString().toUpperCase()) {
        case "ASKPIECE":
          client.sendMessage(new SendPiece(client.getTetrimino()));
          break;
        case "ADDTETRIMINO":
          opPlayer.sendMessage(new AddTetrimino(
              new ITetrimino())); //TODO  Add correct tetrimino coming from other client
          break;
        case "REMOVELINE":
          opPlayer.sendMessage(new RemoveLine(4)); //TODO Add correct line coming from other player
          break;
        case "SENDSCORE":
          opPlayer.sendMessage(new SendScore(5)); // TODO add correct score coming from other player
          break;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  void refillBag() {
    for (int numberOfPlayer = 0; numberOfPlayer < members.size(); numberOfPlayer++) {
      Mino[] newBag = regenBag();
      for (Mino tetrimino : newBag) {
        members.get(numberOfPlayer).addTetrimino(tetrimino);
      }
    }
  }

  /**
   * Regenerates a new bag of seven tetriminodes that has been shuffled.
   *
   * @return Array of shuffled tetriminodes.
   */
  private Mino[] regenBag() {
    Mino[] bag = new Mino[]{
        Mino.S_MINO, Mino.Z_MINO, Mino.O_MINO, Mino.J_MINO, Mino.T_MINO,
        Mino.I_MINO, Mino.L_MINO
    };
    shuffle(bag);
    return bag;
  }

  /**
   * Shuffles an array with the Fisher-Yates algorithm.
   *
   * @param array Shuffled array.
   */
  private static void shuffle(Mino[] array) {
    int n = array.length;
    Random random = new Random();
    for (int i = 0; i < array.length; i++) {
      int randomValue = i + random.nextInt(n - i);
      Mino randomElement = array[randomValue];
      array[randomValue] = array[i];
      array[i] = randomElement;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void serverStopped() {
    super.serverStopped();
  }

  /**
   * Gets the next id.
   *
   * @return Next available id.
   */
  private int getNextId() {
    return this.clientId++;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void clientConnected(CustomClientThread client) {
    super.clientConnected(client);
    members.put(getNextId(), client);
    for (Mino t : firstBag) {
      client.addTetrimino(t);
    }
    if (members.size() == 2) {
      updateAllPlayerState(PlayerStatus.READY);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void clientDisconnected(CustomClientThread client) {
    super.clientDisconnected(client);
    System.out.println(client + "has disconnected");
  }

  /**
   * Updates the player state for every player
   *
   * @param playerState PlayerState to update to.
   */
  public void updateAllPlayerState(PlayerStatus playerState) {
    for (int numberOfPlayer = 0; numberOfPlayer < members.size(); numberOfPlayer++) {
      members.get(numberOfPlayer).setClientStatus(playerState);
    }
  }
}