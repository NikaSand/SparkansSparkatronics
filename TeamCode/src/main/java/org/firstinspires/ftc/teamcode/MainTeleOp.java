package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class MainTeleOp extends LinearOpMode {
    private DcMotor FLMotor;
    private DcMotor FRMotor;
    private DcMotor BLMotor;
    private DcMotor BRMotor;

    @Override
    public void runOpMode() {
        // find the data stuff
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");


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