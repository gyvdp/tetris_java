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

package esi.acgt.atlj.client.model;

import esi.acgt.atlj.client.model.game.MultiplayerGame;
import esi.acgt.atlj.message.PlayerAction;
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
import esi.acgt.atlj.model.Game;
import esi.acgt.atlj.model.player.Direction;
import esi.acgt.atlj.model.player.PlayerStatInterface;
import esi.acgt.atlj.model.player.PlayerStatus;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import javafx.scene.input.KeyCode;


/**
 * Sets up a specialized client with overridden methods from AbstractClient on which application
 * will run on.
 *
 * @see AbstractClient
 */
public class Client extends AbstractClient implements ClientInterface, PropertyChangeListener {

  private ClientStatus status;

  private Game game;

  /**
   * Constructor of a client.
   *
   * @param port Port client must look for.
   * @param host Host client must connect to.
   */
  public Client(int port, String host) {
    super(port, host);
    this.status = ClientStatus.CONNECTED;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleServerMessage(Object information) {
    var player = ((MultiplayerGame) game).getPlayer();
    var otherPlayer = ((MultiplayerGame) game).getOtherPlayer();

    if (information instanceof SendPiece message) { //When next tetrimino is sent from server
      player.setNextTetrimino(message.getMino());
    } else if (information instanceof RemoveLine message) { //When remove line is send from server
      otherPlayer.removeLines(message.getLine());
    } else if (information instanceof AddTetrimino message) { //When add tetrimino is sent from server
      otherPlayer.setActualTetrimino(message.getTetrimino());
    } else if (information instanceof SendScore message) { // When send score is sent from server
      otherPlayer.setScore(message.getScore());
    } else if (information instanceof
        UpdateNextPieceOther message) {//When update next tetrimino is sent from server
      otherPlayer.setNextTetrimino(message.getPiece());
    } else if (information instanceof PlayerState message) {
      if (message.getPlayerState()
          .equals(
              esi.acgt.atlj.message.PlayerStatus.READY)) { // When player state ready is sent from server
        requestNextMino();
        ((MultiplayerGame) game).start();
      } else if (message.getPlayerState().equals
          (esi.acgt.atlj.message.PlayerStatus.LOST)) { // When player state lost in sent from server
        // todo :otherPlayerLost.run();
      } else if (message.getPlayerState().equals
          (esi.acgt.atlj.message.PlayerStatus.DISCONNECTED)) { // When player state disconnected is sent from server
        // todo :playerDisconnected.run();
      }
    } else if (information instanceof SendName message) { // When send name is sent from server
      otherPlayer.setUsername(message.getUsername());
    } else if (information instanceof SetHold message) { // When hold tetrimino is sent from server
      otherPlayer.setHold(message.getHold());
    } else if (information instanceof LockedTetrimino message) { //When locked tetrimino has been send from server.
      otherPlayer.placeTetrimino(message.getTetrimino());
    } else if (information instanceof SendHighScore message) {
      // todo :setHighScoreReceivedFromServer.accept(message.getHighScore());
    } else if (information instanceof SendAllStatistics message) {
      // todo :setStatisticsReceivedFromServer.accept(message.getGame_history(),
      //message.getTetrimino_history());
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
      sendToServer(new PlayerState(esi.acgt.atlj.message.PlayerStatus.LOST));
    } catch (IOException e) {
      System.err.println("Sorry cannot send loss");
    }
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
  public void sendAllGameStats(PlayerStatInterface gameStats) {
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

  @Override
  public void lockTetrimino(TetriminoInterface m) {
    try {
      sendToServer(new LockedTetrimino(m));
    } catch (IOException e) {
      System.err.println("Cannot send name to server");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendName(String name) {
    try {
      sendToServer(new SendName(name));
    } catch (IOException e) {
      System.err.println("Cannot send name to server");
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

  @Override
  public void startMultiplayerGame(String username) {
    this.game = new MultiplayerGame(username);
    ((MultiplayerGame) this.game).addPCLToPlayer(this);
    sendAction(PlayerAction.PLAY_ONLINE);
  }

  @Override
  public void addPCSToBoard(PropertyChangeListener[] listeners) {
    if (game != null) {
      game.addPropertyChangeListenerToBoards(listeners);
    }
  }

  @Override
  public Game getActualGame() {
    return game;
  }

  @Override
  public ClientStatus getStatus() {
    return this.status;
  }

  @Override
  public void keyBoardInput(KeyCode input) {
    if (game instanceof MultiplayerGame) {
      var multiplayerGame = (MultiplayerGame) game;

      if (multiplayerGame.getStatus() != PlayerStatus.LOCK_OUT) {
        switch (input) {
          case LEFT, NUMPAD4 -> multiplayerGame.move(Direction.LEFT);
          case RIGHT, NUMPAD6 -> multiplayerGame.move(Direction.RIGHT);
          case DOWN, NUMPAD2 -> multiplayerGame.softDrop();
          case UP, X, NUMPAD3, NUMPAD5 -> multiplayerGame.rotate(true);
          case SHIFT, C, NUMPAD0 -> multiplayerGame.hold();
          case SPACE, NUMPAD8 -> multiplayerGame.hardDrop();
          case CONTROL, NUMPAD1, NUMPAD9 -> multiplayerGame.rotate(false);
        }
      }
    }

  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case "hold" -> sendHoldMino((Mino) evt.getNewValue());
      case "ACTUAL" -> sendTetriminoToOtherPlayer((TetriminoInterface) evt.getNewValue());
      case "SCORE" -> sendScore((int) evt.getNewValue());
      case "PLACE_TETRIMINO" -> lockTetrimino((TetriminoInterface) evt.getNewValue());
      case "next" -> {
        if (evt.getNewValue() == null) {
          requestNextMino();
        }
      }
    }
  }
}