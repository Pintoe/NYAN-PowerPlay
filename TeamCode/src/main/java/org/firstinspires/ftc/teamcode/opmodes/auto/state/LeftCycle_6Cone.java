package org.firstinspires.ftc.teamcode.opmodes.auto.state;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utilities.math.linearalgebra.Pose;
import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;
import org.firstinspires.ftc.teamcode.utilities.robot.movement.EncoderDrive;
import org.firstinspires.ftc.teamcode.utilities.robot.movement.MotionProfileLocalizerDrive;
import org.firstinspires.ftc.teamcode.utilities.robot.movement.MotionProfileLocalizerLineDrive;
import org.firstinspires.ftc.teamcode.utilities.robot.movement.MotionProfilingDrive;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Claw;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.ClawExtension;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.ClawRotation;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.ClawTilt;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.vision.simulatortests.ApriltagDetectionPipeline;
import org.firstinspires.ftc.teamcode.vision.simulatortests.ParkingPosition;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "Left Cycle 1+2 Low")
public class LeftCycle_6Cone extends LinearOpMode {

    // Create new Instance of the robot
    RobotEx robot = RobotEx.getInstance();

    ApriltagDetectionPipeline sleeveDetection;
    OpenCvCamera camera;
    String webcamName = "Webcam 1";

    public void gotoLow() {
        robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.LOW_JUNCTION);
        robot.clawTilt.setCurrentState(ClawTilt.tiltState.ACTIVE);
    }

    public void rotateExtend(ClawRotation.rotationState rotationState) {
        robot.clawRotation.setCurrentState(rotationState);
        while (!robot.clawRotation.atPosition()) {
            robot.update();
        }

        robot.clawExtension.setCurrentExtensionState(ClawExtension.ExtensionState.MID);
        while (!robot.clawExtension.isAtPosition()) {
            robot.update();
        }
    }

    public void retract() {
        robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.DEFAULT);
    }

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        robot.init(hardwareMap, telemetry);

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
        });

        while (!isStarted()) {
            telemetry.addData("ROTATION: ", sleeveDetection.getParkingPosition());
            telemetry.update();
        }

        // scan sleeve

        // Initialize the robot

        robot.claw.setClawState(Claw.ClawStates.CLOSED);
        robot.claw.disableAutoClose();
        robot.claw.clawGrabberServo.setPosition(robot.claw.getServoPosition(Claw.ClawStates.CLOSED));
        waitForStart();

        ParkingPosition parkPosition = sleeveDetection.getParkingPosition();

        // Notify subsystems before loop
        robot.claw.setClawState(Claw.ClawStates.CLOSED);
        robot.postInit();

        if (isStopRequested()) return;

        // robot.drivetrain.enableAntiTip();
        MotionProfileLocalizerLineDrive robotDrivetrain = new MotionProfileLocalizerLineDrive(this, telemetry);

        robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.drivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        robot.internalIMU.setHeadingOffset(-robot.internalIMU.getCurrentFrameHeadingCCW());

        robot.localizer.setPoseEstimate(
                new Pose2d(0, 0, 0)
        );

        double openPosition = Claw.openPosition;
        double tiltPosition = ClawTilt.activePosition;
        Claw.openPosition = Claw.slightlyOpenPosition;
        this.gotoLow();
        robot.clawRotation.setAngle(42);
        robot.clawRotation.yieldTillAtPosition();
        robot.pause(0.5);
        robot.clawExtension.setCurrentExtensionState(ClawExtension.ExtensionState.ACTIVE);
        robot.clawExtension.yieldTillAtPosition();
        robot.lift.setOffset(-2);
        robot.pause(0.5);
        robot.claw.setClawState(Claw.ClawStates.OPENED);
        this.retract();
        robotDrivetrain.forwardXToPose(new Pose2d(50, 0, Math.toRadians(0)));
        robotDrivetrain.turnToAngle(Math.toRadians(-90));
        robot.lift.setOffset(8);
        robot.clawExtension.setCurrentExtensionState(ClawExtension.ExtensionState.ACTIVE);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -13.1, Math.toRadians(-90)));
        robot.clawExtension.yieldTillAtPosition();
        robot.pause(0.5);
        robot.lift.setOffset(4);
        robot.pause(1);
        robot.claw.setClawState(Claw.ClawStates.CLOSED);
        robot.pause(1);
        this.gotoLow();
        robot.lift.setOffset(3);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -6.5, Math.toRadians(-90)));
        robot.clawRotation.setCurrentState(ClawRotation.rotationState.LEFT);
        robot.clawRotation.yieldTillAtPosition();
        robot.pause(0.75);
        robot.lift.setOffset(-2);
        robot.pause(1);
        robot.claw.setClawState(Claw.ClawStates.OPENED);
        this.retract();
        robot.lift.setOffset(3);
        robot.pause(0.1);
        robot.clawExtension.setCurrentExtensionState(ClawExtension.ExtensionState.ACTIVE);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -12.5, Math.toRadians(-90)));
        robot.clawExtension.yieldTillAtPosition();
        robot.pause(1);
        robot.claw.setClawState(Claw.ClawStates.CLOSED);
        robot.pause(1);
        this.gotoLow();
        robot.lift.setOffset(3);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -6, Math.toRadians(-90)));
        robot.clawRotation.setCurrentState(ClawRotation.rotationState.LEFT);
        robot.pause(1);
        robot.clawRotation.yieldTillAtPosition();
        robot.lift.setOffset(-2);
        robot.pause(1);
        robot.claw.setClawState(Claw.ClawStates.OPENED);
        this.retract();
        robot.clawRotation.setCurrentState(ClawRotation.rotationState.LEFT);

        ClawTilt.activePosition = tiltPosition;
