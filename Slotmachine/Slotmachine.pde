import com.onformative.leap.*;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.*;

LeapMotionP5 leap;
PImage imgBackground, imgSlotMachine, imgSlotmachineLeverDown,imgCredit;
PImage[] imgSlot1=new PImage[4];
PImage[] imgSlot2=new PImage[4];
PImage[] imgSlot3=new PImage[4];
PImage[][] slotColumn1={imgSlot1,imgSlot1,imgSlot1};
PImage[][] slotColumn2={imgSlot1,imgSlot1,imgSlot1};
PImage[][] slotColumn3={imgSlot1,imgSlot1,imgSlot1};
int leverPulled=0;
int firstTime=0;

boolean getrokken = false, timer = false;
ArrayList<PVector> handPositieLijst;
int listSize = 5, startTime, totalTime = 500;

String gameStatus = "INIT";



int pos1_1 =0;
int pos1_2=1;
int pos1_3=2;

int pos2_1=0;
int pos2_2=1;
int pos2_3=2;

int pos3_1=0;
int pos3_2=1;
int pos3_3=2;
int slot1=0,slot2=0,slot3=0;
int credit=100;



void setup()
{
  size(1772/2,1417/2);
	
	frameRate(6);
	imgSlot1[0] = loadImage("images/nmct.png");
	imgSlot1[1]=loadImage("images/dae.png");
	imgSlot1[2]=loadImage("images/devine.png");
	imgSlot1[3]=loadImage("images/howest.png");
  imgCredit=loadImage("images/bitcoin.png");
/*
	imgSlot2[0] = loadImage("images/nmct.png");
	imgSlot2[1]=loadImage("images/dae.png");
	imgSlot2[2]=loadImage("images/devine.png");
	imgSlot2[3]=loadImage("images/howest.png");

	imgSlot3[0] = loadImage("images/nmct.png");
	imgSlot3[1]=loadImage("images/dae.png");
	imgSlot3[2]=loadImage("images/devine.png");
	imgSlot3[3]=loadImage("images/howest.png");*/

  leap = new LeapMotionP5(this);
  handPositieLijst = new ArrayList<PVector>();

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
      println(pos1_1+" "+pos2_1+ " " +pos3_1);
      println(pos1_2+" "+pos2_2+ " " +pos3_2);
      println(pos1_3+" "+pos2_3+ " " +pos3_3);

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

  


}}

void TijdControle(){
    int passedTime = millis() - startTime;
    if (passedTime > totalTime)
      timer = false;
  }

void HendelControle(){
    for(Hand hand : leap.getHandList()){
    //println(hand);
    pushMatrix();

    //Position hand
    PVector handPosition = leap.getPosition(hand); //println("x: " + handPosition.x + " y: " + handPosition.y + " z: " + handPosition.z);
    handPositieLijst.add(handPosition);

    int lijstSize = handPositieLijst.size();

    if(handPositieLijst.get(0).y+100 < handPositieLijst.get(lijstSize-1).y && handPositieLijst.get(0).z+100 < handPositieLijst.get(lijstSize-1).z)
      gameStatus = "PULLED";

    if(lijstSize >= listSize)
      handPositieLijst.remove(0);

    popMatrix();
  }
}



  void keyPressed() {
  if (key == CODED && gameStatus == "BEZIG" && keyPressed==true) {
    if (keyCode == DOWN && keyPressed==true) {
      slot2=1;
      checkSlot2();
      CheckFinished();
    }
    if (keyCode == LEFT && keyPressed==true) {
      slot1=1;
      checkSlot1();
      CheckFinished();
    } 
   if (keyCode==RIGHT && keyPressed==true) {
    
    slot3=1;
    checkSlot3();
    CheckFinished();
  }
}
 
}

void CheckFinished()
{

  if((slot1==1) && (slot2==1) && (slot3==1)){
    
    println(pos1_2+" "+pos2_2+ " " +pos3_2);
    
    if((pos1_2==pos2_2)&& (pos2_2==pos3_2))
    {
      println(pos1_2+" "+pos2_2+ " " +pos3_2+": You have won");
    }


    slot1=0;
    slot2=0;
    slot3=0;
/*
    pos1_1=0;
    pos1_2=1;
    pos1_3=2;

    pos2_1=1;
    pos2_2=2;
    pos2_3=3;

    pos3_1=2;
    pos3_2=3;
    pos3_3=0;*/


    gameStatus = "START";
    handPositieLijst.clear();
    
  }

}

void checkSlot1()
{
	if(slot1==0){
  	 image(slotColumn1[0][pos1_1], 185+35, 192+97.5,70,97.5);
  	 image(slotColumn1[1][pos1_2], 185+35, 290+97.5,70,97.5);
  	 image(slotColumn1[2][pos1_3], 185+35, 290+97.5+97.5,70,97.5);
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
  	image(slotColumn3[0][pos3_1],185+410, 192+97.5,70,97.5 );
  	 image(slotColumn3[1][pos3_2], 185+410, 290+97.5,70,97.5);
  	 image(slotColumn3[2][pos3_3], 185+410, 290+97.5+97.5,70,97.5);
  }
}
