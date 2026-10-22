package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class FieldCentric {
    private DcMotor FRMotor;
    private DcMotor FLMotor;
    private DcMotor BRMotor;
    private DcMotor BLMotor;

    IMU imu;

    public void runOpMode() {

        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters myIMUparameters;

        FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                        )
                )
        );
        imu.resetYaw();
    }
        public void fieldCentric() {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rx = gamepad1.right_stick_x;

            if (gamepad1.options) {
                imu.resetYaw();
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
