//the ball the tank shoots is black so it needs this
import java.awt.Color;

public class Tanks{
	//resizes everything in tank so it plays on different computers
	private static final double RESIZE = TanksMain.RESIZE;
	private boolean player1;
	//x speed of movement for the tank
	private double tankSpeed = 1 * RESIZE;
	// x and y centers of tank
	private double x,y;
	//angle of the tank picture not including the barrel
	private double tankAngle = 0;
	//x and y centers of ball
	private double ballX, ballY;
	//angle of gun
	private double gunAngle = 0;
	//y speed of movement for the tank
	private double grav = 2 * RESIZE;
	// score on board for each tank
	private int score = 0;
	//true when the player shoots the ball
	private boolean shot = false;
	private boolean thisTanksTurn;
	//resultant vector of xVelocity and yVelocity vectors
	private static double shotSpeed = 13*RESIZE;
	//x and y vector velocities of ball
	private double xVelocity, yVelocity;
	private EZSound tankFireSound = EZ.addSound("sounds//thud.wav");
	private EZSound tankMovementSound = EZ.addSound("sounds//EngineLoop.wav");

	//images for tank and ball
	private EZCircle ball;
	private EZImage gun;
	private EZImage tank;
	//creates, places and scales tank ball and gun
	Tanks(double d, double e, boolean firstPlayer){
		x = d;
		y = e;
		player1 = firstPlayer;
		thisTanksTurn = firstPlayer;
		if(player1){
			ball = EZ.addCircle(x, y, 15*RESIZE, 15*RESIZE, Color.BLACK, true);
		}else{
			ball = EZ.addCircle(x - 8*RESIZE, y - 10*RESIZE, 15*RESIZE, 15*RESIZE, Color.BLACK, true);
		}
		ball.hide();



		if (!firstPlayer){
			gun = EZ.addImage("images//tankBarrel1.png", x - 8*RESIZE, y - 10*RESIZE);
			tank = EZ.addImage("images//catTank.png",x, y);
			gun.scaleBy(RESIZE);
			gunAngle = 180;
			gun.rotateTo(gunAngle);
		}else{
			gun = EZ.addImage("images//tankBarrel1.png", x, y);
			tank = EZ.addImage("images//rabbitTank.png",x, y);
			gun.scaleBy(RESIZE);
		}
		tank.scaleBy(RESIZE);
		tankMovementSound.loop();
	}

