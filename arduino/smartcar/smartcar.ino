#include <Smartcar.h>

const int fSpeed   = 70;  // 70% of the full speed forward
const int bSpeed   = -70; // 70% of the full speed backward
const int lDegrees = -75; // degrees to turn left
const int rDegrees = 75;  // degrees to turn right

const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 100;

const int FRONT_IR_PIN = 0;
const int LEFT_IR_PIN  = 1;
const int RIGHT_IR_PIN = 2;
const int BACK_IR_PIN  = 3;


ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);
SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);


GP2Y0A21 frontIR(arduinoRuntime, FRONT_IR_PIN);
GP2Y0A21 leftIR(arduinoRuntime, LEFT_IR_PIN);
GP2Y0A21 rightIR(arduinoRuntime, RIGHT_IR_PIN);
GP2Y0A21 backIR(arduinoRuntime,BACK_IR_PIN);




SimpleCar car(control);

void setup()
{
    Serial.begin(9600);

}

void loop()
{
    handleInput();
    getsensordistance();
    Objectavoid ();
}

void handleInput()
{ // handle serial input if there is any
    if (Serial.available())
    {
        char input = Serial.read(); // read everything that has been received so far and log down
                                    // the last entry
        switch (input)
        {
        case 'a': // rotate counter-clockwise going forward
            car.setSpeed(fSpeed);
            car.setAngle(lDegrees);
            break;
        case 'd': // turn clock-wise
            car.setSpeed(fSpeed);
            car.setAngle(rDegrees);
            break;
        case 'w': // go ahead
            car.setSpeed(fSpeed);
            car.setAngle(0);
            break;
        case 's': // go back
            car.setSpeed(bSpeed);
            car.setAngle(0);
            break;
        default: // if you receive something that you don't know, just stop
            car.setSpeed(0);
            car.setAngle(0);
        }
    }
}
void getsensordistance()
{

     Serial.println(front.getDistance());
     delay(100);

}

void Objectavoid ()
{
  int distance = front.getDistance();
  if(distance > 0 && distance < 100)
  {
     stopCar();
     delay(400);
     lookleft();
     lookright();
     lookback();
  }
}

void stopCar ()
{
car.setSpeed(0);
}

void lookleft()
{
  int leftdistance = leftIR.getDistance();

  if(leftdistance > 15 && leftdistance < 60)
{
  car.setSpeed (30);
  car.setAngle (95);
  delay(2500);
  car.setAngle(-95);
  delay(2000);
  car.setSpeed(0);
  handleInput();
}
}


void lookright()
{
  int rightdistance = rightIR.getDistance();

  if(rightdistance > 15 && rightdistance < 60)
{
  car.setSpeed (30);
  car.setAngle (-95);
  delay(2500);
  car.setAngle(95);
   delay(2000);
  car.setSpeed(0);
   handleInput();

}
}
void lookback()
{
  int backdistance = backIR.getDistance();

   if(backdistance > 15 && backdistance < 60)
{
  car.setSpeed(30);
  car.setAngle(0);
  delay(2000);
  car.setSpeed(0);
  handleInput();
}

}
