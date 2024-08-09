#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <Ticker.h>
#include <LittleFS.h>
#include <PubSubClient.h>
/* Установите здесь свои SSID и пароль */
const char* own_wifi_ssid = "Service_Config_Server";  // SSID
const char* own_wifi_password = "10203055";           // пароль
const int mqtt_port = 1883;                           // Default MQTT port


ESP8266WebServer server(80);
WiFiClient espClient;
Ticker ticker;

String ssid = "";
String password = "";
String mqtt_broker = "";
String mqtt_topic = "";
String mqtt_username = "";
String mqtt_password = "";
PubSubClient* client = nullptr;

void setup() {
  delay(1000);  // Delay before serial setup

  Serial.begin(115200);
  if (!LittleFS.begin()) {
    Serial.println("LittleFS mount failed");
    return;
  }
  // Check if wifi_mqtt file exists, if exists, connect to wifi, connect to mqtt server
  if (LittleFS.exists("/wifi_mqtt")) {
    File file = LittleFS.open("/wifi_mqtt", "r");
    if (file) {
      readConnectionDataFromFS(file);
    }
    LittleFS.end();  // Unmount LittleFS to release resources
    connectToWifi();

    if (isConnectedToWifi()) {
      Serial.println("connected to wifi");
      client = new PubSubClient(espClient);
      connectToMQTT();
    } else {
      Serial.println("unable to connect to wifi...");
      deleteFile("/wifi_mqtt");
      ESP.restart();
    }

  } else {
    Serial.println("No data from File System...");
    startWiFiServer();
  }
}


void connectToMQTT() {
  // Attempt to connect to MQTT broker
  client->setServer(mqtt_broker.c_str(), mqtt_port);  // Set MQTT broker IP and port
  client->setCallback(callback);
  reconnect();  // Set callback function for incoming messages
}

