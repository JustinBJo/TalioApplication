package client.utils;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import javafx.scene.Parent;
import javafx.util.Pair;

import static com.google.inject.Guice.createInjector;

public class BuildUtils {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static <T> Pair<T, Parent> loadFXML(Class<T> c, String fxmlFileName) {
        return FXML.load(c, "client", "scenes", fxmlFileName);
    }

    public static <T> T getInstance(Class<T> c) {
        return INJECTOR.getInstance(c);
    }
}
