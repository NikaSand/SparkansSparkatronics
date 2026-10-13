package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@Autonomous

public class Auton extends LinearOpMode {

    private DcMotor FRMotor;
    private DcMotor FLMotor;
    private DcMotor BRMotor;
    private DcMotor BLMotor;

    @Override
    public void runOpMode() {

        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");

        waitForStart();
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