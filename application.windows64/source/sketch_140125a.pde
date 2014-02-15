import processing.serial.*;

Serial myPort;
BufferedReader reader;
String line;
boolean ackFromArduino = false;
int num = 0;
boolean arduinoAckRecieved = false;

void serialEvent(Serial p) {
  char str = p.readChar();
   if(str == 'z'){
        println( str);
        arduinoAckRecieved= true;
        //readFileAndSendSerial();
   }
   
} 

void setup() {
  reader = createReader("positions.txt");
  println(Serial.list());
  myPort = new Serial(this, Serial.list()[0], 115200);
  //readFileAndSendSerial();
  delay(1000);
  myPort.write("s");  
}

void draw(){
  if(arduinoAckRecieved){
    arduinoAckRecieved = !arduinoAckRecieved;
    readFileAndSendSerial();
      println("looping");
  }

}   

void readFileAndSendSerial(){
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


void writeTheData(){
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




