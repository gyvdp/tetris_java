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

package esi.acgt.atlj.message.messageTypes;

import esi.acgt.atlj.message.AbstractMessage;
import esi.acgt.atlj.model.Game;
import esi.acgt.atlj.model.player.ManagedPlayer;
import esi.acgt.atlj.model.player.UnmanagedPlayer;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;


/**
 * Conveys the state of the player to the client.
 */
public class StartGame extends AbstractMessage {


  String username;
  String usernameOpp;
  Mino actualMino;
  Mino nextMino;

  /**
   * Constructor for player state.
   */
  public StartGame(String usernameOpp, String username, Mino actual, Mino next) {
    this.username = username;
    this.usernameOpp = usernameOpp;
    this.actualMino = actual;
    this.nextMino = next;
  }

  public void execute(Game game) {
    var playerOpp = (UnmanagedPlayer) game.getBoard("search");
    playerOpp.setUsername(this.usernameOpp);
    var player = (ManagedPlayer) game.getBoard(this.username);
    player.setActualTetrimino(Tetrimino.createTetrimino(actualMino));
    player.setNextTetrimino(nextMino);
    playerOpp.setActualTetrimino(Tetrimino.createTetrimino(actualMino));
    playerOpp.setNextTetrimino(nextMino);
    player.start();
  }


  public String toString() {
    return this.usernameOpp + " - vs - " + this.username;
  }
}
