/*Project 3.2      TankNation
 * authors:   Declan Hong
 * 			  Zachery Knoebel
 * This project uses EZ, ArrayList(line 31) and 
 * Private, public member variables and member functions
 */

import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

public class TanksMain {
	
	//below are declaration of global variable for the static main function. 
	
	//Get local width and height of the monitor
	static double localWidth =Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static double localHeight =Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
	static final double RESIZE = localHeight/1000 * 0.8;   // use this change change size of the screen.
	//static final double RESIZE = 0.78;
	static double frameWidth = 2000* RESIZE;
	static double frameHeight = 1000* RESIZE;
	static double hide_SP = 0;
	static double width = 17,height = 25,hole = 30*RESIZE;
	static double shiftHorizontally = 1000+100* RESIZE;
	static int scoreCount = 0,player1Score =0,player2Score = 0,exploCount=-1,fireCount = -1,drumStickCount = 0,birdCount=0;
	static int left =0,right = (int)(frameWidth/10 * 8), player = 0;
	
	static ArrayList<Ground> ground = new ArrayList<Ground>();
	static Tanks tank1,tank2;
	
	static EZSound exploSound,birdCraw,thud,taDa;
	
	static EZImage[] explosion = new EZImage[26];
	static EZImage[] fire = new EZImage[51];
	static EZImage[] bird = new EZImage[40];
	static EZImage[] drumStick = new EZImage[18];
	static EZImage[] arrow = new EZImage[8];
	static EZImage awsdKey,ijklKey,iKey,uKey,oKey,eKey,options,options2;
	
	static Color Tb, Tr, W, black, Tr1;
	static EZRectangle messageBoard,backMesh1,backMesh2,backMesh3,playBn,restartBn,restartBn2,quitBn,quitBn2,hover,select,sBoard;
	static EZText textPlay,restart,quit,restart2,quit2,defeat,scoreNum0,scoreNum00,scoreText,scoreLeft,scoreRight; 
	static EZText keyU,keyE,keyO,player1Text,player2Text,gameInstruction,player1,player2,victory;
	static EZText[] countNum = new EZText[11];
	
	static boolean showIntro = true,reduce = false,expand = false,falling=true;

	static Random randomGenerator = new Random();      //declare a random Generator 
	
	//assign all the images and sounds and objects of the game
	public static void content(){
		
		EZ.initialize(frameWidth, frameHeight);
		EZ.setFrameRate(50000);
		EZ.setFrameRateASAP(true);
		
		EZImage backGround = EZ.addImage("images//backGround.png", frameWidth/2, frameHeight/2);
		backGround.scaleBy(RESIZE);
		
		//insert soundfiles
		exploSound = EZ.addSound("sounds//exploSound.wav");
		birdCraw = EZ.addSound("sounds//birdCraw.wav");
		thud = EZ.addSound("sounds//thud.wav");
		taDa = EZ.addSound("sounds//taDa.wav");
		
		//insert ground and barrier walls
		for(int j = -5; j < 100* RESIZE; j ++){
			for(int i = 0; i < 200* RESIZE; i ++){
				double yPoint = (950 +(j * 10) - height * (950/(Math.sqrt(2*Math.PI * width * width)) * (Math.pow(Math.E, -((i + 1000- shiftHorizontally)* (i + 1000 - shiftHorizontally)) / (2 * width * width)))));
				if(yPoint < 1000){
					ground.add(new Ground(i * 10 + 5, yPoint*RESIZE));
				}
			}
		}
		
		// create 2 tanks
		tank1 = new Tanks(250* RESIZE, 865* RESIZE,true);
		tank2 = new Tanks(1750* RESIZE, 865* RESIZE,false);
			
		scoreBoard();
		menuBoard();
		messageBoard();
		
		//insert explosion images
		for(int i = 0; i<26; i++){
			explosion[i] = EZ.addImage("images//explosion//"+i+".png", frameWidth/2, frameHeight/10 * 3.2);
			explosion[i].scaleBy(3* RESIZE);
			explosion[i].hide();
		}
		
		//insert fire images
		for(int i = 0; i<51; i++){
			fire[i] = EZ.addImage("images//fire//"+i+".png", frameWidth/2, frameHeight/10 * 3.2);
			fire[i].scaleBy(2*RESIZE);
			fire[i].hide();
		}
		
		//insert flying bird images
		for(int i = 0; i<40; i++){
			bird[i] = EZ.addImage("images//bird//"+i+".png", frameWidth+200*RESIZE, frameHeight/10 * 2);
			bird[i].scaleBy(3* RESIZE);
		}
		
		//insert drumStick images
		for(int i = 0; i<11; i++){
			drumStick[i] = EZ.addImage("images//smokechicken//"+i+".png", 10000,10000);
			drumStick[i].scaleBy(3* RESIZE);
		}
		
		EZ.setFrameRate(60);
		EZ.setFrameRateASAP(false);
	}
	
