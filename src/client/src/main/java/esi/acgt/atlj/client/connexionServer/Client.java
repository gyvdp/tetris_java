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

import esi.acgt.atlj.message.messageTypes.AddTetrimino;
import esi.acgt.atlj.message.messageTypes.AskPiece;
import esi.acgt.atlj.message.messageTypes.ReadyState;
import esi.acgt.atlj.message.messageTypes.RemoveLine;
import esi.acgt.atlj.message.messageTypes.SendPiece;
import esi.acgt.atlj.message.messageTypes.SendScore;
import esi.acgt.atlj.message.messageTypes.UpdatePieceUnmanagedBoard;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.util.function.Consumer;


/**
 * Sets up a specialized client with overridden methods from AbstractClient on which application
 * will run on.
 *
 * @see esi.acgt.atlj.client.connexionServer.AbstractClient
 */
public class Client extends AbstractClient implements ClientInterface {

  /**
   * Method used when sendPiece message comes from server.
   */
  private Consumer<Mino> newMino;
  /**
   * Method used when removeLine message comes from server.
   */
  private Consumer<Integer> removeLine;
  /**
   * Method used when addTetrimino message comes from server.
   */
  private Consumer<TetriminoInterface> addTetrimino;
  /**
   * Method used when sendScore message comes from server.
   */
  private Consumer<Integer> sendScore;

  /**
   * Method used when updating next mino of other player
   */
  private Consumer<Mino> updateNextTetriminoOtherPlayer;

  Runnable playerReady;

  /**
   * Constructor of a client.
   *
   * @param port Port client must look for.
   * @param host Host client must connect to.
   */
  public Client(int port, String host) {
    super(port, host);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleServerMessage(Object information) {
    if (information instanceof SendPiece) {
      newMino.accept(((SendPiece) information).getMino());
    } else if (information instanceof RemoveLine) {
      removeLine.accept(((RemoveLine) information).getLine());
    } else if (information instanceof AddTetrimino) {
      addTetrimino.accept(((AddTetrimino) information).getTetrimino());
    } else if (information instanceof SendScore) {
      sendScore.accept(((SendScore) information).getScore());
    } else if (information instanceof UpdatePieceUnmanagedBoard) {
      updateNextTetriminoOtherPlayer.accept(((UpdatePieceUnmanagedBoard) information).getPiece());
    } else if (information instanceof ReadyState) {
      playerReady.run();
    }
  }

  public void connectUpdateNextTetriminoOtherPlayer(Consumer<Mino> updateNextTetriminoOtherPlayer) {
    this.updateNextTetriminoOtherPlayer = updateNextTetriminoOtherPlayer;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void connectNewMinoFromServer(Consumer<Mino> newMino) {
    this.newMino = newMino;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectAddTetrimino(Consumer<TetriminoInterface> addTetrimino) {
    this.addTetrimino = addTetrimino;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectSendScore(Consumer<Integer> sendScore) {
    this.sendScore = sendScore;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectRemoveLine(Consumer<Integer> removeLine) {
    this.removeLine = removeLine;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void connectionEstablished() {
    System.out.println("Connection to server is established");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void closeConnection() {
    System.out.println("Connection to server has been suspended");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void connexionException(Exception e) {
    e.printStackTrace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connect() throws ConnectException {
    connectToServer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requestNextMino() {
    try {
      sendToServer(new AskPiece());
    } catch (IOException e) {
      //pop up to gui
    }
  }

  @Override
  public void connectPlayerReady(Runnable playerReady) {
    this.playerReady = playerReady;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void syncBoardWithServer() {
    try {
      sendToServer(new AskPiece());
    } catch (IOException e) {
      //pop up to gui
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendName(String name) {
    try {
      sendToServer(new AskPiece());
    } catch (IOException e) {
      //pop up to gui
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void sendScore(int score) {
    try {
      sendToServer(new SendScore(score));
    } catch (IOException e) {
      //pop up to gui
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeLine(int line) {
    try {
      sendToServer(new RemoveLine(line));
    } catch (IOException e) {
      //pop up to gui
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {

  }
}