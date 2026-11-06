package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class FieldCentric extends LinearOpMode {
    private DcMotor FRMotor;
    private DcMotor FLMotor;
    private DcMotor BRMotor;
    private DcMotor BLMotor;
    private Servo Turret;

    IMU imu;

    @Override
    public void runOpMode() {

        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
        Turret = hardwareMap.get(Servo.class, "Turret");
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters myIMUparameters;

        FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        Turret.setPosition(0);

        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                                RevHubOrientationOnRobot.UsbFacingDirection.DOWN
                        )
                )
        );

        imu.resetYaw();

        waitForStart();

        while(opModeIsActive()) {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rx = gamepad1.right_stick_x;

            double turretPos = 0;

            if (gamepad1.options) {
                imu.resetYaw();
            }
            if (gamepad2.right_bumper) {
                turretPos+= 0.05092592592;
                Turret.setPosition(turretPos);
            }
            if (gamepad2.left_bumper) {
                turretPos-= 0.05092592592;
                Turret.setPosition(turretPos);
            }


            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            double rotX = (x * Math.cos(-heading)) - (y * Math.sin(-heading));
            double rotY = (y * Math.cos(-heading)) + (x * Math.sin(-heading));

            rotX = rotX * 1.1;

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            FLMotor.setPower(frontLeftPower);
            FRMotor.setPower(frontRightPower);
            BLMotor.setPower(backLeftPower);
            BRMotor.setPower(backRightPower);

        }
    }
}
