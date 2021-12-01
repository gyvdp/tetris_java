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

import esi.acgt.atlj.database.dto.User;
import esi.acgt.atlj.database.exceptions.DtoException;
import esi.acgt.atlj.message.Message;
import esi.acgt.atlj.message.PlayerAction;
import esi.acgt.atlj.message.PlayerStatus;
import esi.acgt.atlj.message.messageTypes.PlayerState;
import esi.acgt.atlj.message.messageTypes.SendAction;
import esi.acgt.atlj.message.messageTypes.SendName;
import esi.acgt.atlj.model.tetrimino.Mino;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Custom thread for each client of the server.
 */
public class CustomClientThread extends Thread {

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
   * State of current player.
   */
  private PlayerStatus clientStatus;
  /**
   * User object of player current player for database.
   */
  private User user;
  /**
   * Handle message from client.
   */
  private BiConsumer<Message, CustomClientThread> handleMessage;
  /**
   * Sends disconnect message to other player.
   */
  private Consumer<CustomClientThread> disconnect;
  /**
   * Runs when need to refill bag
   */
  private Runnable refillBag;
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

  Consumer<CustomClientThread> updateDb;


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
    this.id = id;
    this.server = server;
    this.clientStatus = PlayerStatus.WAITING;
    myTetriminos = new LinkedBlockingQueue<>();
    try {
      clientSocket.setSoTimeout(0);
    } catch (SocketException e) {
      System.err.println("cannot set timeout to client");
    }
    try {
      input = new ObjectInputStream(clientSocket.getInputStream());
      output = new ObjectOutputStream(clientSocket.getOutputStream());
    } catch (IOException ex) {
      try {
        closeAll();
      } catch (Exception ignored) {
      }
      throw ex;
    }
    readyToStop = false;
    this.start();
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
      refillBag.run();
    }
    try {
      return this.myTetriminos.take();
    } catch (InterruptedException e) {
      System.err.println("Cannot give tetrimino");
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
   * Gets the username of the client.
   *
   * @return Username of the client.
   */
  public String getUsername() {
    return this.user.getUsername();
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
    if (message instanceof SendName s) {
      try {
        user = new User(s.getUsername());
        server.checkUser(user);
        return false;
      } catch (DtoException e) {
        System.err.println("Need valid username to start a game");
      }
    }
    if (message instanceof SendAction e) {
      if (e.getAction() == PlayerAction.PLAY_ONLINE) {
        server.addPlayer(this);
      }
      if (e.getAction() == PlayerAction.PLAY_SOLO) {
        server.addSoloPlayer(this);
      }
      return false;
    }
    return true;
  }

  /**
   * Sends an object to the client. This method can be overriden, but if so it should still perform
   * the general function of sending to client, by calling the <code>super.sendToClient()</code>
   * method perhaps after some kind of filtering is done.
   *
   * @param msg the message to be sent.
   */
  public synchronized void sendMessage(Object msg) {
    if (clientSocket == null || output == null) {
      try {
        closeAll();
      } catch (IOException ignored) {
      }
    } else {
      try {
        output.reset();
        output.writeObject(msg);
      } catch (IOException e) {
        System.err.println("Error sending " + msg.toString() + " to client");
      } catch (NullPointerException ignored) {
      }
    }
  }

  /**
   * Getter for user token of client.
   *
   * @return User token of client.
   */
  public User getUser() {
    return this.user;
  }

  /**
   * Connects disconnect lambda.
   *
   * @param disconnect Lambda to connect with.
   */
  public synchronized void connectDisconnect(Consumer<CustomClientThread> disconnect) {
    this.disconnect = disconnect;
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
   * Sets the status of the clients and sends it to all clients.
   *
   * @param cs Status of the client to set and send.
   */
  public void setClientStatus(PlayerStatus cs) {
    this.clientStatus = cs;
    this.sendMessage(new PlayerState(cs));
  }

  /**
   * Return true if the client is connected.
   *
   * @return true if the client is connected.
   */
  final public synchronized boolean isConnected() {
    return clientSocket != null && output != null;
  }

  /**
   * Connects lambda to refill bag from match-up generator.
   *
   * @param refillBag RefillBag lambda to connect.
   */
  public synchronized void connectRefillBag(Runnable refillBag) {
    this.refillBag = refillBag;
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
            if (msg instanceof Message m) {
              handleMessage.accept(m, this);
            }
          }
        } catch (ClassNotFoundException | RuntimeException ex) {
          server.clientException(this, ex);
        }
      }
    } catch (Exception exception) {
      if (!readyToStop) {
        try {
          System.out.println(
              "Client " + this.getIdOfClient() + " has disconnected with" + this.getInetAddress());
          closeAll();
        } catch (Exception ex) {
        }
        server.clientException(this, exception);
      }
    } finally {
      if (this.disconnect != null) {
        updateDb.accept(this);
        disconnect.accept(this);
      }
    }
  }


  /**
   * Connect handle message to match-up generator.
   *
   * @param handleMessage Lambda function to connect.
   */
  public void connectHandleMessage(BiConsumer<Message, CustomClientThread> handleMessage) {
    this.handleMessage = handleMessage;
  }

  public void connectUpdateDb(Consumer<CustomClientThread> updateDb) {
    this.updateDb = updateDb;
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