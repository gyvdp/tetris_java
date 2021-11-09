package esi.acgt.atlj.model;

public class Model implements ModelInterface {

  @Override
  public String[] getUsernames() {
    return new String[0];
  }

  @Override
  public BoardInterface getManagedBoard(String username) {
    return null;
  }

  @Override
  public BoardInterface getUnmanagedBoard(String username) {
    return null;
  }
}
