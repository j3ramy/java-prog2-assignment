package mvc.panels;

import mvc.AppModel;
import mvc.AppView;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StartPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JLabel contentLabel;

    public StartPanel(AppView appView){
        this.appView = appView;
    }

    //Initialize all class components inside this panel
    @Override
    public void init(){
        this.initComponents();
        this.initStyles();
        this.initImages();
        this.initActionListeners();
    }

    @Override
    public void initComponents() {
        //Set layout
        this.setLayout(new BorderLayout());

        this.contentLabel = new JLabel();
        this.add(this.contentLabel, BorderLayout.NORTH); //Attach component panel on left side
    }

    @Override
    public void initStyles() {
        this.contentLabel.setBorder(new EmptyBorder(10, 10, 0, 0));
    }

    @Override
    public void initImages() {

    }

    @Override
    public void initActionListeners(){

    }

    @Override
    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.contentLabel.setText(appModel.getTranslation("label.main.start.content"));
    }
}
