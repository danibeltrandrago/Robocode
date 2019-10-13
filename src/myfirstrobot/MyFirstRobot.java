/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myfirstrobot;
import java.awt.Color;
import static java.lang.Math.*;
//import javafx.util.Pair;
import robocode.*;

/**
 *
 * @author dani
 */
public class MyFirstRobot extends AdvancedRobot{
         
    int moveDirection = 1;
    /* Direccion de avance, +1 hacia donde ibamos, -1 en sentido contrario */
    int dir=1;
    double orientacionChoque;
    double initialLife=1;
    double initialEnergy=1;
    boolean escapeMode=false;
    @Override
    public void run(){
        if(getTime()==0){
            //Inicializamos las variable vida y energía al principio de cada partida
            initialEnergy=getEnergy();
            initialLife=getLife();
        }
        
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
        while(true) {
            
            //Aquí va el movimiento(ataque) que hace siempre el tanque
            //Es importante multiplicar por la variable dir
            //ESta parte se sustituye por el movimiento de ataque
            setTurnRight(dir*10000);
            setTurnGunRight(dir*20);
            setAhead(dir*2000);
            execute();
            System.out.println("I should move!! ");
                    
            if(getLife()<initialLife/2.0 || getEnergy()<initialLife/2.0){
                System.out.println("I should leave!! ");
                escapeMode=true;
            }
            else if(getEnergy()>=initialLife/2.0){
                escapeMode=false;
            }
            
            //Distancias a los muros que limitan el escenario de combate, que los
            //tenemos  que  evitar  ya  que  un  choque  contra  estos  nos  resta  energía 
            //vital:
            double N = getBattleFieldHeight() - getY();
            double S = getY();
            double E = getBattleFieldWidth() - getX();
            double W = getX();
            
            //No nos vamos a chocar con los muros, ya que cuando estemos a menos
            //de una cierta distancia de estos daremos media vuelta.
            if(N < 70 && getHeading() > 270 && getHeading() < 360)
                dir=-1;
            else
                if(N < 70 && getHeading() > 0.0 && getHeading() < 90)
                    dir=-1;
                else
                    //Aquí estamos en el caso de que estemos en zona próxima al muro 
                    // norte pero nuestro sentido de avance es tal que nos aleja de 
                    //este peligro.
                    if(N < 70)
                        dir=1;
            
            if(S < 70 && getHeading() > 90 && getHeading() < 270)
                dir=-1;
            else
                if(S < 70)
                    dir=1;
            if(E < 70 && getHeading() > 0.0 && getHeading() < 180)
                dir=-1;
            else
                if(E < 70)
                    dir=1;
            if(W < 70 && getHeading() > 180 && getHeading() < 360) 
                dir=-1;
            else
                if(W < 70)
                    dir=1;
            
            //Si estamos en las zonas de peligro: 
            if(N < 70 || S < 70 || E < 70 || W < 70){
                orientacionChoque = -dir * 10 - 90; 
            }else{
                orientacionChoque = 0.0;
            }
            
            
            
        }  
        
    }
    
    private void detectRival(){
        setAdjustRadarForRobotTurn(false);
        setTurnLeft(360);
        execute();
        
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
        double absBearing=e.getBearingRadians()+getHeadingRadians();//enemies absolute bearing
        double latVel=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);//enemies later velocity
        double gunTurnAmt;//amount to turn our gun
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
        if(Math.random()>.9){
            setMaxVelocity((12*Math.random())+12);//randomly change speed
        }
        if (e.getDistance() > 150) {//if distance is greater than 150    
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()-latVel/22);//amount to turn our gun, lead just a little bit
            //setTurnGunRightRadians(gunTurnAmt); //turn our gun
            
            //setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity()));//drive towards the enemies predicted future location
            //setAhead((e.getDistance() - 140)*moveDirection);//move forward
            //Escaping instead of chasing:
            if(escapeMode){
                //Para alejarse maximo del rival si estamos lejos
                setTurnGunLeftRadians(gunTurnAmt); //turn our gun
                setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity()));//drive towards the enemies predicted future location
                setAhead((e.getDistance() - 140)*(-1*moveDirection));//move backward
            }else{
                setTurnGunRightRadians(gunTurnAmt); //turn our gun
                setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity()));//drive towards the enemies predicted future location
                setAhead((e.getDistance() - 140)*moveDirection);//move forward
            }
        }
        else{//if we are close enough...
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/15);//amount to turn our gun, lead just a little bit
            //setTurnGunRightRadians(gunTurnAmt);//turn our gun
            
            //setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
            //setAhead((e.getDistance() - 140)*moveDirection);//move forward
            //Escaping instead of chasing:
            if(escapeMode){
                //Para alejarse maximo del rival si estamos cerca
                setTurnGunLeftRadians(gunTurnAmt);//turn our gun
                setTurnLeft(90-e.getBearing()); //turn perpendicular to the enemy
                setAhead((e.getDistance() - 140)*(-1*moveDirection));//move backward
            }else{
                setTurnGunRightRadians(gunTurnAmt);//turn our gun
                setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
                setAhead((e.getDistance() - 140)*moveDirection);//move forward
            }
        }
    }
    
    private void aproachRival(){
        // cos 90 = x/a --> x = a*cos90
        
    }
    
    @Override
    public void onHitWall(HitWallEvent e) { 
       out.println("Ouch, I hit a wall bearing " + e.getBearing() + " degrees.");
       //Esto es para evitar chocarnos contra la pared continuamente al huir
       //aunque se podría hacer siempre
       if(escapeMode){
           if(e.getBearing() > -90 && e.getBearing() <= 0){//Chocamos por la parte delante izquierda
		turnRight(135);
		ahead(100);
            }
            if(e.getBearing() > 0 && e.getBearing() <= 90){//Chocamos por la parte delante derecha
                turnLeft(135);
		ahead(100);
            }
            if(e.getBearing() > 90 && e.getBearing() <= 180){//Chocamos por la parte trasera derecha
                turnLeft(75);
		ahead(100);
            }
            if(e.getBearing() > -180 && e.getBearing() < -90){//Chocamos por la parte trasera izquierda
                turnRight(75);
		ahead(100);
            }
       }
       
   }
    
    
}
