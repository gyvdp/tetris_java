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

package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import java.beans.PropertyChangeListener;

public interface ViewInterface {

  /**
   * Show the primary stage in the center of the screen
   */
  void show();

  /**
   * Setter of controller so that View will be interact with the Controller
   *
   * @param controller Controller to interact with
   */
  void setController(Controller controller);

  /**
   * Show a pop up with the exception message inside
   *
   * @param e excption to display
   */
  void displayError(Exception e);

  /**
   * Create and display a Stage where you put information about the server
   */
  void displayConnexion();

  /**
   * Display the board of your tetris game and opponent one
   *
   * @param username username name of the player
   */
  void displayBoard(String username);

  /**
   * Getter of Player1 and 2 casted in PropertyListener
   *
   * @return Listerners
   */
  PropertyChangeListener[] getBoardListeners();

  void displayMenu(String username);
}
