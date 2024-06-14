package com.mikayelovich.iot.control.webcontroller.util;

import java.net.*;
import java.io.*;
import java.util.regex.Pattern;

public final class InetUtil {
    private InetUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }


    public static String getMacAddress(InetAddress ipAddress) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String command = "";
        if (os.contains("win")) {
            command = "arp -a " + ipAddress.getHostAddress();
        } else if (os.contains("nix") || os.contains("nux")) {
            command = "arp " + ipAddress.getHostAddress();
        } else if (os.contains("mac")) {
            command = "arp -a " + ipAddress.getHostAddress();
        } else {
            throw new IOException("Unsupported operating system: " + os);
        }

        Process p = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(ipAddress.getHostAddress())) {
                String[] parts = line.split("\\s+");
                for (String part : parts) {
                    if (isValidMacAddress(part)) {
                        return part;
                    }
                }
            }
        }
        return "Unknown";
    }

    private static boolean isValidMacAddress(String macAddress) {
        String pattern = "([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})";
        return Pattern.matches(pattern, macAddress);
    }
}
