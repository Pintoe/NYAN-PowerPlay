package org.firstinspires.ftc.teamcode.utilities.robot;

public class DriveConstants {


    public static final double WHEEL_TICKS = 537.7;
    public static final double WHEEL_SIZE = 3.78/2;

    public static final double INCHES_PER_REVOLUTION = 2 * WHEEL_SIZE * Math.PI;

    public static double getEncoderTicksFromInches(double inches) {
        return (inches / DriveConstants.INCHES_PER_REVOLUTION) * DriveConstants.WHEEL_TICKS;
    }

    public static final double BANG_BANG_POWER = -0.25;
    public static final double TICK_THRESHOLD = 50;
    public static final double ANGLE_AT_TIME = 3;

    public static final double TURN_THRESHOLD = Math.toRadians(10);

    public static double MAX_VELOCITY = 10;
    public static double MAX_ACCELERATION = 10;
}
