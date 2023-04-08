package mvc.panels;

import mvc.AppModel;
import mvc.AppView;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RandomMediumPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    public RandomMediumPanel(AppView appView){
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

        //Create component panel as a component inside of this panel
        JPanel componentPanel = new JPanel(new GridBagLayout());
        componentPanel.setBorder(new EmptyBorder(20, 20, 20, 0)); //Border padding
        this.add(componentPanel, BorderLayout.WEST); //Attach component panel on left side

        //Describe the cell behavior in GridLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.fill = GridBagConstraints.HORIZONTAL; //Only stretch elements horizontal in cell
    }

    @Override
    public void initStyles() {

    }

    @Override
    public void initImages() {

    }

    @Override
    public void initActionListeners(){

    }

    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

    }
}
