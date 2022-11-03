package com.scrape.iptv;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SharedResource{

    private String channelURL;
    private String VLC_LOCATION;
    private int fileSize;
    private boolean vlcInstanceStarted = false;



    public SharedResource (String channelURL, String VLC_LOCATION, int fileSize){
        this.channelURL = channelURL;
        this.VLC_LOCATION = VLC_LOCATION;
        this.fileSize = fileSize;

    }

    public void playTV() throws IOException, InterruptedException {


        synchronized (this){

            String tmpFolder = System.getProperty("java.io.tmpdir");
            URL url = new URL(channelURL);
            BufferedInputStream buffInputStream = new BufferedInputStream(url.openStream(),8192);
            DataInputStream dataInputStream = new DataInputStream(buffInputStream);
            Path path = Paths.get(tmpFolder).toAbsolutePath();
            File file = new File(path + "\\temp.ts");

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[8192];

            int c;
            boolean print = true;
            System.out.print("Filling buffer, please wait \n\n");

            while ((c = dataInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer,0,c);
                fileOutputStream.flush();
                fileSize = getFileSize();
                int mb = (fileSize / 1024) /1024;

                if (mb > 6){

                    if (print) {
                        System.out.println();
                        print = false;
                    }
                    notifyAll();

                      if(vlcInstanceStarted == false) {
                          wait();
                      }
                }else{

                  //TODO  print

                }
            }
        }

    }


    private int getFileSize(){


        synchronized (this){
            String tmpFolder = System.getProperty("java.io.tmpdir");
            Path path = Paths.get(tmpFolder).toAbsolutePath();
            File file = new File(path + "\\temp.ts");
            fileSize = (int)file.length();
        }
        return fileSize;
    }

    public void runVLCInstance() throws IOException, InterruptedException {

        synchronized (this){
            Runtime.getRuntime().exec(VLC_LOCATION);
            notifyAll();
            vlcInstanceStarted = true;
        }

    }

    public void killVLCInstance() throws IOException {

        synchronized (this){
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("TASKKILL /IM VLC.EXE");
        }

    }

    public void clear(){

        synchronized (this){

            String tmpFolder = System.getProperty("java.io.tmpdir");
            Path path = Paths.get(tmpFolder).toAbsolutePath();
            File file = new File(path + "\\temp.ts");
            if (file.exists()) {
                file.deleteOnExit();
            }
        }

    }


}
