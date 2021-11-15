package esi.acgt.atlj.client.controller;

import esi.acgt.atlj.client.model.ClientModel;
import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.model.game.Direction;
import esi.acgt.atlj.model.game.GameStatus;
import java.util.Objects;
import javafx.scene.input.KeyCode;

/**
 * Controller of the Client application
 */
public class Controller {

  private final ClientModel model;

  private final ViewInterface view;

  /**
   * Constructor of Controller
   *
   * @param model Model of the Client
   * @param view  View of the Client
   */
  public Controller(ClientModel model, ViewInterface view) {
    Objects.requireNonNull(model, "model can not be null");
    Objects.requireNonNull(view, "view can not be null");
    this.model = model;
    this.view = view;
  }


  /**
   * Launches the view
   */
  public void start() {
    view.setController(this);
    view.displayConnexion();
    view.show();
  }

  /**
   * End the Programme
   */
  public void disconnect() {
    model.closeConnection();
  }

  /**
   * Forward inputs from the view to the model
   *
   * @param input keyboard input from the view
   */
  public void keyBoardInput(KeyCode input) {
    if (this.model.getStatus() != GameStatus.NOT_STARTED
        && this.model.getStatus() != GameStatus.LOCK_OUT) {
      switch (input) {
        case LEFT, NUMPAD4 -> this.model.move(Direction.LEFT);
        case RIGHT, NUMPAD6 -> this.model.move(Direction.RIGHT);
        case DOWN, NUMPAD2 -> this.model.softDrop();
        case UP, X, NUMPAD3, NUMPAD5 -> this.model.rotate(true);
        case SHIFT, C, NUMPAD0 -> this.model.hold();
        case SPACE, NUMPAD8 -> this.model.hardDrop();
        case CONTROL, NUMPAD1, NUMPAD9 -> this.model.rotate(false);
      }
    }

  }

  /**
   * Connexion to the server and when it's done start the board view
   *
   * @param ip       ip of the server to connect to
   * @param port     port of the server to connect to
   * @param username username of the player
   */
  public void connexion(String ip, int port, String username) {
    try {
      this.model.initManagedBoard(username);
      this.view.displayBoard(username);
      this.model.connect(port, ip);
      this.model.addPropertyChangeListener(this.view.getListeners());
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion();
    }

    this.view.show();
  }

  public void solo(String username) {
    try {
      this.model.initManagedBoard(username);
      this.view.displayBoard(username);
      this.model.addPropertyChangeListener(this.view.getListeners());
      this.model.start();
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion();
    }
    this.view.show();
  }

}
