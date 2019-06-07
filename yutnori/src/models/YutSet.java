package models;

public class YutSet {
  Yut[] yutSet;
  final int YUTSETSIZE = 4;

  public YutSet(){
    yutSet = new Yut[4];
    for(int i = 0; i < YUTSETSIZE; i++){
      yutSet[i] = new Yut();
    }
  }

  public int rollYut(){
    int cal = 0;
    for(int i = 0; i < YUTSETSIZE-1; i++){
      if(!yutSet[i].throwYut()){
        cal++;
      }
    }

    // Throw back do.
    if(!yutSet[YUTSETSIZE-1].throwYut()){
      if(cal == 0){
        return 0;
      }
      cal++;
    }


    return cal+1;
  }
}
