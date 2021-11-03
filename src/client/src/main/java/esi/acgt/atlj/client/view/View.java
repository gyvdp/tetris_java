package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class View implements ViewInterface {

  private final Stage primaryStage;
  private BothPlayerView scene;

  public View(Stage stage) {
    this.primaryStage = stage;
    this.primaryStage.setTitle("Tetris");
    this.scene = new BothPlayerView(stage);
  }


  @Override
  public void show() {
    this.primaryStage.setResizable(false);
    this.primaryStage.show();
  }

  @Override
  public void setAction(Controller controller) {
    this.primaryStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (key) -> {
      controller.keyBoardInput(key.getCode());
    });
  }

  @Override
  public void update(Object o) {
    this.scene.update(o);
  }
}
