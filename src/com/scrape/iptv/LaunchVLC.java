package com.scrape.iptv;

import java.io.IOException;

public class LaunchVLC extends Thread{

    private SharedResource resource;

    public LaunchVLC (SharedResource resource){

        this.resource = resource;

    }

    @Override
    public void run() {

        try {
            resource.killVLCInstance();
            resource.clear();
            resource.runVLCInstance();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
