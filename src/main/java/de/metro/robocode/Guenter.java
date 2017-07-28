package de.metro.robocode;

import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class Guenter extends Robot {

    private static final int NORTH = 0;
    private static final int EAST = 90;
    private static final int SOUTH = 180;
    private static final int WEST = 270;

    double fieldWidth = 0;
    double fieldHeight = 0;
    boolean initialized = false;
    boolean reachedWall = false;

    @Override
    public void run() {

        if ( !initialized ) {
            fieldWidth = getBattleFieldWidth();
            fieldHeight = getBattleFieldHeight();
        }

        final double radius = 100.0;
        final double angle = 90.0;

        while ( !reachedWall ) {
            goForTheWall();

        }
        while ( true ) {
            ahead( radius );
            turnLeft( angle );
            turnGunLeft( angle );
            fireBullet( getEnergy() );
        }
    }

    private void goForTheWall() {
        final double heading = getHeading();
        if ( heading == WEST ) {
            ahead( 10 );
        } else if ( heading > EAST && heading < WEST ) {
            turnRight( WEST - heading );

        } else if ( heading < EAST ) {
            turnLeft( heading + EAST );
        } else { //heading > WEST
            turnLeft( heading - WEST );
        }
    }

    @Override
    public void onScannedRobot(final ScannedRobotEvent e) {
        fire( 1 );
    }

    @Override
    public void onHitByBullet(final HitByBulletEvent e) {
        turnLeft( 90 - e.getBearing() );
    }

    @Override
    public void onHitWall( final HitWallEvent event ) {
        if ( getY() > fieldHeight / 2 ) {
            turnLeft( event.getBearing() );
        } else {
            turnRight( event.getBearing() );
        }
        reachedWall = true;
    }

}