	//assign all the content that on show on the menu
	public static void menuBoard(){
		
		// create some rectangles for buttons and hover effects.
		backMesh1 = EZ.addRectangle(frameWidth/4, frameHeight/2, 1000*RESIZE, 1000*RESIZE, Tb, true);
		backMesh2 = EZ.addRectangle(frameWidth/4*3, frameHeight/2, 1000 * RESIZE, 1000*RESIZE, Tb, true);
		//backMesh3 = EZ.addRectangle(frameWidth/4*3, frameHeight/2, 1000 * RESIZE, 1000*RESIZE, Tb, true);
		playBn = EZ.addRectangle(frameWidth/4, frameHeight/10*8, 260*RESIZE, 80*RESIZE,Tr, true);
		restartBn = EZ.addRectangle(635* RESIZE,1100* RESIZE, 260 * RESIZE, 80*RESIZE,Tr, true);
		
		quitBn = EZ.addRectangle(280* RESIZE,1100* RESIZE, 260 * RESIZE, 80*RESIZE, Tr, true);
		
		victory = EZ.addText(10000, 10000,"You Won!", W,100*RESIZE);
		defeat = EZ.addText(10000, 10000, "You Lost!",W,100*RESIZE);
		
		restart = EZ.addText(635* RESIZE,1100* RESIZE,"Restart",W,50*RESIZE);
		quit = EZ.addText(280* RESIZE,1100* RESIZE,"Quit",W,50*RESIZE);
		
		
		awsdKey = EZ.addImage("images//awsdKey.png", frameWidth/10*1.5, frameHeight/10*5);
		ijklKey = EZ.addImage("images//ijklKey.png", frameWidth/10*6.5, frameHeight/10*5);
	
		
		eKey = EZ.addImage("images//e.png", frameWidth/10*3.3, frameHeight/10*4.5);
		oKey = EZ.addImage("images//o.png", frameWidth/10*3.3, frameHeight/10*5.8);
		uKey = EZ.addImage("images//u.png", frameWidth/10*8.3, frameHeight/10*4.5);
		options = EZ.addImage("images//options.png", frameWidth/2, frameHeight/10*0.6);
		options2 = EZ.addImage("images//options.png", frameWidth/2, frameHeight/10*9.5);
		awsdKey.scaleBy(RESIZE);
		ijklKey.scaleBy(RESIZE);
		uKey.scaleBy(RESIZE);
		oKey.scaleBy(RESIZE);
		eKey.scaleBy(RESIZE);
		options.scaleBy(0.8*RESIZE);
		options2.scaleBy(0.8*RESIZE);

		player1Text = EZ.addText(frameWidth*.25, frameHeight/10*3, "Player 1", Color.white,50* RESIZE);
		player2Text = EZ.addText(frameWidth*.75, frameHeight/10*3, "Player 2", Color.white,50* RESIZE);
		keyE = EZ.addText(frameWidth/10*4.3, frameHeight/10*4.5, "Fire Cannon", Color.white,50* RESIZE);
		keyU = EZ.addText(frameWidth/10*9.3, frameHeight/10*4.5, "Fire Cannon", Color.white,50* RESIZE);
		keyO = EZ.addText(frameWidth/10*4.3, frameHeight/10*5.8, "Show Menu", Color.white,50* RESIZE);
		
		gameInstruction = EZ.addText(frameWidth/2, frameHeight/10*2, "Instruction", Color.white,80* RESIZE);
		
		for (int i = 0; i<8;i++){
			arrow[i] = EZ.addImage("images//arrow.png", 10000, 10000);
			arrow[i].rotateTo(45+(45 *(i*2)));
			arrow[i].scaleBy(0.8*RESIZE);
		}
		arrow[0].translateTo(frameWidth/10*1.5, frameHeight/10*3.8);
		arrow[1].translateTo(frameWidth/10*2.3, frameHeight/10*5.5);
		arrow[2].translateTo(frameWidth/10*1.5, frameHeight/10*6.2);
		arrow[3].translateTo(frameWidth/10*0.7, frameHeight/10*5.5);
		
		arrow[4].translateTo(frameWidth/10*6.5, frameHeight/10*3.8);
		arrow[5].translateTo(frameWidth/10*7.3, frameHeight/10*5.5);
		arrow[6].translateTo(frameWidth/10*6.5, frameHeight/10*6.2);
		arrow[7].translateTo(frameWidth/10*5.7, frameHeight/10*5.5);
	}	
	
