package org.capstone.job_fair.utils;

// Java Program to Ping an IP address
import java.io.*;
import java.net.*;

public class NetworkUtil {
        // Sends ping request to a provided IP address
        public static void sendPingRequest(String ipAddress)
              throws UnknownHostException, IOException
        {
            InetAddress geek = InetAddress.getByName(ipAddress);
            System.out.println("Sending Ping Request to " + ipAddress);
            if (geek.isReachable(5000))
                System.out.println("Host is reachable");
            else
                System.out.println("Sorry ! We can't reach to this host");
        }
}
