package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class FalconsTeleOp extends LinearOpMode {
    //Initialize motors, servos, sensors, imus, etc.
    DcMotorEx backWheels;
    Servo frontSteering;

    double currentSpeed = 0.0;
    final double FORWARD_HEADING = 0.5;
    double currentHeading = FORWARD_HEADING;

    final double NORMAL_STEERING_MULTIPLIER = 0.3;
    final double SLOW_STEERING_MULTIPLIER = 0.1;

    boolean isCruising = false;
    boolean lastDpadUp;
    boolean lastDpadDown;


    // The following code will run as soon as "INIT" is pressed on the Driver Station
    public void runOpMode() {

        //Define those motors and stuff
        //The string should be the name on the Driver Hub
        // Set the strings at the top of the MecanumDrive file; they are shared between TeleOp and Autonomous
        backWheels = (DcMotorEx) hardwareMap.dcMotor.get("back_wheels");

        // Use the following line as a template for defining new servos
        frontSteering = (Servo) hardwareMap.servo.get("front_wheels");

        //Set them to the correct modes
        //This resets the encoder values when the code is initialized
        backWheels.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        //This makes the wheels tense up and stay in position when it is not moving, opposite is FLOAT
        backWheels.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        //This lets you look at encoder values while the OpMode is active
        //If you have a STOP_AND_RESET_ENCODER, make sure to put this below it
        backWheels.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // The program will pause here until the Play icon is pressed on the Driver Station
        waitForStart();

        // opModeIsActive() returns "true" as long as the Stop button has not been pressed on the Driver Station
        while(opModeIsActive()) {

            // Manual Drive code
            if (!isCruising) {
                if (gamepad1.left_trigger > 0) {
                    // Deceleration controls
                    currentSpeed = -gamepad1.left_trigger;
                } else {
                    // Acceleration controls
                    currentSpeed = gamepad1.right_trigger;
                }
            }

            if (Math.abs(gamepad1.right_stick_x) > 0.0) {
                // Slow steering controls
                currentHeading = FORWARD_HEADING + (gamepad1.right_stick_x * SLOW_STEERING_MULTIPLIER);
            } else {
                // Normal steering controls
                currentHeading = FORWARD_HEADING + (gamepad1.left_stick_x * NORMAL_STEERING_MULTIPLIER);
            }

            // Cruise Control code
            if (gamepad1.dpad_right) {
                isCruising = true;
            } else if (gamepad1.dpad_left) {
                isCruising = false;
            }

            if (isCruising) {
                if (gamepad1.dpad_up && !lastDpadUp) {
                    // Increase cruising speed
                    currentSpeed += 0.1;
                } else if (gamepad1.dpad_down && !lastDpadDown) {
                    // Decrease cruising speed
                    currentSpeed -= 0.1;
                }
            }
            lastDpadUp = gamepad1.dpad_up;
            lastDpadDown = gamepad1.dpad_down;

            // Set motor/servo powers/positions based on values
            backWheels.setPower(currentSpeed);
            frontSteering.setPosition(currentHeading);



            // If you want to print information to the Driver Station, use telemetry
            // addData() lets you give a string which is automatically followed by a ":" when printed
            //     the variable that you list after the comma will be displayed next to the label
            // update() only needs to be run once and will "push" all of the added data

            //telemetry.addData("Label", "Information");
            //telemetry.update();

        } // opModeActive loop ends
    }
} // end class