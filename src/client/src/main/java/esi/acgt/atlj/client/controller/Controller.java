package esi.acgt.atlj.client.controller;

import esi.acgt.atlj.client.model.ClientModel;
import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.model.board.BoardStatus;
import esi.acgt.atlj.model.board.Direction;
import java.util.Objects;
import javafx.scene.input.KeyCode;

/**
 * Controller of the Client application
 */
public class Controller {

  private final ClientModel model;

  private final ViewInterface view;

  /**
   * Construtctor of Controller
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
   * Launch the view
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
    System.exit(0);
  }

  /**
   * Forward inputs from the view to the model
   *
   * @param input keyboard input from the view
   */
  public void keyBoardInput(KeyCode input) {
    if (this.model.getStatus() != BoardStatus.NOT_STARTED) {
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
      this.model.connect(6969, "localhost");
      this.model.addPropertyChangeListener(this.view.getListeners());
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion();
    }
    this.view.show();
  }

}
