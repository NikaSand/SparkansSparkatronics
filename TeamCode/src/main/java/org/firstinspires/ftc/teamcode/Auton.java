package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.util.Range.clip;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous

public class Auton extends LinearOpMode {

    private DcMotor FRMotor;
    private DcMotor FLMotor;
    private DcMotor BRMotor;
    private DcMotor BLMotor;
    private DcMotor ITMotor;

    IMU imu;

    @Override
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

    private void setDrivePower(double left, double right) {
        FLMotor.setPower(left);
        BLMotor.setPower(left);
        FRMotor.setPower(right);
        BRMotor.setPower(right);
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

    public void fieldCentric() {
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;

        if (gamepad1.options) {
            imu.resetYaw();
        }

        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = (x*Math.cos(-heading)) - (y*Math.sin(-heading));
        double rotY = (y*Math.cos(-heading)) + (x*Math.sin(-heading));

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



    public void driveLesbian(double speed, double targetHeading, double durationSecond) {
        // usage, call, speed 0-1, targetHeading direction dependent, durationSecond time
        double Kp = 0.03; // move laterrr
        double Ki = 0.0; // move laterrr
        double Kd = 0.002; // move laterrr
        ElapsedTime runTimer = new ElapsedTime();
        ElapsedTime loopTimer = new ElapsedTime();
        double integral = 0;
        double lastError = 0;

        while (opModeIsActive() && runTimer.seconds() < durationSecond) {
            double dt = loopTimer.seconds();
            loopTimer.reset();
            double currentHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double error = AngleUnit.normalizeDegrees(targetHeading - currentHeading);

            integral += error * dt;
            integral = Range.clip(integral, -10, 10);
            double derivative = dt > 0 ? (error - lastError) / dt : 0;
            lastError = error;

            double correction = Kp * error + Ki * integral + Kd * derivative;
            correction = Range.clip(correction, -0.3, 0.3);

            double leftPower = speed - correction;
            double rightPower = speed + correction;

            double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
            if (max > 1.0) { leftPower /= max; rightPower /= max; }

            setDrivePower(leftPower, rightPower);

            telemetry.addData("err", error);
            telemetry.update();
        }
        setDrivePower(0, 0);
        sleep(100);
    }

    public void imuTurnToHeading(double targetHeading, double toleranceDeg, double timeoutSeconds) {
        double Kp = 0.03; // move laterrr
        double Ki = 0.0; // move laterrr
        double Kd = 0.002; // move laterrr
        ElapsedTime timeoutTimer = new ElapsedTime();
        ElapsedTime loopTimer = new ElapsedTime();
        ElapsedTime settleTimer = new ElapsedTime();
        double integral = 0;
        double lastError = AngleUnit.normalizeDegrees(
                targetHeading - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));

        while (opModeIsActive() && timeoutTimer.seconds() < timeoutSeconds) {
            double dt = loopTimer.seconds();
            loopTimer.reset();

            double currentHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double error = AngleUnit.normalizeDegrees(targetHeading - currentHeading);

            if (Math.abs(error) > toleranceDeg) settleTimer.reset();
            if (settleTimer.seconds() > 0.2) break;

            integral += error * dt;
            integral = Range.clip(integral, -10, 10);
            double derivative = dt > 0 ? (error - lastError) / dt : 0;
            lastError = error;

            double correction = Kp * error + Ki * integral + Kd * derivative;
            correction = Range.clip(correction, -0.6, 0.6);

            setDrivePower(-correction, correction);
        }
        setDrivePower(0, 0);
        sleep(100);
    }


}
