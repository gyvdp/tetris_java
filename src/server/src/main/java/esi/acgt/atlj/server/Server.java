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


import esi.acgt.atlj.model.Message;
import esi.acgt.atlj.model.tetrimino.ITetrimino;
import esi.acgt.atlj.model.tetrimino.JTetrimino;
import esi.acgt.atlj.model.tetrimino.LTetrimino;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.OTetrimino;
import esi.acgt.atlj.model.tetrimino.STetrimino;
import esi.acgt.atlj.model.tetrimino.TTetrimino;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.ZTetrimino;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


public class Server extends AbstractServer {

  private final HashMap<Integer, CustomClientThread> members;
  private int clientId;

  public Server(int port) {
    super(port);
    clientId = 0;
    members = new HashMap<Integer, CustomClientThread>();
    this.startServer();
  }

  @Override
  protected void exceptionHook(Exception e) {
    super.exceptionHook(e);
  }

  @Override
  protected void handleMessageClient(Object msg, CustomClientThread client) {
    if (msg.equals("askPiece")) {
      Tetrimino tetrimino = client.getTetrimino();
      client.sendMessage(tetrimino);
    } else if (msg.equals("")) {
      System.out.println("TODO");
    }
  }

  @Override
  void refillBag() {
    for (int numberOfPlayer = 0; numberOfPlayer < members.size(); numberOfPlayer++) {
      Tetrimino[] newBag = regenBag();
      for (Tetrimino tetrimino : newBag) {
        members.get(numberOfPlayer).addTetrimino(tetrimino);
      }
    }
  }

  private Tetrimino[] regenBag() {
    Tetrimino[] bag = new Tetrimino[]{
        new ITetrimino(), new ZTetrimino(), new STetrimino(), new TTetrimino(), new OTetrimino(),
        new JTetrimino(), new LTetrimino()
    };
    shuffle(bag);
    return bag;
  }

  private static void shuffle(Tetrimino[] array) {
    int n = array.length;
    Random random = new Random();
    // Loop over array.
    for (int i = 0; i < array.length; i++) {
      // Get a random index of the array past the current index.
      // ... The argument is an exclusive bound.
      //     It will not go past the array send.
      int randomValue = i + random.nextInt(n - i);
      // Swap the random element with the present element.
      Tetrimino randomElement = array[randomValue];
      array[randomValue] = array[i];
      array[i] = randomElement;
    }
  }

  @Override
  protected void serverStopped() {
    super.serverStopped();
  }

  private int getNextId() {
    return this.clientId++;
  }

  @Override
  protected void clientConnected(CustomClientThread client) {
    super.clientConnected(client);
    members.put(getNextId(), client);
    System.out.println(members.size());
  }

  @Override
  protected void clientDiconnected(CustomClientThread client) {
    super.clientDiconnected(client);
  }

  /**
   * Sends a message to a specific client
   *
   * @param information Message to send to client.
   * @param clientId    Unique id of client.
   */
  void sentToClient(Object information, int clientId) {
    members.get(0).sendMessage("i");
  }

}