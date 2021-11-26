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

package esi.acgt.atlj.server.model;

import esi.acgt.atlj.message.Message;
import esi.acgt.atlj.message.messageTypes.AddTetrimino;
import esi.acgt.atlj.message.messageTypes.LockedTetrimino;
import esi.acgt.atlj.message.messageTypes.RemoveLine;
import esi.acgt.atlj.message.messageTypes.SendPiece;
import esi.acgt.atlj.message.messageTypes.SendScore;
import esi.acgt.atlj.message.messageTypes.SetHold;
import esi.acgt.atlj.model.game.ManagedGame;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.server.CustomClientThread;
import esi.acgt.atlj.server.database.DataBase;
import esi.acgt.atlj.server.database.DataBaseInterface;
import java.util.HashMap;
import java.util.List;

public class ServerModel {

  /**
   * First players.
   */
  ManagedGame playerOne;

  /**
   * DataBase connexion
   */
  DataBaseInterface dataBase;

  /**
   * Seconds player.
   */
  ManagedGame playerTwo;

  StatisticCounter statistics;

  /**
   * Map to define with players manages which game.
   */
  HashMap<CustomClientThread, ManagedGame> gameHashMap;

  /**
   * Constructor for server model.
   */
  public ServerModel(List<CustomClientThread> clients) {
    this.playerTwo = new ManagedGame("two");
    this.playerOne = new ManagedGame("one");
    dataBase = new DataBase();

    gameHashMap = new HashMap<>();
    gameHashMap.put(clients.get(0), playerOne);
    gameHashMap.put(clients.get(1), playerTwo);
    statistics = new StatisticCounter();
    statistics.start();
  }

  /**
   * Receives a message and handles it.
   *
   * @param information Message that he needs to handle.
   * @param client      Client that sent the message.
   */
  public void receiveMessage(Message information, CustomClientThread client) {
    var game = gameHashMap.get(client);
    if (information instanceof SendPiece message) { //When next tetrimino is sent from server
      game.setNextTetrimino(Tetrimino.createTetrimino(message.getMino()));
    }
    if (information instanceof RemoveLine message) { //When remove line is send from server
      game.removeLines(message.getLine());
    }
    if (information instanceof AddTetrimino message) { //When add tetrimino is sent from server
      game.setActualTetrimino(message.getTetrimino());
    }
    if (information instanceof SendScore message) { // When send score is sent from server
      game.setScore(message.getScore());
      statistics.addScore(message.getScore());
    }
    if (information instanceof SetHold message) { // When hold tetrimino is sent from server
      game.setHold(message.getHold());
    }
    if (information instanceof LockedTetrimino message) { //When locked tetrimino has been send from server.
      game.placeTetrimino(message.getTetrimino());
    }
  }


  /**
   * Sends all necessary information to database.
   */
  public void sendToDataBase() {
    dataBase.score(statistics.getScore(), "Gregory");
  }

  public void checkNameInDB(String username) {
    dataBase.connectToDb();
    dataBase.checkUserInDb(username);
  }
}

