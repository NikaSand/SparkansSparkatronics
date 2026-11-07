package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

public class RealTeleOp extends LinearOpMode {
    private DcMotor FLMotor;
    private DcMotor FRMotor;
    private DcMotor BLMotor;
    private DcMotor BRMotor;
    private DcMotor downIntake;
    private DcMotor upIntake;
    private CRServo Turret;
    private CRServo Shooter;
    private Limelight3A limelight;

    IMU imu;

    @Override
    public void runOpMode() {
        // find the data stuff
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor"); //port0
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor"); //port1
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor"); //port2
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor"); //port3
        downIntake = hardwareMap.get(DcMotor.class, "downIntake");
        upIntake = hardwareMap.get(DcMotor.class, "upIntake");
        Turret = hardwareMap.get(CRServo.class, "Turret");
        Shooter = hardwareMap.get(CRServo.class, "Shooter");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters myIMUparameters;




        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                                RevHubOrientationOnRobot.UsbFacingDirection.DOWN
                        )
                )
        );

        imu.resetYaw();
        limelight.pipelineSwitch(0);
        //Turret.setPosition(0); IF NO LIMELIGHT
        double turretPos = 0;



        waitForStart();
        
        limelight.start();

        while (opModeIsActive()) {
            //double x = gamepad1.left_stick_x;    FIELD CENTRIC IF WE SO DESIRE
            //double y = -gamepad1.left_stick_y;
            //double rx = gamepad1.right_stick_x;

            LLResult llResult = limelight.getLatestResult();

            if (llResult != null && llResult.isValid()) {
                double tx = llResult.getTx(); // Horizontal offset from target (-29.8 to 29.8 degrees)
                double ty = llResult.getTy();
                double ta = llResult.getTa();

                // Loop through detected AprilTags
                for (LLResultTypes.FiducialResult fiducial : llResult.getFiducialResults()) {
                    telemetry.addData("AprilTag ID", fiducial.getFiducialId());
                }

                telemetry.addData("Tx", tx);
                telemetry.addData("Ty", ty);
                telemetry.addData("Ta", ta);

                // Simple proportional tracking: if target is to the right/left, move turret
                if (tx > 1.0) {
                    Turret.setPower(0.15); // Turn right
                } else if (tx < -1.0) {
                    Turret.setPower(-0.15); // Turn left
                } else {
                    Turret.setPower(0.0); // Locked on target dead-center
                }

            } else {
                telemetry.addData("Limelight", "No Valid Target");
                Turret.setPower(0.0); // Stop motor if target is lost
            }

            telemetry.update();

            if (gamepad1.options) {
                imu.resetYaw();
            }
            if (gamepad1.right_bumper) {
                turretPos+= 0.05092592592;
                //Turret.setPosition(turretPos); IF NO LIMELIGHT
            }
            if (gamepad1.left_bumper) {
                turretPos-= 0.05092592592;
                //Turret.setPosition(turretPos); IF NO LIMELIGHT
            }
            if (gamepad1.right_trigger_pressed) {
                downIntake.setPower(100);
                upIntake.setPower(100);
            } else {
                downIntake.setPower(0);
                upIntake.setPower(0);
            }
            float triggerPressure = gamepad1.left_trigger;
            if (triggerPressure > 0.1) {
                Shooter.setPower(1);
            } else {
                Shooter.setPower(0);
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
            FRP = Range.clip(FRP, -1.0, 1.0);
            RLP = Range.clip(RLP, -1.0, 1.0);
            RRP = Range.clip(RRP, -1.0, 1.0);


            // Set motor power             FIELD CENTRIC IF WE SO DESIRE
            //FLMotor.setPower(FLP);
            //FRMotor.setPower(FRP);
            //BLMotor.setPower(RLP);
            //BRMotor.setPower(RRP);

            //double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            //double rotX = (x * Math.cos(-heading)) - (y * Math.sin(-heading));
            //double rotY = (y * Math.cos(-heading)) + (x * Math.sin(-heading));

            //rotX = rotX * 1.1;

            //double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

            //double frontLeftPower = (rotY + rotX + rx) / denominator;
            //double backLeftPower = (rotY - rotX + rx) / denominator;
            //double frontRightPower = (rotY - rotX - rx) / denominator;
            //double backRightPower = (rotY + rotX - rx) / denominator;

            //FLMotor.setPower(frontLeftPower);
            //FRMotor.setPower(frontRightPower);
            //BLMotor.setPower(backLeftPower);
            //BRMotor.setPower(backRightPower);
        }
        limelight.stop();
        FLMotor.setPower(0);
        FRMotor.setPower(0);
        BLMotor.setPower(0);
        BRMotor.setPower(0);
        downIntake.setPower(0);
        upIntake.setPower(0);
        Turret.setPower(0);
        Shooter.setPower(0);
    }
}

