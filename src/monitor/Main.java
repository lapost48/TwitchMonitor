package monitor;

import gui.MonitorWindow;
import gui.ViewerMonitor;

import java.util.concurrent.TimeUnit;

public class Main
{
    public static ViewerMonitor observer = new ViewerMonitor("jdbc:sqlite:H:/StreamData/Testing/ViewerInfo.db");

    public static void main(String[] args)
    {
        MonitorWindow window = new MonitorWindow("Viewer Monitor", observer);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Main.observer.notifyStreamState(false)));


        while(true) {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (observer.isStreaming())
                observer.viewerUpdate();
        }
    }
}
