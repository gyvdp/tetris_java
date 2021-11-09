package esi.acgt.atlj.model;

import esi.acgt.atlj.model.board.BoardInterface;

public class Model implements ModelInterface {

  @Override
  public String[] getUsernames() {
    return new String[0];
  }

  @Override
  public BoardInterface getBoard(String username) {
    return null;
  }
}
