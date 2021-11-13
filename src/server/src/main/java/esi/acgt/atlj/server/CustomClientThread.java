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
import esi.acgt.atlj.message.PlayerStatus;
import esi.acgt.atlj.model.tetrimino.Mino;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomClientThread extends Thread {

  /**
   * State of current player.
   */
  private PlayerStatus clientStatus;
  /**
   * List of next tetriminos of player.
   */
  private BlockingQueue<Mino> myTetriminos;
  /**
   * Socket of client.
   */
  private final Socket clientSocket;
  /**
   * Server that client is connected to.
   */
  private final AbstractServer server;
  /**
   * Input stream of socket
   */
  private ObjectInputStream input;
  /**
   * Output stream of socket
   */
  private ObjectOutputStream output;
  /**
   * Status of client connexion.
   */
  private boolean isActive;


  /**
   * Constructor for custom client thread.
   *
   * @param clientSocket Socket of client.
   * @param server       Server to which the client is connected.
   * @throws SocketException Thrown if function cannot instantiate input or output streams.
   */
  public CustomClientThread(Socket clientSocket, AbstractServer server) throws SocketException {
    clientStatus = PlayerStatus.WAITING;
    this.clientSocket = clientSocket;
    this.myTetriminos = new LinkedBlockingQueue<>();
    clientSocket.setSoTimeout(0);
    this.server = server;
    try {
      input = new ObjectInputStream(clientSocket.getInputStream());
      output = new ObjectOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      System.out.println("Cannot create input or output stream");
      try {
        closeSocket();
      } catch (IOException ioException) {
        System.out.println("Cannot close the socket");
      }
    }
    isActive = true;
    start();
  }

  /**
   * Adds a tetrimino to the players list.
   *
   * @param tetrimino Tetrimino to add.
   */
  public void addTetrimino(Mino tetrimino) {
    myTetriminos.add(tetrimino);
  }

  /**
   * Gets the state of the player.
   *
   * @return Player state.
   */
  public PlayerStatus getClientStatus() {
    return clientStatus;
  }

  /**
   * Sets the state of the player.
   *
   * @param clientStatus Status to set the player to.
   */
  public void setClientStatus(PlayerStatus clientStatus) {
    this.clientStatus = clientStatus;
  }

  /**
   * Pop front of players list of tetriminos;
   *
   * @return First element of list myTetriminos.
   */
  public Mino getTetrimino() {
    Mino tetriminoToSend = null;
    if (myTetriminos.isEmpty()) {
      server.refill();
    }
    try {
      tetriminoToSend = myTetriminos.take();
    } catch (InterruptedException e) {
      System.out.println("Sorry have to wait to long for new piece");
    }
    return tetriminoToSend;
  }

  /**
   * Closes socket and all streams.
   *
   * @throws IOException Thrown if function cannot close socket or streams.
   */
  private void closeSocket() throws IOException {
    if (clientSocket != null) {
      clientSocket.close();
    }
    if (output != null) {
      output.close();
    }
    if (input != null) {
      input.close();
    }
  }

  @Override
  public void run() {
    server.clientConnected(this);
    try {
      Object obj;
      while (isActive) {
        try {
          obj = input.readObject();
          if (isActive) {
            server.receiveMessageFromClient(obj, this);
          }
        } catch (ClassNotFoundException | RuntimeException ex) {
        }
      }
    } catch (Exception e) {
      if (isActive) {
        try {
          close();
        } catch (Exception ex) {
          System.out.println("Cannot close socket");
        }
      }
    }
  }

  /**
   * Sends a message to the client.
   */
  public void sendMessage(Object information) {
    if (clientSocket == null || output == null) {
      System.out.println("Sorry stream does not exist");
    }
    try {
      if (information instanceof Message) {
        output.writeObject((Message) information);
        output.flush();
      }
    } catch (IOException e) {
      System.out.println("Could not send message" + information.toString());
    }
  }

  /**
   * Closes the client socket.
   */
  final public void close() throws IOException {
    isActive = false;
    closeSocket();
  }

}