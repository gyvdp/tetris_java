package esi.acgt.atlj.client.controller;

import esi.acgt.atlj.client.connexionServer.Client;
import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.model.Message;
import esi.acgt.atlj.model.ModelInterface;
import java.io.IOException;
import java.util.Objects;
import javafx.scene.input.KeyCode;

public class Controller {

  private final Client client;
  private final ModelInterface model;
  private final ViewInterface view;

  public Controller(ModelInterface model, ViewInterface view) {

    Objects.requireNonNull(model, "model can not be null");
    Objects.requireNonNull(view, "view can not be null");

/*    if (model == null) {
      throw new IllegalArgumentException();
    }

    if (view == null) {
      throw new IllegalArgumentException("view can not be null");
    }
*/
    this.client = new Client(6969, "localhost");
    this.model = model;
    this.view = view;
    this.model.addPropertyChangeListener(this.view);
    this.model.updateTest();
  }

  public void start() {
    this.client.connectToServer();
    Message message = new Message("askPiece");
    try {
      this.client.sendToServer(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
    view.setController(this);
    view.displayConnexion();
    view.show();
  }

  public void keyBoardInput(KeyCode input) {
    switch (input) {
      case LEFT:
        //this.model.moveLeft();
        break;
      case RIGHT:
        //this.model.moveRight();
        break;
      case DOWN:
        //this.model.softDrop();
        break;
      case UP:
        //this.model.rotateClockwise();
        break;
      case SHIFT:
        //this.model.hold();
        break;
      case SPACE:
        //this.model.hardDrop();
        break;
      case CONTROL:
        //this.model.rotateCounterClockwise();
        break;
    }
  }

  public void connexion(String ip, int port, String username) {
    System.out.println("Connect to : " + ip + " , port : " + port + ".");
    try {
      // Bla bla bla test connection
      this.view.displayBoard();
      this.model.setPlayer1(username);
    } catch (Exception e) {
      this.view.displayError(e);
    }
  }

}