	//Tank functions====================================================================================
	public double getAngle(){
		return gunAngle;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getWidth() {
		return tank.getWidth() * RESIZE;
	}
	public double getHeight() {
		return tank.getHeight() * RESIZE;
	}
	public boolean getShot(){
		return shot;
	}
	public double getTankAngle(){
		return Math.toRadians(-tankAngle);
	}
	//Tank Controls (Move and Shoot)=======================================================================
	public void controlls(boolean gravityFront, boolean gravityBack, char left, char right, char anglePlus, char angleMinus, char shoot){
		/*there are two points of the tank being checked with the gravity function. One is the bottom left point on the tank and the other
		 * is the bottom right point. with those two point we change the angle of the tank and move it up and down to keep it on the hill 
		 */
		if(gravityBack && gravityFront){
			y += grav;
		}
		if(!gravityBack && !gravityFront  && !(tankAngle > 45 && tankAngle < 315)){
			y -= grav;
		}

		if(gravityBack && !gravityFront){
			tankAngle +=2 * RESIZE;
		}
		if(!gravityBack && gravityFront){
			tankAngle -=2 * RESIZE;
		}

		//moves gun and ball pictures to the right spot compared to the tanks position and angle
		if(player1){
			gun.translateTo(x, y);
		}else{
			gun.translateTo(x - 9*Math.cos(Math.toRadians(-tankAngle))*RESIZE, y - 10*Math.cos(Math.toRadians(-tankAngle))*RESIZE);
		}
		if(!shot){
			if(player1){
				ballX = x;
				ballY = y;
			}else{
				ballX = x - 9*Math.cos(Math.toRadians(-tankAngle))*RESIZE;
				ballY = y - 10*Math.cos(Math.toRadians(-tankAngle))*RESIZE;
			}
			ball.translateTo(ballX, ballY);
		}
		//moves tank picture
		tank.translateTo(x, y);
		//tank wont be able to move unless it is that tank's turn 
		if (thisTanksTurn && !shot){
			tank.rotateTo(tankAngle);
			gun.rotateTo(gunAngle);
			ball.show();
			//tracks ball as it moves
			ballX = ball.getXCenter();
			ballY = ball.getYCenter();
			//checks the angle of the gun and sets the x and y speeds relative to the angle of fire
			xVelocity = (shotSpeed * Math.cos(Math.toRadians(gunAngle)));
			yVelocity = (shotSpeed * Math.sin(Math.toRadians(gunAngle)));
			//shoots ball
			if(EZInteraction.isKeyDown(shoot)){
				tankFireSound.play();
				shot = true;
			}
			//keeps gun and tank angles between 0 and 360 (I would have used floor mod or something but then 360 would be 0)
			if (gunAngle < 0){
				gunAngle = 360;
			}
			if (gunAngle > 360){
				gunAngle = 0;
			}
			if (tankAngle < 0){
				tankAngle = 360;
			}
			if (tankAngle > 360){
				tankAngle = 0;
			}
			//moves tank left and right if the surface they are on is not too steep
			if (EZInteraction.isKeyDown(left) && x > 0 + tank.getWidth()/2 * RESIZE
					&& !(tankAngle > 40 && tankAngle < 90)){
				x -= tankSpeed;
			}
			else if(EZInteraction.isKeyDown(right) && x < 2000*RESIZE - tank.getWidth()/2
					&& !(tankAngle > 270 && tankAngle < 320)){
				x += tankSpeed;
			}

			//next 40ish lines keep the tank barrel from moving past the 0 and 180 lines of the tank picture (math and what not)
			if (tankAngle > 270 && tankAngle <= 360){
				if((gunAngle > tankAngle) || (gunAngle < 5)){
					gunAngle --;
				}
				if(gunAngle < tankAngle - 180 && gunAngle > 90){
					gunAngle ++;
				}
				if(EZInteraction.isKeyDown(anglePlus)){
					if(gunAngle > tankAngle - 180){
						gunAngle --;
					}
				}
				if(EZInteraction.isKeyDown(angleMinus)){
					if(gunAngle < tankAngle){
						gunAngle ++;
					}
				}
			}
			if (tankAngle >=0 && tankAngle < 90){
				if(gunAngle > tankAngle && gunAngle < 90){
					gunAngle --;
				}
				if(gunAngle < tankAngle + 180 && gunAngle > 90){
					gunAngle ++;
				}
				if(EZInteraction.isKeyDown(anglePlus)){
					if((gunAngle > tankAngle + 180) || gunAngle < 90 ){
						gunAngle --;
					}
				}
				if(EZInteraction.isKeyDown(angleMinus)){
					if((gunAngle < tankAngle) || (gunAngle >= tankAngle + 180)){
						gunAngle ++;
					}
				}

			}

		}
		//shoots ball
		if (shot){
			ball.translateBy(xVelocity, yVelocity);
			//.1 is the acceleration of gravity on the ball
			yVelocity +=   .1*RESIZE;
			ballX = ball.getXCenter();
			ballY = ball.getYCenter();
			//in case the ball is fired off to the side enough that it doesn't hit anything it will still get reset
			if (ball.getYCenter() > 1010 * RESIZE){
				setBall();
			}
		}
	}
	//Ball Functions===================================================================================
	public double getBallX(){
		return ballX;
	}
	public double getBallY(){
		return ballY;
	}
	public double getBallWidth() {
		return ball.getWidth();
	}
	public double getBallHeight() {
		return ball.getHeight();
	}
	//used to reset the ball after it hits something or goes off screen on bottom
	public void setBall(){

		yVelocity = (shotSpeed * Math.sin(Math.toRadians(gunAngle)));
		shot = false;
		if(player1){
			ball.translateTo(x,y);
		}else{
			ball.translateTo(x - 9*Math.cos(Math.toRadians(-tankAngle))*RESIZE, y - 10*Math.cos(Math.toRadians(-tankAngle))*RESIZE);
		}
	}
	//checks if the ball touches something. If it does, the ball explodes 
	public boolean ballHit(double obstacleX, double obstacleY, double obstacleWidth, double obstacleHeight){

		if((shot && ball.getXCenter() > obstacleX - obstacleWidth/2) && (ball.getXCenter() < obstacleX + obstacleWidth/2)
				&& (ball.getYCenter() > obstacleY - obstacleHeight/2) && (ball.getYCenter() < obstacleY + obstacleHeight/2)){
			TanksMain.exploSound.play();
			TanksMain.exploCount = 0;

			// plays series of explosion pictures and rotates the pictures to match the angle at which it hits the object
			for(int e = 0; e<26; e++){

				TanksMain.explosion[e].rotateTo(-90 + Math.toDegrees(Math.atan2(yVelocity, xVelocity)));
				TanksMain.explosion[e].translateTo(ballX, ballY);

			}
			setBall();
			thisTanksTurn = false;

			return true;
		}else{
			return false;
		}
	}
	//other Functions===============================================================
	public boolean getTurn(){
		return thisTanksTurn;
	}
	public int score(){
		return score;
	}
	public void setTurn(){
		thisTanksTurn = true;
	}

	public void translateTo(double d, double i) {
		tank.translateTo(d, i);

	}





}
