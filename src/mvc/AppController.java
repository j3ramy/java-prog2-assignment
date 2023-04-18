package mvc;

import javax.swing.*;
import java.awt.*;

public class AppController {
    private AppView appView; //AppView reference
    private AppModel appModel; //AppModel reference

    /**
     * Gets app view
     * @return  app view
     *
     * @BigO: O(1)
     * **/
    public AppView getAppView() {
        return appView;
    }

    /**
     * Gets app model
     * @return  app model
     *
     * @BigO: O(1)
     * **/
    public AppModel getAppModel() {
        return appModel;
    }

    /**
     * Initializes the rest of the mvc pattern by creating a JFrame view for the GUI and set its properties. Init the model as well where the data is loaded
     *
     * @BigO: O(n)
     * **/
    public void init(){
        //Init AppView
        this.appModel = new AppModel(this);
        this.appView = new AppView(this);

        this.appView.setMinimumSize(new Dimension(1440, 900));
        this.appView.setTitle("Individualleistung Programmierung II (J. Seidel, WWI22SCA)");
        this.appView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Terminate the complete app when 'X' on the window title bar is clicked
        this.appView.setLocationRelativeTo(null); //Let window appear in the center of the screen when started
        this.appView.setVisible(true);

        this.appView.init();

        //Init AppModel
        this.appModel.init();
    }

    /**
     * Closes the application
     *
     * @BigO: O(1)
     * **/
    public void close(){
        //Close application
        System.exit(0);
    }
}
