package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.util.Range.clip;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous

public class Auton extends LinearOpMode {

    private DcMotor FRMotor;
    private DcMotor FLMotor;
    private DcMotor BRMotor;
    private DcMotor BLMotor;

    IMU imu;

    @Override
    public void runOpMode() {

        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
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
        // auton put here peoples

        if (opModeIsActive()) {

            Translate(0,1,0);
            sleep(1000);


            FRMotor.setPower(0);
            BRMotor.setPower(0);
            FLMotor.setPower(0);
            BLMotor.setPower(0);

        }

    }

    private void Translate(double x, double y, double r)
    {
        double ForwardBack = y;
        double LeftRight = x;
        double Rotate = r;
        double SpeedMultiplier = 1.0;
        double FLP = (ForwardBack + LeftRight + Rotate) * SpeedMultiplier;
        double FRP = -(ForwardBack - LeftRight - Rotate) * SpeedMultiplier;
        double RLP = (ForwardBack - LeftRight + Rotate) * SpeedMultiplier;
        double RRP = -(ForwardBack + LeftRight - Rotate) * SpeedMultiplier;


        // Clip power to ensure it stays within -1.0 to 1.0
        FLP = clip(FLP, -1.0, 1.0);
        FRP = clip(FRP, -1.0, 1.0);
        RLP = clip(RLP, -1.0, 1.0);
        RRP = clip(RRP, -1.0, 1.0);

        // Set motor power
        FLMotor.setPower(FLP);
        FRMotor.setPower(FRP);
        BLMotor.setPower(RLP);
        BRMotor.setPower(RRP);
    }

    public void driveLesbian(double speed, double targetHeading, double durationSecond) {
        // usage, call, speed 0-1, targetHeading direction dependent, durationSecond time
        double Kp = 0.3; // move laterrr
        ElapsedTime runTimer = new ElapsedTime();

        while (opModeIsActive() && runTimer.seconds() < durationSecond) {
            double currentHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double error = targetHeading - currentHeading;

            while (error > 180) error -=360;
            while (error < -180) error +=360;

            double correction = error * Kp;
            correction Range.clip(correction, -0.3, 0.3);

        }


    }


}