/*        robot.lift.setOffset(2);
        robot.pause(0.1);
        robot.clawExtension.setCurrentExtensionState(ClawExtension.ExtensionState.ACTIVE);
        robotDrivetrain.strafeYToPose(new Pose2d(52.5, -11.5, Math.toRadians(-90)));
        robot.clawExtension.yieldTillAtPosition();
        robot.pause(0.75);
        robot.claw.setClawState(Claw.ClawStates.CLOSED);
        robot.pause(0.5);
        this.gotoLow();
        robot.lift.setOffset(3);
        robotDrivetrain.strafeYToPose(new Pose2d(52.5, -6, Math.toRadians(-90)));
        robot.clawRotation.setCurrentState(ClawRotation.rotationState.LEFT);
        robot.pause(0.5);
        robot.clawRotation.yieldTillAtPosition();
        robot.lift.setOffset(-2);
        robot.pause(1);
        robot.claw.setClawState(Claw.ClawStates.OPENED);
        this.retract();
        robot.clawRotation.setCurrentState(ClawRotation.rotationState.LEFT);*/
/*        robot.lift.setOffset(0);
        robot.pause(0.1);
        robot.clawExtension.setCurrentExtensionState(ClawExtension.ExtensionState.ACTIVE);
        robotDrivetrain.strafeYToPose(new Pose2d(51.5, -14, Math.toRadians(-90)));
        robot.clawExtension.yieldTillAtPosition();
        robot.pause(0.75);
        robot.claw.setClawState(Claw.ClawStates.CLOSED);
        robot.pause(0.5);
        this.gotoLow();
        robot.lift.setOffset(3);
        robotDrivetrain.strafeYToPose(new Pose2d(51.5, -9.5, Math.toRadians(-90)));
        robot.clawRotation.setCurrentState(ClawRotation.rotationState.LEFT);
        robot.pause(0.5);
        robot.clawRotation.yieldTillAtPosition();
        robot.lift.setOffset(-2);
        robot.pause(1);
        robot.claw.setClawState(Claw.ClawStates.OPENED);
        this.retract();*/
        switch (parkPosition) {
            case RIGHT:
                robotDrivetrain.strafeYToPose(new Pose2d(52, 24, Math.toRadians(-90)));
                break;
            case CENTER:
                robotDrivetrain.strafeYToPose(new Pose2d(52, 0, Math.toRadians(-90)));
                break;
            case LEFT:
                robotDrivetrain.strafeYToPose(new Pose2d(52,-20, Math.toRadians(-90)));
                break;

        }

        robotDrivetrain.turnToAngle(0);
        robotDrivetrain.forwardXToPose(new Pose2d(0, 0, 0));
        Claw.openPosition = openPosition;
        ClawRotation.leftPosition = 0.85;



/*        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -12, Math.toRadians(-90)));
        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -15.5, Math.toRadians(-90)));
        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -12, Math.toRadians(-90)));
        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -15.5, Math.toRadians(-90)));
        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -12, Math.toRadians(-90)));
        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -15.5, Math.toRadians(-90)));
        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -12, Math.toRadians(-90)));
        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -15.5, Math.toRadians(-90)));
        robot.pause(1);
        robotDrivetrain.strafeYToPose(new Pose2d(50, -12, Math.toRadians(-90)));
        robot.pause(1);*/

        robot.pause(1);
        robot.persistData();

    }
}
