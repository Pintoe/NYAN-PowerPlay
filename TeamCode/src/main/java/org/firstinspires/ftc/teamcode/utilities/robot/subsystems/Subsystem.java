package org.firstinspires.ftc.teamcode.utilities.robot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Subsystem {

    void onInit(HardwareMap hardwareMap);

    void onOpmodeStarted();

    void onCyclePassed();

}