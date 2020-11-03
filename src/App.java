import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.TwitchViewerController;
import gui.TwitchViewerGUI;
import model.TwitchViewerModel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class App
{

    public static void main(String[] args) throws IOException
    {
        TwitchViewerGUI view = new TwitchViewerGUI("Viewer Monitor");

        TwitchViewerModel model;
        File configFile = new File("config.json");
        if(configFile.createNewFile())
        {
            FileWriter writer = new FileWriter(configFile);
            model = new TwitchViewerModel();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(model.getConfig(), writer);
            writer.close();
        }
        else
        {
            FileReader reader = new FileReader(configFile);
            Gson gson = new GsonBuilder().create();
            model = new TwitchViewerModel(gson.fromJson(reader, model.Config.class));
            reader.close();
        }

        TwitchViewerController controller = new TwitchViewerController(model, view);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> controller.exit()));

        controller.init();
        controller.run();
    }
}
