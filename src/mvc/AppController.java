package mvc;

import javax.swing.*;
import java.awt.*;

public class AppController {
    private AppView appView;
    private AppModel appModel;
    public AppView getAppView() {
        return appView;
    }
    public AppModel getAppModel() {
        return appModel;
    }
    public void init(){
        //Init AppView
        this.appView = new AppView(this);

        this.appView.setMinimumSize(new Dimension(1280, 810));
        this.appView.setTitle("Individualleistung Programmierung II (J. Seidel, WWI22SCA)");
        this.appView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Terminate the complete app when 'X' on the window title bar is clicked
        this.appView.setLocationRelativeTo(null); //Let window appear in the center of the screen when started
        this.appView.setVisible(true);

        this.appView.init();

        //Init AppModel
        this.appModel = new AppModel(this);
        this.appModel.init();
    }
    public void run(){

    }
    public void close(){
        System.exit(0);
    }
}
