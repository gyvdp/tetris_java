package esi.acgt.atlj.client.controller;

import esi.acgt.atlj.client.connexionServer.Client;
import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.message.messageTypes.AskPiece;
import esi.acgt.atlj.model.ModelInterface;
import esi.acgt.atlj.model.board.Direction;
import java.util.Objects;
import javafx.scene.input.KeyCode;

/**
 * Controller of the Client application
 */
public class Controller {

  private final Client client;
  private final ModelInterface model;
  private final ViewInterface view;

  /**
   * Construtctor of Controller
   *
   * @param model Model of the Client
   * @param view  View of the Client
   */
  public Controller(ModelInterface model, ViewInterface view) {

    Objects.requireNonNull(model, "model can not be null");
    Objects.requireNonNull(view, "view can not be null");

    this.client = new Client(6969, "localhost");
    this.model = model;
    this.view = view;
    this.model.addPropertyChangeListener(this.view);
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
    System.exit(0);
  }

  /**
   * Forward inputs from the view to the model
   *
   * @param input keyboard input from the view
   */
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
        System.out.println("hard drop");
        break;
      case CONTROL:
        //this.model.rotateCounterClockwise();
        break;
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
    System.out.println("Connect to : " + ip + " , port : " + port + ".");
    try {
      this.client.connectToServer();
      this.client.sendToServer(new AskPiece());
      this.view.displayBoard();
      this.model.updateTest();
      this.model.setPlayer1(username);
      this.view.show();
    } catch (Exception e) {
      this.view.displayError(e);
      this.view.displayConnexion();
    }
  }

}
