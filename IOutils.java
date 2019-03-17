package m2oReloadedDownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class IOutils {
    public static void writeToTxtFile(String stringToWrite, String path, boolean append) {
        PrintWriter p = null;
        File f = new File(path);
        try {
            p = new PrintWriter(new FileOutputStream(f, append));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            p.println(stringToWrite);
        }
        finally {
            p.flush();
            p.close();
        }
    }



}
