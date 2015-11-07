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

    private static String url = "http://www.baka-tsuki.org/project/index.php?title=Category:Light_novel_(English)";
    private static String api = "http://btapi-shadowys.rhcloud.com/api?title=";

    public static void main(String[] args) {
        listLightNovels("mo");
        findSpecificLNVolume("Moonlight_Sculptor", 2);
    }

    //Lists Lightnovels on BakaTsuki whom meet the search criteria
    public static void listLightNovels(String search) {
        try {
            File file = new File("/Users/Jimmyle/Desktop/test.txt");
            Writer writer = new BufferedWriter(new FileWriter(file));
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("div#mw-pages a[title^=" + search + "]");

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

    //Lists Info of LightNovel's chosen volume
    private static void findSpecificLNVolume(String title, int volume) {
        try {
            File file = new File("/Users/Jimmyle/Desktop/test.txt");
            //Creates new writer to write to file chosen
            Writer writer = new BufferedWriter(new FileWriter(file));
            //Connect to the URL
            URL url = new URL(api + cleanLightNovelTitle(title) + "&volumeno=" + volume);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            JsonObject jsonObject = getJsonInfo(request);
            printVolumeNum(jsonObject, writer);
            printVolChapters(jsonObject, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Cleans the String gotten from list to coincide with URL from api
    private static String cleanLightNovelTitle(String title) {
        return (title.replace(" ", "_"));
    }

    //Prints out the volume number
    private static void printVolumeNum(JsonObject jsonObject, Writer writer) {
        String volume_num = jsonObject.get("title").toString();
        volume_num += "\r\n\r\n";
        try {
            writer.write(volume_num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Prints out all chapters of the specific volume
    private static void printVolChapters(JsonObject jsonObject, Writer writer) {
        String result;
        JsonArray jsonArray = jsonObject.getAsJsonArray("chapters");
        try {
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.get(i).getAsJsonObject();
                result = jsonObject.get("title").toString();
                result += "\r\n" + jsonObject.get("link").toString();
                if (i == jsonArray.size()-1) {
                    writer.write(result);
                    break;
                }
                writer.write(result + "\r\n\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Gets Json object for other functions to use
    private static JsonObject getJsonInfo(HttpURLConnection httpURLConnection) throws IOException {
        //Convert to JSON to get info
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) httpURLConnection.getContent()));
        JsonObject jsonObject = root.getAsJsonObject();

        //Gets down to the books json array from the api
        JsonArray jsonArray = jsonObject.getAsJsonArray("sections");
        jsonObject = jsonArray.get(0).getAsJsonObject();
        jsonArray = jsonObject.getAsJsonArray("books");
        jsonObject = jsonArray.get(0).getAsJsonObject();
        return jsonObject;
    }

}
