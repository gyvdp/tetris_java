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

package esi.acgt.atlj.client.connexionServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;


/**
 * Contains all necessary methods to set up a client for a client-server architecture. When a client
 * is connected to the server he can exchange Object instances.
 */
public abstract class AbstractClient implements Runnable {

  /**
   * Host name of server wishing to connect to.
   */
  private final String host;
  /**
   * Port number of server wishing to connect to.
   */
  private final int port;
  /**
   * Socket that will establish a connection with server
   */
  private Socket clientSocket;
  /**
   * Output stream from clientSocket.
   */
  private ObjectOutputStream output;
  /**
   * Input stream from clientSocket.
   */
  private ObjectInputStream input;
  /**
   * Thread of client.
   */
  private Thread clientThread;
  /**
   * Indicator of active connection
   */
  private boolean isActive;

  /**
   * Constructor for client.
   *
   * @param port Port number.
   * @param host Host name of server.
   */
  public AbstractClient(int port, String host) {
    this.isActive = false;
    this.host = host;
    this.port = port;
  }

  /**
   * Checks if still connected by means of checking if dedicated client data reading thread is non-
   * null and alive.
   *
   * @return True if connection is still active.
   */
  public boolean isConnected() {
    return clientThread != null;
  }

  /**
   * Sends information to the server.
   *
   * @param information Object to send to server.
   */
  protected void sendToServer(Object information) throws IOException {
    if (clientSocket == null) {
      throw new SocketException("Socket is null");
    }
    output.writeObject(information);
    output.flush();
  }

  /**
   * Closes the connection to server which also closes input and output stream.
   */
  public void closeConnectionToServer() {
    isActive = false;
    try {
      if (clientSocket != null) {
        clientSocket.close();
        output.close();
        input.close();
        closeConnection();
      }
    } catch (IOException e) {
      System.out.println("Sorry the connection cannot close");
      connexionException(e);
    }
  }

  /**
   * Handles object coming from server.
   *
   * @param information Object sent by server.
   */
  protected abstract void handleServerMessage(Object information);


  /**
   * Establishes a connection to the server.
   */
  public void connectToServer() throws ConnectException {
    if (isConnected()) {
      return;
    }
    try {
      clientSocket = new Socket(this.host, this.port);
      output = new ObjectOutputStream(clientSocket.getOutputStream());
      input = new ObjectInputStream(clientSocket.getInputStream());
      clientThread = new Thread(this);
      this.isActive = true;
      clientThread.start();
    } catch (IOException e) {
      closeConnectionToServer();
      connexionException(e);
      throw new ConnectException("Sorry could not find " + host + " at " + port);
    }
  }

  /**
   * Hook function when connection is established. Need to be overridden for specific behaviour.
   */
  protected void connectionEstablished() {
  }

  /**
   * Hook function when connection to server had ended. Need to be overridden for specific
   * behaviour.
   */
  protected void closeConnection() {
  }


  /**
   * Hook function when connection to server throws exception. Need to be overridden for specific
   * behaviour.
   *
   * @param e Exception that has been raised.
   */
  protected void connexionException(Exception e) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    connectionEstablished();
    Object information;
    try {
      while (isActive) {
        try {
          information = input.readObject();
          if (isActive) {
            handleServerMessage(information);
          }
        } catch (RuntimeException | ClassNotFoundException | SocketException ignored) {
        }
      }
    } catch (Exception e) {
      if (isActive) {
        System.out.println("j");
        closeConnectionToServer();
      }
      connexionException(e);
    }
  }

  /**
   * Getter for hostname of server wishing to connect to.
   *
   * @return Host name of server.
   */
  public String getHost() {
    return host;
  }

  /**
   * Getter of port of that server is listening on.
   *
   * @return Port name of server
   */
  public int getPort() {
    return port;
  }
}