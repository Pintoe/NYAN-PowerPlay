package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utilities.datastructures.DataPool;
import org.firstinspires.ftc.teamcode.utilities.robot.PersistentData;
import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Claw;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Lift;
import org.firstinspires.ftc.teamcode.vision.simulatortests.distanceestimation.ConeDetectionTesting;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Example teleop code for a basic mecanum drive
 */
@TeleOp(name = "Updated Auto Align")
@Disabled
public class NewAutoAlignTeleop extends LinearOpMode {

    // Create new Instance of the robot
    RobotEx robot = RobotEx.getInstance();

    ConeDetectionTesting coneDetection;
    OpenCvCamera camera;
    String webcamName = "Webcam 1";

    @Override
    public void runOpMode() {
  /*

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        // Initialize the robot
        robot.init(hardwareMap, telemetry);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        coneDetection = new ConeDetectionTesting();
        camera.setPipeline(coneDetection);

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

        while (opModeInInit()) {
            telemetry.addData("Auto heading: ", PersistentData.heading);
            telemetry.update();
        }

        waitForStart();

        // Notify subsystems before loop
        robot.postInit();

        if (isStopRequested()) return;

        // Initialize variables for loop
        Gamepad currentFrameGamepad1 = new Gamepad();
        Gamepad currentFrameGamepad2 = new  Gamepad();

        Gamepad previousFrameGamepad1 = new Gamepad();
        Gamepad previousFrameGamepad2 = new Gamepad();

        robot.drivetrain.enableAntiTip();

        // Declare state variables
        boolean intakeOn = false;
        boolean intakeDirection = false;

        boolean robotCentric = true;

        double degreesError = 0;

        DataPool<Double> headingPool = new DataPool<>();

        robot.update();

        while(opModeIsActive()) {

            headingPool.addData(robot.internalIMU.getCurrentFrameHeadingCCW());
            // Retain information about the previous frame's gamepad
            try {
                previousFrameGamepad1.copy(currentFrameGamepad1);
                previousFrameGamepad2.copy(currentFrameGamepad2);

                currentFrameGamepad1.copy(gamepad1);
                currentFrameGamepad2.copy(gamepad2);
            } catch (RobotCoreException ignored) {

            }

            // Handle Intake State
*//*            if (currentFrameGamepad1.a != previousFrameGamepad1.a && currentFrameGamepad1.a) {
                intakeOn = !intakeOn;
                intakeDirection = false;
            }

            if (currentFrameGamepad1.b != previousFrameGamepad1.b && currentFrameGamepad1.b) {
                intakeOn = !intakeOn;
                intakeDirection = true;
            }*//*

            if (currentFrameGamepad1.right_trigger > 0.2 && previousFrameGamepad1.right_trigger < 0.2) {
                intakeOn = !intakeOn;
                intakeDirection = false;
            }

            if (currentFrameGamepad1.left_trigger > 0.2 && previousFrameGamepad1.left_trigger < 0.2) {
                intakeOn = !intakeOn;
                intakeDirection = true;
            }


            if (intakeOn) {
                robot.intake.enableIntakeMotor(intakeDirection);
            } else {
                robot.intake.disableIntakeMotor();
            }

            // Handle Claw State
            if (currentFrameGamepad2.b) {
                robot.claw.setClawState(Claw.ClawStates.CLOSED);
            } else if (currentFrameGamepad2.a) {
                robot.claw.setClawState(Claw.ClawStates.OPENED);
            }

            if (currentFrameGamepad1.y && previousFrameGamepad1.y != currentFrameGamepad1.y) {
                robotCentric = !robotCentric;
            }

            // Handle Drivetrain
            if (gamepad1.left_bumper) {
                robot.drivetrain.fieldCentricRotationPIDFromGamepad(
                        currentFrameGamepad1.left_stick_y,
                        currentFrameGamepad1.left_stick_x,
                        currentFrameGamepad1.right_stick_y,
                        currentFrameGamepad1.right_stick_x
                );
            } else if (currentFrameGamepad1.left_bumper) {

                if (currentFrameGamepad1.left_bumper != previousFrameGamepad1.left_bumper) {
*//*                    degreesError = headingPool.getDataAndRemoveAtTimestamp(
                            System.currentTimeMillis() -
                    );*//*
                }

                robot.drivetrain.fieldCentricRotationPIDFromGamepad(
                        currentFrameGamepad1.left_stick_y,
                        currentFrameGamepad1.left_stick_x,
                        Math.sin(degreesError),
                        Math.cos(degreesError)
                );

            } else {
                if (robotCentric) {
                    robot.drivetrain.robotCentricDriveFromGamepad(
                            currentFrameGamepad1.left_stick_y,
                            currentFrameGamepad1.left_stick_x,
                            currentFrameGamepad1.right_stick_x
                    );
                }
                else {
                    robot.drivetrain.fieldCentricDriveFromGamepad(
                            currentFrameGamepad1.left_stick_y,
                            currentFrameGamepad1.left_stick_x,
                            currentFrameGamepad1.right_stick_x
                    );
                }
            }



            // Handle Manual Lift State

*//*            if (currentFrameGamepad2.x) {
                robot.intake.ON_MOTOR_POWER = 0.75;
            }

            if (currentFrameGamepad2.y) {
                robot.intake.ON_MOTOR_POWER = 1;
            }*//*

            robot.lift.driveLiftFromGamepad(
                    currentFrameGamepad2.left_trigger,
                    currentFrameGamepad2.right_trigger
            );

            if (currentFrameGamepad2.dpad_up) {
                robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.HIGH_JUNCTION);
            } else if (currentFrameGamepad2.dpad_left) {
                robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.MIDDLE_JUNCTION);
            } else if (currentFrameGamepad2.dpad_right) {
                robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.LOW_JUNCTION);
            } else if (currentFrameGamepad2.dpad_down){
                robot.lift.setCurrentLiftTargetPosition(Lift.LIFT_POSITIONS.GROUND_JUNCTION);
            }

            if (currentFrameGamepad1.x) {
                robot.drivetrain.disableAntiTip();
            }


            // Handle Manual Extension State

*//*            robot.clawExtension.driveLiftFromGamepad(
                    gamepad2.right_stick_y
            );*//*


            *//*
            if (gamepad1.right_trigger > 0.1) {
                robot.lift.leftLiftMotor.setPower(gamepad1.right_trigger);
                robot.lift.rightLiftMotor.setPower(gamepad1.right_trigger);
            } else if (gamepad1.left_trigger > 0.1) {
                robot.lift.leftLiftMotor.setPower(-gamepad1.left_trigger);
                robot.lift.rightLiftMotor.setPower(-gamepad1.left_trigger);
            } else {
                robot.lift.leftLiftMotor.setPower(0);
                robot.lift.rightLiftMotor.setPower(0);
            }

             *//*

            double frameTime = robot.update();


            // telemetry.addData("IMU orientation: ", robot.internalIMU.getCurrentFrameOrientation());
            // telemetry.addData("CCW IMU orientation: ", robot.internalIMU.getCurrentFrameHeadingCCW());

            // telemetry.addData("CW IMU orientation: ", robot.internalIMU.getCurrentFrameHeadingCW());
            telemetry.addData("Robot Tilt : ", robot.internalIMU.getCurrentFrameTilt());
            // telemetry.addData("Robot Tilt Acceleration y: ", robot.internalIMU.getCurrentFrameVelocity().yRotationRate);

            // telemetry.addData("Joystick Orientation: ", Math.atan2(-currentFrameGamepad1.right_stick_x, -currentFrameGamepad1.right_stick_y));
*//*            telemetry.addData(
                    "Updated Joystick Orientation: ",
                    Math.atan2(currentFrameGamepad1.right_stick_y, currentFrameGamepad1.right_stick_x) - Math.PI / 2
            );*//*
            // ^ https://www.desmos.com/calculator/jp45vcfcbt

            telemetry.addData("Frame Time: ", frameTime);
            telemetry.addData("Refresh Rate: ", (frameTime != 0) ? (1000 / frameTime) : "inf");

            telemetry.update();

        }*/
    }
}