	//Asssign all the content that on end game message box
	public static void messageBoard(){
		messageBoard = EZ.addRectangle(frameWidth/2, frameHeight/2, 1200*RESIZE, 800*RESIZE, Tb, true);
		messageBoard.scaleBy(0.01);
		restartBn2 = EZ.addRectangle(frameWidth/10*3.5, frameHeight/10*7, 260 * RESIZE, 80*RESIZE,Tr, true);
		restartBn2.scaleBy(0.01);
		quitBn2 = EZ.addRectangle(frameWidth/10*6.5, frameHeight/10*7, 260 * RESIZE, 80*RESIZE, Tr, true);
		quitBn2.scaleBy(0.01);
		
		hover = EZ.addRectangle(10000, 10000, 260 * RESIZE, 80*RESIZE, Color.cyan, true);
		select = EZ.addRectangle(10000, 10000, 260 * RESIZE, 80*RESIZE, Color.yellow, true);
		
		restart2 = EZ.addText(10000, 10000,"Restart",W,50*RESIZE);
		quit2 = EZ.addText(10000, 10000,"Quit",W,50*RESIZE);
		textPlay = EZ.addText(frameWidth/4, frameHeight/10*8, "Play", Color.white,50*RESIZE);
		
		for(int d=10;d>=0;d--){
			countNum[d] =EZ.addText(frameWidth/2, frameHeight/2,""+d+"", W, 180*RESIZE);
			countNum[d].hide();
		}
		
		
		player1 = EZ.addText(10000, 10000,"Player 1 Won!", W,100*RESIZE);
		player2 = EZ.addText(10000, 10000,"Player 2 Won!", W,100*RESIZE);
	}
	
	//Update the the victory message during ending stage
	public static void message(EZText msg){
		msg.translateTo(frameWidth/2, frameHeight/10*3);
		if(reduce==true){
			msg.translateTo(10000, 10000);
		}
	}
	
