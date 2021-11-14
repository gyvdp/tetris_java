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
    //this.view.displayConnexion();
    //this.view.show();
  }

  /**
   * Forward inputs from the view to the model
   *
   * @param input keyboard input from the view
   */
  public void keyBoardInput(KeyCode input) {
    if (this.model.getStatus() != GameStatus.NOT_STARTED) {
      switch (input) {
        case LEFT -> this.model.move(Direction.LEFT);
        case RIGHT -> this.model.move(Direction.RIGHT);
        case DOWN -> this.model.softDrop();
        case SHIFT -> this.model.hold();
        case SPACE -> this.model.hardDrop();
        case CONTROL -> this.model.rotate(true);
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
