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
import esi.acgt.atlj.database.dto.GameHistoryDto;
import esi.acgt.atlj.database.dto.TetriminoDto;
import esi.acgt.atlj.database.dto.UserDto;
import esi.acgt.atlj.database.exceptions.BusinessException;
import esi.acgt.atlj.message.AbstractMessage;
import esi.acgt.atlj.message.messageTypes.AskPiece;
import esi.acgt.atlj.message.messageTypes.HighScore;
import esi.acgt.atlj.message.messageTypes.NextMino;
import esi.acgt.atlj.message.messageTypes.PlayerState;
import esi.acgt.atlj.message.messageTypes.SendStartGame;
import esi.acgt.atlj.model.Game;
import esi.acgt.atlj.model.player.PlayerStatInterface;
import esi.acgt.atlj.message.messageTypes.SendAllStatistics;
import esi.acgt.atlj.model.player.Action;
import esi.acgt.atlj.model.player.PlayerStatus;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.server.AbstractServer;
import esi.acgt.atlj.server.CustomClientThread;
import esi.acgt.atlj.server.Server;
import esi.acgt.atlj.server.utils.game.MultiplayerGame;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Starts and manages a match-up between two clients.
 */
public class MatchUpHandler extends Thread {

  private final Game game;

  /**
   * Generator for new bags of tetriminos.
   */
  private BagGenerator bagGenerator;
  /**
   * Interaction with database
   */
  private final BusinessInterface interactDatabase;
  /**
   * List of clients.
   */
  HashMap<String, CustomClientThread> clients;

  /**
   * List of users
   */
  HashMap<String, UserDto> users;


  /**
   * Server match up belongs to.
   */
  AbstractServer server;
  /**
   * Unique id of generated match-up.
   */
  int id;

  private static final java.util.logging.Logger logger = Logger.getLogger(
      MatchUpHandler.class.getName());


  /**
   * Constructor for match-up generator. Assigns its current id to both clients and launches the
   * game in a new thread.
   *
   * @param clients            Client that are going head to head in match-up.
   * @param idGeneratedMatchUp Id of generated match-up.
   * @param interactDatabase   Interface to interact with database.
   */
  public MatchUpHandler(List<CustomClientThread> clients, int idGeneratedMatchUp,
      BusinessInterface interactDatabase, AbstractServer server) {
    this.clients = new HashMap<>();
    this.users = new HashMap<>();
    this.clients.put(clients.get(0).getUsername(), clients.get(0));
    this.clients.put(clients.get(1).getUsername(), clients.get(1));
    this.users.put(clients.get(0).getUsername(), clients.get(0).getUser());
    this.users.put(clients.get(1).getUsername(), clients.get(1).getUser());
    this.game = new MultiplayerGame(clients.get(0).getUsername(), clients.get(1).getUsername());
    this.server = server;
    this.interactDatabase = interactDatabase;
    this.bagGenerator = new BagGenerator(clients.get(0).getUsername(),
        clients.get(1).getUsername());
    this.id = idGeneratedMatchUp;
    for (CustomClientThread client : clients) {
      client.connectHandleMessage(this.handleMessage);
    }
    this.start();
  }


  /**
   * Sends to each player inside match up that game is ready to start and high score of all
   * players.
   */
  public void startGame() {
    for (var entry : clients.entrySet()) {
      CustomClientThread client = entry.getValue();
      var username = client.getUsername();
      var usernamesOther = getOtherPlayerUsername(client);
      client.sendMessage(new SendStartGame
          (usernamesOther[0], username, bagGenerator.takeMino(username),
              bagGenerator.takeMino(username)));
      client.sendMessage(new HighScore(getHighScores()));
    }
  }

  /**
   * Gets the username of all the other players.
   *
   * @param client Client that needs to be excluded from usernames.
   * @return Array of all other usernames.
   */
  public String[] getOtherPlayerUsername(CustomClientThread client) {
    String[] usernames = new String[1];
    for (var entry : clients.entrySet()) {
      if (!Objects.equals(entry.getValue().getUsername(), client.getUsername())) {
        usernames[0] = entry.getValue().getUsername();
      }
    }
    return usernames;
  }

