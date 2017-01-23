package in.capstone.newsapp.bus;

/**
 * Created by bhaskar.gangabathina on 23-12-2016.
 */

public class MainBus extends RxEventBus {

    private static MainBus instance;

    private MainBus() {
    }

    public static MainBus getInstance() {
        if (instance == null)
            instance = new MainBus();
        return instance;
    }
}
