/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myfirstrobot;
import java.awt.Color;
import static java.lang.Math.*;
import javafx.util.Pair;
import robocode.*;

/**
 *
 * @author dani
 */
public class MyFirstRobot extends AdvancedRobot{
         
    int moveDirection = 1;
    
    public void run(){
        setTurnRadarLeft(Double.POSITIVE_INFINITY); //Gira fins que trobi l'event onScannedRobot salti
        setAdjustRadarForRobotTurn(true); // Perque el radar no sigue afectat per el moviment
        double nearCorner_X, nearCorner_Y;
        
        if(getX() < (getBattleFieldWidth()-getX()))nearCorner_X = 0;
        else nearCorner_X = getBattleFieldWidth();
        
        if(getY() < (getBattleFieldHeight()-getY()))nearCorner_Y = 0;
        else nearCorner_Y = getBattleFieldHeight();
        
        turnLeftRadians(tan((nearCorner_X-getX())/(nearCorner_Y-getY())));
        ahead(hypot(nearCorner_X-getX() , nearCorner_Y-getY()));
        execute();
        
        
    }
    
    private void detectRival(){
        setAdjustRadarForRobotTurn(false);
        setTurnLeft(360);
        execute();
        
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
        /*double absBearing=e.getBearingRadians()+getHeadingRadians();//enemies absolute bearing
        double latVel=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);//enemies later velocity
        double gunTurnAmt;//amount to turn our gun
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
        if(Math.random()>.9){
            setMaxVelocity((12*Math.random())+12);//randomly change speed
        }
        if (e.getDistance() > 150) {//if distance is greater than 150
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/22);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt); //turn our gun
            //setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity()));//drive towards the enemies predicted future location
            //setAhead((e.getDistance() - 140)*moveDirection);//move forward
        }
        else{//if we are close enough...
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/15);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt);//turn our gun
            //setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
            //setAhead((e.getDistance() - 140)*moveDirection);//move forward
        }*/
    }
    
    private void aproachRival(){
        // cos 90 = x/a --> x = a*cos90
        
    }
    
    
    
    
}
