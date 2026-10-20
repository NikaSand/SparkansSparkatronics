package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
public class AssignmentMecanumDriveFieldOrientated extends OpMode {
    AssignmentMecanumDrive drive = new AssignmentMecanumDrive();
    double forward, strafe, rotate;

    @Override
    public void init() {
        drive.init(hardwareMap);
    }

    @Override
    public void loop(){
        forward = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        drive.driveFieldRelavite(forward,strafe,rotate);
    }
}
