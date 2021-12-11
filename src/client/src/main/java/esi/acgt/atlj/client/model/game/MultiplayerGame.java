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

package esi.acgt.atlj.client.model.game;

import esi.acgt.atlj.model.Game;
import esi.acgt.atlj.model.player.Direction;
import esi.acgt.atlj.model.player.ManagedPlayer;
import esi.acgt.atlj.model.player.PlayerStatus;
import esi.acgt.atlj.model.player.UnmanagedPlayer;
import java.beans.PropertyChangeListener;

public class MultiplayerGame extends Game {

  private ManagedPlayer player;
  private UnmanagedPlayer otherPlayer;

  public MultiplayerGame(String username) {
    super();
    player = new ManagedPlayer(username);
    otherPlayer = new UnmanagedPlayer();
    players.add(player);
    players.add(otherPlayer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListenerToBoards(PropertyChangeListener[] listener) {
    this.player.addPropertyChangeListener(listener[0]);
    this.otherPlayer.addPropertyChangeListener(listener[1]);
  }

  public void addPCSToPlayer(PropertyChangeListener listener) {
    this.player.addPropertyChangeListener(listener);
  }

  /**
   * Moves a player mino
   *
   * @param direction Direction to move mino in.
   */
  public void move(Direction direction) {
    this.player.move(direction);
  }

  /**
   * Holds a player's mino.
   */
  public void hold() {
    this.player.hold();
  }

  /**
   * Hard drops a player's mino
   */
  public void hardDrop() {
    this.player.hardDrop();
  }

  /**
   * Soft drops a player's mino.
   */
  public void softDrop() {
    this.player.softDrop();
  }

  /**
   * Rotates a players mino
   *
   * @param clockwise Direction of rotation
   */
  public void rotate(boolean clockwise) {
    player.rotate(clockwise);
  }

  /**
   * Gets status of the game
   *
   * @return Current status of the game.
   */
  public PlayerStatus getStatus() {
    return this.player.getStatus();
  }


  public ManagedPlayer getPlayer() {
    return this.player;
  }

  public UnmanagedPlayer getOtherPlayer() {
    return this.otherPlayer;
  }

}
