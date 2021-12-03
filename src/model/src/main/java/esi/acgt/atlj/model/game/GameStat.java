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

package esi.acgt.atlj.model.game;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GameStat implements GameStatInterface {

  /**
   * The score of the player
   */
  protected int score;

  /**
   * The highscore of the player
   */
  protected int highScore;

  /**
   * The number of lines burnt
   */
  protected int burns;

  /**
   * The count of actions of the player
   */
  protected Map<Action, Integer> actions;

  /**
   * The Property Change Support to notify when something changes
   */
  protected transient PropertyChangeSupport pcs;

  /**
   * Send score to the server (lambda)
   */
  protected transient Consumer<Integer> setScoreServer;

  /**
   * GameStat constructor
   *
   * @param pcs The Property Change Support
   */
  public GameStat(PropertyChangeSupport pcs) {
    this.pcs = pcs;
    this.score = 0;
    this.highScore = 0;
    this.burns = 0;
    this.actions = new HashMap<>();
  }

  /**
   * Connect the lambda to notify the server of a score change
   *
   * @param setScoreServer lambda to contact the server
   */
  public void connectSendScore(Consumer<Integer> setScoreServer) {
    this.setScoreServer = setScoreServer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getHighScore() {
    return this.highScore;
  }

  /**
   * Set the high-score
   *
   * @param highScore the new high score
   */
  public void setHighScore(int highScore) {
    var old = this.highScore;
    this.highScore = highScore;
    this.pcs.firePropertyChange("HIGH_SCORE", old, highScore);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getScore() {
    return this.score;
  }

  /**
   * Set the score and notify the pcs
   * <p>
   * * @param score new score
   */
  public void setScore(int score) {
    var old = this.score;
    this.score = score;
    this.pcs.firePropertyChange("SCORE", old, score);
    if (this.setScoreServer != null) {
      this.setScoreServer.accept(score);
    }

    if (score > highScore) {
      setHighScore(score);
    }
  }

  /**
   * Increase the score
   *
   * @param increase the number to add to actual score
   */
  public void increaseScore(int increase) {
    setScore(score + increase);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Action, Integer> getActionCount() {
    return actions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getBurns() {
    return this.burns;
  }

  /**
   * Set the number of line burnt
   *
   * @param burns the new nb burns
   */
  public void setBurns(int burns) {
    var old = this.burns;
    this.burns = burns;
    this.pcs.firePropertyChange("BURNS", old, burns);
  }

  /**
   * Increase the number of burns
   *
   * @param increment number to add to actual burns
   */
  public void increaseBurns(int increment) {
    if (increment > 0) {
      setBurns(this.burns + increment);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLevel() {
    return (burns / 10) + 1;
  }

  /**
   * Apply an action to the actual stats
   *
   * @param action Action to apply to stats
   */
  public void applyAction(Action action) {
    if (!actions.containsKey(action)) {
      actions.put(action, 0);
    }

    actions.put(action, actions.get(action) + 1);
    this.pcs.firePropertyChange("ACTION", null, action);

    var multiplier = action.getMultiplyLevel() ? getLevel() : 1;
    increaseScore(action.getPoints() * multiplier);
    increaseBurns(action.getBurns());
  }
}
