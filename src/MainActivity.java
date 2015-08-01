import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

/**
 * Created by jimmy on 7/31/2015.
 */
public class MainActivity {

    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\jimmy\\IdeaProjects\\LightNovel\\test.txt");

            if (!file.exists()) {
                file.createNewFile();
            }
            String url = "http://www.baka-tsuki.org/project/index.php?title=Category:" +
                    "Light_novel_(English)";
            Writer writer = new BufferedWriter(new FileWriter(file));
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("div#mw-pages a[title^=m]");

            for (Element element: elements) {
                String title = element.text();
                String abs_url = element.attr("abs:href");
                title += "\r\n" + abs_url + "\r\n\r\n";
                writer.write(title);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
