import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_140125a extends PApplet {



Serial myPort;
BufferedReader reader;
String line;
boolean ackFromArduino = false;
int num = 0;
boolean arduinoAckRecieved = false;

public void serialEvent(Serial p) {
  char str = p.readChar();
   if(str == 'z'){
        println( str);
        arduinoAckRecieved= true;
        //readFileAndSendSerial();
   }
   
} 

public void setup() {
  reader = createReader("positions.txt");
  println(Serial.list());
  myPort = new Serial(this, Serial.list()[0], 115200);
  //readFileAndSendSerial();
  delay(1000);
  myPort.write("s");  
}

public void draw(){
  if(arduinoAckRecieved){
    arduinoAckRecieved = !arduinoAckRecieved;
    readFileAndSendSerial();
      println("looping");
  }

}   

public void readFileAndSendSerial(){
    try {
    line = reader.readLine();
  } catch (IOException e) {
    e.printStackTrace();
    line = null;
  }
  if (line == null) {
    noLoop();  
  } else {
      delay(1000);
      writeTheData();
  } 
}


public void writeTheData(){
      int buffersize = 64;
      float floatVersion = (float)line.length()/buffersize;
      println(line.length(),floatVersion);
 
      int fragment = floatVersion > line.length()/buffersize ? 1 : 0;
      
      for(int i = 0; i < (line.length()/buffersize + fragment) ; i++){
        boolean x = (line.length() - i*buffersize ) < buffersize;
        int fragmentLength = x ?-(i*buffersize - line.length()) : buffersize;
        myPort.write(line.substring(i*buffersize,(i*buffersize)+fragmentLength));  
        delay(15);
      }  
}




  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_140125a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
