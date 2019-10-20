/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myfirstrobot;
import robocode.*;

/**
 *
 * @author dani
 */
public class MyThirdRobot extends AdvancedRobot {
    int moveDirection=1;
    
    @Override
    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true); 
        while(true)turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double absBearing= e.getBearingRadians() + getHeadingRadians();
        double latVel=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);
        double bulletPower = 1.0 + Math.random()*2.0;
        
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
        double predictedX = getX() + e.getDistance() * Math.sin(absBearing);
        double predictedY = getY() + e.getDistance() * Math.cos(absBearing);
        for(double deltaTime = 0; deltaTime*(20.0-3.0*bulletPower) < Math.hypot(getX()-predictedX, getY()-predictedY);++deltaTime){
            predictedX += Math.sin(e.getHeadingRadians()) * e.getVelocity();
            predictedY += Math.cos(e.getHeadingRadians()) * e.getVelocity();
            if(predictedX < getWidth() || predictedY < getHeight()|| predictedX > getBattleFieldWidth() - getWidth()|| predictedY > getBattleFieldHeight() - getHeight()){
                predictedX = Math.min(Math.max(getWidth(),predictedX), getBattleFieldWidth() - getWidth());
                predictedY = Math.min(Math.max(getHeight(), predictedY), getBattleFieldHeight() - getHeight());
                break;
            }
        }
        
        double theta =robocode.util.Utils.normalAbsoluteAngle(Math.atan2(predictedX - getX(), predictedY - getY()));
        setTurnRadarRightRadians(getRadarTurnRemainingRadians());
        if(e.getDistance() > 150){
            setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(theta-getGunHeadingRadians()));
            setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity()));
            setAhead((e.getDistance() - Math.random()*50 - 120)*moveDirection);
            setFire(bulletPower);
        }else{
            setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(theta- getGunHeadingRadians()));
            setTurnLeft(-90-e.getBearing());
            setAhead(e.getDistance()*moveDirection);
            setFire(3);
        }
                
        
    }
    @Override
    public void onHitWall(HitWallEvent e){
        moveDirection=-moveDirection;//reverse direction upon hitting a wall
    }
}
