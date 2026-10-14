package org.firstinspires.ftc.teamcode;

//IN THIS CODE WE WILL TRY TO INCORPORATE IMU BOTPOSE TO MECANUM DRIVETRAIN


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

@TeleOp
public class IMU_test_mecanum extends LinearOpMode {
    private DcMotor FLMotor;
    private DcMotor FRMotor;
    private DcMotor BLMotor;
    private DcMotor BRMotor;

    IMU imu;



    @Override
    public void runOpMode() {
        // find the data stuff

        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor"); //port 0
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor"); //port 1
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor"); //port 2
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor"); //port 3
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters myIMUparameters;

        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                        )
                )
        );

        waitForStart();

        while (opModeIsActive()) {
            // setting the power and the equations for cool wheel things
            double ForwardBack = -gamepad1.left_stick_y;
            double LeftRight = gamepad1.left_stick_x;
            double Rotate = -gamepad1.right_stick_x;
            double SpeedMultiplier = 1.0;

            double FLP = (ForwardBack + LeftRight + Rotate) * SpeedMultiplier;
            double FRP = -(ForwardBack - LeftRight - Rotate) * SpeedMultiplier;
            double RLP = (ForwardBack - LeftRight + Rotate) * SpeedMultiplier;
            double RRP = -(ForwardBack + LeftRight - Rotate) * SpeedMultiplier;

            // Clip power to ensure it stays within -1.0 to 1.0
            FLP = Range.clip(FLP, -1.0, 1.0);
            FRP = Range.clip(FRP, -1.0, 1.0);
            RLP = Range.clip(RLP, -1.0, 1.0);
            RRP = Range.clip(RRP, -1.0, 1.0);

            // Set motor power
            FLMotor.setPower(FLP);
            FRMotor.setPower(FRP);
            BLMotor.setPower(RLP);
            BRMotor.setPower(RRP);
        }
    }
}