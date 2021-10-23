package esi.acgt.atlj.client;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.client.view.View;
import esi.acgt.atlj.client.view.ViewInterface;
import esi.acgt.atlj.model.Model;
import esi.acgt.atlj.model.ModelInterface;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage stage) {
    ModelInterface model = new Model();
    ViewInterface view = new View(stage);
    Controller controller = new Controller(model, view);
    controller.start();
  }

}
