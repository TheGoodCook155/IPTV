package com.scrape.iptv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class for channels preparation (printing in main() + pointing to the stream)
 */

public class Data {

    private static String file;

    public Data(String file){
        this.file = file;
    }

    public Map<String, String> returnData(){
        Map<String,String> channels = new HashMap<>();
        String channelName = "";
        String channelURL = "";

        boolean isPair = false;

        try{

        Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));

        while (scanner.hasNext()){

            String channelLine = scanner.nextLine();

            if (channelLine.contains("#EXTINF:-1")){
                channelName = channelLine.replace("#EXTINF:-1,","").trim();
              if (isPair == true){
                  isPair = false;
              }

            }
            if (channelLine.contains("http")){
                channelURL = channelLine;
                if (isPair == false){
                    isPair = true;
                }

            }

            if (isPair == true){
                channels.put(channelName,channelURL);
            }

        }

        }catch (FileNotFoundException e){

        } catch (IOException e) {
            e.printStackTrace();
        }

        return channels;
    }


}
