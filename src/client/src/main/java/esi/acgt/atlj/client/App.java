package esi.acgt.atlj.client;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.client.model.ClientModel;
import esi.acgt.atlj.client.view.View;
import esi.acgt.atlj.client.view.ViewInterface;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Client Application
 */
public class App extends Application {

  @Override
  public void start(Stage stage) {
    ClientModel model = new ClientModel();
    ViewInterface view = new View(stage);
    Controller controller = new Controller(model, view);
    controller.start();
  }

}
