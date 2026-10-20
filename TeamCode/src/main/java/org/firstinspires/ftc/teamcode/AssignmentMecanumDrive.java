package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class AssignmentMecanumDrive{
    private DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    private IMU imu;
    public void init(HardwareMap hwMap) {
        frontLeftMotor = hwMap.get(DcMotor.class,"front_left_motor"); //the strings are our motor names (Idk them yet)
        backLeftMotor = hwMap.get(DcMotor.class, "back_left_motor");
        frontRightMotor = hwMap.get(DcMotor.class, "front_right_motor");
        backRightMotor = hwMap.get(DcMotor.class, "back_right_motor");

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hwMap.get(IMU.class,"imu");

        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);

        imu.initialize(new IMU.Parameters(RevOrientation));

    }

    public void drive(double forward,double strafe, double rotate){
        double frontLeftpower = forward + strafe + rotate;
        double backLeftpower = forward - strafe + rotate;
        double frontRightpower = forward - strafe - rotate;
        double backRightpower = forward + strafe - rotate;

        double maxPower = 1.0;
        double maxSpeed = 1.0;

        maxPower = Math.max(maxPower,Math.abs(frontLeftpower));
        maxPower = Math.max(maxPower,Math.abs(backLeftpower));
        maxPower = Math.max(maxPower,Math.abs(frontRightpower));
        maxPower = Math.max(maxPower,Math.abs(backRightpower));

        frontLeftMotor.setPower(maxSpeed * frontLeftpower / maxPower);
        backLeftMotor.setPower(maxSpeed * backLeftpower / maxPower);
        frontRightMotor.setPower(maxSpeed * frontRightpower / maxPower);
        backRightMotor.setPower(maxSpeed * backRightpower / maxPower);
    }

    public void driveFieldRelavite(double forward, double strafe, double rotate){
        double theta = Math.atan2(forward, strafe);
        double r = Math.hypot(strafe, forward);

        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        double newForward = r * Math.sin(theta);
        double newStrafe = r * Math.cos(theta);

        this.drive(newForward,newStrafe,rotate);

    }
}