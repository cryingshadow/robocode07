package de.metro.robocode;

import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

public class Guenter extends Robot {

    private static final int NORTH = 0;
    private static final int EAST = 90;
    private static final int SOUTH = 180;
    private static final int WEST = 270;

    private static int consecutiveRobotScans = 0;

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

        final double radius = Rules.MAX_VELOCITY * 2;

        setAdjustRadarForGunTurn( false );

        int counter = 0;
        while ( true ) {
            if ( !reachedWall ) {
                goForTheWall( getHeading() );
                turnGunEast();
            } else {
                ahead( radius );
                switch ( counter++ % 3 ) {
                    case 0:
                        turnGunNorth();
                        break;
                    case 1:
                        turnGunEast();
                        break;
                    case 2:
                        turnGunSouth();
                        break;
                    default:
                        turnGunEast();
                }
            }
        }
    }

    private void turnGunEast(  ) {
        final double gunHeading = getGunHeading();
        if ( gunHeading > EAST && gunHeading < WEST ) {
            turnGunLeft( gunHeading - EAST );
        } else if ( gunHeading < EAST ) {
            turnGunRight( EAST - gunHeading );
        } else { //heading > WEST
            turnGunRight( gunHeading - WEST + EAST );
        }
    }

    private void turnGunNorth() {
        final double gunHeading = getGunHeading();
        if ( gunHeading < SOUTH ) {
            turnGunLeft( gunHeading );
        } else {
            turnGunRight( 360 - gunHeading );
        }
    }

    private void turnGunSouth() {
        final double gunHeading = getGunHeading();
        if ( gunHeading < SOUTH ) {
            turnGunRight( SOUTH - gunHeading );
        } else {
            turnGunLeft( gunHeading - SOUTH );
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
        consecutiveRobotScans++;
        fire( getBulletPower( e.getDistance(), consecutiveRobotScans ) );
        if ( consecutiveRobotScans > 3 && consecutiveRobotScans % 2 == 0 ) {
            ahead( Rules.MAX_VELOCITY );
        }
        scan();
        consecutiveRobotScans = 0;
    }

    public double getBulletPower( final double distance, final int consecutiveScans ) {
        return Rules.MAX_BULLET_POWER - ( ( distance / fieldWidth ) * Rules.MAX_BULLET_POWER )
                + consecutiveScans > 3
                    ? 2
                    : 0;
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
            turnGunEast();
        }
    }

    @Override
    public void onHitRobot( final HitRobotEvent event ) {
        if ( event.getBearing() < 0 ) {
            turnGunLeft( -event.getBearing() );
        } else {
            turnGunRight( event.getBearing() );
        }
    }

}
