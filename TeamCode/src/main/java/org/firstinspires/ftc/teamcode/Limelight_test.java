package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode; //Put this

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;


//THIS CODE IS GOOD FOR DISTANCE


public class Limelight_test extends OpMode {
    private Limelight3A limelight;
    //private IMU imu;

    private DcMotor turret;

    @Override
    public void init() {
        turret = hardwareMap.get(DcMotor.class,"turret");
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

        Tx

        if (llResult != null && llResult.isValid()) {
            Pose3D botPose = llResult.getBotpose();
            List<LLResultTypes.FiducialResult> tagId = llResult.getFiducialResults(); // Extracts the AprilTag ID number
            telemetry.addData("AprilTag ID", tagId);
            telemetry.addData("Tx", llResult.getTx());
            telemetry.addData("Ty", llResult.getTy());
            telemetry.addData("Ta", llResult.getTa());
            //telemetry.addData("Botpose", botpose.toString());
            telemetry.update();

        }
        if (Tx < 1)

    }

    @Override
    public void stop() {
        limelight.stop();
        turret.setPower(0);
    }
}
