package de.metro.robocode;

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

        while ( true ) {
            if ( !reachedWall ) {
                goForTheWall( getHeading() );
            } else {
                turnGunEast( getGunHeading() );
                ahead( radius );
                //                if ( getX() < 30 ) {
                //                    fireBullet( 1 );
                //                } else {
                //                    turnLeft( 180 );
                //                }
            }
        }
    }

    private void turnGunEast( final double gunHeading ) {
        if ( gunHeading < EAST && gunHeading > WEST ) {
            turnGunRight( WEST - gunHeading );
        } else if ( gunHeading > EAST ) {
            turnGunLeft( gunHeading + EAST );
        } else { //heading < WEST
            turnGunLeft( gunHeading - WEST );
        }
    }

    private void turnRadarEast( final double radarHeading ) {
        if ( radarHeading > EAST && radarHeading < WEST ) {
            turnRadarRight( WEST - radarHeading );
        } else if ( radarHeading < EAST ) {
            turnRadarLeft( radarHeading + EAST );
        } else { //heading > WEST
            turnRadarLeft( radarHeading - WEST );
        }
    }

    private void goForTheWall( final double heading ) {
        if ( heading == WEST ) {
            ahead( getX() );
        } else if ( heading > EAST && heading < WEST ) {
            turnRight( WEST - heading );

        } else if ( heading < EAST ) {
            turnLeft( heading + EAST );
        } else { //heading > WEST
            turnLeft( heading - WEST );
        }
    }

    private void turnNorth( final double heading ) {
        if ( heading > SOUTH ) {
            turnRight( 360 - heading );
        } else {
            turnLeft( heading );
        }
    }

    @Override
    public void onScannedRobot( final ScannedRobotEvent e ) {
        if ( fieldWidth > 0 ) {

        }
        e.getDistance();
        // Rules.MAX_BULLET_POWER;
        fire( 10 );
    }

    //    @Override
    //    public void onHitByBullet( final HitByBulletEvent e ) {
    //        turnLeft( 90 - e.getBearing() );
    //    }

    @Override
    public void onHitWall( final HitWallEvent event ) {
        if ( !reachedWall ) {
            turnNorth( getHeading() );
            if ( getHeading() == NORTH ) {
                reachedWall = true;
            }
        } else {
            turnLeft( 180 );
        }
    }

}
