package esi.acgt.atlj.client.controller;

import esi.acgt.atlj.client.model.ClientModel;
import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.model.board.Direction;
import java.util.Objects;
import javafx.scene.input.KeyCode;

public class Controller {

  private final ClientModel model;

  private final ViewInterface view;

  public Controller(ClientModel model, ViewInterface view) {
    Objects.requireNonNull(model, "model can not be null");
    Objects.requireNonNull(view, "view can not be null");
    this.model = model;
    this.view = view;
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
      case LEFT -> this.model.move(Direction.LEFT);
      case RIGHT -> this.model.move(Direction.RIGHT);
      case DOWN -> this.model.softDrop();
      case UP -> this.model.move(Direction.UP);
      case SHIFT -> this.model.hold();
      case SPACE -> this.model.hardDrop();
      case CONTROL -> this.model.rotate(true);
    }

  }

  public void connexion(String ip, int port, String username) {
    try {
      this.model.connect(port, ip);
      this.view.displayBoard();
      this.model.initManagedBoard(username);
      this.model.addPropertyChangeListener(this.view);
      this.model.start();
      this.view.show();
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion();
    }
  }

}
