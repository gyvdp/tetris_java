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

package esi.acgt.atlj.server;


import java.util.HashMap;

public class Server extends AbstractServer {

  private int clientId;

  private HashMap<Integer, CustomClientThread> members;

  public Server(int port) {
    super(port);

    clientId=0;
    members = new HashMap<Integer, CustomClientThread>();
    this.startServer();

  }

  @Override
  protected void exceptionHook(Exception e) {
    super.exceptionHook(e);
  }

  @Override
  protected void handleMessageClient(Object msg, CustomClientThread client) {

  }

  @Override
  protected void serverStopped() {
    super.serverStopped();
  }

  private int getNextId(){
    return this.clientId++;
  }

  @Override
  protected void clientConnected(CustomClientThread client) {
    super.clientConnected(client);
    members.put(getNextId(), client);
  }

  @Override
  protected void clientDiconnected(CustomClientThread client) {
    super.clientDiconnected(client);
  }

  /**
   * Sends a message to a specific client
   * @param information Message to send to client.
   * @param clientId Unique id of client.
   */
  void sentToClient(Object information, int clientId){

  }
}