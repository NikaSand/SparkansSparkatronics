package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Jar_Limelight_test extends OpMode {
    private Limelight3A limelight;
    private IMU imu;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class,"limelight"); //"Limelight" is the name
        limelight.pipelineSwitch(0); // Number is april tag
        imu = hardwareMap.get(IMU.class,"imu");
        RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(com.qualcomm.hardware.rev.RevHubOrientationOnRobot.LogoFacingDirection.UP,
                com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));
    }

    @Override
    public void start() {
        limelight.start();
    }

    @Override
    public void loop() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw());
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()){
            Pose3D botpose = result.getBotpose_MT2();
            telemetry.addData("Tx",result.getTx());
            telemetry.addData("Ty",result.getTy());
            telemetry.addData("Area",result.getTa());
            telemetry.addData("BotPose",botpose.toString());
            telemetry.addData("Yaw", botpose.getOrientation().getYaw());
        }
    }
    @Override
    public void stop(){
        
    }

}