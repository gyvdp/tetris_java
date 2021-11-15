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

import esi.acgt.atlj.message.PlayerStatus;
import esi.acgt.atlj.message.messageTypes.PlayerState;
import esi.acgt.atlj.model.tetrimino.Mino;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Custom thread for each client of the server.
 */
public class CustomClientThread extends Thread {


  /**
   * State of current player.
   */
  private PlayerStatus clientStatus;
  /**
   * List of next tetriminos of player.
   */
  private final BlockingQueue<Mino> myTetriminos;

  /**
   * Unique id of client.
   */
  private final int id;
  /**
   * A reference to the Server that created this instance.
   */
  private final AbstractServer server;

  /**
   * Sockets are used in the operating system as channels of communication between two processes.
   *
   * @see java.net.Socket
   */
  private Socket clientSocket;

  /**
   * Stream used to read from the client.
   */
  private ObjectInputStream input;

  /**
   * Stream used to write to the client.
   */
  private ObjectOutputStream output;

  /**
   * Indicates if the thread is ready to stop. Set to true when closing of the connection is
   * initiated.
   */
  private boolean readyToStop;

  /**
   * Constructs a new connection to a client.
   *
   * @param clientSocket contains the client's socket.
   * @param server       a reference to the server that created this instance
   * @throws IOException if an I/O error occur when creating the connection.
   */
  protected CustomClientThread(Socket clientSocket, AbstractServer server, int id)
      throws IOException {
    this.clientSocket = clientSocket;
    this.server = server;
    this.id = id;
    this.clientStatus = PlayerStatus.WAITING;
    myTetriminos = new LinkedBlockingQueue<>();
    try {
      clientSocket.setSoTimeout(0);// make sure timeout is infinite
    } catch (SocketException e) {
      System.err.println("cannot set timeout to client");
    }
    try {
      input = new ObjectInputStream(clientSocket.getInputStream());
      output = new ObjectOutputStream(clientSocket.getOutputStream());
    } catch (IOException ex) {
      try {
        closeAll();
      } catch (Exception exc) {
      }
      throw ex;
    }
    readyToStop = false;
    start(); // Start the thread waits for data from the socket
  }

  /**
   * Gets the id of the client.
   *
   * @return Id of client.
   */
  public int getIdOfClient() {
    return this.id;
  }

  /**
   * Sets the status of the clients and sends it to all clients.
   *
   * @param cs Status of the client to set and send.
   */
  public void setClientStatus(PlayerStatus cs) {
    this.clientStatus = cs;
    this.sendMessage(new PlayerState(PlayerStatus.READY));
  }

  /**
   * Closes all connection to the server.
   *
   * @throws IOException if an I/O error occur when closing the connection.
   */
  private void closeAll() throws IOException {
    try {
      if (clientSocket != null) {
        clientSocket.close();
      }
      if (output != null) {
        output.close();
      }
      if (input != null) {
        input.close();
      }
    } finally {
      output = null;
      input = null;
      clientSocket = null;
    }
  }

  /**
   * Adds a mino to the current list of tetriminos of the client.
   *
   * @param e Mino to add to current list of minos.
   */
  public void addMino(Mino e) {
    this.myTetriminos.add(e);
  }

  /**
   * Gets the head of list of client's current mino.
   *
   * @return Head of list unless it throws exception than returns default O.MINO.
   */
  public Mino getMino() {
    if (this.myTetriminos.isEmpty()) {
      server.refillBag();
    }
    try {
      return this.myTetriminos.take();
    } catch (InterruptedException e) {
      System.out.println("Cannot give tetrimino");
    }
    return Mino.O_MINO;
  }

  /**
   * Returns the address of the client.
   *
   * @return the client's Internet address.
   */
  final public InetAddress getInetAddress() {
    return clientSocket == null ? null : clientSocket.getInetAddress();
  }

  /**
   * Closes the client. If the connection is already closed, this call has no effect.
   *
   * @throws IOException if an error occurs when closing the socket.
   */
  final public void close() throws IOException {
    readyToStop = true;
    closeAll();
  }

  /**
   * Hook method called each time a new message is received by this client. If this method return
   * true, then the method
   * <code>handleMessageFromClient()</code> of <code>AbstractServer</code>
   * will also be called after. The default implementation simply returns true.
   *
   * @param message the message sent.
   * @return True if the message needs to be handled by server.
   */
  protected boolean handleMessageFromClient(Object message) {
    //TODO when server has a model
    return true;
  }

  /**
   * Sends an object to the client. This method can be overriden, but if so it should still perform
   * the general function of sending to client, by calling the <code>super.sendToClient()</code>
   * method perhaps after some kind of filtering is done.
   *
   * @param msg the message to be sent.
   * @throws IOException if an I/O error occur when sending the message.
   */
  public void sendMessage(Object msg) {
    if (clientSocket == null || output == null) {
      System.err.println("Client socket does not exist");
    }
    try {
      output.reset();
      output.writeObject(msg);
    } catch (IOException e) {
      System.err.println("Error sending " + msg.toString() + " to client");
    }
  }

  /**
   * Getter for status of client.
   *
   * @return Status of current client.
   */
  public PlayerStatus getClientStatus() {
    return this.clientStatus;
  }

  /**
   * Return true if the client is connected.
   *
   * @return true if the client is connected.
   */
  final public boolean isConnected() {
    return clientSocket != null && output != null;
  }

  /**
   * Constantly reads the client's input stream. Sends all objects that are read to the server. Not
   * to be called.
   */
  @Override
  final public void run() {
    server.clientConnected(this);
    try {
      Object msg;
      while (!readyToStop) {
        try {
          msg = input.readObject();
          if (!readyToStop && handleMessageFromClient(msg)) {
            server.receiveMessageFromClient(msg, this);
          }
        } catch (ClassNotFoundException | RuntimeException ex) {
          server.clientException(this, ex);
        }
      }
    } catch (Exception exception) {
      if (!readyToStop) {
        try {
          closeAll();
        } catch (Exception ex) {
        }
        server.clientException(this, exception);
      }
    } finally {
      server.clientDisconnected(this);
    }
  }

  /**
   * Returns a string representation of the client.
   *
   * @return the client's description.
   */
  @Override
  public String toString() {
    return clientSocket == null ? null
        : clientSocket.getInetAddress().getHostName()
            + " (" + clientSocket.getInetAddress().getHostAddress() + ")";
  }
}