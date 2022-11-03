package com.scrape.iptv;


import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;


public class Main {

   public static final String TV_CHANNELS = "tv_channels_70057@loc@35992.m3u";
    public static String username = System.getProperty("user.name");
    public static String channelURL = ""; //this is the location
   public static final String VLC_LOCATION = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe " + "C:\\Users\\" + username + "\\AppData\\Local\\Temp\\temp.ts";

    public static void main(String[] args) throws IOException {

        Data data = new Data(TV_CHANNELS);

       Map<String, String> channels = data.returnData();
       channels.put(channels.size() + ".: ","Quit Program");
       Map<Integer,String> pickChannel = new HashMap<>();
        int counter = 1;
        boolean stopProgram = false;
        String command = "";

        System.out.println("Pick a channel or type \"q\" or \"quit\" to quit the program" + "\n");


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

                //watch TV
                channelURL = pickChannel.get(counter);
                SharedResource sharedResource = new SharedResource(channelURL,VLC_LOCATION,0);
                Store storeThread = new Store(sharedResource);
                LaunchVLC launchVLCThread = new LaunchVLC(sharedResource);


                storeThread.start();
                launchVLCThread.start();


                int tempChannelPick = 0;
                counter = 1;


                if (storeThread.isAlive() && launchVLCThread.isAlive()){
                    tempChannelPick = counter;

                }

                if (tempChannelPick != counter){
                    storeThread.stop();
                    launchVLCThread.stop();
                }

            } else {
                //kill the app
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
