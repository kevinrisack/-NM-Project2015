import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import com.onformative.leap.*; 
import com.leapmotion.leap.*; 
import com.leapmotion.leap.Gesture.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Slotmachine extends PApplet {





LeapMotionP5 leap;
PImage imgBackground, imgSlotMachine, imgSlotmachineLeverDown,imgCredit;;
PImage[] imgSlot1=new PImage[4];

PImage[][] slotColumn1={imgSlot1,imgSlot1,imgSlot1};
PImage[][] slotColumn2={imgSlot1,imgSlot1,imgSlot1};
PImage[][] slotColumn3={imgSlot1,imgSlot1,imgSlot1};
int leverPulled=0;
int firstTime=0;
int credit=100;

boolean getrokken = false, timer = false;
ArrayList<PVector> handPositieLijst;
ArrayList<PVector>handPositieLijst2;
int listSize = 5, startTime, totalTime = 500;

String gameStatus = "INIT";

int lastSlotStopped=0;
boolean hitcheat = false;


int pos1_1 = 0;
int pos1_2=1;
int pos1_3=2;

int pos2_1=2;
int pos2_2=3;
int pos2_3=1;

int pos3_1=0;
int pos3_2=2;
int pos3_3=1;
int slot1=0,slot2=0,slot3=0;



public void setup()
{
  size(1772/2,1417/2);
  
  frameRate(6);
  imgSlot1[0] = loadImage("images/nmct.png");
  imgSlot1[1]=loadImage("images/dae.png");
  imgSlot1[2]=loadImage("images/devine.png");
  imgSlot1[3]=loadImage("images/howest.png");
  imgCredit=loadImage("images/bitcoin.png");

  leap = new LeapMotionP5(this);
  handPositieLijst = new ArrayList<PVector>();
  handPositieLijst2 = new ArrayList<PVector>();

  imgSlotMachine = loadImage("images/Slotmachine.png");
  imgSlotmachineLeverDown = loadImage("images/Slotmachine-leverDown.png");


  /*slotColumn1[0]=imgSlot1;
  slotColumn1[1]=imgSlot1;
  slotColumn1[2]=imgSlot1;

  slotColumn2[0]=imgSlot1;
  slotColumn2[1]=imgSlot1;
  slotColumn2[2]=imgSlot1;

  slotColumn2[0]=imgSlot1;
  slotColumn2[1]=imgSlot1;
  slotColumn2[2]=imgSlot1;*/
  

}

public void draw()
{
  
  if(gameStatus == "INIT")
  {
     background(50);
     frameRate(12);
     image(imgSlotMachine,0,0,1772/2,1417/2);
      image(imgCredit,width-150,25,30,30);
      textSize(16);
      text(""+credit,width-100,50);

    image(slotColumn1[0][pos1_1], 185+35, 192+97.5f,70,97.5f);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5f,70,97.5f);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5f+97.5f,70,97.5f);

     image(slotColumn2[0][pos2_1],185+210, 192+97.5f,70,97.5f );
     image(slotColumn2[1][pos2_2], 185+210, 290+97.5f,70,97.5f);
     image(slotColumn2[2][pos2_3], 185+210, 290+97.5f+97.5f,70,97.5f);

     image(slotColumn3[0][pos3_1],185+410, 192+97.5f,70,97.5f );
     image(slotColumn3[1][pos3_2], 185+410, 290+97.5f,70,97.5f);
     image(slotColumn3[2][pos3_3], 185+410, 290+97.5f+97.5f,70,97.5f);

     gameStatus = "START";
  }

  if(gameStatus == "START")
  {
     HendelControle();
     
}

if(gameStatus == "PULLED")
  {
    background(50);
    image(imgSlotmachineLeverDown, 0,0,1772/2,1417/2);
    timer = true;
    startTime = millis();
    
    handPositieLijst.clear();
    gameStatus = "BEZIG";
  }

if(gameStatus == "BEZIG") {
  if(timer == false)
  {
    background(50);
    image(imgSlotMachine,0,0,1772/2,1417/2);
  }
  else
  {
    background(50);
    image(imgSlotmachineLeverDown, 0,0,1772/2,1417/2);
    TijdControle();
  }

 
  keyPressed();
  
  checkSlot1();
  checkSlot2();
  checkSlot3();

  


}
if(gameStatus=="HIT"){
  println("gamestatus=HIT");

  HitControle();
  


}

}

public void TijdControle(){
    int passedTime = millis() - startTime;
    if (passedTime > totalTime)
      timer = false;
  }

public void HendelControle(){
    for(Hand hand : leap.getHandList()){
    println(hand);
    pushMatrix();

    //Position hand
    PVector handPosition = leap.getPosition(hand); println("x: " + handPosition.x + " y: " + handPosition.y + " z: " + handPosition.z);
    handPositieLijst.add(handPosition);

    int lijstSize = handPositieLijst.size();

    if(handPositieLijst.get(0).y+100 < handPositieLijst.get(lijstSize-1).y && handPositieLijst.get(0).z+100 < handPositieLijst.get(lijstSize-1).z)
      {
        gameStatus = "PULLED";
        handPositieLijst.clear();
      }

    if(handPositieLijst.size() >= listSize)
      handPositieLijst.remove(0);

    popMatrix();
  }
}

