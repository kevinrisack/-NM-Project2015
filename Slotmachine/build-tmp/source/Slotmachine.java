import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import com.onformative.leap.*; 
import com.leapmotion.leap.*; 
import com.leapmotion.leap.Gesture.*; 
import ddf.minim.*; 

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
Minim minim;
AudioPlayer winplayer;
AudioPlayer hendelplayer;
AudioInput input;
PImage imgBackground, imgSlotMachine, imgSlotmachineLeverDown,imgCredit,imgWin, imgLegende, imgx2, imgBet, imgPull, imgLastChance;
PImage[] imgSlot1=new PImage[4];
PImage[] imgSlot2=new PImage[4];
PImage[] imgSlot3=new PImage[4];

PImage[][] slotColumn1={imgSlot1,imgSlot1,imgSlot1};
PImage[][] slotColumn2={imgSlot2,imgSlot2,imgSlot2};
PImage[][] slotColumn3={imgSlot3,imgSlot3,imgSlot3};
int leverPulled=0;
int firstTime=0;
int credit=100;
int bet=0;
int multiplier=1;
boolean won=false;

boolean doubleScore = false;


boolean getrokken = false, timer = false;
ArrayList<PVector> handPositieLijst;
ArrayList<PVector>handPositieLijst2;
int listSize = 5, startTime, totalTime = 500;

String gameStatus = "INIT";



PImage imgStrawberry = new PImage();
int strawberryY = height +100;
boolean allowStrawberry = false ,timerStrawberry = false;
int startTimeStrawberry, totalTimeStrawberry = 5000;


int pos1_1 = 0;
int pos1_2=1;
int pos1_3=2;

int pos2_1=2;
int pos2_2=3;
int pos2_3=0;

int pos3_1=3;
int pos3_2=0;
int pos3_3=1;
int slot1=0,slot2=0,slot3=0;



public void setup()
{
  size(1772/2,1417/2);
  
  frameRate(12);
  imgSlot1[0] = loadImage("images/Orange.png");
  imgSlot1[1]=loadImage("images/Lemon.png");
  imgSlot1[2]=loadImage("images/Grapes.png");
  imgSlot1[3]=loadImage("images/Cherry.png");

  imgSlot2[0] = loadImage("images/Orange.png");
  imgSlot2[3]=loadImage("images/Lemon.png");
  imgSlot2[1]=loadImage("images/Grapes.png");
  imgSlot2[2]=loadImage("images/Cherry.png");

  imgSlot3[3] = loadImage("images/Orange.png");
  imgSlot3[1]=loadImage("images/Lemon.png");
  imgSlot3[0]=loadImage("images/Grapes.png");
  imgSlot3[2]=loadImage("images/Cherry.png");
  imgCredit=loadImage("images/bitcoin.png");
  minim = new Minim(this);
  winplayer = minim.loadFile("sounds/win.mp3");
  hendelplayer=minim.loadFile("sounds/handle.mp3");

  imgLegende = loadImage("images/Legende.png");
  imgx2 = loadImage("images/X2.png");
  imgBet = loadImage("images/bet.png");
  imgPull = loadImage("images/pull.png");
  imgLastChance = loadImage("images/last_chance.png");

  imgStrawberry = loadImage("images/strawberry.png");
  imgWin=loadImage("images/YouWin.png");

  leap = new LeapMotionP5(this);
  handPositieLijst = new ArrayList<PVector>();
  handPositieLijst2 = new ArrayList<PVector>();

  imgSlotMachine = loadImage("images/Slotmachine.png");
  imgSlotmachineLeverDown = loadImage("images/Slotmachine-leverDown.png");

  

}

public void draw()
{
  
  if(gameStatus == "INIT")
  {
     background(50);
     image(imgSlotMachine,0,0,1772/2,1417/2);
      image(imgCredit,width-150,25,30,30);
      textSize(16);
      text(""+credit,width-100,50);

      image(imgLegende, 0, 50, 140,195);

     image(slotColumn1[0][pos1_1], 185+35, 192+97.5f,70,97.5f);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5f,70,97.5f);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5f+97.5f,70,97.5f);

     image(slotColumn2[0][pos2_1],185+210, 192+97.5f,70,97.5f );
     image(slotColumn2[1][pos2_2], 185+210, 290+97.5f,70,97.5f);
     image(slotColumn2[2][pos2_3], 185+210, 290+97.5f+97.5f,70,97.5f);

     image(slotColumn3[0][pos3_1],185+410, 192+97.5f,70,97.5f );
     image(slotColumn3[1][pos3_2], 185+410, 290+97.5f,70,97.5f);
     image(slotColumn3[2][pos3_3], 185+410, 290+97.5f+97.5f,70,97.5f);

     gameStatus = "BET";
  }
  if(gameStatus=="BET")
  {
    won=false; doubleScore = false; multiplier = 1;
    if(winplayer.isPlaying())
      winplayer.pause();

      drawSlots();

      TijdControle();
      if(timer == false)
      {
      image(imgBet, 150, 10,100,100);
      
      checkFingers();
    }
  
  
  }

  if(gameStatus == "START")
  {

    drawSlots();
    
    TijdControle();
    if(timer == false)
    {
     image(imgPull, 595, 10, 120, 100);
     HendelControle();
    }
     
}

