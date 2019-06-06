package models;

import java.util.Random;

public class Yut {


  private boolean status;

  Yut(){
    status = true;
  }

  public boolean getStatus(){
    return status;
  }

  public boolean throwYut(){
    Random generate = new Random();
    status = generate.nextBoolean();
    return status;
  }
}
