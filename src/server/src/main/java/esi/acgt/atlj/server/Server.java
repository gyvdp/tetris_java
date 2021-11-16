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
import esi.acgt.atlj.message.messageTypes.*;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.server.model.MatchUpGenerator;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;


public class Server extends AbstractServer {

  /**
   * current number of match-ups;
   */
  private int matchUpId = 0;

  /**
   * Hash map of all members in function of their clientId.
   */
  private final HashMap<Integer, CustomClientThread> members;

  /**
   * All the clients that are currently waiting to play
   */
  private final BlockingQueue<CustomClientThread> waitingList;

  /**
   * All match-ups with their id.
   */
  private final HashMap<Integer, MatchUpGenerator> matchUps;
  /**
   * current tally of client id.
   */
  private int clientId;

  /**
   * Constructor for a server.
   *
   * @param port Port for server to listen on.
   */
  public Server(int port) throws IOException {
    super(port);
    waitingList = new LinkedBlockingQueue<>();
    clientId = 0;
    members = new HashMap<>();
    matchUps = new HashMap<>();
    this.listen();
  }

  @Override
  protected synchronized void clientException(CustomClientThread client, Throwable exception) {
    super.clientException(client, exception);
    if (client.isConnected()) {
      client.sendMessage(
          new IllegalArgumentException("Cannot parse message " + exception.getMessage()));
    }
  }

  /**
   * Returns the next match-up id.
   *
   * @return Id for creation of next match-up.
   */
  private int getNextMatchupId() {
    matchUpId++;
    return matchUpId;
  }

  /**
   * Gets the local address of the server.
   *
   * @return Local address of server.
   */
  private static InetAddress getLocalAddress() {
    try {
      Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
      while (b.hasMoreElements()) {
        for (InterfaceAddress f : b.nextElement().getInterfaceAddresses()) {
          if (f.getAddress().isSiteLocalAddress()) {
            return f.getAddress();
          }
        }
      }
    } catch (SocketException e) {
      System.err.println("Error with network interface, cannot get local ip address");
    }
    return null;
  }


  /**
   * Quits the server and closes all aspects of the connection to clients.
   *
   * @throws IOException If socket thread cannot close.
   */
  public void quit() throws IOException {
    this.stopListening();
    this.close();
  }

  /**
   * Return the server IP address.
   *
   * @return the server IP address.
   */
  public String getIP() {
    if (getLocalAddress() == null) {
      return "Unknown";
    }
    return getLocalAddress().getHostAddress();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void serverStopped() {
    super.serverStopped();
    System.out.println("Bye bye... Server is closing");
    System.exit(0);
  }

  @Override
  protected void handleMessageFromClient(Object msg, CustomClientThread client) {

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
    waitingList.add(client);
    if (waitingList.size() % 2 == 0)
      matchUps.put(getNextMatchupId(), new MatchUpGenerator(
          waitingList.stream().limit(2).collect(Collectors.toList()), this.matchUpId));
    System.out.println("Client " + client.getIdOfClient() + " has connected successfully with "
        + client.getInetAddress());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void clientDisconnected(CustomClientThread client) {
    super.clientDisconnected(client);
    try {
      waitingList.remove(client);
    } catch (NullPointerException ignored) {
    }
    if (members.size() > 1) { //TODO send to only client in match up disconnection
      members.get(client.getIdOfClient() == 0 ? 1 : 0)
          .sendMessage(new PlayerState(PlayerStatus.DISCONNECTED));
    }
    members.remove(client.getIdOfClient());
    clientId = 0;
  }


}