if(gameStatus == "PULLED")
  {
    hendelplayer.rewind();
    hendelplayer.play();
    background(50);
    image(imgSlotmachineLeverDown, 0,0,1772/2,1417/2);
     image(imgCredit,width-150,25,30,30);
      text(""+credit,width-100,50);
    image(imgLegende, 0, 50, 140,195);

   timer = true; totalTime = 500; startTime = millis();
    
    allowStrawberry = random(1) > .5f; strawberryY=height+100;
    if(allowStrawberry == false) strawberryY = -200;
    totalTimeStrawberry = 5000; startTimeStrawberry = millis();
    
    handPositieLijst.clear();
    gameStatus = "BEZIG";
  }

if(gameStatus == "BEZIG") {

  if(timer == false)
  {
    background(50);
    image(imgSlotMachine,0,0,1772/2,1417/2);
     image(imgCredit,width-150,25,30,30);
      text(""+credit,width-100,50);
      image(imgLegende, 0, 50, 140,195);
  }
  else
  {
    background(50);
    image(imgSlotmachineLeverDown, 0,0,1772/2,1417/2);
     image(imgCredit,width-150,25,30,30);
      text(""+credit,width-100,50);
      image(imgLegende, 0, 50, 140,195);
    TijdControle();
  }

  keyPressed();
  
  checkSlot1();
  checkSlot2();
  checkSlot3();

  if(doubleScore == true)
        image(imgx2, 0, 250, 140, 195);

  if(allowStrawberry==false)
    TijdControleStrawberry();
  if(strawberryY < height+100 && allowStrawberry == true && doubleScore == false)
  {
    image(imgStrawberry, 500, strawberryY);
    strawberryY = strawberryY+20;
    checkFruitSlice();
  }
  


}
if(gameStatus=="HIT"){

  drawSlots();
  image(imgLastChance, 370, 30, 150, 100);

  HitControle();
  TijdControle();

  if(timer == false)
  {
    slot1 = slot2 = slot3 = 0;
    timer = true; totalTime = 2000; startTime = millis();
    gameStatus = "BET";
  }


}
if(gameStatus=="WIN"){

   drawSlots();

     image(imgWin,(width/2)-150,height/2-50,300,100);

    TijdControle();
     if(timer == false)
     {
      slot1 = slot2 = slot3 = 0;
      gameStatus = "BET";
    }
}
}

public void drawSlots()
{
  background(50);
  image(imgSlotMachine,0,0,1772/2,1417/2);
      image(imgCredit,width-150,25,30,30);
      textSize(16);
      text(""+credit,width-100,50);

      image(imgLegende, 0, 50, 140,195);

      if(doubleScore == true)
        image(imgx2, 0, 250, 140, 195);

       image(slotColumn1[0][pos1_1], 185+35, 192+97.5f,70,97.5f);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5f,70,97.5f);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5f+97.5f,70,97.5f);

     image(slotColumn2[0][pos2_1],185+210, 192+97.5f,70,97.5f );
     image(slotColumn2[1][pos2_2], 185+210, 290+97.5f,70,97.5f);
     image(slotColumn2[2][pos2_3], 185+210, 290+97.5f+97.5f,70,97.5f);

     image(slotColumn3[0][pos3_1],185+410, 192+97.5f,70,97.5f );
     image(slotColumn3[1][pos3_2], 185+410, 290+97.5f,70,97.5f);
     image(slotColumn3[2][pos3_3], 185+410, 290+97.5f+97.5f,70,97.5f);
}


public void TijdControle(){
    int passedTime = millis() - startTime;
    if (passedTime > totalTime)
      timer = false;
  }
   public void TijdControleStrawberry(){
    int passedTime = millis() - startTimeStrawberry;
    if (passedTime > totalTimeStrawberry)
      allowStrawberry = true;
  }

  public void checkFruitSlice(){
      for(Hand hand : leap.getHandList()){
    pushMatrix();

    //Position hand
    PVector handPosition = leap.getPosition(hand);
    handPositieLijst2.add(handPosition);

    int lijstSize = handPositieLijst2.size();

    if(handPositieLijst2.get(0).x+200 < handPositieLijst2.get(lijstSize-1).x || handPositieLijst2.get(0).y+200 < handPositieLijst2.get(lijstSize-1).y || handPositieLijst2.get(0).x-200 > handPositieLijst2.get(lijstSize-1).x || handPositieLijst2.get(0).y-200 > handPositieLijst2.get(lijstSize-1).y)
    {
        multiplier=2; doubleScore = true;
        handPositieLijst2.clear();
    }
  }
  }


  
