package models;

import java.util.Random;

public class Yut {

  private boolean status;
  Random generate = new Random();

  Yut(){
    status = true;
  }

  public boolean getStatus(){
    return status;
  }

  public boolean throwYut(){
    status = generate.nextBoolean();
    return status;
  }
}
