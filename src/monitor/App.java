package monitor;

import controller.TwitchViewerController;
import gui.TwitchViewerGUI;
import model.TwitchViewerDatabase;
import model.TwitchViewerModel;

public class App
{
//    public static ViewerMonitor observer = new ViewerMonitor("jdbc:sqlite:H:/StreamData/Testing/ViewerInfo.db");

    public static void main(String[] args)
    {
        TwitchViewerGUI view = new TwitchViewerGUI("Viewer Monitor");
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> App.observer.notifyStreamState(false)));
        TwitchViewerModel model = new TwitchViewerModel();
        TwitchViewerController controller = new TwitchViewerController(model, view);

        controller.init();
        controller.run();
    }
}