	//check use input on the menue
	public static void menu(){
		
		while(showIntro == true){
			
			int clickX = 0;
			int clickY = 0;
			int count = 0;

			clickX = EZInteraction.getXMouse();
			clickY = EZInteraction.getYMouse();

			showMenu();
			if (playBn.isPointInElement(clickX, clickY)){
				hover.translateTo(frameWidth/4, frameHeight/10*8);
			}
			
			else if (restartBn.isPointInElement(clickX, clickY)){
				hover.translateTo(635* RESIZE,1100* RESIZE);
			}
		
			else if (quitBn.isPointInElement(clickX, clickY)){
				hover.translateTo(280* RESIZE,1100* RESIZE);
			}
			else {
				hover.translateTo(10000,10000);
			}
			// If I press and release my left mouse button, then....
			if(EZInteraction.wasMouseLeftButtonPressed()){
				if (playBn.isPointInElement(clickX, clickY)){
					select.translateTo(frameWidth/4, frameHeight/10*8);
				}
				else if (restartBn.isPointInElement(clickX, clickY)){
					select.translateTo(635* RESIZE,1100* RESIZE);
				}
				else if (quitBn.isPointInElement(clickX, clickY)){
					select.translateTo(280* RESIZE,1100* RESIZE);
				}
			}
			if (EZInteraction.wasMouseLeftButtonReleased()) {
				select.translateTo(10000,10000);
				hover.translateTo(10000,10000);
				if (playBn.isPointInElement(clickX, clickY)){
					hideMenu();
				}
				else if (quitBn.isPointInElement(clickX, clickY)){
					System.exit(0);
				}

				else if (restartBn.isPointInElement(clickX, clickY)){
					hideMenu();
				}
			}
		EZ.refreshScreen();
		}
	}
	
	//function to hide the menue
	public static void hideMenu(){
		hide_SP = -20* RESIZE;
		while(backMesh1.getXCenter()>= -frameWidth*.35&&
			backMesh2.getXCenter()<= frameWidth*1.35){
			
			backMesh1.translateBy(hide_SP, 0);
			
			player1Text.translateBy(hide_SP, 0);
			awsdKey.translateBy(hide_SP, 0);
			
			playBn.translateBy(hide_SP, 0);
			textPlay.translateBy(hide_SP, 0);
			restartBn.translateBy(hide_SP, 0);
			restart.translateBy(hide_SP, 0);
			quitBn.translateBy(hide_SP, 0);
			quit.translateBy(hide_SP, 0);
		
			eKey.translateBy(hide_SP, 0);
			oKey.translateBy(hide_SP, 0);
		
			keyE.translateBy(hide_SP, 0);
			keyO.translateBy(hide_SP, 0);
		
			hover.translateBy(hide_SP, 0);
			select.translateBy(hide_SP, 0);
		
			gameInstruction.translateBy(hide_SP, 0);
			options.translateBy(hide_SP, 0);
			
			backMesh2.translateBy(-hide_SP, 0);
			player2Text.translateBy(-hide_SP, 0);
			ijklKey.translateBy(-hide_SP, 0);
			uKey.translateBy(-hide_SP, 0);
			keyU.translateBy(-hide_SP, 0);
			options2.translateBy(-hide_SP, 0);
			for (int i = 0; i<4;i++){
				arrow[i].translateBy(hide_SP, 0);
				arrow[i+4].translateBy(-hide_SP, 0);
			}
			EZ.refreshScreen();
		}
		if(backMesh1.getXCenter()<= -frameWidth/4){
			showIntro = false;
		}
	}

	//display menu
	public static void showMenu(){
		hide_SP = 20* RESIZE;
		while(backMesh1.getXCenter()<=frameWidth/4-20* RESIZE&&
			backMesh2.getXCenter()>= frameWidth*.75){

			backMesh1.translateBy(hide_SP, 0);
			
			player1Text.translateBy(hide_SP, 0);
			awsdKey.translateBy(hide_SP, 0);
			
			playBn.translateBy(hide_SP, 0);
			textPlay.translateBy(hide_SP, 0);
			restartBn.translateBy(hide_SP, 0);
			restart.translateBy(hide_SP, 0);
			quitBn.translateBy(hide_SP, 0);
			quit.translateBy(hide_SP, 0);
			
			eKey.translateBy(hide_SP, 0);
			oKey.translateBy(hide_SP, 0);
			
			keyE.translateBy(hide_SP, 0);
			keyO.translateBy(hide_SP, 0);
			
			hover.translateBy(hide_SP, 0);
			select.translateBy(hide_SP, 0);
			
			gameInstruction.translateBy(hide_SP, 0);
			options.translateBy(hide_SP, 0);
			
			backMesh2.translateBy(-hide_SP, 0);
			player2Text.translateBy(-hide_SP, 0);
			ijklKey.translateBy(-hide_SP, 0);
			uKey.translateBy(-hide_SP, 0);
			keyU.translateBy(-hide_SP, 0);
			options2.translateBy(-hide_SP, 0);
			
			for (int i = 0; i<4;i++){
				arrow[i].translateBy(hide_SP, 0);
				arrow[i+4].translateBy(-hide_SP, 0);
			}
			EZ.refreshScreen();
		}
	}
	
