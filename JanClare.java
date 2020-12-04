// Jan Clare 201319103

import robocode.*;
import java.awt.Color;


// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * JanClare - a robot by (your name here)
 */
public class JanClare extends AdvancedRobot
{
	boolean directionAhead = false;
	/**
	 * run: JanClare's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {
			// Run around like crazy
			setColour("Black");
			changeHeading();
			setTurnRight(90);
			setTurnRadarRight(getHeading()- getGunHeading());
			waitFor(new TurnCompleteCondition(this));
			setTurnLeft(180);
			waitFor(new TurnCompleteCondition(this));
			setTurnRight(180);
			waitFor(new TurnCompleteCondition(this));
			
			// always intend to scan around
			//setTurnRadarRight(360);
			//execute();
		}
	}
	
	// Function to change colour so I can see what behaviour the tank is currently doing
	public void setColour(String selected){
		// Change colour of bot
		if (selected == "Black"){
			setBodyColor(new Color(0, 0, 0));
			setGunColor(new Color(0, 0, 0));
			setRadarColor(new Color(0, 0, 0));
			setBulletColor(new Color(0, 0, 0));
			setScanColor(new Color(255, 255, 255));
		}
		if (selected == "Red"){
			setBodyColor(new Color(255,0,0));
			setGunColor(new Color(255,0,0));
			setRadarColor(new Color(255,0,0));
			setBulletColor(new Color(255,0,0));
		}
		if (selected == "White"){
			setBodyColor(new Color(255, 255, 255));
			setGunColor(new Color(255, 255, 255));
			setRadarColor(new Color(255, 255, 255));
			setBulletColor(new Color(255, 255, 255));
		}
	}
	
	// If was going forwards now go backwards else go forwards
	public void changeHeading(){
		if (directionAhead){
			setBack(9999);
			directionAhead = false;
		} else {
			setAhead(9999);
			directionAhead = true;
		}
	}
	
	//Behaviour to aggresively pursue a target
	public void lockAndDestroy(ScannedRobotEvent e){
		
		// Lock radar on target 
		setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());
		// Lock gun on target
		setTurnGunRight(getHeading() - getGunHeading() + e.getBearing());
		// Lock direction onto target
		setTurnRight(e.getBearing());
		
		// If bot is far away
		if (e.getDistance() >= 400){
			// Move towards it, maybe pop some warning shots
			setAhead(100);
			fire(1);
		}
		
		// If bot is a bit closer
		if (e.getDistance() < 400 && e.getDistance() >= 300 ) {
			// Sneak closer... up the anti...
			setAhead(100);
			fire(2);
		}
		
		// If bot is a close
		else if (e.getDistance() <300 && e.getDistance() >= 200 ) {
			// Sneak closer... up the anti...
			setAhead(100);
			fire(3);
		}
		
		// If bot is comfort zone
		else if (e.getDistance() <200 && e.getDistance() >= 100 ) {
			// MOVE IN and shoot!
			setAhead(100);
			fire(3);
		}
		
		// If bot is in CRITICAL DISTANCE
		else if (e.getDistance() <100) {
			// Stop and DESTROY ENEMY
			fire(4);
		}
		execute();
		
	}
	
	public void backOff(ScannedRobotEvent e){
		//1.Face enemy
		// Lock radar on target 
		setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());
		// Lock gun on target
		setTurnGunRight(getHeading() - getGunHeading() + e.getBearing());
		// Lock direction onto target
		setTurnRight(e.getBearing());
		
		// If bot is at a fair distance
		if (e.getDistance() < 400 && e.getDistance() >= 300 ) {
			// Sneak off... fire...
			setBack(100);
			fire(2);
		}
		
		// If bot is a close
		else if (e.getDistance() <300 && e.getDistance() >= 200 ) {
			// go back... still shoot...
			setBack(100);
			fire(2);
		}
		
		// If bot is comfort zone
		else if (e.getDistance() <200 && e.getDistance() >= 100 ) {
			// MOVE IN and shoot!
			setBack(100);
			fire(3);
		}
		
		// If bot is in CRITICAL DISTANCE
		else if (e.getDistance() <100) {
			// Stop and DESTROY
			setBack(100);
			fire(4);
		}
		execute();
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//If robot is really far away then take a pot shot
		if (e.getDistance() >= 400){
			fire(1);
		}else if (e.getDistance() < 200 && getEnergy() < 50){
			// If it's close and you're on low health then back off
			setColour("White");
			backOff(e);
		} else {
			// if you're strong and it's in reasonable distance DESTROY
			setColour("Red");
			lockAndDestroy(e);
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		//change direction
		changeHeading();
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		//change direction
		changeHeading();
	}
	
	public void onHitRobot(HitRobotEvent e) {
		//change direction
		if (e.isMyFault()) {
			changeHeading();
		}
	}	
}
