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
import esi.acgt.atlj.server.utils.MatchUpHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Tetris server
 */
public class Server extends AbstractServer {

  /**
   * All the clients that are currently waiting to play
   */
  private final BlockingQueue<CustomClientThread> waitingList;

  /**
   * Database interaction
   */
  BusinessInterface interactDatabase;

  /**
   * current number of match-ups;
   */
  private int matchUpId = 0;
  /**
   * current tally of client id.
   */
  private int clientId;

  private BlockingQueue<String> usernames;

  private static final java.util.logging.Logger logger = Logger.getLogger(Server.class.getName());


  /**
   * Constructor for a server.
   *
   * @param port Port for server to listen on.
   */
  public Server(int port) throws IOException {
    super(port);
    waitingList = new LinkedBlockingQueue<>();
    usernames = new LinkedBlockingQueue<>();
    clientId = 0;
    interactDatabase = new BusinessModel();
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
      logger.log(Level.SEVERE,
          ("Cannot get IP address"));

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
   * {@inheritDoc}
   */
  @Override
  protected void serverStarted(int port) {
    logger.log(Level.INFO,
        String.format("Server is running on ip : %s and port : %s", getIP(), port));

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
    logger.log(Level.INFO,
        ("Server is closed"));
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
    logger.log(Level.INFO,
        String.format("Client has been added with ip address %s", client.getInetAddress()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected synchronized void checkUser(CustomClientThread user) {
    if (usernames.contains(user.getUsername())) {
      try {
        user.close();
      } catch (IOException ignored) {
      }
    } else {
      usernames.add(user.getUsername());
    }
    try {
      UserDto persistentUser = interactDatabase.getUser(user.getUsername());
      if (persistentUser != null) {
        user.getUser().set(persistentUser);
      } else {
        user.getUser().setId(interactDatabase.addUser(user.getUsername()));
        logger.log(Level.INFO,
            String.format("User has been added to database %s", user.getUsername()));
      }
    } catch (BusinessException e) {
      logger.log(Level.SEVERE,
          String.format("Cannot create user %s", user.getUsername()));
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
      logger.log(Level.SEVERE,
          ("Cannot create a message with all statistics"));
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
      createMatchup();
      logger.log(Level.INFO,
          String.format("A new match-up has been created with id: %s", this.matchUpId));
    }
  }

  private void createMatchup() {
    List<CustomClientThread> clientsToAdd = new ArrayList<>();
    try {
      clientsToAdd.add(waitingList.take());
      clientsToAdd.add(waitingList.take());
    } catch (InterruptedException ignored) {
    }
    new MatchUpHandler(
        clientsToAdd, this.matchUpId,
        interactDatabase, this);

  }

  public synchronized void addClientToWaitingList(CustomClientThread client) {
    if (!waitingList.contains(client)) {
      waitingList.add(client);
      logger.log(Level.INFO,
          String.format("%s has been added to the waiting list", client.getUsername()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void clientDisconnected(CustomClientThread client) {
    super.clientDisconnected(client);
    logger.log(Level.INFO,
        String.format("%s has been disconnected from server", client.getUsername()));
    try {
      waitingList.remove(client);
    } catch (NullPointerException ignored) {
    }
    clientId--;
  }


}