	//assign contents on the score board
	public static void scoreBoard(){
		Tb = new Color(0f,0f,0f,.3f);
		Tr = new Color(1f,0f,0f,.6f);
		Tr1 =new Color(1f,0f,0f,.2f);
		W = new Color(255,255,255);
		black =new Color(0f,0f,0f,.9f);
		
		sBoard = EZ.addRectangle(left+(frameWidth/10)*1, frameHeight/100 * 4, 300 * RESIZE, 80*RESIZE, black, true);
		scoreNum0 = EZ.addText(left+(frameWidth/100)*16, frameHeight/100 * 4, "0", Tr1,70*RESIZE);
		scoreNum00 =EZ.addText(left+(frameWidth/100)*14, frameHeight/100 * 4, "0", Tr1,70*RESIZE);
		scoreText =EZ.addText(left+(frameWidth/100)*8, frameHeight/200 * 7, "score:", Color.YELLOW,68*RESIZE);
		
		sBoard = EZ.addRectangle(right+(frameWidth/10)*1, frameHeight/100 * 4, 300 * RESIZE, 80*RESIZE, black, true);
		scoreNum0 = EZ.addText(right+(frameWidth/100)*16, frameHeight/100 * 4, "0", Tr1,70*RESIZE);
		scoreNum00 =EZ.addText(right+(frameWidth/100)*14, frameHeight/100 * 4, "0", Tr1,70*RESIZE);
		scoreText =EZ.addText(right+(frameWidth/100)*8, frameHeight/200 * 7, "score:", Color.YELLOW,68*RESIZE);
		//scoreNum0.setFont("fonts//DS-DIGI.TTF");

		
		scoreLeft = EZ.addText(left+(frameWidth/100)*16,frameHeight/100 * 4, ""+player1Score+"", Color.RED,70*RESIZE);
		scoreRight = EZ.addText(right+(frameWidth/100)*16,frameHeight/100 * 4, ""+player2Score+"", Color.RED,70*RESIZE);

	}
	
	//function to update score on the score board after player score a point 
	public static void updateScore(Tanks tank){
		if(tank == tank1){
			scoreLeft.hide();
			scoreLeft = EZ.addText(left+(frameWidth/100)*16,frameHeight/100 * 4, ""+player1Score+"", Color.RED,70*RESIZE);
		}
		if(tank == tank2){
			scoreRight.hide();
			scoreRight = EZ.addText(right+(frameWidth/100)*16,frameHeight/100 * 4, ""+player2Score+"", Color.RED,70*RESIZE);
		}
	}
	
	//function to let user to press key to control tanks and options of the game
	public static void controlUnit(){
		if(EZInteraction.isKeyDown('y')||EZInteraction.isKeyDown('Y')){
			if(backMesh1.getXCenter()< frameWidth/4){
				showIntro = true;
			}
		}
		if(EZInteraction.isKeyDown('g')||EZInteraction.isKeyDown('G')){
			reduce = true;
			if(player1Score==3){
				player1.hide();
			}
			if(player2Score==3){
				player2.hide();
			}
		}
		if(EZInteraction.isKeyDown('h')||EZInteraction.isKeyDown('H')){
			expand = true;
		}
		tank1.controlls(gravityFront(tank1),gravityBack(tank1),'a', 'd', 'w', 's', 'e');
		if (EZInteraction.isKeyDown('e')){
			tank2.setTurn();
		}
		tank2.controlls(gravityFront(tank2),gravityBack(tank2),'j', 'l', 'k', 'i', 'o');
		if (EZInteraction.isKeyDown('o')){
			tank1.setTurn();
		}
		
	}
	
