package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class MainTeleOp extends LinearOpMode {
    private DcMotor FLMotor;
    private DcMotor FRMotor;
    private DcMotor BLMotor;
    private DcMotor BRMotor;
    private DcMotor upIntake;
    private Servo Turret;

    IMU imu;

    @Override
    public void runOpMode() {
        // find the data stuff
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor"); //port0
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor"); //port1
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor"); //port2
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor"); //port3
        upIntake = hardwareMap.get(DcMotor.class, "upIntake");
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

        while (opModeIsActive()) {

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
            if (gamepad2.right_trigger_pressed) {
                upIntake.setPower(100);
            }
            // setting the power and the equations for cool wheel things
            double ForwardBack = -gamepad1.left_stick_y; //-
            double Rotate = -gamepad1.left_stick_x;
            double LeftRight = gamepad1.right_stick_x; //-
            double SpeedMultiplier = 1.0;

            double FLP = -(ForwardBack + LeftRight + Rotate) * SpeedMultiplier;
            double FRP = (ForwardBack - LeftRight - Rotate) * SpeedMultiplier;
            double RLP = -(ForwardBack - LeftRight + Rotate) * SpeedMultiplier;
            double RRP = (ForwardBack + LeftRight - Rotate) * SpeedMultiplier;

            // Clip power to ensure it stays within -1.0 to 1.0
            FLP = Range.clip(FLP, -1.0, 1.0);
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