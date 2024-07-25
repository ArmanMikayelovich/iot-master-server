
# IoT Web Control

## Overview
IoT Web Control is a project designed to control IoT devices using a web interface. It leverages MQTT for communication between the web server and the IoT devices. The project includes configurations for both the front-end and back-end, as well as the state machine, REST controllers, and various commands for interacting with IoT devices.

## Table of Contents
- [Overview](#overview)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [MQTT Configuration](#mqtt-configuration)
- [Properties](#properties)
- [Front-End](#front-end)
- [Back-End](#back-end)
- [StateMachine](#statemachine)
- [REST Controllers](#rest-controllers)
- [Fetch Requests](#fetch-requests)
- [Commands](#commands)
- [ESP32 WiFi & MQTT Framework](#esp32-wifi--mqtt-framework)
    - [Connecting to WiFi](#connecting-to-wifi)
    - [Using the File System](#using-the-file-system)
    - [Running the Web Server](#running-the-web-server)
    - [Connecting to MQTT Broker](#connecting-to-mqtt-broker)
    - [Serving Commands from MQTT Topics](#serving-commands-from-mqtt-topics)
- [MQTT Listener and Publisher](#mqtt-listener-and-publisher)
- [Security Configuration](#security-configuration)
- [Swagger Configuration](#swagger-configuration)
- [CORS Configuration](#cors-configuration)
- [Connected Device Service](#connected-device-service)
- [Logging Configuration](#logging-configuration)
- [SSL Configuration](#ssl-configuration)
- [Microcontroller Pin Definitions](#microcontroller-pin-definitions)
- [MQTT Event Handling](#mqtt-event-handling)
- [Service Layer](#service-layer)
- [License](#license)

## Getting Started

### Prerequisites
- **Java 11** or higher
- **Maven**
- **Node.js** and **npm** (for front-end)
- **An MQTT broker** (e.g., Mosquitto)

### Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/iot-web-control.git
   cd iot-web-control
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Open the front-end in your browser**:
   Navigate to `http://localhost:8443` (or the configured server port).

## MQTT Configuration
The project communicates with IoT devices using MQTT. Ensure you have an MQTT broker running and configured.

### MQTT Configuration in `application.properties`
```properties
mqtt.broker.host=localhost
mqtt.broker.port=1883
mqtt.broker.username=esp8266
mqtt.broker.password=esp8266
mqtt.broker.pool-size=10
mqtt.registration-topic=registration
mqtt.event-listener-topic=answer-topic
mqtt.event-listener-topic.message-format-regex=\{(.*?)\}\{(.*?)\}\{(.*?)\}
```

## Properties
The `application.properties` file contains essential configurations for the application.

### Application Properties
```properties
spring.application.name=iot-web-control
server.socketlistener.port=44444
server.tomcat.remoteip.port-header=8443
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

logging.level.com.mikayelovich.iot.control=DEBUG
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=123456
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=mycert

server.ip-address=192.168.1.101
```

## Front-End
The front-end is a web interface that interacts with the back-end through REST APIs to control IoT devices. It is served by the Spring Boot application.

### Technologies Used
- **HTML**
- **CSS**
- **JavaScript**
- **Thyemeleaf**
-
### Structure
The front-end consists of various components that make fetch requests to the back-end to perform actions and update the UI accordingly.

## Back-End
The back-end is a Spring Boot application that handles MQTT communication, processes commands, and serves the front-end.

### Main Application
The main application class initializes the Spring Boot application and autowires the MQTT message publisher.

## StateMachine
The `StateMachine` class manages the state of MQTT events and topics.

### Registering Events
Events are registered with topics and stored in a map for tracking.
```java
public void registerEvent(String topic, MqttEvent event) {
    topicsWithEvents.putIfAbsent(topic, new ArrayList<>());
    topicsWithEvents.get(topic).add(event);
}
```

## REST Controllers
REST controllers handle HTTP requests and interact with the service layer to perform actions.

### HomePageController
Serves the main page of the web application.

### DevicesController
Handles requests related to connected devices and provides device information.

### MqttCommandExecutionController
Executes various commands on IoT devices by publishing messages to the MQTT broker.

## Fetch Requests
The front-end makes fetch requests to the REST APIs to control and monitor IoT devices. These requests are typically made using JavaScript's `fetch` API or a library like Axios.

### Example Fetch Request
```javascript
fetch('/execute/digital-write/', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        pinNumber: 13,
        state: 'HIGH'
    })
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

## Commands
Commands are used to interact with IoT devices. They are mapped to executable strings that can be sent over MQTT.

### DigitalWriteCommand
Executes a digital write operation on a specified pin.

### EnablePinForDuration
Enables a pin for a specified duration.

### PinModeCommand
Sets the mode of a specified pin.

### ReadCommand
Reads the state of a specified pin.

### RestartCommand
Restarts the IoT device.

## ESP32 WiFi & MQTT Framework
The ESP32 WiFi framework is responsible for connecting the ESP32 to a WiFi network and receiving commands from the server.

### Connecting to WiFi
The ESP32 connects to a specified WiFi network using credentials provided in the code.

### Using the File System
The ESP32 uses the file system to store configuration data. If the required information is not found, a web server is started to allow the user to input the necessary data.

### Running the Web Server
If the required information is not found in the file system, the ESP32 starts a web server to serve a configuration page.

### Connecting to MQTT Broker
The ESP32 connects to the MQTT broker using the provided credentials and starts listening for commands.

### Serving Commands from MQTT Topics
Commands received from the MQTT topics are processed and executed on the ESP32.

## MQTT Listener and Publisher

### MQTT Listener
The `MqttListener` class listens for messages from the MQTT broker and processes them.

### MQTT Publisher
The `MQTTMessagePublisher` class publishes messages to the MQTT broker.

### Security Configuration
The `SecurityConfig` class configures web security settings, including disabling CSRF and allowing all requests.

### Swagger Configuration
The `SwaggerConfig` class sets up Swagger for API documentation, providing an interface for exploring the APIs.

### CORS Configuration
The `WebConfig` class configures CORS settings, allowing all origins, methods, and headers.

## Connected Device Service

### Connected Device Service
The `ConnectedDeviceService` class manages the connected devices and provides information about them.

## Logging Configuration

### Logging Configuration
Logging is configured in `application.properties` to provide debug-level logs for the application.

### Microcontroller Pin Definitions
The `Esp32Devkit1Pin` class defines pin modes and their usage, providing a mapping of pin numbers to their functions.

### MQTT Event Handling
MQTT events are handled by the `StateMachine` class, which registers and tracks events for different topics.
 