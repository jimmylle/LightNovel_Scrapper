import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jimmy on 7/31/2015.
 * Testing Stuff
 */
public class MainActivity {

    private static String url = "http://www.baka-tsuki.org/project/index.php?title=";
    private static String api = "https://baka-tsuki-api.herokuapp.com/api?title=";

    public static void main(String[] args) {
        //listLightNovels();
        findSpecificLN("Moonlight_Sculptor", 1);
    }

    public static void listLightNovels() {
        try {
            File file = new File("C:\\Users\\jimmy\\Desktop\\test.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            String url = "http://www.baka-tsuki.org/project/index.php?title=Category:" +
                    "Light_novel_(English)";
            Writer writer = new BufferedWriter(new FileWriter(file));
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("div#mw-pages a[title^=m]");

            for (Element element : elements) {
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

    private static void findSpecificLN(String title,int volume) {
        try {
            File file = new File("C:\\Users\\jimmy\\Desktop\\test.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Writer writer = new BufferedWriter(new FileWriter(file));
            //Connect to the URL
            URL url = new URL(api + cleanLightNovelTitle(title) + "&volumeno=" + volume);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //Convert to JSON to get info
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));
            String jsonString = root.toString();
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("sections");
            jsonObject = jsonArray.get(0).getAsJsonObject();
            jsonArray = jsonObject.getAsJsonArray("books");
            jsonObject = jsonArray.get(0).getAsJsonObject();
            jsonArray = jsonObject.getAsJsonArray("chapters");
            String result;
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.get(i).getAsJsonObject();
                result = jsonObject.get("title").toString();
                writer.write(result + "\r\n\r\n");
            }
            //String size = Integer.toString(jsonArray.size());
            //writer.write(size);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String cleanLightNovelTitle(String title) {
        return (title.replace(" ", "_"));
    }
}
