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

import esi.acgt.atlj.database.business.BusinessInterface;
import esi.acgt.atlj.database.business.BusinessModel;
import esi.acgt.atlj.database.dto.GameHistoryDto;
import esi.acgt.atlj.database.dto.TetriminoDto;
import esi.acgt.atlj.database.dto.UserDto;
import esi.acgt.atlj.database.exceptions.BusinessException;
import esi.acgt.atlj.message.messageTypes.SendAllStatistics;
import esi.acgt.atlj.server.utils.MatchUpGenerator;
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

/**
 * Tetris server
 */
public class Server extends AbstractServer {

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
   * Database interaction
   */
  BusinessInterface interactDatabase;
  /**
   * current number of match-ups;
   */
  private int matchUpId = 0;
  Runnable decrementMatchUpId = () -> {
    this.matchUpId--;
  };
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
    interactDatabase = new BusinessModel();
    members = new HashMap<>();
    matchUps = new HashMap<>();
    this.listen();
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
   * {@inheritDoc}
   */
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
   * {@inheritDoc}
   */
  @Override
  protected void serverStarted(int port) {
    System.out.println("Server is running on " + getIP() + ":" + port + "...");
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
    System.out.println("Client" + client.getIdOfClient() + " has connected with ip address :"
        + client.getInetAddress());
    members.put(getNextId(), client);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected synchronized void checkUser(UserDto user) {
    try {
      UserDto persistentUser = interactDatabase.getUser(user.getUsername());
      if (persistentUser != null) {
        user.set(persistentUser);
      } else {
        user.setId(interactDatabase.addUser(user.getUsername()));
      }
    } catch (BusinessException e) {
      System.err.println("error creating or adding user");
    }
  }

  public synchronized void getStatOfPlayer(SendAllStatistics m, CustomClientThread client) {
    try {
      m.setGame_history(
          parseEntityGameHistory(interactDatabase.getGameStatEntity(client.getUser())));
      m.setTetrimino_history(
          parseEntityTetrimino(interactDatabase.getTetriminoEntity(client.getUser())));
      client.sendMessage(m);
    } catch (BusinessException e) {
      System.err.println("Cannot create message with all statistics");
    }
  }

  /**
   * Parses a database object of Game History to send to user via hashmap.
   *
   * @param entity Entity to parse.
   * @return Hashmap that has been parsed.
   */
  private HashMap<String, Integer> parseEntityGameHistory(GameHistoryDto entity) {
    HashMap<String, Integer> statistics = new HashMap<>();
    statistics.put("SCORE", entity.getHighScore());
    statistics.put("LOST", entity.getNbLost());
    statistics.put("WON", entity.getNbWon());
    statistics.put("LEVEL", entity.getHighestLevel());
    return statistics;
  }


  private HashMap<String, Integer> parseEntityTetrimino(TetriminoDto entity) {
    HashMap<String, Integer> statistics = new HashMap<>();
    statistics.put("SINGLE", entity.getSingle());
    statistics.put("DOUBLE", entity.getDoubles());
    statistics.put("TRIPLE", entity.getTriple());
    statistics.put("TETRIS", entity.getTetris());
    statistics.put("SOFT_DROP", entity.getSoftdrop());
    statistics.put("HARD_DROP", entity.getHarddrop());
    statistics.put("BURN", entity.getBurns());
    return statistics;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void addPlayer(CustomClientThread client) {
    addClientToWaitingList(client);
    if (waitingList.size() % 2 == 0) {
      MatchUpGenerator matchUp = new MatchUpGenerator(
          waitingList.stream().limit(2).collect(Collectors.toList()), this.matchUpId,
          interactDatabase, this);
      matchUp.connectDecrementMatchUpId(this.decrementMatchUpId);
      matchUps.put(getNextMatchupId(), matchUp);
      System.out.println("A new match-up has been created with id: " + this.matchUpId);
      try {
        waitingList.take();
        waitingList.take();
      } catch (InterruptedException ignored) {
      }
    }
  }


  public void addClientToWaitingList(CustomClientThread client) {
    waitingList.add(client);
    System.out.println(
        "Client " + client.getIdOfClient() + " is in the waiting list");
  }

  @Override
  protected synchronized void playerQuit(CustomClientThread client) {
    super.playerQuit(client);
    if (waitingList.remove(client)) {
      System.out.println("Client" + client.getIdOfClient() +
          " has quit the game, he is no longer is the waiting list");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void clientDisconnected(CustomClientThread client) {
    super.clientDisconnected(client);
    try {
      if (waitingList.remove(client)) {
        System.out.println(
            "Client " + client.getIdOfClient() + " has been removed from the waiting list");
      }
    } catch (NullPointerException ignored) {
    }
    members.remove(client.getIdOfClient());
    clientId--;
  }


}