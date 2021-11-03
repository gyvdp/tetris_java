package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;
import esi.acgt.atlj.client.utils.Observer;
import javafx.stage.Stage;

public interface ViewInterface extends Observer {

  void show();

  void setAction(Controller controller);
}
