package esi.acgt.atlj.client.view;

import javafx.stage.Stage;

public class View implements ViewInterface {

  private final Stage primaryStage;

  public View(Stage stage) {
    this.primaryStage = stage;
    this.primaryStage.setTitle("Ceci n'est pas un client");
  }


  @Override
  public void show() {
    this.primaryStage.show();
  }

}
