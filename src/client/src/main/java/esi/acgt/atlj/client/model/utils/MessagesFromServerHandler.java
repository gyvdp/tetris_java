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
import esi.acgt.atlj.model.game.GameInterface;
import esi.acgt.atlj.model.game.GameStat;
import esi.acgt.atlj.model.game.ManagedGame;
import esi.acgt.atlj.model.game.UnmanagedGame;
import esi.acgt.atlj.model.tetrimino.Mino;
import esi.acgt.atlj.model.tetrimino.Tetrimino;
import esi.acgt.atlj.model.tetrimino.TetriminoInterface;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utils to handle messages from server.
 */
public class MessagesFromServerHandler {

  /**
   * Current playing player.
   */
  private ManagedGame player;

  /**
   * Other player that is playing.
   */
  private UnmanagedGame otherPlayer;

  private List<GameInterface> players;

  /**
   * Constructor for message from server.
   *
   * @param otherPlayer Other player that is playing
   * @param player      Current playing player.
   * @param client      Client that will receive message from server.
   */
  public MessagesFromServerHandler(UnmanagedGame otherPlayer, ManagedGame player,
      ClientInterface client, List<GameInterface> players) {
    this.otherPlayer = otherPlayer;
    this.client = client;
    this.player = player;
    this.players = players;
    connectLambdaClient();
  }


  /**
   * Updates players next mino.
   */
  Consumer<Mino> nextMinoSentFromServer = (Mino nextMino) ->
      player.setNextTetrimino(Tetrimino.createTetrimino(nextMino));

  /**
   * Sets the name of the other player.
   */
  Consumer<String> nameSentFromServer = (String name) -> otherPlayer.setUsername(name);

  /**
   * Removes lines from other player.
   */
  Consumer<List<Integer>> removeLineSentFromServer = (List<Integer> line) ->
  {
    otherPlayer.removeLines(line);
    otherPlayer.setBurns(line.size());
  };

  /**
   * Lockes the other players tetrimino.
   */
  Consumer<TetriminoInterface> lockedTetriminoSentFromServer = (TetriminoInterface tetriminoInterface) ->
      otherPlayer.placeTetrimino(tetriminoInterface);

  /**
   * Updates the other players falling tetrimino.
   */
  Consumer<TetriminoInterface> fallingTetriminoSentFromServer = (TetriminoInterface tetriminoInterface) ->
      otherPlayer.setActualTetrimino(tetriminoInterface);

  /**
   * Updates the hold tetrimino from the other player sent from the server.
   */
  Consumer<Mino> holdReceivedFromServer = (Mino mino) ->
      otherPlayer.setHold(mino);

  /**
   * Updates the next tetrimino from the other player sent from the server.
   */
  Consumer<Mino> updateNextTetriminoOtherPlayer = (Mino m) ->
      otherPlayer.setNextTetrimino(Tetrimino.createTetrimino(m));

  /**
   * Starts a game when server sent green light.
   */
  Runnable playerReady = this::start;

  /**
   * Sets other player to lost.
   */
  Runnable playerLostSentFromServer = () ->
      this.otherPlayer.playerStatus("LOCK OUT", 0.9);

  /**
   * Sets other player to disconnected.
   */
  Runnable playerDisconnectedSentFromServer = () ->
      this.otherPlayer.playerStatus("Disconnected", 1);
  /**
   * Client that will received message from server.
   */
  private ClientInterface client;
  /**
   * Updates the score from the other player.
   */

  Consumer<Integer> scoreReceivedFromServer = (Integer score) ->
  {
    if (client != null) {
      otherPlayer.setScore(score);
    }
  };

  Consumer<HashMap<String, Integer>> setHighScoreReceivedFromServer = (HashMap<String, Integer> highScore) ->
  {
    if (client != null) {
      for (var player : players) {
        var highscore = highScore.get(player.getUsername());
        if (highscore != null) {
          ((GameStat) player.getStats()).setHighScore(highscore);
        }
      }
    }
  };

  Consumer<HashMap<String, Integer>> setStatisticsReceivedFromServer = (HashMap<String, Integer> statistics) ->
  {
    if (client != null) {
      // TODO
    }
  };


  /**
   * All necessary lambda connection with client
   */
  private void connectLambdaClient() {
    if (client != null) {
      client.connectNewMinoFromServer(this.nextMinoSentFromServer);
      client.connectRemoveLine(this.removeLineSentFromServer);
      client.connectAddTetrimino(this.fallingTetriminoSentFromServer);
      client.connectSendScore(this.scoreReceivedFromServer);
      client.connectUpdateNextTetriminoOtherPlayer(this.updateNextTetriminoOtherPlayer);
      client.connectPlayerReady(this.playerReady);
      client.connectOtherPlayerLost(this.playerLostSentFromServer);
      client.connectPlayerDisconnected(this.playerDisconnectedSentFromServer);
      client.connectReceiveUserName(this.nameSentFromServer);
      client.connectHold(this.holdReceivedFromServer);
      client.connectlockTetrimino(this.lockedTetriminoSentFromServer);
      client.connectStatistics(this.setStatisticsReceivedFromServer);
      client.connectHighScore(this.setHighScoreReceivedFromServer);
    }
  }

  /**
   * Starts a new game.
   */
  public void start() {
    this.player.start();
    this.otherPlayer.playerStatus("", 0);
  }

}
