pagckage teamcode;

import com.qualcomm.robotcore.eventloop.LinearOpMode;
import com.qualcomm.robotcore.hardware.Dcmotor;


public class AssignmentMecanumDrive{
    private Dcmotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;

    public void init(HardwareMap hwMap) {
        frontLeftMotor = hwMap.get(DcmMotor.class,"front_left_motor"); //the strings are our motor names (Idk them yet)
        backLeftMotor = hwMap.get(DcMotor.class, "back_left_motor");
        frontRightMotor = hwMap.get(DcMotor.class, "front_right_motor");
        backRightMotor = hwMap.get(DcMotor.class, "back_right_motor");

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

    }

}