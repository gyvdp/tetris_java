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

import esi.acgt.atlj.message.GameMessage;
import esi.acgt.atlj.model.Game;
import esi.acgt.atlj.model.player.AbstractPlayer;
import esi.acgt.atlj.model.player.Action;

/**
 * Sends score to server.
 */
public class GameStatAction extends GameMessage {

  private final Action action;

  /**
   * Adds the score to a player.
   *
   * @param action action to send.
   * @param name   Player that has sent the message.
   */
  public GameStatAction(Action action, String name) {
    super(name);
    this.action = action;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(Game game) {
    var player = (AbstractPlayer) (game.getBoard(this.userName));
    player.applyStatAction(this.action);
  }
}
