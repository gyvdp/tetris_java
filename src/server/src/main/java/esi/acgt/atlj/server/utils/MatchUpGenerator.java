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

package esi.acgt.atlj.server.utils;

import esi.acgt.atlj.database.business.BusinessInterface;
import esi.acgt.atlj.database.dto.User;
import esi.acgt.atlj.database.exceptions.BusinessException;
import esi.acgt.atlj.message.Message;
import esi.acgt.atlj.message.PlayerStatus;
import esi.acgt.atlj.message.messageTypes.AskPiece;
import esi.acgt.atlj.message.messageTypes.PlayerState;
import esi.acgt.atlj.message.messageTypes.SendAllStatistics;
import esi.acgt.atlj.message.messageTypes.SendGameStats;
import esi.acgt.atlj.message.messageTypes.SendHighScore;
import esi.acgt.atlj.message.messageTypes.SendName;
import esi.acgt.atlj.message.messageTypes.SendPiece;
import esi.acgt.atlj.message.messageTypes.UpdateNextPieceOther;
import esi.acgt.atlj.model.game.GameStatInterface;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.server.CustomClientThread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * Starts and manages a match-up between two clients.
 */
public class MatchUpGenerator extends Thread {

  /**
   * Game server side
   */
  private final MatchUpModel model;
  /**
   * Generator for new bags of tetriminos.
   */
  private final BagGenerator bagGenerator;
  /**
   * Interaction with database
   */
  private final BusinessInterface interactDatabase;
  /**
   * List of clients.
   */
  List<CustomClientThread> clients;

  /**
   * List of spectators
   */
  List<CustomClientThread> spectators;

  /**
   * Unique id of generated match-up.
   */
  int id;

  /**
   * Constructor for match-up generator. Assigns its current id to both clients and launches the
   * game in a new thread.
   *
   * @param clients            Client that are going head to head in match-up.
   * @param idGeneratedMatchUp Id of generated match-up.
   * @param interactDatabase   Interface to interact with database.
   */
  public MatchUpGenerator(List<CustomClientThread> clients, int idGeneratedMatchUp,
      BusinessInterface interactDatabase) {
    this.clients = clients;
    this.interactDatabase = interactDatabase;
    this.spectators = new ArrayList<>();
    this.model = new MatchUpModel(clients);
    this.bagGenerator = new BagGenerator();
    this.id = idGeneratedMatchUp;
    for (CustomClientThread client : clients) {
      getOpposingClient(client).sendMessage(new SendName(client.getUsername()));
      clientLambdaConnections(client);
      SendAllStatistics statistics = new SendAllStatistics();
      try {
        client.sendMessage(new SendHighScore(getBothPlayersHighScoreDB()));
        createSendAllStatisticsMessage(statistics, client);
      } catch (BusinessException e) {
        System.err.println("Cannot get user high score");
      }
    }
    this.start();
  }

  /**
   * Lambda expression to refill bags.
   */
  Runnable refillBag = this::refillBags;

  /**
   * Lambda expression to handle message from client.
   */
  BiConsumer<Message, CustomClientThread> handleMessage = (Message m, CustomClientThread client) ->
  {

    if (client.getClientStatus().equals(PlayerStatus.READY)) {
      sendMessageToModel(m, client);
      CustomClientThread opPlayer = getOpposingClient(client);
      if (opPlayer != null) {
        if (m instanceof AskPiece) {
          Mino mino = client.getMino();
          client.sendMessage(new SendPiece(mino));
          opPlayer.sendMessage(new UpdateNextPieceOther(mino));
        } else if (m instanceof SendGameStats stats) {
          setGameStats(stats.getGameStats(), client.getUser());
        } else {
          opPlayer.sendMessage(m);
        }
      }

    } else {
      System.err.println("Message dropped " + m.toString() + " from " + client.getInetAddress());
    }
  };

  /**
   * Gets all user stats
   */
  BiConsumer<SendAllStatistics, CustomClientThread> getAllStats = this::createSendAllStatisticsMessage;

  /**
   * Creates a message will both tables for a specific user.
   *
   * @param m      Message to create.
   * @param client Client to get all statistics from.
   */
  private void createSendAllStatisticsMessage(SendAllStatistics m, CustomClientThread client) {
    try {
      m.setGame_history(interactDatabase.selectAllFromGameHistory(client.getUser()));
      m.setTetrimino_history(
          interactDatabase.selectAllFromTetriminoHistory(client.getUser()));
      client.sendMessage(m);
    } catch (BusinessException e) {
      System.err.println("Cannot create message with all statistics");
    }
  }