	//function to deploy eplosion images and sounds
	public static void explode(Tanks tank,double angle){
		exploSound.play();
		exploCount = 0;
		for(int e = 0; e<26; e++){
			explosion[e].rotateTo(angle);
			explosion[e].translateTo(tank.getBallX(), tank.getBallY());
		}	
		tank.setBall();
	}
	
	//function to display the cooked up chicken after the bird is hitted
	public static void fireBird(Tanks tank){
		double newLocation = frameWidth+randomGenerator.nextInt(500)*RESIZE;
		fireCount = 0;
		birdCraw.play();
		for(int e = 0; e<51; e++){
			fire[e].translateTo(tank.getBallX(), tank.getBallY());
		}	
		for(int e = 0; e<11; e++){
			drumStick[e].translateTo(tank.getBallX(), tank.getBallY());
		}	
		tank.setBall();
		for(int i = 0; i<40; i++){
			bird[i].translateBy(newLocation,0);
		}
	}
	
	//function to check collision of all objects.
	public static void targetCheck(){
		groundCheck(tank1);
		groundCheck(tank2);
		tankCheck(tank1,tank2);
		tankCheck(tank2,tank1);
		birdCheck(tank1);
		birdCheck(tank2);
	}
	
	//to check if the front of tanks is in touch of any ground.
	public static boolean gravityFront(Tanks tank){
		boolean a = true;
		
		for(int i = 0; i<ground.size(); i++){
			if(ground.get(i).gravity(tank.getX() + (tank.getWidth()/2) * Math.cos(tank.getTankAngle() + Math.PI), 
				(tank.getY()+tank.getHeight()/2) + (-tank.getWidth()/2) * Math.sin(tank.getTankAngle() + Math.PI),
				tank.getWidth())){
				a = true;
			}else{
				a = false;
				break;
			}
		}
		return a;
	}
	
	//to check if the back of tanks is intouch of any ground.
	public static boolean gravityBack(Tanks tank){
		boolean a = true;
		for(int i = 0; i<ground.size(); i++){
			if(ground.get(i).gravity(tank.getX() + (tank.getWidth()/2) * Math.cos(tank.getTankAngle()), 
				(tank.getY()+tank.getHeight()/2) + (-tank.getWidth()/2) * Math.sin(tank.getTankAngle()),
				tank.getWidth())){
				a = true;
			}else{
				a = false;
				break;
			}
		}
		return a;
	}
	
	//checking if cannon ball has hit the ground
	public static void groundCheck(Tanks tank){
		for(int i = ground.size() - 1; i > 0; i--){
			if(tank.ballHit(ground.get(i).getXCenter(), ground.get(i).getYCenter(), 
				ground.get(i).getWidth(), ground.get(i).getHeight())){
				for(int j = ground.size() - 1; j > 0; j--){
					if(ground.get(j).remove(tank.getBallX(),tank.getBallY(),
						tank.getBallWidth(), tank.getBallHeight())){
						ground.remove(j);
					}
				}
			}
		}
	}
	
	//checking if cannon ball has hit the tanks
	public static void tankCheck(Tanks ball,Tanks tank){
		
		if( ball.getBallX() > tank.getX()- tank.getWidth()/2&&
			ball.getBallX() < tank.getX()+ tank.getWidth()/2&&
			ball.getBallY() > tank.getY()- tank.getHeight()/2&&
			ball.getBallY() < tank.getY()+ tank.getHeight()/2
			){
			explode(ball,0);
			if(ball==tank1){
				player1Score +=1;
			}
			else{
				player2Score +=1;
			}
			updateScore(ball);
		}
	}
	