public void HitControle(){

    println("hitcontrole");
      for(Hand hand : leap.getHandList()){
    pushMatrix();

    //Position hand
    PVector handPosition = leap.getPosition(hand);
    handPositieLijst2.add(handPosition);

    int lijstSize = handPositieLijst2.size();

    if(handPositieLijst2.get(0).x-300 > handPositieLijst2.get(lijstSize-1).x)
    {
      println("Slot1 slipped");

      slot1=0;
      checkSlot1();
     slot1=1;
     checkSlot1();
     


        handPositieLijst2.clear();
     slot1=0;    
    slot2=0;
    slot3=0;
       gameStatus = "START";

    }
    else if(handPositieLijst2.get(0).y+300 < handPositieLijst2.get(lijstSize-1).y)
    {
      println("Slot2 slipped");
      slot2=0;
      checkSlot2();
      slot2=1;
      checkSlot2();
      
        handPositieLijst2.clear();
       
    slot1=0;
    slot2=0;
    slot3=0;
        gameStatus = "START";
         
         
    }

   else if(handPositieLijst2.get(0).x+300 < handPositieLijst2.get(lijstSize-1).x)
    {
      println("Slot3 slipped");
      slot3=0;
      checkSlot3();
      slot3=1;
      checkSlot3();
     
        handPositieLijst2.clear(); 
    slot1=0;
    slot2=0;
    slot3=0;
        gameStatus = "START";
        

    }

     if(handPositieLijst2.size() >= listSize)
      handPositieLijst2.remove(0);

    popMatrix();
    }
  
}



  public void keyPressed() {
  if (key == CODED && gameStatus == "BEZIG" && keyPressed==true) {
    if (keyCode == DOWN) {
      slot2=1;
      
      checkSlot2();
      CheckFinished();
    }
    if (keyCode == LEFT) {
      slot1=1;
      
      checkSlot1();
      CheckFinished();
    } 
   if (keyCode==RIGHT) {
    slot3=1;
    
    checkSlot3();
    CheckFinished();
  }
}
 
}

public void CheckFinished()
{
  if((slot1==1) && (slot2==1) && (slot3==1)){
   
   

    /*pos1_1=0;
    pos1_2=1;
    pos1_3=2;

    pos2_1=1;
    pos2_2=2;
    pos2_3=3;

    pos3_1=2;
    pos3_2=3;
    pos3_3=0;*/

    handPositieLijst.clear(); gameStatus="HIT";
    
  }

}

public void checkSlot1()
{
  if(slot1==0){
     image(slotColumn1[0][pos1_1], 185+35, 192+97.5f,70,97.5f);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5f,70,97.5f);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5f+97.5f,70,97.5f);
     pos1_1++; 
     pos1_2++;
     pos1_3++;

     if (pos1_1 > 3) {
    pos1_1 = 0;
  } 
  if(pos1_2 >3)
  {
    pos1_2=0;

  }
  if(pos1_3 >3){
    pos1_3=0;
  }

}
  else{
        image(slotColumn1[0][pos1_1], 185+35, 192+97.5f,70,97.5f);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5f,70,97.5f);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5f+97.5f,70,97.5f);
  }

}

public void checkSlot2()
{
  if(slot2==0){
     image(slotColumn2[0][pos2_1],185+210, 192+97.5f,70,97.5f );
     image(slotColumn2[1][pos2_2], 185+210, 290+97.5f,70,97.5f);
     image(slotColumn2[2][pos2_3], 185+210, 290+97.5f+97.5f,70,97.5f);
     pos2_1++; 
     pos2_2++;
     pos2_3++;

     if (pos2_1 > 3) {
    pos2_1 = 0;
  } 
  if(pos2_2 >3)
  {
    pos2_2=0;

  }
  if(pos2_3 >3){
    pos2_3=0;
  }

}
  else{
     image(slotColumn2[0][pos2_1],185+210, 192+97.5f,70,97.5f );
     image(slotColumn2[1][pos2_2], 185+210, 290+97.5f,70,97.5f);
     image(slotColumn2[2][pos2_3], 185+210, 290+97.5f+97.5f,70,97.5f);
  }
  

}

public void checkSlot3()
{
  if(slot3==0){
    image(slotColumn3[0][pos3_1],185+410, 192+97.5f,70,97.5f );
     image(slotColumn3[1][pos3_2], 185+410, 290+97.5f,70,97.5f);
     image(slotColumn3[2][pos3_3], 185+410, 290+97.5f+97.5f,70,97.5f);
     pos3_1++; 
     pos3_2++;
     pos3_3++;

     if (pos3_1 > 3) {
    pos3_1 = 0;
  } 
  if(pos3_2 >3)
  {
    pos3_2=0;

  }
  if(pos3_3 >3){
    pos3_3=0;
  }

}
  else{
    image(slotColumn3[0][pos3_1],185+410, 192+97.5f,70,97.5f );
     image(slotColumn3[1][pos3_2], 185+410, 290+97.5f,70,97.5f);
     image(slotColumn3[2][pos3_3], 185+410, 290+97.5f+97.5f,70,97.5f);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Slotmachine" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
