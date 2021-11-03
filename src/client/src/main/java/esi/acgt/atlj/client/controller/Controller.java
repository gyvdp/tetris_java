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
    view.setAction(this);
    view.show();
  }

  public void keyBoardInput(KeyCode input) {
    System.out.print("Action : " + input.toString() + ", ");
    //this.model.action(input);
  }
}