void reconnect() {

  while (!client->connected()) {
    Serial.print("Attempting MQTT connection...");

    if (client->connect("ESP8266Client", mqtt_username.c_str(), mqtt_password.c_str())) {
      Serial.println("connected");
      client->subscribe(mqtt_topic.c_str());  // Subscribe to MQTT topic
    } else {
      Serial.print("failed, rc=");
      Serial.print(client->state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  // Handle incoming MQTT messages here
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");

  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  // TODO write data in line
  // processCommands(line)
  Serial.println();
}


void deleteFile(const char* filename) {
  if (LittleFS.exists(filename)) {
    LittleFS.remove(filename);
    Serial.println("File deleted");
  } else {
    Serial.println("File not found, cannot delete");
  }
}



void connectToWifi() {
  // Attempt to connect to Wi-Fi
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  delay(2000);

  WiFi.begin(ssid, password);
  Serial.println(password);
  delay(1000);
  int maxRetries = 40;
  int retryCount = 0;
  // Wait for connection or timeout
  while (WiFi.status() != WL_CONNECTED && retryCount < maxRetries) {
    delay(500);
    Serial.print(".");
    retryCount++;
  }
  Serial.println("wifi status");
  Serial.println(WiFi.status());
}

bool isConnectedToWifi() {
  // Check connection status
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println();
    Serial.println("WiFi connected");
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());
  } else {
    Serial.println();
    Serial.println("Failed to connect to WiFi");
  }
  return WiFi.status() == WL_CONNECTED;
}

void startWiFiServer() {
  Serial.println("starting server");
  IPAddress local_ip = IPAddress(192, 168, 1, 1);
  IPAddress gateway = IPAddress(192, 168, 1, 1);
  IPAddress subnet = IPAddress(255, 255, 255, 0);
  WiFi.softAP(own_wifi_ssid, own_wifi_password);
  WiFi.softAPConfig(local_ip, gateway, subnet);
  delay(100);

  server.on("/", HTTP_GET, handleMainPage);
  server.on("/save", HTTP_POST, handleSave);

  server.begin();
  Serial.println("HTTP server started");
}

void readConnectionDataFromFS(File& file) {
  ssid = file.readStringUntil('\n').c_str();
  ssid.trim();

  password = file.readStringUntil('\n').c_str();
  password.trim();  // Consider securely handling password
  mqtt_broker = file.readStringUntil('\n').c_str();
  mqtt_broker.trim();
  mqtt_topic = file.readStringUntil('\n').c_str();
  mqtt_topic.trim();

  mqtt_username = file.readStringUntil('\n').c_str();
  mqtt_username.trim();
  ;
  mqtt_password = file.readStringUntil('\n').c_str();
  mqtt_password.trim();
  // Consider securely handling password
  file.close();
  Serial.println("successfully read data from file");
  // Print read data to verify
  Serial.print("SSID: ");
  Serial.println(ssid);
  Serial.print("Password: ");
  Serial.println(password);
  Serial.print("MQTT Broker: ");
  Serial.println(mqtt_broker);
  Serial.print("MQTT Topic: ");
  Serial.println(mqtt_topic);
  Serial.print("MQTT Username: ");
  Serial.println(mqtt_username);
  Serial.print("MQTT Password: ");
  Serial.println(mqtt_password);
}

void handleMainPage() {
  String html = "<html><body><h1>Node Control Panel</h1>";
  html += "<form method='post' action='/save'>";
  html += "WiFi SSID: <input type='text' name='ssid' value=''><br>";
  html += "WiFi Password: <input type='password' name='password'><br>";  // Password input should be blank for security
  html += "MQTT Broker: <input type='text' name='mqtt_broker' value=''><br>";
  html += "MQTT Topic: <input type='text' name='mqtt_topic' value=''><br>";
  html += "MQTT Username: <input type='text' name='mqtt_username' value=''><br>";
  html += "MQTT Password: <input type='password' name='mqtt_password'><br>";  // Password input should be blank for security
  html += "<input type='submit' value='Save'></form>";
  html += "</body></html>";
  server.send(200, "text/html", html);
}

void handleSave() {
  Serial.println("saving args...");
  bool isValidData = server.hasArg("ssid") && server.hasArg("password") && server.hasArg("mqtt_broker")
                     && server.hasArg("mqtt_topic") && server.hasArg("mqtt_username");
  if (isValidData) {
    ssid = server.arg("ssid").c_str();          // Update ssid
    password = server.arg("password").c_str();  // Update password
    // Update other variables if necessary
    mqtt_broker = server.arg("mqtt_broker").c_str();
    mqtt_topic = server.arg("mqtt_topic").c_str();
    mqtt_username = server.arg("mqtt_username").c_str();
    mqtt_password = server.arg("mqtt_password").c_str();


    createFile();
    writeInFile();

    server.send(200, "text/plain", "Configuration saved. Restarting...");
    delay(1000);
    ESP.restart();
  } else {
    server.send(400, "text/plain", "Missing arguments");
  }
}

void createFile() {
  // Check if /wifi_mqtt file exists
  if (!LittleFS.exists("/wifi_mqtt")) {
    Serial.println("File /wifi_mqtt does not exist. Creating...");

    // Open file in write mode (create it)
    File file = LittleFS.open("/wifi_mqtt", "w");
    if (!file) {
      Serial.println("Failed to create file");
    } else {
      Serial.println("File created successfully");
    }
  }
}

void writeInFile() {
  // Save to LittleFS
  File file = LittleFS.open("/wifi_mqtt", "w");
  if (!file) {
    Serial.println("Failed to create file");
  } else {
    file.println(ssid);
    file.println(password);  // Consider storing password securely if needed
    file.println(mqtt_broker);
    file.println(mqtt_topic);
    file.println(mqtt_username);
    file.println(mqtt_password);  // Consider storing password securely if needed
    file.close();
  }
}


void loop() {
  if (client != nullptr) {
    if (!client->connected()) {
      reconnect();
    }
    client->loop();
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
  ESP.restart();  // Restart the ESP32
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

  int value = valueString.equals("HIGH") ? HIGH : valueString.equals("LOW") ? LOW
                                                                            : -1;

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
  // client.println(response);//TODO refactor

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
  ticker.once_ms(duration * 1000, disablePin, pin);
}

void disablePin(int pin) {
  Serial.println("Disabling pin. Pin: " + String(pin));
  digitalWrite(pin, LOW);
}
