package com.scrape.iptv;

import java.io.IOException;

public class Store extends Thread{

    private SharedResource resource;

    public Store (SharedResource resource){

        this.resource = resource;

    }

    @Override
    public void run() {
        try {

            resource.playTV();

        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
        }

    }
}
