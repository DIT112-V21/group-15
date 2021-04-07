#include <Smartcar.h>

const int fSpeed   = 50;  // 50% of the full speed forward
const int bSpeed   = -50; // 50% of the full speed backward
const int lDegrees = -75; // degrees to turn left
const int rDegrees = 75;  // degrees to turn right

const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 100;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);
SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

SimpleCar car(control);

void setup()
{
    Serial.begin(9600);

}

void loop()
{
    handleInput();
    getsensordistance();

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
  if(front.getDistance() >= 30)
  {
     stopCar();
     delay (200);
     backward();
     delay (300);
     stopCar();

  }
}

void stopCar ()
{
car.setSpeed(0);
}

void forward()
{
  car.setSpeed (40);
}

void backward()
{
  car.setSpeed (-40);
}

void leftturn()
{
  car.setSpeed (30);
  car.setAngle (-95);
  delay (2000);
  car.setAngle(0);
}

void rightturn()
{
  car.setSpeed (30);
  car.setAngle (95);
  delay(2000);
  car.setAngle(0);
}




