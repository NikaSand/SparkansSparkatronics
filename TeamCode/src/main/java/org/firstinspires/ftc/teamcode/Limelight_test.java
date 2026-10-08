package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;


//THIS CODE IS GOOD FOR DISTANCE


public class Limelight_test extends OpMode {
    private Limelight3A limelight;
    //private IMU imu;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        //if delay then put start here
        //imu = hardwareMap.get(IMU.class, "imu");
        // RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
        //         RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        //imu.initialize(new IMU.Parameters(revHubOrientationOnRobot))
    }

    @Override
    public void start() {
        limelight.start();
    }

    @Override
    public void loop() {
        //YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles(); **THESE ONLY FOR W/ IMU
        //limelight.updateRobotOrientation(orientation.getYaw());
        LLResult llResult = limelight.getLatestResult(); //this pulls data from the limelight
        if (llResult != null && llResult.isValid()) {
            Pose3D botPose = llResult.getBotpose();
            telemetry.addData("Tx", llResult.getTx());
            telemetry.addData("Ty", llResult.getTy());
            //telemetry.addData("Botpose", botpose.toString());

        }

    }

}