	//checking if cannon ball has hit the bird
	public static void birdCheck(Tanks ball){
		if( bird[0].getXCenter() > ball.getBallX()- ball.getWidth()/2&&
			bird[0].getXCenter() < ball.getBallX()+ ball.getWidth()/2&&
			bird[0].getYCenter() > ball.getBallY()- ball.getHeight()/2&&
			bird[0].getYCenter() < ball.getBallY()+ ball.getHeight()/2
			){
				fireBird(ball);
				if(ball==tank1){
					player1Score +=1;
				}
				else{
					player2Score +=1;
				}
				updateScore(ball);
				falling = true;
			}
	}
	
	//show motion images 
	public static void animationShow(){
		double newLocation = frameWidth+randomGenerator.nextInt(1000)*RESIZE;
		if (exploCount > -1) {
			explosion[exploCount].show();
		}
		if (fireCount > -1) {
			fire[fireCount].show();
		}
		if (birdCount > -1) {
			bird[birdCount].show();
		}
		if (drumStickCount > -1) {
			drumStick[drumStickCount].show();
		}
		
		if (bird[0].getXCenter()>-10){
			for(int i = 0; i<40; i++){
				bird[i].translateBy(-2*RESIZE,0);
			}
		}
		if (bird[0].getXCenter()<0){
			for(int i = 0; i<40; i++){
				bird[i].translateBy(newLocation,0);
			}
		}
		if (drumStick[0].getYCenter()<frameHeight+100*RESIZE && falling == true){
			for(int i = 0; i<11; i++){
				drumStick[i].translateBy(0,5*RESIZE);
			}
			for(int d = 0; d<ground.size(); d++){
				if(!ground.get(d).gravity(drumStick[0].getXCenter(),drumStick[0].getYCenter(),drumStick[0].getWidth())){
					falling = false;
				}
			}
		}
		if(expand == true && messageBoard.getScale()<1){
			messageBoard.scaleBy(1.1);
			restartBn2.scaleBy(1.1);
			quitBn2.scaleBy(1.1);
			if(messageBoard.getScale()>1){
				restart2.translateTo(frameWidth/10*3.5, frameHeight/10*7);
				quit2.translateTo(frameWidth/10*6.5, frameHeight/10*7);
				message(victory);
				expand = false;
				countDown();
			}
		}
		if(reduce == true && messageBoard.getScale()>0.01){
			messageBoard.scaleBy(0.9);
			restartBn2.scaleBy(0.9);
			quitBn2.scaleBy(0.9);
			restart2.translateTo(10000,10000);
			quit2.translateTo(10000,10000);
			message(victory);
			if(messageBoard.getScale()<0.01){
				reset();
				reduce = false;
				player1Score=0;
				player2Score=0;
			}
		}
	}
	
	//hide motion images
	public static void animationHide(){
		if (exploCount > -1) {
			explosion[exploCount].hide();
			exploCount++;
			if(exploCount==25){
				exploCount=-1;
			}
		}
		if (fireCount > -1) {
			fire[fireCount].hide();
			fireCount++;
			if(fireCount==51){
				fireCount=-1;
			}
		}
		if (birdCount > -1) {
			bird[birdCount].hide();
			birdCount++;
			if(birdCount==40){
				birdCount=0;
			}
		}
		if (drumStickCount > -1) {
			drumStick[drumStickCount].hide();
			drumStickCount++;
			if(drumStickCount==11){
				drumStickCount=0;
			}
		}
	}
	
	public static void endGame(){
		if(tank1.getY()> 1050*RESIZE){
			player2Score = 3;
			updateScore(tank2);
			victory = player2 ; 
		}
		if(tank2.getY()> 1050*RESIZE){
			player1Score = 3;
			updateScore(tank1);
			victory = player1 ; 
		}
		if(player1Score==3){
			expand = true;
			victory = player1 ; 
		}
		if(player2Score==3){
			expand = true;
			victory = player2 ; 
		}
	}
	
