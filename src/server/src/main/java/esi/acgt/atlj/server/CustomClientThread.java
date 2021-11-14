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
import esi.acgt.atlj.message.messageTypes.PlayerState;
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

  private final int id;


  /**
   * Constructor for custom client thread.
   *
   * @param clientSocket Socket of client.
   * @param server       Server to which the client is connected.
   * @throws SocketException Thrown if function cannot instantiate input or output streams.
   */
  public CustomClientThread(Socket clientSocket, AbstractServer server, int id)
      throws SocketException {
    this.clientStatus = PlayerStatus.WAITING;
    this.id = id;
    this.clientSocket = clientSocket;
    this.myTetriminos = new LinkedBlockingQueue<>();
    this.clientSocket.setSoTimeout(0);
    this.server = server;
    try {
      this.input = new ObjectInputStream(clientSocket.getInputStream());
      this.output = new ObjectOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      System.err.println("Cannot create input or output stream");
      try {
        closeSocket();
      } catch (IOException ioException) {
        System.err.println("Cannot close the socket");
      }
    }
    this.isActive = true;
    start();
  }

  /**
   * Getter for the client id.
   *
   * @return The id of the client.
   */
  public int getIdOfClient() {
    return this.id;
  }

  /**
   * Adds a tetrimino to the players list.
   *
   * @param tetrimino Tetrimino to add.
   */
  public void addTetrimino(Mino tetrimino) {
    this.myTetriminos.add(tetrimino);
  }

  /**
   * Gets the state of the player.
   *
   * @return Player state.
   */
  public PlayerStatus getClientStatus() {
    return this.clientStatus;
  }

  /**
   * Sets the state of the player.
   *
   * @param clientStatus Status to set the player to.
   */
  public void setClientStatus(PlayerStatus clientStatus) {
    this.clientStatus = clientStatus;
    sendMessage(new PlayerState(clientStatus));
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
      System.err.println("Piece took to long cannot send");
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
          System.err.println("Cannot close socket");
        }
      }
    } finally {
      server.clientDisconnected(this);
    }
  }

  /**
   * Sends a message to the client.
   */
  public void sendMessage(Object information) {
    if (clientSocket == null || output == null) {
      System.err.println("Sorry stream does not exist");
    }
    try {
      if (information instanceof Message) {
        output.writeObject(information);
        output.flush();
      }
    } catch (IOException e) {
      if (((Message) information).messageType != null) {
        System.err.println("Could not send message" + information);
      }
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