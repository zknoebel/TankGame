

public class Ground {
	//Resizes the ground
	private static final double RESIZE = TanksMain.RESIZE;
	private double groundScale = 1.1 * RESIZE;
	//sets ground's x and y value
	private double x,y;
	//sets the size of the hole when there is an explosion
	private double holeSize = 50 * RESIZE;
	//the ground is composed of hundreds of tiny square EZ images
	private EZImage ground;

	//creates ground object and places and scales the image
	Ground(double x, double y){
		this.x = x;
		this.y = y;
		ground = EZ.addImage("images//ground2.png", x, y);
		ground.scaleBy(groundScale);

	}

	//function for removing squares of ground used when the ball explodes (also returns boolean for changing arraylist the ground is in)
	public boolean remove(double ballX, double ballY, double ballWidth, double ballHeight){
		if(Math.sqrt((x - (ballX + ballWidth/2))*(x - (ballX + ballWidth/2)) + 
				(y - (ballY + ballHeight/2))*(y - (ballY + ballHeight/2))) < holeSize){
			EZ.removeEZElement(ground);
			return true;
		}
		else{
			return false;
		}
	}

	// removes ground (used to remove all ground created underneath the bottom of the window
	public void disappear() {
		EZ.removeEZElement(ground);
	}

	public double getYCenter() {
		return ground.getYCenter();
	}
	public double getXCenter() {
		return ground.getXCenter();
	}

	//at the moment the width and height are the same for the ground pictures, but just incase we decide to change that we have a function for both
	public double getWidth(){
		return ground.getWidth();
	}
	public double getHeight(){
		return ground.getHeight();
	}

	//this is the gravity for the tank it checks if a certain point of the tank is touching the ground
	public boolean gravity(double tankX, double tankY, double tankWidth){
		if(tankX > x - ground.getWidth()/2 && tankX < x + ground.getWidth()/2 && tankY > y - ground.getHeight()/2 
				&& tankY < y + ground.getHeight()/2){
			return false;
		}
		else{
			return true;
		}
	}

}
