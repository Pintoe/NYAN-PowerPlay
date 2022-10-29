package org.firstinspires.ftc.teamcode.utilities.robot.extensions;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.checkerframework.checker.index.qual.LTEqLengthOf;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.InternalIMU;

import java.util.Base64;

public class EncoderDrive {

    public static final double WHEEL_TICKS = 537.7;
    public static final double WHEEL_SIZE = 3.78/2;

    public static final double INCHES_PER_REVOLUTION = 2 * WHEEL_SIZE * Math.PI;

    public static double getEncoderTicksFromInches(double inches) {
        double ticks = (inches / EncoderDrive.INCHES_PER_REVOLUTION) * WHEEL_TICKS;
        return ticks;
    }

    private final double BANG_BANG_POWER = -0.5;
    private final double TICK_THRESHOLD = 50;
    private final double ANGLE_AT_TIME = 3;

    private final double TURN_THRESHOLD = Math.toRadians(10);

    RobotEx robot = RobotEx.getInstance();

    InternalIMU imu = robot.internalIMU;
    Drivetrain dt = robot.drivetrain;

    Telemetry telemetry;

    public EncoderDrive() {}

    public EncoderDrive(Telemetry t) {this.telemetry = t; }
    public void driveForwardFromInchesBB(double inches) {
        double ticksToMove = EncoderDrive.getEncoderTicksFromInches(inches);

        int[] startEncoderPosition = this.dt.getCWMotorTicks();
        double startPosition = Drivetrain.getAverageFromArray(startEncoderPosition);

        double targetPosition = startPosition + ticksToMove;

        boolean targetReached = false;

        while (!targetReached) {

            ;
            double currentFramePosition = Drivetrain.getAverageFromArray(this.dt.getCWMotorTicks());

            double error = targetPosition - currentFramePosition;

            double currentFramePower;

            if (error < TICK_THRESHOLD) {
                currentFramePower = 0;
                targetReached = true;
            } else if (error > 0) {
                currentFramePower = this.BANG_BANG_POWER;
            } else {
                currentFramePower = -this.BANG_BANG_POWER;
            }

            this.dt.robotCentricDriveFromGamepad(
                    currentFramePower,
                    0,
                    0
            );

            if (this.telemetry != null) {
                telemetry.addData("Error: ", error);
                telemetry.addData("pos: ", currentFramePosition);
                telemetry.addData("Start: ", startPosition);
                telemetry.update();

            }
            this.robot.update();


        }

    }

    public void turnToIMUAngle(double angle) {


        double currentIMUPosition = this.imu.getCurrentFrameHeadingCCW();
        double turnError = Double.MAX_VALUE;

        ElapsedTime turnTimer = new ElapsedTime();

        boolean atTarget = false;

        double atTargetStartTime = -1;
        while (!atTarget) {
            turnError = this.dt.headingPID.getOutputFromError(angle, currentIMUPosition);


            this.dt.robotCentricDriveFromGamepad(
                    0,
                    0,
                    Math.min(Math.max(turnError, -1), 1)
            );

            currentIMUPosition = this.imu.getCurrentFrameHeadingCCW();

            if (Math.abs(turnError) < TURN_THRESHOLD) {
                if ((turnTimer.milliseconds() - atTargetStartTime) / 1000 > ANGLE_AT_TIME) {
                    atTarget = true;
                } else if (atTargetStartTime == -1) {
                    atTargetStartTime = turnTimer.milliseconds();
                }
            } else {
                atTargetStartTime = -1;
            }

            telemetry.addData("Time in between: ", atTargetStartTime);
            this.robot.update();
        }





    }

}