  /**
   * Lambda expression to handle message from client.
   */
  BiConsumer<AbstractMessage, CustomClientThread> handleMessage = (AbstractMessage m, CustomClientThread client) ->
  {
    if (m instanceof AskPiece) {
      Mino mino = bagGenerator.takeMino(client.getUsername());
      client.sendMessage(new NextMino(mino, client.getUsername()));
      sendToAllOthers(client, new NextMino(mino, client.getUsername()));
    } else if (m instanceof PlayerState) {
      if (((PlayerState) m).getPlayerStatus().equals(PlayerStatus.STOPPED)) {
        quit(client);
        sendToAllOthers(client, m);
      }
    } else {
      sendToAllOthers(client, m);
    }
  };


  /**
   * Sends a message to all other entities participating in the match up except the client that has
   * sent the message.
   *
   * @param client  Client that has sent the message.
   * @param message Message that has been sent.
   */
  public void sendToAllOthers(CustomClientThread client, AbstractMessage message) {
    for (var entry : clients.entrySet()) {
      if (!Objects.equals(entry.getValue().getUsername(), client.getUsername())) {
        entry.getValue().sendMessage(message);
      }
    }
    sendMessageToModel(message);
  }

  /**
   * When a player quit a match up. Check if match up has ended and updates user specific
   *
   * @param client Client that has quit the match up.
   */
  public void quit(CustomClientThread client) {
    setGameStats(game.getBoard(client.getUsername()).getStats(), client.getUser());
    clients.remove(client.getUsername());
    server.getStatOfPlayer(new SendAllStatistics(), client);
    checkEndOfMatchUp();
  }

  /**
   * Checks if the match up has ended.
   */
  private void checkEndOfMatchUp() {
    if (clients.size() == 0) {
      updateStandings();
      logger.log(Level.INFO,
          String.format("The match up has been ended id : %s", ++this.id));
    }
  }


  /**
   * Sends a received message to the model to be treated.
   *
   * @param m Message that needs to be sent.
   */
  public void sendMessageToModel(AbstractMessage m) {
    ((MultiplayerGame) game).handleMessage(m);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    startGame();
  }

  /**
   * Gets high score of both players.
   *
   * @return A hashmap where the high score is identified by the username.
   */
  private HashMap<String, Integer> getHighScores() {
    HashMap<String, Integer> highScores = new HashMap<>();
    for (var entry : clients.entrySet()) {
      CustomClientThread client = entry.getValue();
      try {
        highScores.put(client.getUsername(), interactDatabase.getUserHighScore(client.getUser()));
      } catch (BusinessException e) {
        logger.log(Level.SEVERE,
            ("Cannot get high score of both users"));

      }
    }
    return highScores;
  }

  /**
   * Sets the game statistics to a given user.
   *
   * @param statistics Statistics to set to user.
   * @param user       User to set statistics to.
   */
  private void setGameStats(PlayerStatInterface statistics, UserDto user) {
    try {
      GameHistoryDto gameDto = new GameHistoryDto(user.getId(), statistics.getLevel(),
          statistics.getScore(), 0, 0);
      interactDatabase.setGameStatEntity(gameDto);
      TetriminoDto tetriminoDto = new TetriminoDto(user.getId(),
          statistics.getActionCount().getOrDefault(Action.SINGLE, 0),
          statistics.getActionCount().getOrDefault(Action.DOUBLE, 0),
          statistics.getActionCount().getOrDefault(Action.TRIPLE, 0),
          statistics.getActionCount().getOrDefault(Action.TETRIS, 0),
          statistics.getActionCount().getOrDefault(Action.SOFT_DROP, 0),
          statistics.getActionCount().getOrDefault(Action.HARD_DROP, 0),
          statistics.getBurns());
      interactDatabase.setTetriminoEntity(tetriminoDto);
    } catch (BusinessException e) {
      logger.log(Level.SEVERE,
          ("Cannot set user in database"));
    }
  }

  /**
   * Updates the standings of the database.
   */
  public void updateStandings() {
    var result = ((MultiplayerGame) game).getStandings();
    GameHistoryDto dtoLost = new GameHistoryDto(users.get(result.get(0)).getId(), 0, 0, 1, 0);
    GameHistoryDto dtoWon = new GameHistoryDto(users.get(result.get(1)).getId(), 0, 0, 0, 1);
    try {
      interactDatabase.setGameStatEntity(dtoLost);
      interactDatabase.setGameStatEntity(dtoWon);
    } catch (BusinessException e) {
      logger.log(Level.SEVERE,
          ("Cannot send won and lost games to database"));
    }
  }
}
