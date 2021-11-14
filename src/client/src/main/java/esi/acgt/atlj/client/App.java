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

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    ClientModel model = new ClientModel();
    ViewInterface view = new View();
    Controller controller = new Controller(model, view);
    controller.start();
    final Parameters params = getParameters();
    for (var param : params.getRaw()) {
      switch (param) {
        case "--localhost" -> controller.connexion("127.0.0.1", 6969, "Pacliclown");
        case "--client" -> controller.solo("Andrew");
      }
    }
  }

}
