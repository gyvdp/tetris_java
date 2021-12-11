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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Class that takes care of every pair of players in the server.
 */
public class BagGenerator {


  private final HashMap<String, List<Mino>> playerTetrimino;

  /**
   * Constructor for server model.
   */
  public BagGenerator(String usernameOne, String usernameTwo) {
    List<Mino> firstBag = regenBag();
    this.playerTetrimino = new HashMap<>();
    playerTetrimino.put(usernameOne, firstBag);
    playerTetrimino.put(usernameTwo, firstBag);
  }

  /**
   * Takes the first mino of the array from a specific user.
   *
   * @param username Username of player to take.
   * @return First mino of array.
   */
  public Mino takeMino(String username) {
    List<Mino> mino = this.playerTetrimino.get(username);
    if (mino.size() < 2) {
      refillBags();
    }
    Mino m = mino.remove(0);
    System.out.println(m);
    return m;
  }

  /**
   * Refills bag of both players.
   */
  private void refillBags() {
    System.out.println("refill");
    var bag = regenBag();
    for (var entry : playerTetrimino.entrySet()) {
      entry.getValue().addAll(bag);
    }
  }

  /**
   * Regenerates a new bag of seven mino that has been shuffled. The reason to them being minos and
   * not tetriminos is that we only need the color.
   *
   * @return Array of shuffled minos.
   */
  public List<Mino> regenBag() {
    System.out.println("regen");
    List<Mino> bag = new ArrayList<>(Arrays.asList(
        Mino.S_MINO, Mino.Z_MINO, Mino.O_MINO, Mino.J_MINO, Mino.T_MINO,
        Mino.I_MINO, Mino.L_MINO));
    Collections.shuffle(bag);
    return bag;
  }

}
