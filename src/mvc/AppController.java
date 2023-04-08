package mvc;

import javax.swing.*;

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

        this.appView.setSize(854, 480);
        this.appView.setTitle("Medium Recommendation App");
        this.appView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.appView.setVisible(true);
        this.appView.setLocationRelativeTo(null); //Make Window appear in the center of the screen

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
