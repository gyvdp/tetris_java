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

package esi.acgt.atlj.client.model.utils;

import esi.acgt.atlj.client.connexionServer.ClientInterface;
import esi.acgt.atlj.model.game.GameStat;
import esi.acgt.atlj.model.game.GameStatInterface;
import esi.acgt.atlj.model.game.ManagedGame;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Interaction from game via client to server.
 */
public class MessagesToServerHandler {

  /**
   * Player wishing to send a message to server.
   */
  private ManagedGame player;

  /**
   * Client sending player messages to server.
   */
  private ClientInterface client;
  /**
   * Request a next tetrimino to the server.
   */
  Runnable requestNextMinoToServer = () ->
  {
    if (client != null) {
      this.client.requestNextMino();
    } else {
      var minos = Mino.values();
      player.setNextTetrimino(
          Tetrimino.createTetrimino(minos[(int) (Math.random() * (minos.length))]));
    }
  };
  /**
   * Sends falling tetrimino to server.
   */
  Consumer<TetriminoInterface> sendFallingTetriminoToServer = (TetriminoInterface tetriminoInterface) ->
  {
    if (client != null) {
      this.client.sendTetriminoToOtherPlayer(tetriminoInterface);
    }
  };
  /**
   * Sends current hold piece to server.
   */
  Consumer<Mino> sendHoldPieceToServer = (Mino m) ->
  {
    if (client != null) {
      this.client.sendHoldMino(m);
    }
  };
  /**
   * Sends score to server.
   */
  Consumer<Integer> sendScoreToServer = (Integer score) -> {
    if (client != null) {
      this.client.sendScore(score);
    }
  };
  /**
   * Sends destroyed lines to server.
   */
  Consumer<List<Integer>> sendDestroyedLinesToServer = (List<Integer> lineDestroyed) -> {
    if (client != null) {
      client.removeLine(lineDestroyed);
    }
  };
  /**
   * Sends to server that player lost.
   */
  Runnable sendLostStatusToServer = () ->
  {
    if (client != null) {
      this.client.notifyLoss();
    }
  };
  /**
   * Sends locked tetrimino to server.
   */
  Consumer<TetriminoInterface> sendLockedTetriminoToServer = (TetriminoInterface m) -> {
    if (client != null) {
      client.lockTetrimino(m);
    }
  };

  Consumer<GameStatInterface> sendAllGameStatistics = (GameStatInterface allGameStats) -> {
    if (client != null) {
      client.sendAllGameStats(allGameStats);
    }
  };

  Runnable askAllStatsToServer = () -> {
    if (client != null) {
      this.client.askAllStatistics();
    }
  };


  /**
   * Constructor for message to server.
   *
   * @param game   Game that has to send to server.
   * @param client Client that will send information to server.
   */
  public MessagesToServerHandler(ManagedGame game, ClientInterface client) {
    this.player = game;
    this.client = client;
    connectLambdaPlayer();
  }

  /**
   * All necessary connection with player
   */
  public void connectLambdaPlayer() {
    player.connectAskNewMino(requestNextMinoToServer);
    player.connectAddTetrimino(sendFallingTetriminoToServer);
    player.connectHoldMino(this.sendHoldPieceToServer);
    player.connectLineDestroyed(this.sendDestroyedLinesToServer);
    player.connectSendScoreServer(sendScoreToServer);
    player.connectLost(this.sendLostStatusToServer);
    player.connectTetriminoLock(this.sendLockedTetriminoToServer);
    player.connectSendGameStatistics(this.sendAllGameStatistics);
  }


}
