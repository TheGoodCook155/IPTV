package com.scrape.iptv;


import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

/**
 * In order for this implementation to work you must set up the TEMP environment variable because the stream is stored locally in the %TEMP% folder and the file acts as a buffer
 * The Windows delimiter is used throughout the app so the app is not UNIX friendly
 * The piping is done through VLC (you must have VLC installed...Feel free to use another player(btw read the dev documentation) or build one by yourself)
 * Tested on WIN 10 and WIN 7 while using VPN
 * Parts of the app are censored due to obvious reasons
 * Use the code as an example on how to build IP TV stream app
 * There are a lot of improvements that can be done, and I may push some updates in the upcoming period
 *
 * Enjoy!
 */


public class Main {

   public static final String TV_CHANNELS = "channels.m3u";
    public static String username = System.getProperty("user.name");
    public static String channelURL = "";
   public static final String VLC_LOCATION = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe " + "C:\\Users\\" + username + "\\AppData\\Local\\Temp\\temp.ts";


    public static void main(String[] args) throws IOException {

        Data data = new Data(TV_CHANNELS);

       Map<String, String> channels = data.returnData();
       Map<Integer,String> pickChannel = new HashMap<>();
        int counter = 1;
        boolean stopProgram = false;
        String command = "";

        Scanner scanner = new Scanner(System.in);

        while(stopProgram == false){

            System.out.println("Pick a TV channel: \n");
            for (Map.Entry<String, String> entry : channels.entrySet()) {
                System.out.println(counter + "." + "Channel: " + entry.getKey());
                pickChannel.put(counter, entry.getValue());
                counter++;
            }
            System.out.println("153. Quit Program \n");

            try{

            counter = scanner.nextInt();

            }catch (InputMismatchException e){
                System.out.println("Please use digits only!!! Shutting down\n");
            }

            if (isBetween(counter, 1, channels.size()) == true) {

                int tempChannelPick = 0;
                channelURL = pickChannel.get(counter);
                StreamTV streamTV = new StreamTV(VLC_LOCATION);
                Thread startVLCInstenceThread = new Thread(streamTV);
                startVLCInstenceThread.start();
                StoreStreamThread storeStreamThread = new StoreStreamThread(channelURL);
                storeStreamThread.start();
                counter = 1;
                if (startVLCInstenceThread.isAlive() && storeStreamThread.isAlive()){
                    tempChannelPick = counter;
                }

                if (tempChannelPick != counter){
                    startVLCInstenceThread.stop();
                    storeStreamThread.stop();
                }

            } else {
                stopProgram = true;
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("TASKKILL /IM VLC.EXE");
                System.exit(1);
            }
        }
    }

    public static boolean isBetween(int number, int lower, int upper) {
        return lower <= number && number <= upper;
    }


}
