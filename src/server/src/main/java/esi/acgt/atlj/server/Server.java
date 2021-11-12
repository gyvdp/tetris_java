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


import esi.acgt.atlj.message.Message;
import esi.acgt.atlj.message.messageTypes.*;
import esi.acgt.atlj.model.tetrimino.ITetrimino;
import esi.acgt.atlj.model.tetrimino.JTetrimino;
import esi.acgt.atlj.model.tetrimino.LTetrimino;
import esi.acgt.atlj.model.tetrimino.OTetrimino;
import esi.acgt.atlj.model.tetrimino.STetrimino;
import esi.acgt.atlj.model.tetrimino.TTetrimino;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.ZTetrimino;
import java.util.HashMap;
import java.util.Random;

public class Server extends AbstractServer {

  private final Tetrimino[] firstBag;
  private final HashMap<Integer, CustomClientThread> members;
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
    switch (msg.toString()) {
      case ("askPiece"):
        client.sendMessage(new SendPiece(client.getTetrimino()));
        break;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  void refillBag() {
    for (int numberOfPlayer = 0; numberOfPlayer < members.size(); numberOfPlayer++) {
      Tetrimino[] newBag = regenBag();
      for (Tetrimino tetrimino : newBag) {
        members.get(numberOfPlayer).addTetrimino(tetrimino);
      }
    }
  }

  /**
   * Regenerates a new bag of seven tetriminodes that has been shuffled.
   *
   * @return Array of shuffled tetriminodes.
   */
  private Tetrimino[] regenBag() {
    Tetrimino[] bag = new Tetrimino[]{
        new ITetrimino(), new ZTetrimino(), new STetrimino(), new TTetrimino(), new OTetrimino(),
        new JTetrimino(), new LTetrimino()
    };
    shuffle(bag);
    return bag;
  }

  /**
   * Shuffles an array with the Fisher-Yates algorithm.
   *
   * @param array Shuffled array.
   */
  private static void shuffle(Tetrimino[] array) {
    int n = array.length;
    Random random = new Random();
    for (int i = 0; i < array.length; i++) {
      int randomValue = i + random.nextInt(n - i);
      Tetrimino randomElement = array[randomValue];
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
    for (Tetrimino t : firstBag) {
      client.addTetrimino(t);
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
   * Sends a message to a specific client
   *
   * @param information messageTypes.Message to send to client.
   * @param clientId    Unique id of client.
   */
  void sendToClient(Object information, int clientId) {
    if (information instanceof Message) {
      members.get(clientId).sendMessage((Message) information);
    }
  }

}