	public static void countDown(){
		
		taDa.play();
		
		int clickX = 0;
		int clickY = 0;
		int count = 0;
		
		for(int d = 10; d >= 0; d--){
			countNum[d].show();
			while(count < 50){
				
				// Get the mouse X and Y position
				clickX = EZInteraction.getXMouse();
				clickY = EZInteraction.getYMouse();

				// Move the picture of my finger to the position of clickX, clickY
				if (restartBn2.isPointInElement(clickX, clickY)){
					hover.translateTo(frameWidth/10*3.5, frameHeight/10*7);
				}

				else if (quitBn2.isPointInElement(clickX, clickY)){
					hover.translateTo(frameWidth/10*6.5, frameHeight/10*7);
				}
				else {
					hover.translateTo(10000,10000);

				}
				// If I press and release my left mouse button, then....
				if(EZInteraction.wasMouseLeftButtonPressed()){
					
					if (restartBn2.isPointInElement(clickX, clickY)){
						select.translateTo(frameWidth/10*3.5, frameHeight/10*7);
					}
					else if (quitBn2.isPointInElement(clickX, clickY)){
						select.translateTo(frameWidth/10*6.5, frameHeight/10*7);
					}
				}
				if (EZInteraction.wasMouseLeftButtonReleased()) {
					select.translateTo(10000,10000);
					hover.translateTo(10000,10000);
					if (quitBn2.isPointInElement(clickX, clickY)){
						System.exit(0);
					}
					else if (restartBn2.isPointInElement(clickX, clickY)){
						player1Score = 0;
						player2Score = 0;
						updateScore(tank1);
						updateScore(tank2);	
						tank1.translateTo(250* RESIZE, -10000);
						tank2.translateTo(1750* RESIZE, -10000);
						count = 50;
						reduce = true;
					}
				}
				count += 1;
				EZ.refreshScreen();
			}
			countNum[d].hide();
			if (count== 50){
				count = 0;
			}
		}

		if (reduce == false){
			System.exit(0);
		}
	}
	
	public static void reset(){
		EZ.setFrameRate(50000);
		EZ.setFrameRateASAP(true);
		for(int e = 0; e<11; e++){
			drumStick[e].translateTo(10000,10000);
		}
		
		for(int i = ground.size() - 1; i > 0; i--){
			for(int j = ground.size() - 1; j > 0; j--){
				ground.get(j).disappear();;
			}
		}
		
		//insert ground and barrier walls
		for(int j = -5; j < 100* RESIZE; j ++){
			for(int i = 0; i < 200* RESIZE; i ++){
				double yPoint = (950 +(j * 10) - height * (950/(Math.sqrt(2*Math.PI * width * width)) * (Math.pow(Math.E, -((i + 1000- shiftHorizontally)* (i + 1000 - shiftHorizontally)) / (2 * width * width)))));
				if(yPoint < 1000){
					ground.add(new Ground(i * 10 + 5, yPoint*RESIZE));
				}
			}
		}
		
		EZ.setFrameRate(60);
		EZ.setFrameRateASAP(false);
		tank1.translateTo(250* RESIZE, 865* RESIZE);
		tank2.translateTo(1750* RESIZE, 865* RESIZE);
	}
	
	
	// this is the main loop
	public static void main(String[] args){
		
		content();                 	//insert all images and sounds of game
		
		while (true){				//this is the main loop
			
			menu();					//display a menu and waiting for user input
			
			animationShow();		//display animation images
			
			controlUnit();			//deploy keyboard control of the game
			
			targetCheck();			//check collision of any object
			
			EZ.refreshScreen();		//this fresh the screen so you could see the changes of any images display.
			
			EZ.setFrameRate(60);	//slow down the frameRate to 50
			
			animationHide();		//hide animation images
			
			endGame();				//endGame messages and options
		}
	}
}
