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

import esi.acgt.atlj.database.dto.UserDto;
import esi.acgt.atlj.database.exceptions.DtoException;
import esi.acgt.atlj.message.AbstractMessage;
import esi.acgt.atlj.message.ServerRequest;
import esi.acgt.atlj.message.messageTypes.PlayerState;
import esi.acgt.atlj.message.messageTypes.Request;
import esi.acgt.atlj.message.messageTypes.Connection;
import esi.acgt.atlj.message.messageTypes.SendAllStatistics;
import esi.acgt.atlj.model.player.PlayerStatus;
import esi.acgt.atlj.server.utils.MatchUpHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom thread for each client of the server.
 */
public class CustomClientThread extends Thread {

  /**
   * Unique id of client.
   */
  private final int id;
  /**
   * A reference to the Server that created this instance.
   */
  private final AbstractServer server;

  /**
   * User object of player current player for database.
   */
  private UserDto user;
  /**
   * Handle message from client.
   */
  private BiConsumer<AbstractMessage, CustomClientThread> handleMessage;

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


  private static final java.util.logging.Logger logger = Logger.getLogger(
      CustomClientThread.class.getName());


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
    try {
      clientSocket.setSoTimeout(0);
    } catch (SocketException e) {
      logger.log(Level.INFO,
          ("Cannot set timeout"));
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
    setName("Client connection" + getClientNumber());
    this.start();
  }

  /**
   * Gets the id of the client.
   *
   * @return Id of client.
   */
  public int getClientNumber() {
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
    if (message instanceof Connection) {
      try {
        user = new UserDto(((Connection) message).getUsername());
        server.checkUser(this);
        server.getStatOfPlayer(new SendAllStatistics(), this);
      } catch (DtoException ignored) {
      }
    } else if (message instanceof Request) {
      if (((Request) message).getAction() == ServerRequest.MULTIPLAYER) {
        server.addPlayer(this);
      }
    } else if (message instanceof PlayerState m) {
      if (m.getPlayerStatus() == PlayerStatus.CANCEL) {
        server.quitWaitingList(this);
      }
      if (m.getPlayerStatus() == PlayerStatus.STOPPED) {
        return true;
      }
    } else {
      return true;
    }
    return false;
  }

  /**
   * Sends an object to the client. This method can be overridden, but if so it should still perform
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
        logger.log(Level.INFO,
            String.format("Error sending %s to %s", ((AbstractMessage) msg).getType(),
                getUsername()));
      } catch (NullPointerException ignored) {
      }
    }
  }

  /**
   * Getter for user token of client.
   *
   * @return User token of client.
   */
  public UserDto getUser() {
    return this.user;
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
            if (msg instanceof AbstractMessage m) {
              if (handleMessage != null) {
                handleMessage.accept(m, this);
              }
            }
          }
        } catch (ClassNotFoundException | RuntimeException ex) {
          server.clientException(this, ex);
        }
      }
    } catch (Exception exception) {
      if (!readyToStop) {
        try {
          closeAll();
        } catch (Exception ignored) {
        }
        server.clientException(this, exception);
      }
    } finally {
      server.clientDisconnected(this);
    }
  }


  /**
   * Connect handle message to match-up generator.
   *
   * @param handleMessage Lambda function to connect.
   */
  public void connectHandleMessage(BiConsumer<AbstractMessage, CustomClientThread> handleMessage) {
    this.handleMessage = handleMessage;
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