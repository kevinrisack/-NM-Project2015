import com.onformative.leap.*;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.*;

LeapMotionP5 leap;
PImage imgBackground, imgSlotMachine, imgSlotmachineLeverDown,imgCredit;
PImage[] imgSlot1=new PImage[4];
PImage[] imgSlot2=new PImage[4];
PImage[] imgSlot3=new PImage[4];

PImage[][] slotColumn1={imgSlot1,imgSlot1,imgSlot1};
PImage[][] slotColumn2={imgSlot2,imgSlot2,imgSlot2};
PImage[][] slotColumn3={imgSlot3,imgSlot3,imgSlot3};
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

PImage imgStrawberry = new PImage();
int strawberryY = 1;
boolean allowStrawberry = false ,timerStrawberry = false;


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



void setup()
{
  size(1772/2,1417/2);
  
  frameRate(6);
  imgSlot1[0] = loadImage("images/nmct.png");
  imgSlot1[1]=loadImage("images/dae.png");
  imgSlot1[2]=loadImage("images/devine.png");
  imgSlot1[3]=loadImage("images/howest.png");

  imgSlot2[0] = loadImage("images/nmct.png");
  imgSlot2[3]=loadImage("images/dae.png");
  imgSlot2[1]=loadImage("images/devine.png");
  imgSlot2[2]=loadImage("images/howest.png");

  imgSlot3[3] = loadImage("images/nmct.png");
  imgSlot3[1]=loadImage("images/dae.png");
  imgSlot3[0]=loadImage("images/devine.png");
  imgSlot3[2]=loadImage("images/howest.png");
  imgCredit=loadImage("images/bitcoin.png");

  imgStrawberry = loadImage("images/strawberry.png");

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

void draw()
{
  
  if(gameStatus == "INIT")
  {
     background(50);
     frameRate(12);
     image(imgSlotMachine,0,0,1772/2,1417/2);
      image(imgCredit,width-150,25,30,30);
      textSize(16);
      text(""+credit,width-100,50);

     image(slotColumn1[0][pos1_1], 185+35, 192+97.5,70,97.5);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5,70,97.5);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5+97.5,70,97.5);

     image(slotColumn2[0][pos2_1],185+210, 192+97.5,70,97.5 );
     image(slotColumn2[1][pos2_2], 185+210, 290+97.5,70,97.5);
     image(slotColumn2[2][pos2_3], 185+210, 290+97.5+97.5,70,97.5);

     image(slotColumn3[0][pos3_1],185+410, 192+97.5,70,97.5 );
     image(slotColumn3[1][pos3_2], 185+410, 290+97.5,70,97.5);
     image(slotColumn3[2][pos3_3], 185+410, 290+97.5+97.5,70,97.5);

     gameStatus = "START";
  }

  if(gameStatus == "START")
  {
    println("Gamestatus = " + gameStatus); 

    gameStatus = "PULLED";
    timerStrawberry = false;
    
     HendelControle();
     
}

if(gameStatus == "PULLED")
  {
    background(50);
    image(imgSlotmachineLeverDown, 0,0,1772/2,1417/2);
     image(imgCredit,width-150,25,30,30);
      text(""+credit,width-100,50);
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
     image(imgCredit,width-150,25,30,30);
      text(""+credit,width-100,50);
  }
  else
  {
    background(50);
    image(imgSlotmachineLeverDown, 0,0,1772/2,1417/2);
     image(imgCredit,width-150,25,30,30);
      text(""+credit,width-100,50);
    TijdControle();
  }

  keyPressed();
  
  checkSlot1();
  checkSlot2();
  checkSlot3();

  if(strawberryY < height+100 && allowStrawberry == true)
  {
    image(imgStrawberry, 500, strawberryY);
    strawberryY = strawberryY+20;
  }
  


}
if(gameStatus=="HIT"){
  println("Gamestatus = " + gameStatus);

  HitControle();
  TijdControle();

  if(timer == false)
    gameStatus = "START";


}

}

void TijdControle(){
    int passedTime = millis() - startTime;
    if (passedTime > totalTime)
      timer = false;
  }

void HendelControle(){
    for(Hand hand : leap.getHandList()){
    println(hand);
    pushMatrix();

    //Position hand
    PVector handPosition = leap.getPosition(hand); println("x: " + handPosition.x + " y: " + handPosition.y + " z: " + handPosition.z);
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

void HitControle(){

    println("hitcontrole");
      for(Hand hand : leap.getHandList()){
    pushMatrix();

    //Position hand
    PVector handPosition = leap.getPosition(hand);
    handPositieLijst2.add(handPosition);

    int lijstSize = handPositieLijst2.size();

    if(handPositieLijst2.get(0).x+200 < handPositieLijst2.get(lijstSize-1).x)
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
    else if(handPositieLijst2.get(0).y+200 < handPositieLijst2.get(lijstSize-1).y)
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

   else if(handPositieLijst2.get(0).x-200 > handPositieLijst2.get(lijstSize-1).x)
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



  void keyPressed() {
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

void CheckFinished()
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

    handPositieLijst.clear();
    
    totalTime = 5000; timer = true; startTime = millis();

    gameStatus="HIT";
    
  }

}

void checkSlot1()
{
  if(slot1==0){
     image(slotColumn1[0][pos1_1], 185+35, 192+97.5,70,97.5);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5,70,97.5);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5+97.5,70,97.5);
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
        image(slotColumn1[0][pos1_1], 185+35, 192+97.5,70,97.5);
     image(slotColumn1[1][pos1_2], 185+35, 290+97.5,70,97.5);
     image(slotColumn1[2][pos1_3], 185+35, 290+97.5+97.5,70,97.5);
  }

}

void checkSlot2()
{
  if(slot2==0){
     image(slotColumn2[0][pos2_1],185+210, 192+97.5,70,97.5 );
     image(slotColumn2[1][pos2_2], 185+210, 290+97.5,70,97.5);
     image(slotColumn2[2][pos2_3], 185+210, 290+97.5+97.5,70,97.5);
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
     image(slotColumn2[0][pos2_1],185+210, 192+97.5,70,97.5 );
     image(slotColumn2[1][pos2_2], 185+210, 290+97.5,70,97.5);
     image(slotColumn2[2][pos2_3], 185+210, 290+97.5+97.5,70,97.5);
  }
  

}

void checkSlot3()
{
  if(slot3==0){
    image(slotColumn3[0][pos3_1],185+410, 192+97.5,70,97.5 );
     image(slotColumn3[1][pos3_2], 185+410, 290+97.5,70,97.5);
     image(slotColumn3[2][pos3_3], 185+410, 290+97.5+97.5,70,97.5);
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
    image(slotColumn3[0][pos3_1],185+410, 192+97.5,70,97.5 );
     image(slotColumn3[1][pos3_2], 185+410, 290+97.5,70,97.5);
     image(slotColumn3[2][pos3_3], 185+410, 290+97.5+97.5,70,97.5);
  }
}
