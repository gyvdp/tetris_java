package esi.acgt.atlj.model;

public class Model implements ModelInterface {

  @Override
  public void moveLeft() {
    System.out.println("Move Left");
  }

  @Override
  public void moveRight() {
    System.out.println("Move Right");
  }

  @Override
  public void softDrop() {
    System.out.println("Soft Drop");
  }

  @Override
  public void rotateClockwise() {
    System.out.println("rotate clockWise");
  }

  @Override
  public void hold() {
    System.out.println("hold");
  }

  @Override
  public void hardDrop() {
    System.out.println("Hard drop");
  }

  @Override
  public void rotateCounterClockwise() {
    System.out.println("Rotate counterClockwise");
  }
}
