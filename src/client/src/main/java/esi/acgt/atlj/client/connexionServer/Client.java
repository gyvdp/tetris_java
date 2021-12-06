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

import esi.acgt.atlj.message.PlayerAction;
import esi.acgt.atlj.message.PlayerStatus;
import esi.acgt.atlj.message.messageTypes.AddTetrimino;
import esi.acgt.atlj.message.messageTypes.AskPiece;
import esi.acgt.atlj.message.messageTypes.LockedTetrimino;
import esi.acgt.atlj.message.messageTypes.PlayerState;
import esi.acgt.atlj.message.messageTypes.RemoveLine;
import esi.acgt.atlj.message.messageTypes.SendAction;
import esi.acgt.atlj.message.messageTypes.SendAllStatistics;
import esi.acgt.atlj.message.messageTypes.SendGameStats;
import esi.acgt.atlj.message.messageTypes.SendHighScore;
import esi.acgt.atlj.message.messageTypes.SendName;
import esi.acgt.atlj.message.messageTypes.SendPiece;
import esi.acgt.atlj.message.messageTypes.SendScore;
import esi.acgt.atlj.message.messageTypes.SetHold;
import esi.acgt.atlj.message.messageTypes.UpdateNextPieceOther;
import esi.acgt.atlj.model.game.GameStat;
import esi.acgt.atlj.model.game.GameStatInterface;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * Sets up a specialized client with overridden methods from AbstractClient on which application
 * will run on.
 *
 * @see esi.acgt.atlj.client.connexionServer.AbstractClient
 */
public class Client extends AbstractClient implements ClientInterface {

  private PropertyChangeSupport pcs;

  /**
   * Lambda to run when players are ready
   */
  Runnable playerReady;
  /**
   * Lambda to run when other player has lost
   */
  Runnable otherPlayerLost;
  /**
   * Lambda to run when other player has disconnected
   */
  Runnable playerDisconnected;
  /**
   * Lambda to run when a locked tetrimino has been sent.
   */
  Consumer<TetriminoInterface> locked;
  /**
   * Method used when sendPiece message comes from server.
   */
  private Consumer<Mino> newMino;
  /**
   * Method used when a name is received
   */
  private Consumer<String> receiveName;
  /**
   * Method used when setting hold of other player.
   */
  private Consumer<Mino> hold;
  /**
   * Method used when removeLine message comes from server.
   */
  private Consumer<List<Integer>> removeLine;
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

  /**
   * Method used when highscore is received from server.
   */
  private Consumer<HashMap<String, Integer>> setHighScoreReceivedFromServer;

  /**
   * Method used when all statistics are received from server.
   */
  private BiConsumer<HashMap<String, Integer>, HashMap<String, Integer>> setStatisticsReceivedFromServer;

