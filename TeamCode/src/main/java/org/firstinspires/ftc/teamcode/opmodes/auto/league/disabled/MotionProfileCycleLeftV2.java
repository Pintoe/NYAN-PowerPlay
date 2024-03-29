package org.firstinspires.ftc.teamcode.opmodes.auto.league.disabled;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;
import org.firstinspires.ftc.teamcode.utilities.robot.movement.EncoderDrive;
import org.firstinspires.ftc.teamcode.utilities.robot.movement.MotionProfilingDrive;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Claw;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.vision.simulatortests.ApriltagDetectionPipeline;
import org.firstinspires.ftc.teamcode.vision.simulatortests.ParkingPosition;
import org.openftc.easyopencv.OpenCvCamera;

/**
 * Example teleop code for a basic mecanum drive
 */
@Autonomous(name = "Motion Profile Cycle Left V2")
@Disabled
public class MotionProfileCycleLeftV2 extends LinearOpMode {

    // Create new Instance of the robot
    RobotEx robot = RobotEx.getInstance();

    ApriltagDetectionPipeline sleeveDetection;
    OpenCvCamera camera;
    String webcamName = "Webcam 1";

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot.init(hardwareMap, telemetry);
/*
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        sleeveDetection = new ApriltagDetectionPipeline();
        camera.setPipeline(sleeveDetection);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(640,480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });*/
/*
        while (!isStarted()) {
            telemetry.addData("ROTATION: ", sleeveDetection.getParkingPosition());
            telemetry.update();
        }*/

        // scan sleeve

        // Initialize the robot

        robot.claw.setClawState(Claw.ClawStates.CLOSED);
        robot.claw.disableAutoClose();
        waitForStart();

        // Notify subsystems before loop
        robot.postInit();

        if (isStopRequested()) return;

        // robot.drivetrain.enableAntiTip();
        MotionProfilingDrive robotDrivetrain = new MotionProfilingDrive(this, telemetry);
        EncoderDrive robotDrivetrainE = new EncoderDrive(this, telemetry);

        robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.drivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // ParkingPosition parkPosition = sleeveDetection.getParkingPosition();

        ParkingPosition parkPosition = ParkingPosition.CENTER;
        // camera.stopStreaming();

        // robotDrivetrain.turnToIMUAngle(Math.toRadians(180));

        robotDrivetrain.driveForwardWithConstantHeading(-16, Math.toRadians(0));
        robotDrivetrainE.turnToIMUAngle(-Math.toRadians(90));
        robotDrivetrainE.turnToIMUAngle(Math.toRadians(180));
        robotDrivetrain.driveForwardWithConstantHeading(41, Math.toRadians(180));
        robotDrivetrain.driveForwardWithConstantHeading(-5, Math.toRadians(180));
        robotDrivetrainE.turnToIMUAngle(Math.toRadians(90));
        robotDrivetrain.driveForwardWithConstantHeading(20, Math .toRadians(90));
        robotDrivetrain.driveForwardWithConstantHeading(-31, Math.toRadians(90));
        robotDrivetrainE.turnToIMUAngle(Math.toRadians(0));
        robotDrivetrainE.turnToIMUAngle(Math.toRadians(90));
        robotDrivetrain.driveForwardWithConstantHeading(31, Math.toRadians(90));
        robotDrivetrain.driveForwardWithConstantHeading(-31, Math.toRadians(90));

/*        robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.DEFAULT);
        robotDrivetrainE.turnToIMUAngle(Math.toRadians(90));
        robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.LOW_JUNCTION);
        robotDrivetrain.driveForward(40);
        robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.GROUND_JUNCTION);
        robot.lift.incrementOffset(5);
        robot.pause(0.5);
        robot.claw.setClawState(Claw.ClawStates.CLOSED);
        robot.pause(0.5);
        robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.LOW_JUNCTION);
        robotDrivetrain.driveForward(-15);
        robotDrivetrainE.turnToIMUAngle(Math.toRadians(0));
        robotDrivetrain.driveForward(9);
        robot.pause(0.25);
        robot.claw.setClawState(Claw.ClawStates.OPENED);
        robot.pause(0.5);
        robot.claw.setClawState(Claw.ClawStates.SLIGHTLY_OPENED);
        robotDrivetrain.driveForward(-9);
        robotDrivetrainE.turnToIMUAngle(-Math.toRadians(90));
        robotDrivetrainE.strafeRightFromBB(10);*/

        // sleep(5000);

        switch (parkPosition) {
            case LEFT:
                robotDrivetrain.driveForward(30);
                break;
            case RIGHT:
                robotDrivetrain.driveForward(-15);
                break;
            case CENTER:
                robotDrivetrain.driveForward(10);
                break;
        }

//        robotDrivetrain.turnToIMUAngle(Math.toRadians(180));
        // robot.drivetrain.enableAntiTip();

        robot.persistData();

    }
}
