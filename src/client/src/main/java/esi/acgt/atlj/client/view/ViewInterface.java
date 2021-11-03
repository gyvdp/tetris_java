package esi.acgt.atlj.client.view;

import esi.acgt.atlj.client.controller.Controller;

public interface ViewInterface {

  public void show();

  public void setController(Controller controller);

  public void displayError(Exception e);

  public void displayConnexion();

  public void displayBoard();
}