  /**
   * Constructor of a client.
   *
   * @param port Port client must look for.
   * @param host Host client must connect to.
   */
  public Client(int port, String host) {
    super(port, host);
    this.pcs = new PropertyChangeSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleServerMessage(Object information) {
    if (information instanceof SendPiece message) { //When next tetrimino is sent from server
      newMino.accept(message.getMino());
    } else if (information instanceof RemoveLine message) { //When remove line is send from server
      removeLine.accept(message.getLine());
    } else if (information instanceof AddTetrimino message) { //When add tetrimino is sent from server
      addTetrimino.accept(message.getTetrimino());
    } else if (information instanceof SendScore message) { // When send score is sent from server
      sendScore.accept(message.getScore());
    } else if (information instanceof
        UpdateNextPieceOther message) {//When update next tetrimino is sent from server
      updateNextTetriminoOtherPlayer.accept(message.getPiece());
    } else if (information instanceof PlayerState message) {
      if (message.getPlayerState().equals
          (PlayerStatus.READY)) { // When player state ready is sent from server
        playerReady.run();
      } else if (message.getPlayerState().equals
          (PlayerStatus.LOST)) { // When player state lost in sent from server
        otherPlayerLost.run();
      } else if (message.getPlayerState().equals
          (PlayerStatus.DISCONNECTED)) { // When player state disconnected is sent from server
        playerDisconnected.run();
      }
    } else if (information instanceof SendName message) { // When send name is sent from server
      receiveName.accept(message.getUsername());
    } else if (information instanceof SetHold message) { // When hold tetrimino is sent from server
      hold.accept(message.getHold());
    } else if (information instanceof LockedTetrimino message) { //When locked tetrimino has been send from server.
      locked.accept(message.getTetrimino());
    } else if (information instanceof SendHighScore message) {
      setHighScoreReceivedFromServer.accept(message.getHighScore());
    } else if (information instanceof SendAllStatistics message) {
      setStatisticsReceivedFromServer.accept(message.getGame_history(),
          message.getTetrimino_history());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closeConnectionToServer() {
    super.closeConnectionToServer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectReceiveUserName(Consumer<String> receiveName) {
    this.receiveName = receiveName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendNameToServer(String name) {
    try {
      sendToServer(new SendName(name));
    } catch (IOException e) {
      System.err.println("Sorry cannot send name");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void notifyLoss() {
    try {
      sendToServer(new PlayerState(PlayerStatus.LOST));
    } catch (IOException e) {
      System.err.println("Sorry cannot send loss");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  public void connectRemoveLine(Consumer<List<Integer>> removeLine) {
    this.removeLine = removeLine;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendTetriminoToOtherPlayer(TetriminoInterface tetriminoInterface) {
    try {
      sendToServer(new AddTetrimino(tetriminoInterface));
    } catch (IOException e) {
      System.err.println("Cannot send tetrimino to server");
    }
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
    System.err.println("Connection to server has been suspended");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void connexionException(Exception e) {

  }

  @Override
  public void sendAllGameStats(GameStatInterface gameStats) {
    try {
      sendToServer(new SendGameStats(gameStats));
    } catch (IOException e) {
      System.err.println("Cannot send game stats to server");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connect() throws ConnectException {
    connectToServer();
  }

  @Override
  public void askAllStatistics() {
    try {
      sendToServer(new SendAllStatistics());
    } catch (IOException e) {
      System.err.println("Cannot send game stats to server");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requestNextMino() {
    try {
      sendToServer(new AskPiece());
    } catch (IOException e) {
      System.err.println("cannot ask piece to server");
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param a Action to send.
   */
  @Override
  public void sendAction(PlayerAction a) {
    try {
      SendAction action = new SendAction();
      action.setAction(a);
      sendToServer(action);
    } catch (IOException e) {
      System.err.println("Cannot send action to server");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendHoldMino(Mino m) {
    try {
      sendToServer(new SetHold(m));
    } catch (IOException e) {
      System.err.println("Cannot send hold piece");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectHold(Consumer<Mino> hold) {
    this.hold = hold;
  }

  public void connectStatistics(
      BiConsumer<HashMap<String, Integer>, HashMap<String, Integer>> setStatisticsReceivedFromServer) {
    this.setStatisticsReceivedFromServer = setStatisticsReceivedFromServer;
  }

  public void connectHighScore(Consumer<HashMap<String, Integer>> setHighScoreReceivedFromServer) {
    this.setHighScoreReceivedFromServer = setHighScoreReceivedFromServer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectPlayerReady(Runnable playerReady) {
    this.playerReady = playerReady;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectOtherPlayerLost(Runnable otherPlayerLost) {
    this.otherPlayerLost = otherPlayerLost;
  }

  @Override
  public void lockTetrimino(TetriminoInterface m) {
    try {
      sendToServer(new LockedTetrimino(m));
    } catch (IOException e) {
      System.err.println("Cannot send name to server");
    }
  }

  @Override
  public void connectlockTetrimino(Consumer<TetriminoInterface> tetriminoInterfaceConsumer) {
    this.locked = tetriminoInterfaceConsumer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectPlayerDisconnected(Runnable playerDisconnected) {
    this.playerDisconnected = playerDisconnected;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendScore(int score) {
    try {
      sendToServer(new SendScore(score));
    } catch (IOException e) {
      System.err.println("Cannot send score to server");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeLine(List<Integer> lines) {
    try {
      sendToServer(new RemoveLine(lines));
    } catch (IOException e) {
      System.err.println("Cannot send line to remove");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListenerToClient(PropertyChangeListener propertyChangeListener) {
    if (this.pcs.getPropertyChangeListeners().length == 1) {
      this.pcs.removePropertyChangeListener(this.pcs.getPropertyChangeListeners()[0]);
    }
    this.pcs.addPropertyChangeListener(propertyChangeListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fireDataToMenu(HashMap<String, Integer> gameStats,
      HashMap<String, Integer> tetriminoStats) {
    int won = gameStats.getOrDefault("WON", 0);
    int loses = gameStats.getOrDefault("LOST", 0);
    this.pcs.firePropertyChange("WON", null, won);
    this.pcs.firePropertyChange("LOST", null, loses);
    this.pcs.firePropertyChange("SCORE", null, gameStats.getOrDefault("SCORE", 0));
    this.pcs.firePropertyChange("BURN", null, tetriminoStats.getOrDefault("BURN", 0));
    this.pcs.firePropertyChange("HARD", null, tetriminoStats.getOrDefault("HARD", 0));
    if (won == 0 && loses == 0) {
      this.pcs.firePropertyChange("PERCENT", null, -1.0);
    } else {
      if (loses == 0) {
        this.pcs.firePropertyChange("PERCENT", null, (double) 100);
      }
      this.pcs.firePropertyChange("PERCENT", null, ((double) (won / loses)) * 100);
    }
  }
}