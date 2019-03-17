package m2oReloadedDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader implements Runnable{
    private File f;
    private String url;

    public File getF() {
        return f;
    }

    public void setF(File f) {
        this.f = f;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Downloader(File f, String url) {
        setF(f);
        setUrl(url);
    }


    @Override
    public void run() {
        this.downloadFile(f, url);
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    public void downloadFile(File f, String url) {
        URL u=null;
        ReadableByteChannel rbc=null;
        FileOutputStream out=null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            out = new FileOutputStream(f);
            rbc = Channels.newChannel(u.openStream());
            out.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
