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

import esi.acgt.atlj.model.tetrimino.Mino;
import java.util.Random;

/**
 * Class that takes care of every pair of players in the server.
 */
public class BagGenerator {

  /**
   * First random bag of tetriminos of game.
   */
  private final Mino[] firstBag;

  /**
   * Constructor for server model.
   */
  public BagGenerator() {  //TODO get pair of players
    this.firstBag = regenBag();
  }

  /**
   * Shuffles an array with the Fisher-Yates algorithm.
   *
   * @param array Shuffled array.
   */
  private static void shuffleTetriminos(Mino[] array) {
    int n = array.length;
    Random random = new Random(System.currentTimeMillis());
    for (int i = 0; i < array.length; i++) {
      int randomValue = i + random.nextInt(n - i);
      Mino randomElement = array[randomValue];
      array[randomValue] = array[i];
      array[i] = randomElement;
    }
  }

  /**
   * Regenerates a new bag of seven mino that has been shuffled. The reason to them being minos and
   * not tetriminos is that we only need the color.
   *
   * @return Array of shuffled minos.
   */
  public Mino[] regenBag() {
    Mino[] bag = new Mino[]{
        Mino.S_MINO, Mino.Z_MINO, Mino.O_MINO, Mino.J_MINO, Mino.T_MINO,
        Mino.I_MINO, Mino.L_MINO
    };
    shuffleTetriminos(bag);
    return bag;
  }

}
