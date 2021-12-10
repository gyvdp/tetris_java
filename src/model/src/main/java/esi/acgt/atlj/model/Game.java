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

package esi.acgt.atlj.model;

import esi.acgt.atlj.model.player.PlayerInterface;
import java.util.LinkedList;
import java.util.List;

public abstract class Game implements GameInterface {

  protected List<PlayerInterface> players;

  protected Game() {
    this.players = new LinkedList<>();
  }

  @Override
  public String[] getUsernames() {
    var players = new String[this.players.size()];
    for (var i = 0; i < this.players.size(); ++i) {
      players[i] = this.players.get(i).getUsername();
    }
    return players;
  }

  @Override
  public PlayerInterface getBoard(String username) {
    for (PlayerInterface player : this.players) {
      if (player.getUsername().equals(username)) {
        return player;
      }
    }
    return null;
  }

  @Override
  public void addPlayer(PlayerInterface board) {
    this.players.add(board);
  }
}