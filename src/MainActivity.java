import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.*;

/**
 * Created by jimmy on 7/31/2015.
 */
public class MainActivity {

    public static void main(String[] args) {
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                try {
                    File file = new File("C:\\Users\\jimmy\\Desktop\\test-v" + i + "-c" + j + ".doc");

                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    String url = "http://royalroadweed.blogspot.co.il/2014/11/volume-" + i;
                    url += "-chapter-" + j + ".html";
                    Writer writer = new BufferedWriter(new FileWriter(file));
                    Document document = Jsoup.connect(url).get();
                    Elements span = document.select("span[style*=background-color]");
                    Desktop desktop = Desktop.getDesktop();
                    //desktop.open(file);
                    for (Element s : span) {
                        String text = Jsoup.clean(s.text(), Whitelist.basic());
                        writer.write(text + "\r\n\r\n");
                    }
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
