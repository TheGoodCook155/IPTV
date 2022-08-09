package com.scrape.iptv;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Thread that stores the stream to hard disk
 */

public class StoreStreamThread extends Thread{

    private String channelURL;

    public StoreStreamThread(String channelURL) {
        this.channelURL = channelURL;
    }

    @Override
    public void run() {
        try {
            playTV(channelURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playTV(String channelURL) throws IOException {

        String tmpFolder = System.getProperty("java.io.tmpdir");
        URL url = new URL(channelURL);
        BufferedInputStream buffInputStream = new BufferedInputStream(url.openStream(),8192);
        DataInputStream dataInputStream = new DataInputStream(buffInputStream);
        Path path = Paths.get(tmpFolder).toAbsolutePath();
        File file = new File(path + "\\temp.ts");

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] buffer = new byte[8192];

        int c;

        while ((c = dataInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer,0,c);
            fileOutputStream.flush();
        }


    }

}
