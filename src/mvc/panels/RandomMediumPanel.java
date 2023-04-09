package mvc.panels;

import mvc.AppModel;
import mvc.AppView;
import util.Colors;
import util.data.Medium;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RandomMediumPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JButton confirmButton, rejectButton;
    private MetadataViewPanel metadataViewPanel;

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
        componentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.add(componentPanel, BorderLayout.CENTER);

        //Describe the cell behavior in GridLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL; //Only stretch elements horizontal in cell

        //Create component panel as a component inside of this panel
        this.metadataViewPanel = new MetadataViewPanel(this.appView);
        this.metadataViewPanel.init();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        componentPanel.add(this.metadataViewPanel, constraints);

        JPanel buttonContainer = new JPanel(new GridLayout(2, 1, 0, 10));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        componentPanel.add(buttonContainer, constraints);

        this.confirmButton = new JButton();
        buttonContainer.add(this.confirmButton);

        this.rejectButton = new JButton();
        buttonContainer.add(this.rejectButton);
    }

    @Override
    public void initStyles() {
        this.confirmButton.setBackground(Colors.GREEN);
        this.rejectButton.setBackground(Colors.RED);
    }

    @Override
    public void initImages() {

    }

    @Override
    public void initActionListeners(){
        this.confirmButton.addActionListener((e) -> this.appView.showCloseAppDialog());

        this.rejectButton.addActionListener((e) -> this.searchForRandomMedium());
    }

    @Override
    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.confirmButton.setText(appModel.getTranslation("button.confirm"));
        this.rejectButton.setText(appModel.getTranslation("button.reject"));

        this.metadataViewPanel.setTranslations();
    }

    public void searchForRandomMedium(){
        Medium medium = this.appView.getAppController().getAppModel().getRandomMedium();

        if(medium == null){
            this.appView.showNoMediumFoundDialog();
            return;
        }

        this.metadataViewPanel.fillDataView(medium);
    }
}
