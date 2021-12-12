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

package esi.acgt.atlj.test.model.tetrimino;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import esi.acgt.atlj.model.shape.JShape;
import esi.acgt.atlj.model.shape.Orientation;
import esi.acgt.atlj.model.tetrimino.JTetrimino;
import esi.acgt.atlj.model.tetrimino.Mino;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JTetriminoTest {

  @Nested
  class Constructor {

    @Test
    public void ok() {
      JTetrimino tetrimino = new JTetrimino();
      Mino mino = Mino.J_MINO;

      assertEquals(mino, tetrimino.getType(), "mino type");
      assertEquals(3, tetrimino.getX(), "initial x value");
      assertEquals(0, tetrimino.getY(), "initial y value");

      Mino[][] minos = tetrimino.getMinos();
      Mino[][] expectedMinos = {
          {mino, null, null, null},
          {mino, mino, mino, null},
          {null, null, null, null},
          {null, null, null, null}
      };

      assertArrayEquals(expectedMinos, minos, "tetrimino shape");
    }
  }

  @Nested
  class Rotation {

    @Nested
    public class NoCollision {

      @Test
      public void NorthToEast() {
//        Mino mino = Mino.J_MINO;
        JTetrimino tetrimino = new JTetrimino();
        boolean[][] area = {
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true}
        };
        tetrimino.rotate(true, area);

        Mino[][] minos = tetrimino.getMinos();
        Mino[][] expectedMinos = JShape.shapes.get(Orientation.EAST);
        assertArrayEquals(expectedMinos, minos, "tetrimino shape");
      }

      @Test
      public void EastToSouth() {
//        Mino mino = Mino.J_MINO;
        JTetrimino tetrimino = new JTetrimino();
        boolean[][] area = {
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true}
        };
        tetrimino.rotate(true, area);
        tetrimino.rotate(true, area);

        Mino[][] minos = tetrimino.getMinos();
        Mino[][] expectedMinos = JShape.shapes.get(Orientation.SOUTH);
        assertArrayEquals(expectedMinos, minos, "tetrimino shape");
      }

      @Test
      public void SouthToWest() {
//        Mino mino = Mino.J_MINO;
        JTetrimino tetrimino = new JTetrimino();
        boolean[][] area = {
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true}
        };
        tetrimino.rotate(true, area);
        tetrimino.rotate(true, area);
        tetrimino.rotate(true, area);

        Mino[][] minos = tetrimino.getMinos();
        Mino[][] expectedMinos = JShape.shapes.get(Orientation.WEST);
        assertArrayEquals(expectedMinos, minos, "tetrimino shape");
      }

      @Test
      public void WestToNorth() {
//        Mino mino = Mino.J_MINO;
        JTetrimino tetrimino = new JTetrimino();
        boolean[][] area = {
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true}
        };
        tetrimino.rotate(true, area);
        tetrimino.rotate(true, area);
        tetrimino.rotate(true, area);
        tetrimino.rotate(true, area);

        Mino[][] minos = tetrimino.getMinos();
        Mino[][] expectedMinos = JShape.shapes.get(Orientation.NORTH);
        assertArrayEquals(expectedMinos, minos, "tetrimino shape");
      }
    }
  }
}