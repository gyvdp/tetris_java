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

package esi.acgt.atlj.client.connexionServer;

import esi.acgt.atlj.model.tetrimino.Mino;
import java.beans.PropertyChangeListener;

/**
 * Contains all necessary methods to interact with the client.
 */
public interface ClientInterface extends PropertyChangeListener {

  /**
   * Tries to establish a connexion with server.
   */
  public void connect();

  /**
   * Request the next color of Tetrimino.
   *
   * @return Next tetrimino in the bag of the server.
   */
  public Mino requestNextMino();

  /**
   * Syncs your board with the server.
   */
  public void syncBoardWithServer();

  /**
   * Sends your name to the server.
   *
   * @param name Name of the current player.
   */
  public void sendName(String name);

  /**
   * Sends your score to the server.
   *
   * @param Score Score of the current player.
   */
  public void sendScore(int Score);

  /**
   * Sends line that current player just destroyed.
   *
   * @param line Line that current player just destroyed.
   */
  public void removeLine(int line);
}
