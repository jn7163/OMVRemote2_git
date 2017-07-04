package utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import Client.JSONRPCClient;

/**
 * Created by thiba on 16/11/2016.
 */

public class Wol {

    private static byte[] getMacBytes(String mac) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        byte[] bytes = new byte[6];
        if (mac.length() != 12) {
            throw new IllegalArgumentException("Invalid MAC address...");
        }
        try {
            String hex;
            for (int i = 0; i < 6; i++) {
                hex = mac.substring(i * 2, i * 2 + 2);
                bytes[i] = (byte) Integer.parseInt(hex, 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit...");
        }
        return bytes;
    }

    public static void wakeup(String serveurIP,final String mac, int port) throws IOException {



        if (mac == null) {
            return;
        }
        MagicPacket.send(mac.trim(),serveurIP.trim(),port);
        return;
        /*
        try {
            byte[] macBytes = getMacBytes(mac.replace(":",""));
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }
            InetAddress address = InetAddress.getByName(serveurIP);

            address = getBroadcast(address);

            if(address == null)
            {
                Log.i("Wol", "Broadcast is null");
                return;
            }
            final DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            final DatagramSocket socket = new DatagramSocket();
            Thread t = new Thread() {
                public void run() {

                    try {
                        socket.send(packet);
                        Log.i("Wol", "send packet to "+packet.getAddress()+" on port "+packet.getPort() +" with mac "+mac);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }};
            t.start();




        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    private static InetAddress getBroadcast(InetAddress inetAddr) {

        //NetworkInterface temp;
        InetAddress iAddr = null;
        try {

            Enumeration<NetworkInterface> tmps =NetworkInterface.getNetworkInterfaces();


          while (tmps.hasMoreElements())
          {
              NetworkInterface temp = tmps.nextElement();



                //temp = NetworkInterface.getByInetAddress(inetAddr);
                List<InterfaceAddress> addresses = temp.getInterfaceAddresses();

                for (InterfaceAddress inetAddress : addresses) {
                    iAddr = inetAddress.getBroadcast();
                    if(iAddr != null) {
                        Log.i("Wol", "iAddr=" + iAddr);
                        return iAddr;
                    }
                }
            }
            return iAddr;

        } catch (SocketException e) {

            e.printStackTrace();
            Log.i("Wol", "getBroadcast" + e.getMessage());
        }
        return null;
    }
}
