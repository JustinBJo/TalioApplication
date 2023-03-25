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

    /**
     * Uses MyFXML to load an FXML file using dependency injection
     * @param c the class of the controller
     * @param fxmlFileName name of the FXML file
     * @param <T> the class of the controller
     * @return a pair of the controller and its JavaFX node
     */
    public static <T> Pair<T, Parent> loadFXML(
            Class<T> c,
            String fxmlFileName
    ) {
        return FXML.load(c, "client", "scenes", fxmlFileName);
    }

    /**
     * Returns the appropriate instance for the given injection type.
     * When feasible, avoid using this method, in favor of injecting
     * your dependencies ahead of time in the constructor.
     */
    public static <T> T getInstance(Class<T> c) {
        return INJECTOR.getInstance(c);
    }
}
