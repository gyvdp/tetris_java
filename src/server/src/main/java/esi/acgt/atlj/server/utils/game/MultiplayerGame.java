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

package esi.acgt.atlj.server.utils.game;

import esi.acgt.atlj.message.AbstractMessage;
import esi.acgt.atlj.message.GameMessage;
import esi.acgt.atlj.message.MessageType;
import esi.acgt.atlj.model.Game;
import esi.acgt.atlj.model.player.ManagedPlayer;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of an ongoing multiplayer game.
 */
public class MultiplayerGame extends Game {

  /**
   * Constructor for a multiplayer game.
   *
   * @param usernameOne Name of first player.
   * @param usernameTwo Name of second player.
   */
  public MultiplayerGame(String usernameOne, String usernameTwo) {
    super();
    ManagedPlayer playerOne = new ManagedPlayer(usernameOne);
    ManagedPlayer playerTwo = new ManagedPlayer(usernameTwo);
    players.add(playerOne);
    players.add(playerTwo);
  }

  /**
   * Executes the message if it updates in some sort of way the game.
   *
   * @param m Message to execute.
   */
  public void handleMessage(AbstractMessage m) {
    if (m.getType() == MessageType.GAME) {
      ((GameMessage) m).execute(this);
    }
  }

  /**
   * Finds the winner and loser of a multiplayer game;
   *
   * @return List with the username of the winner and the loser.
   */
  public List<String> getStandings() {
    List<String> standings = new ArrayList<>();
    if (players.get(0).getStats().getScore() > players.get(1).getStats().getScore()) {
      standings.add(players.get(0).getUsername());
      standings.add(players.get(1).getUsername());
    } else {
      standings.add(players.get(1).getUsername());
      standings.add(players.get(0).getUsername());
    }
    return standings;
  }

  @Override
  public void addPropertyChangeListenerToBoards(PropertyChangeListener[] listener) {

  }
}
