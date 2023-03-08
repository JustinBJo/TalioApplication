package client.scenes;


import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class MainSceneCtrl {
    private final MainCtrlTalio mainCtrl;

    @Inject
    public  MainSceneCtrl(MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        mainCtrl.showConnect();
    }

    @FXML
    private ListView boards;

    @FXML
    private ListView lists;

    private int i =0;
    private int k=0;

    public void addBoard(){

        boards.getItems().add("Board: " + i);
        i++;
        }

    public void addList() {
        lists.getItems().add("List: " + k);
        k++;
    }


}
