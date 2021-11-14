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


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractServer implements Runnable {

  /**
   * Port on which server will listen to.
   */
  private final int port;
  /**
   * List of all client threads
   */
  private final List<Thread> threads;
  /**
   * Socket of server
   */
  private ServerSocket serverSocket = null;
  /**
   * Thread that will await for client to connect.
   */
  private Thread connectionThread;
  /**
   * True if server is active.
   */
  private boolean isActive = false;
  /**
   * Server timeout for accepting connexions. Defaults to half a second.
   */
  private final int timeout = 500;
  /**
   * Maximum number of clients that can be in the queue at the same time. Default to 10 clients.
   */
  private final int backlog = 10;

  /**
   * Constructor for Abstract server
   *
   * @param port Port on which to listen to.
   */
  public AbstractServer(int port) {
    this.port = port;
    threads = new ArrayList<Thread>();
  }

  /**
   * Stops the server from running.
   */
  public void stopServer() throws IOException {
    if (serverSocket == null) {
      return;
    }
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      synchronized (this) {
        for (Thread clientSockets : threads) {
          ((CustomClientThread) clientSockets).close();
        }
      }
    }
    serverStopped();
  }

  /**
   * Hook for when there is an exception in the server. Defaults to nothing, needs to be
   * overridden.
   *
   * @param e Exception e.
   */
  protected void exceptionHook(Exception e) {
    System.err.println(e.getMessage());
  }

  /**
   * Hook for when server has stopped. Defaults to nothing, needs to be overridden.
   */
  protected void serverStopped() {
  }

  /**
   * Hook for when message from client is received. Defaults to nothing, needs to be overridden.
   *
   * @param msg    Object that has been received.
   * @param client Client that has sent the message.
   */
  protected void handleMessageClient(Object msg, CustomClientThread client) {
  }

  /**
   * Hook for when a client has connected.
   *
   * @param client Client that has successfully connected to the server.
   */
  protected void clientConnected(CustomClientThread client) {
  }

  /**
   * Hook for when a client has disconnected from the server.
   *
   * @param client Client that has successfully disconnected form the server.
   */
  synchronized protected void clientDisconnected(CustomClientThread client) {
    threads.remove(client);
    System.out.println("Client " + client.getIdOfClient() + " has disconnected");
  }

  /**
   * Establishes server socket, create new listener thread and turns isActive to true.
   */
  public void startServer() {
    try {
      if (!isListening() && serverSocket == null) {
        this.serverSocket = new ServerSocket(this.port, this.backlog);
      }
      serverSocket.setSoTimeout(timeout);
    } catch (IOException e) {
      exceptionHook(e);
    } finally {
      connectionThread = new Thread(this);
      isActive = true;
      connectionThread.start();
    }
  }

  /**
   * Asked by client if his tetriminos list if empty. This method is synchronized to ensure that
   * whatever effects it has does not conflict with work being done by another thread.
   */
  public synchronized void refill() {
    refillBag();
  }

  /**
   * Hook function when bag needs to be refilled. Need to be overridden for specific behaviour.
   */
  void refillBag() {
  }

  /**
   * Checks if the connection thread is declared and alive.
   *
   * @return True if connection thread is listening.
   */
  public boolean isListening() {
    return (connectionThread != null && connectionThread.isAlive());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    try {
      while (isActive) {
        try {
          Socket clientSocket = serverSocket.accept();
          synchronized (this) {
            if (isActive) {
              CustomClientThread customClientThread = new CustomClientThread(clientSocket, this,
                  threads.size());
              this.threads.add(customClientThread);
            }
          }
        } catch (IOException e) {
          //Other client is disconnected //TODO
        }
      }
    } finally {
      this.isActive = false;
      serverStopped();
    }
  }

  /**
   * Receives a message sent form the client to the server. This method is synchronized to ensure
   * that whatever effects it has does not conflict with work being done by another thread.
   *
   * @param msg    messageTypes.Message received form client.
   * @param client Author of the message.
   */
  final synchronized void receiveMessageFromClient(Object msg, CustomClientThread client) {
    this.handleMessageClient(msg, client);
  }
}