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
import esi.acgt.atlj.server.model.BagGenerator;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class Server extends AbstractServer {

  /**
   * First bag generated by the server.
   */
  Mino[] firstBag;

  /**
   * Model of server to generate new bags
   */
  private BagGenerator bagGenerator;
  /**
   * Hash map of all members in function of their clientId.
   */
  private final HashMap<Integer, CustomClientThread> members;
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
    this.bagGenerator = new BagGenerator();
    firstBag = bagGenerator.regenBag();
    clientId = 0;
    members = new HashMap<>();
    this.listen();
  }

  @Override
  protected synchronized void clientException(CustomClientThread client, Throwable exception) {
    super.clientException(client, exception);
    if (client.isConnected()) {
      client.sendMessage(
          new IllegalArgumentException("Message illisible " + exception.getMessage()));
    }
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
  void refillBag() {
    Mino[] bag = bagGenerator.regenBag();
    for (Map.Entry<Integer, CustomClientThread> entry : members.entrySet()) {
      for (Mino m : bag) {
        entry.getValue().addMino(m);
      }
    }
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
    if (client.getClientStatus().equals(PlayerStatus.READY)) {
      CustomClientThread opPlayer = members.get(0).equals(client) ? members.get(1) : members.get(0);
      if (msg.toString().equalsIgnoreCase("ASK_PIECE")) {
        Mino m = client.getMino();
        client.sendMessage(new SendPiece(m));
        opPlayer.sendMessage(new UpdatePieceUnmanagedBoard(m));
      } else {
        opPlayer.sendMessage(msg);
      }
    } else {
      System.err.println("Message dropped " + msg.toString() + " from " + client.getInetAddress());
    }
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
    for (Mino t : firstBag) {
      client.addMino(t);
    }
    if (members.size() == 2) {
      updateAllPlayerState(PlayerStatus.READY);
    }
    System.out.println(
        "Client " + client.getIdOfClient() + " has connected successfully with "
            + client.getInetAddress());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void clientDisconnected(CustomClientThread client) {
    super.clientDisconnected(client);
    if (members.size() > 1) {
      members.get(client.getIdOfClient() == 0 ? 1 : 0)
          .sendMessage(new PlayerState(PlayerStatus.DISCONNECTED));
    }
    members.remove(client.getIdOfClient());
    clientId = 0;
  }


  /**
   * Updates the player state for every player
   *
   * @param playerState PlayerState to update to.
   */
  public void updateAllPlayerState(PlayerStatus playerState) {
    for (int numberOfPlayer = 0; numberOfPlayer < members.size(); numberOfPlayer++) {
      members.get(numberOfPlayer).setClientStatus(playerState);
    }
  }
}