public void checkFingers(){
  bet=0;
  int iFingers=0;
for(Finger f: leap.getFingerList())
{
  iFingers++;
}
switch (iFingers) {

  case 1: bet=1;
  credit-=1;
  timer = true; totalTime = 2000; startTime = millis();
  gameStatus="START";
  break;
  case 2:
  bet=2;
  credit-=2;
  timer = true; totalTime = 2000; startTime = millis();
  gameStatus="START";
  break; 
  case 3:
   bet=3;
  credit-=3;
  timer = true; totalTime = 2000; startTime = millis();
  gameStatus="START";
  break;
  
}




}

public void HendelControle(){
    for(Hand hand : leap.getHandList()){
    pushMatrix();

    //Position hand
    PVector handPosition = leap.getPosition(hand); 
    handPositieLijst.add(handPosition);

    int lijstSize = handPositieLijst.size();

    if(handPositieLijst.get(0).y+100 < handPositieLijst.get(lijstSize-1).y && handPositieLijst.get(0).z+100 < handPositieLijst.get(lijstSize-1).z)
      {
        totalTime = 500;
        gameStatus = "PULLED";
        handPositieLijst.clear();
      }

    if(handPositieLijst.size() >= listSize)
      handPositieLijst.remove(0);

    popMatrix();
  }
}

public void HitControle(){

   checkSlot1();
   checkSlot2();
   checkSlot3();
      for(Hand hand : leap.getHandList()){
    pushMatrix();

    //Position hand
    PVector handPosition = leap.getPosition(hand);
    handPositieLijst2.add(handPosition);

    int lijstSize = handPositieLijst2.size();

    if(handPositieLijst2.get(0).x+200 < handPositieLijst2.get(lijstSize-1).x)
    {
      slot1=0;
      checkSlot1();
     slot1=1;
     checkSlot1();
     


        handPositieLijst2.clear();
     slot1=0;    
    slot2=0;
    slot3=0;
       gameStatus = "BET";
       winControle();

    }
    else if(handPositieLijst2.get(0).y+200 < handPositieLijst2.get(lijstSize-1).y)
    {
      slot2=0;
      checkSlot2();
      slot2=1;
      checkSlot2();
      
        handPositieLijst2.clear();
       
    slot1=0;
    slot2=0;
    slot3=0;
        gameStatus = "BET";
        winControle();
         
         
    }

   else if(handPositieLijst2.get(0).x-200 > handPositieLijst2.get(lijstSize-1).x)
    {
      slot3=0;
      checkSlot3();
      slot3=1;
      checkSlot3();
     
        handPositieLijst2.clear(); 
    slot1=0;
    slot2=0;
    slot3=0;
        gameStatus = "BET";
        winControle();
        

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

    winControle();
  
    handPositieLijst.clear();
    if(won==false)
    {
    totalTime = 5000; timer = true; startTime = millis();

    gameStatus="HIT";}
    
  }

}
public void winControle()
{
  if((pos1_2==0)&&(pos2_2==0)&&(pos3_2==3))
  {
    
       credit+=bet+((bet*2)*multiplier);
        timer = true; totalTime = 4000; startTime = millis();
    winplayer.rewind();
    winplayer.play();
    won=true;
    gameStatus="WIN";
      
    }
   else if((pos1_2==1)&&(pos2_2==3)&&(pos3_2==1))
   {
      credit+=bet+((bet*3)*multiplier);
      timer = true; totalTime = 4000; startTime = millis();
    winplayer.rewind();
    winplayer.play();
    won=true;
    gameStatus="WIN";
   }
    else if((pos1_2==2)&&(pos2_2==1)&&(pos3_2==0))
   {
     credit+=bet+((bet*4)*multiplier);
      timer = true; totalTime = 4000; startTime = millis();
    winplayer.rewind();
    winplayer.play();
    won=true;
    gameStatus="WIN";
   }
   else if((pos1_2==3)&&(pos2_2==2)&&(pos3_2==2)){
     
     credit+=bet+((bet*5)*multiplier);
      timer = true; totalTime = 4000; startTime = millis();
    winplayer.rewind();
    winplayer.play();
    won=true;
    gameStatus="WIN";
   }
   else{
     won=false;
   
   }
    
  
}

public void checkSlot1()
{
  if(slot1==0){
     image(slotColumn1[0][pos1_1], 185+35, 192+97.5f,70,97.5f);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5f,70,97.5f);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5f+97.5f,70,97.5f);
     pos1_1--; 
     pos1_2--;
     pos1_3--;

     if (pos1_1 <0) {
    pos1_1 = 3;
  } 
  if(pos1_2 <0)
  {
    pos1_2=3;

  }
  if(pos1_3 <0){
    pos1_3=3;
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
     pos2_1--; 
     pos2_2--;
     pos2_3--;

     if (pos2_1 <0) {
    pos2_1 = 3;
  } 
  if(pos2_2 <0)
  {
    pos2_2=3;

  }
  if(pos2_3 <0){
    pos2_3=3;
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
     pos3_1--; 
     pos3_2--;
     pos3_3--;

     if (pos3_1 <0) {
    pos3_1 = 3;
  } 
  if(pos3_2 <0)
  {
    pos3_2=3;

  }
  if(pos3_3 <0){
    pos3_3=3;
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
