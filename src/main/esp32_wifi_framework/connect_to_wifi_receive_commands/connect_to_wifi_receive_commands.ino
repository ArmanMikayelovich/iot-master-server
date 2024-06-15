#include <WiFi.h>
#include <Stepper.h>
#include <Ticker.h>
#include "esp_system.h"

const char* ssid = "TP-Link_261A";
const char* password = "58790274";
const char* host = "192.168.1.115";
const uint16_t port = 44444;

WiFiClient client;
Ticker pinTicker;

void setup() {
  Serial.begin(115200);
  Serial.println("Starting setup...");

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");

  if (!client.connect(host, port)) {
    Serial.println("Connection to server failed");
    while (true);
  }
  Serial.println("Connected to server");

  Serial.println("Setup completed");
}

void loop() {
  if (!client.connected()) {
    Serial.println("Server disconnected. Attempting to reconnect...");
    while (!client.connected()) {
      if (client.connect(host, port)) {
        Serial.println("Reconnected to server");
      } else {
        Serial.println("Failed to connect, retrying...");
        delay(2000);
      }
    }
  }

  while (client.available()) {
    String line = client.readStringUntil('\n');
    Serial.print("Received command: ");
    Serial.println(line);
    processCommands(line);
  }
}

void processCommands(String commands) {
  int startIndex = 0;
  int endIndex = 0;

  while ((endIndex = commands.indexOf(')', startIndex)) != -1) {
    String command = commands.substring(startIndex, endIndex + 1);
    executeCommand(command);
    startIndex = endIndex + 1;
  }
}

void executeCommand(const String& command) {
  Serial.println("Executing command: " + command);

  if (command.startsWith("pinMode")) {
    processPinMode(command);
  } else if (command.startsWith("digitalWrite")) {
    processDigitalWrite(command);
  } else if (command.startsWith("digitalRead")) {
    processDigitalRead(command);
  } else if (command.startsWith("enablePinFor")) {
    processEnablePinFor(command);
  } else if (command.startsWith("restart")) {
    processRestartESP32();
  } else {
    Serial.println("Unknown command");
  }
}

void processRestartESP32() {
  Serial.println("Restarting ESP32...");
  esp_restart(); // Restart the ESP32
}

void processPinMode(const String& command) {
  Serial.println("Processing pinMode command");
  Serial.println("Command: " + command);

  int firstCommaIndex = command.indexOf('(');
  int lastCommaIndex = command.indexOf(',');
  int endIndex = command.indexOf(')');

  String pinString = command.substring(firstCommaIndex + 1, lastCommaIndex);
  String modeString = command.substring(lastCommaIndex + 1, endIndex);

  int pin = pinString.toInt();
  int mode;

  if (modeString.equals("OUTPUT")) {
    mode = OUTPUT;
  } else if (modeString.equals("INPUT")) {
    mode = INPUT;
  } else if (modeString.equals("INPUT_PULLUP")) {
    mode = INPUT_PULLUP;
  } else {
    Serial.println("Error: Invalid mode");
    return;
  }

  pinMode(pin, mode);
  Serial.println("Pin Mode set. Pin: " + String(pin) + ", Mode: " + modeString);
}

void processDigitalWrite(const String& command) {
  Serial.println("Processing digitalWrite command");
  Serial.println("Command: " + command);

  int startIdx = command.indexOf('(');
  int endIdx = command.indexOf(')');
  int commaIdx = command.indexOf(',');

  int pin = command.substring(startIdx + 1, commaIdx).toInt();
  String valueString = command.substring(commaIdx + 1, endIdx);

  int value = valueString.equals("HIGH") ? HIGH : valueString.equals("LOW") ? LOW : -1;

  digitalWrite(pin, value);
  Serial.println("DigitalWrite executed. Pin: " + String(pin) + ", Value: " + valueString);
}

void processDigitalRead(const String& command) {
  Serial.println("Processing digitalRead command");
  Serial.println("Command: " + command);

  int startIdx = command.indexOf('(');
  int endIdx = command.indexOf(')');

  int pin = command.substring(startIdx + 1, endIdx).toInt();
  int pinState = digitalRead(pin);

  String response = "Pin " + String(pin) + " is " + (pinState == HIGH ? "HIGH" : "LOW");
  client.println(response);

  Serial.println("DigitalRead executed. Pin: " + String(pin) + ", State: " + (pinState == HIGH ? "HIGH" : "LOW"));
}

void processEnablePinFor(const String& command) {
  Serial.println("Processing enablePinFor command");
  Serial.println("Command: " + command);

  int firstCommaIndex = command.indexOf('(');
  int secondCommaIndex = command.indexOf(',', firstCommaIndex + 1);
  int endIndex = command.indexOf(')');

  int pin = command.substring(firstCommaIndex + 1, secondCommaIndex).toInt();
  int duration = command.substring(secondCommaIndex + 1, endIndex).toInt();

  enablePinFor(pin, duration);
}

void enablePinFor(int pin, int duration) {
  Serial.println("Enabling pin for duration. Pin: " + String(pin) + ", Duration: " + String(duration));

  digitalWrite(pin, HIGH);
  pinTicker.once_ms(duration * 1000, disablePin, pin);
}

void disablePin(int pin) {
  Serial.println("Disabling pin. Pin: " + String(pin));
  digitalWrite(pin, LOW);
}
