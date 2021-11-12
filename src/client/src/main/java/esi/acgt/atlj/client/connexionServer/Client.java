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

import esi.acgt.atlj.message.Message;
import esi.acgt.atlj.message.MessageType;
import esi.acgt.atlj.message.messageTypes.SendPiece;

/**
 * Sets up a specialized client with overridden methods from AbstractClient on which application
 * will run on.
 *
 * @see esi.acgt.atlj.client.connexionServer.AbstractClient
 */
public class Client extends AbstractClient {

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
    System.out.println(information);
    if (information instanceof SendPiece) {
      System.out.println(((SendPiece) information).getTetrimino());
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
    System.out.println("Connection to server has been suspended");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void connexionException(Exception e) {
    e.printStackTrace();
  }
}