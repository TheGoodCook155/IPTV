package com.scrape.iptv;

import java.io.IOException;

public class StreamTV extends Thread{

    /**
     * Thread that handles launching and terminating the VLC instance
     */

    private String VLC_LOCATION;

    public StreamTV(String VLC_LOCATION){
        this.VLC_LOCATION = VLC_LOCATION;
    }

    @Override
    public void run() {
        try {
            killPlayer();
            Runtime.getRuntime().exec(VLC_LOCATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void killPlayer() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("TASKKILL /IM VLC.EXE");
    }
}
