package esi.acgt.atlj.client.controller;

import esi.acgt.atlj.client.connexionServer.Client;
import esi.acgt.atlj.client.connexionServer.ClientInterface;
import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.message.messageTypes.AskPiece;
import esi.acgt.atlj.model.ClientModel;
import esi.acgt.atlj.model.board.Direction;
import java.util.Objects;
import javafx.scene.input.KeyCode;

public class Controller {

  private final ClientModel model;
  private final ClientInterface client;
  private final ViewInterface view;

  public Controller(ClientModel model, ViewInterface view) {

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
    this.model.addPropertyChangeListener(this.client);
  }

  public void start() {
    view.setController(this);
    view.displayConnexion();
    view.show();
  }

  public void disconnect() {
    System.exit(0);
  }

  public void keyBoardInput(KeyCode input) {
    switch (input) {
      case LEFT:
        this.model.move(Direction.LEFT);
        System.out.println("move left");
        break;
      case RIGHT:
        this.model.move(Direction.RIGHT);
        System.out.println("move right");
        break;
      case DOWN:
        this.model.softDrop();
        System.out.println("move down");
        break;
      case UP:
        this.model.move(Direction.UP);
        System.out.println("move up");
        break;
      case SHIFT:
        this.model.hold();
        break;
      case SPACE:
        this.model.hardDrop();
        break;
      case CONTROL:
        this.model.rotate(true);
        break;
    }
  }

  public void connexion(String ip, int port, String username) {
    System.out.println("Connect to : " + ip + " , port : " + port + ".");
    try {
      this.client.connect();
      this.view.displayBoard();
      this.model.initManagedBoard(username);
      this.model.start();
      this.model.addPropertyChangeListener(this.view);
      this.view.show();
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion();
    }
  }

}
