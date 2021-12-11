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

import esi.acgt.atlj.message.AbstractMessage;
import esi.acgt.atlj.message.messageTypes.GameStatAction;
import esi.acgt.atlj.message.messageTypes.Hold;
import esi.acgt.atlj.message.messageTypes.LockTetrimino;
import esi.acgt.atlj.message.messageTypes.NextMino;
import esi.acgt.atlj.message.messageTypes.SetFallingTetrimino;
import esi.acgt.atlj.model.player.ManagedPlayer;
import esi.acgt.atlj.server.CustomClientThread;
import java.util.HashMap;
import java.util.List;

public class MatchUpModel {

  /**
   * First players.
   */
  ManagedPlayer playerOne;

  /**
   * Seconds player.
   */
  ManagedPlayer playerTwo;

  /**
   * Map to define with players manages which game.
   */
  HashMap<CustomClientThread, ManagedPlayer> gameHashMap;


  List<CustomClientThread> clientThreads;

  /**
   * Constructor for server model.
   */
  public MatchUpModel(List<CustomClientThread> clients) {
    this.playerTwo = new ManagedPlayer("two");
    this.playerOne = new ManagedPlayer("one");
    gameHashMap = new HashMap<>();
    gameHashMap.put(clients.get(0), playerOne);
    gameHashMap.put(clients.get(1), playerTwo);
    this.clientThreads = clients;
  }

  /**
   * Receives a message and handles it.
   *
   * @param information Message that he needs to handle.
   * @param client      Client that sent the message.
   */
  public void receiveMessage(AbstractMessage information, CustomClientThread client) {
    var game = gameHashMap.get(client);
    if (information instanceof NextMino message) { //When next tetrimino is sent from server
      //game.setNextTetrimino(message.getMino());
    }
    if (information instanceof SetFallingTetrimino message) { //When add tetrimino is sent from server
      //game.setActualTetrimino(message.getTetrimino());
    }
    if (information instanceof GameStatAction message) { // When send score is sent from server
      //game.getStats().setScore(message.getScore());
    }
    if (information instanceof Hold message) { // When hold tetrimino is sent from server
      //game.setHold(message.getHold());
    }
    if (information instanceof LockTetrimino message) { //When locked tetrimino has been send from server.
      //game.placeTetrimino(message.getTetrimino());
    }
  }

  /**
   * Returns score of game of client.
   *
   * @param client Client to get game score for
   * @return Score of the client
   */
  public int getGameScore(CustomClientThread client) {
    return gameHashMap.get(client).getStats().getScore();
  }

  /**
   * Returns the player that lost in a list of players.
   *
   * @return Player that lost in the list of players.
   */
  public CustomClientThread getLoser() {
    return getGameScore(clientThreads.get(1)) > getGameScore(clientThreads.get(0))
        ? clientThreads.get(0)
        : clientThreads.get(1);
  }
}

