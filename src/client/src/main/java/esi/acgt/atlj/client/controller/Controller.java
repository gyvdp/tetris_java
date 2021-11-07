package esi.acgt.atlj.client.controller;

import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.model.ModelInterface;
import javafx.scene.input.KeyCode;

public class Controller {

  private final ModelInterface model;
  private final ViewInterface view;

  public Controller(ModelInterface model, ViewInterface view) {
    if (model == null) {
      throw new IllegalArgumentException("model can not be null");
    }

    if (view == null) {
      throw new IllegalArgumentException("view can not be null");
    }

    this.model = model;
    this.view = view;
  }

  public void start() {
    view.setController(this);
    view.displayConnexion();
    view.show();
  }

  public void keyBoardInput(KeyCode input) {
    switch (input) {
      case LEFT:
        this.model.moveLeft();
        break;
      case RIGHT:
        this.model.moveRight();
        break;
      case DOWN:
        this.model.softDrop();
        break;
      case UP:
        this.model.rotateClockwise();
        break;
      case SHIFT:
        this.model.hold();
        break;
      case SPACE:
        this.model.hardDrop();
        break;
      case CONTROL:
        this.model.rotateCounterClockwise();
        break;
    }
  }

  public void connexion(String ip, int port, String username) {
    System.out.println("Connect to : " + ip + " , port : " + port + ".");
    System.out.println("Register : " + username);
    try {
      // Bla bla bla test connection
      this.view.displayBoard();
    } catch (Exception e) {
      this.view.displayError(e);
    }
  }
}
