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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class CustomClientThread extends Thread{

  private final Socket clientSocket;

  private AbstractServer server;

  private ObjectInputStream input;

  private ObjectOutputStream output;

  private boolean isActive;


  /**
   * Constructor for custom client thread.
   * @param clientSocket Socket of client.
   * @param server Server to which the client is connected.
   * @throws SocketException Thrown if function cannot instantiate input or output streams.
   */
  public CustomClientThread(Socket clientSocket, AbstractServer server) throws SocketException {
    this.clientSocket=clientSocket;
    clientSocket.setSoTimeout(0);
    this.server = server;
    try {
      input = new ObjectInputStream(clientSocket.getInputStream());
      output = new ObjectOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      System.out.println("Cannot create input or output stream");
      try {
        closeSocket();
      } catch (IOException ioException) {
        System.out.println("Cannot close the socket");
      }
    }
    isActive = true;
    start();
  }
  /**
   * Closes socket and all streams.
   * @throws IOException Thrown if function cannot close socket or streams.
   */
  private void closeSocket() throws IOException {
    if (clientSocket != null)
      clientSocket.close();
    if (output != null )
      output.close();
    if (input != null)
      input.close();
  }

  @Override
  public void run() {
    server.clientConnected(this);
    try {
      Object obj;
      while (isActive){
        try {
          sendMessage("Welcome");
          obj = input.readObject();
          if (isActive)
            server.receiveMessageFromClient(obj, this);
        } catch (ClassNotFoundException | RuntimeException ex){
        }
      }
    } catch (Exception e){
      if (isActive){
        try {
          close();
        } catch (Exception ex){
          System.out.println("Cannot close socket");
        }
      }
    }
  }

  /**
   * Sends a message to the client.
   */
  public void sendMessage(Object information) throws IOException{
    if (clientSocket == null || output == null)
      System.out.println("Sorry stream does not exist");
    output.writeObject(information);
    output.flush();
  }

  /**
   * Closes the client socket.
   */
  final public void close() throws  IOException{
    isActive = false;
    closeSocket();
  }

}