  /**
   * Decrements the id of match-up in server when this closes.
   */
  private Runnable decrementMatchUpId;
  /**
   * Handles disconnection of player from match-up. If both players have disconnected, kills
   * thread.
   */
  Consumer<CustomClientThread> disconnect = (CustomClientThread clientThread) -> {
    int notPlaying = 0;
    getOpposingClient(clientThread).sendMessage(new PlayerState(PlayerStatus.DISCONNECTED));
    for (CustomClientThread customClientThread : clients) {
      if (!(customClientThread.isConnected())) {
        notPlaying++;
      }
    }
    if (notPlaying == 2) { //If both players are not playing match-up should end.
      updateDb();
      System.out.println("Match-up " + this.id + 1 + " has ended");
      decrementMatchUpId.run();
      this.interrupt();
    }
  };


  /**
   * Gets high score of both players.
   *
   * @return A hashmap where the high score is identified by the username.
   * @throws BusinessException If query to get user high score has failed.
   */
  private HashMap<String, Integer> getBothPlayersHighScoreDB() throws BusinessException {
    HashMap<String, Integer> highScores = new HashMap<>();
    for (CustomClientThread client : clients) {
      highScores.put(client.getUsername(), interactDatabase.getUserHighScore(client.getUser()));
    }
    return highScores;
  }

  private void setGameStats(GameStatInterface statistics, User user) {
    try {
      interactDatabase.addBurns(user, statistics.getBurns());
      interactDatabase.setHighestLevel(user, statistics.getLevel());
      interactDatabase.setUserHighScore(user, statistics.getScore());
      interactDatabase.addDestroyedLines(user, statistics.getActionCount());
    } catch (BusinessException e) {
      System.out.println("Cannot set user in database.");
    }

  }

  /**
   * Updates the database with specific values of the game
   */
  public void updateDb() {
    CustomClientThread loser = model.getLoser(clients);
    try {
      interactDatabase.addLostGame(loser.getUser());
      interactDatabase.addWonGame(getOpposingClient(loser).getUser());
    } catch (BusinessException e) {
      System.err.println("Cannot send won and lost games to database \n" + e.getMessage());
    }
  }

  /**
   * Connects all necessary lambdas to client.
   *
   * @param client Client to connect lambdas to.
   */
  public void clientLambdaConnections(CustomClientThread client) {
    if (client != null) {
      client.connectRefillBag(this.refillBag);
      client.connectHandleMessage(this.handleMessage);
      client.connectDisconnect(this.disconnect);
      client.connectAllStats(this.getAllStats);
    }
  }

  public synchronized void addSpectator(CustomClientThread client) {
    this.spectators.add(client);
    //client.sendMessage(new SendBoard(model.receiveBoard(client), client.getNameOfClient());
    //todo add spectator??.
  }

  /**
   * Connects decrement match up id from server
   *
   * @param dec Lambda to connect.
   */
  public void connectDecrementMatchUpId(Runnable dec) {
    this.decrementMatchUpId = dec;
  }

  /**
   * Sends a received message to the model to be treated.
   *
   * @param m Message that needs to be sent.
   * @param c Client that sent the message.
   */
  public void sendMessageToModel(Message m, CustomClientThread c) {
    model.receiveMessage(m, c);
  }

  /**
   * Sends a message to the spectator.
   *
   * @param m Message to send to spectator.
   * @param c Client to send message to.
   */
  public void sendMessageToSpectator(Message m, CustomClientThread c) {
    c.sendMessage(m); //todo specific spectator message with name of player sending it.
  }

  /**
   * Gets the opposing client of the given client.
   *
   * @param client Client to get adversary of.
   * @return Opposing client of given client.
   */
  private CustomClientThread getOpposingClient(CustomClientThread client) {
    return clients.get(0).equals(client) ? clients.get(1) : clients.get(0);
  }

  /**
   * Asks bag generator to refill bag for each client with same mino.
   */
  synchronized void refillBags() {
    Mino[] bag = bagGenerator.regenBag();
    for (CustomClientThread client : clients) {
      for (Mino m : bag) {
        client.addMino(m);
      }
    }
  }


  /**
   * Updates the player state for every player.
   *
   * @param playerState PlayerState to update to.
   */
  public void updateAllPlayerState(PlayerStatus playerState) {
    for (CustomClientThread client : clients) {
      client.setClientStatus(playerState);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    refillBags();
    updateAllPlayerState(PlayerStatus.READY);
  }
}
