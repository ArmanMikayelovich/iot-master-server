/*<![CDATA[*/
const protocol = "https://"
const ip = "192.168.1.101";
const port = "8443"

document.addEventListener('DOMContentLoaded', () => {
    fetchDevices('devices-pin-mode');
    fetchDevices('devices-enable-pin');
    fetchDevices('devices-digital-write');
    fetchDevices('devices-restart-device');
    populatePinNumbers('pinNumber-pin-mode');
    populatePinNumbers('pinNumber-enable-pin');
    populatePinNumbers('pinNumber-digital-write');

    // Call fetchDevicePinInfo() every 2 seconds
    setInterval(fetchDevicePinInfo, 2000);
});

async function fetchDevices(dropdownId) {
    try {
        const response = await fetch(`${protocol}${ip}:${port}/devices`);
        const devices = await response.json();
        const devicesDropdown = document.getElementById(dropdownId);

        devices.forEach(device => {
            const option = document.createElement('option');
            option.value = device;
            option.text = device;
            if (devicesDropdown) {
                devicesDropdown.add(option);
            }
        });
        console.log('Devices fetched successfully:', devices);
    } catch (error) {
       alert('Error fetching devices:' +  error);
    }
}

function populatePinNumbers(dropdownId) {
    const pinNumbers = Array.from({length: 40}, (_, i) => i); // 0 to 39
    const pinNumberDropdown = document.getElementById(dropdownId);

    pinNumbers.forEach(pin => {
        const option = document.createElement('option');
        option.value = pin;
        option.text = pin;
        if (pinNumberDropdown) {
            pinNumberDropdown.add(option);
        }
    });
    console.log('Pin numbers populated for dropdown:', dropdownId);
}

async function sendPinModeCommand() {
    const uniqueId = document.getElementById('devices-pin-mode').value;
    const pin = document.getElementById('pinNumber-pin-mode').value;
    const pinModeState = document.getElementById('pinModeState-pin-mode').value;
    console.log('Sending Pin Mode Command:');
    console.log('UniqueId:', uniqueId);
    console.log('Pin Number:', pin);
    console.log('Pin Mode State:', pinModeState);

    const data = {
        uniqueId: uniqueId,
        pin: parseInt(pin),
        pinModeState: pinModeState
    };
    console.log('Data to send:', JSON.stringify(data));
    try {
        const response = await fetch(`${protocol}${ip}:${port}/execute/pin-mode/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
        } else {
            alert(`Error executing command. ${ await convertReadableStreamToString(response.body)}`);
        }
    } catch (error) {
       alert('Error:', await convertReadableStreamToString(error));
    }
}

async function sendEnablePinForDuration() {
    const uniqueId = document.getElementById('devices-enable-pin').value;
    const pin = document.getElementById('pinNumber-enable-pin').value;
    const duration = document.getElementById('duration-enable-pin').value;

    const data = {
        uniqueId: uniqueId,
        pin: parseInt(pin),
        durationInSeconds: parseInt(duration)
    };

    try {
        const response = await fetch(`${protocol}${ip}:${port}/execute/enable-pin-for-duration/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
        } else {
            alert(`Error executing command. ${ await convertReadableStreamToString(response.body)}`);
        }
    } catch (error) {
       alert('Error:', await convertReadableStreamToString(error));
    }
}

async function sendDigitalWriteCommand() {
    const uniqueId = document.getElementById('devices-digital-write').value;
    const pin = document.getElementById('pinNumber-digital-write').value;
    const state = document.getElementById('state-digital-write').value;

    const data = {
        uniqueId: uniqueId,
        pin: parseInt(pin),
        state: state
    };

    try {
        const response = await fetch(`${protocol}${ip}:${port}/execute/digital-write/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
        } else {
            alert(`Error executing command. ${ await convertReadableStreamToString(response.body)}`);
        }
    } catch (error) {
        alert('Error:', await convertReadableStreamToString(error));    }
}

async function sendRestartDevice() {
    const uniqueId = document.getElementById('devices-restart-device').value;

    const data = {
        uniqueId: uniqueId,
    };

    try {
        const response = await fetch(`${protocol}${ip}:${port}/execute/restart-device/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
        } else {
            alert(`Error executing command. ${ await convertReadableStreamToString(response.body)}`);
        }
    } catch (error) {
        alert('Error:', await convertReadableStreamToString(error));    }
}

async function convertReadableStreamToString(readableStream) {
    if(readableStream){
        const reader = readableStream.getReader();

        const decoder = new TextDecoder();
        let result = '';
        while (true) {
            const { done, value } = await reader.read();
            if (done) {
                break;
            }
            result += decoder.decode(value, { stream: true });
        }
        return result;
    }
}


function fetchDevicePinInfo(uniqueId) {
    fetch(`${protocol}${ip}:${port}/devices/pin-info?${uniqueId}`)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('pinInfoBody');
            tableBody.innerHTML = '';

            Object.keys(data).forEach(uniqueId => {
                const pins = data[uniqueId];
                const row = document.createElement('tr');
                const uniqueIdCell = document.createElement('td');
                uniqueIdCell.textContent = uniqueId;
                row.appendChild(uniqueIdCell);

                const pinsCell = document.createElement('td');
                const pinsTable = document.createElement('table');
                pins.forEach(pin => {
                    const pinRow = document.createElement('tr');
                    const pinNameCell = document.createElement('td');
                    pinNameCell.textContent = Object.keys(pin)[0];
                    pinRow.appendChild(pinNameCell);

                    const pinStateCell = document.createElement('td');
                    pinStateCell.textContent = Object.values(pin)[0];
                    pinRow.appendChild(pinStateCell);

                    pinsTable.appendChild(pinRow);
                });

                pinsCell.appendChild(pinsTable);
                row.appendChild(pinsCell);
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            alert('Error:',  convertReadableStreamToString(error));
        });
}
