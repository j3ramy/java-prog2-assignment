package mvc.panels;

import mvc.AppModel;
import mvc.AppView;
import util.Util;
import util.enums.Language;
import util.enums.Provider;
import util.file.CustomData;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class SettingsPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JComboBox languageComboBox;
    private JLabel languageLabel;
    private JPanel checkboxPanel;
    private JCheckBox netflixCheckbox, disneyPlusCheckbox, amazonPrimeCheckbox, applePlusCheckbox;
    private JButton saveButton;

    public SettingsPanel(AppView appView){
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
        componentPanel.setBorder(new EmptyBorder(20, 20, 200, 500)); //Border padding
        this.add(componentPanel, BorderLayout.CENTER); //Attach component panel on left side

        //Describe the cell behavior in GridLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.fill = GridBagConstraints.HORIZONTAL; //Only stretch elements horizontal in cell

        this.languageLabel = new JLabel("");
        constraints.gridx = 0; //Set cell x
        constraints.gridy = 0; //Set cell y
        componentPanel.add(this.languageLabel, constraints); //Add component with constraints

        this.languageComboBox = new JComboBox(Language.values());
        constraints.gridx = 1;
        componentPanel.add(this.languageComboBox, constraints);

        //Panel for checkboxes to stack them on each other
        this.checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(this.checkboxPanel, BoxLayout.Y_AXIS)); //Stack vertically
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        componentPanel.add(this.checkboxPanel, constraints);

        this.netflixCheckbox = new JCheckBox("Netflix");
        this.checkboxPanel.add(this.netflixCheckbox);

        this.disneyPlusCheckbox = new JCheckBox("Disney Plus");
        this.checkboxPanel.add(this.disneyPlusCheckbox);

        this.amazonPrimeCheckbox = new JCheckBox("Amazon Prime");
        this.checkboxPanel.add(this.amazonPrimeCheckbox);

        this.applePlusCheckbox = new JCheckBox("Apple Plus");
        this.checkboxPanel.add(this.applePlusCheckbox);

        this.saveButton = new JButton("");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        componentPanel.add(this.saveButton, constraints);
    }

    @Override
    public void initStyles() {

    }

    @Override
    public void initImages() {

    }

    @Override
    public void initActionListeners(){
        this.saveButton.addActionListener(e -> {
            this.saveSettings();
        });
    }

    @Override
    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.languageLabel.setText(appModel.getTranslation("label.settings.language"));
        this.checkboxPanel.setBorder(BorderFactory.createTitledBorder(appModel.getTranslation("label.settings.providers")));
        this.saveButton.setText(appModel.getTranslation("button.settings.save"));
    }

    public void loadSettings(CustomData data){
        for(Provider provider : data.getProviders()){
            if(provider == Provider.NETFLIX) this.netflixCheckbox.setSelected(true);
            if(provider == Provider.DISNEY_PLUS) this.disneyPlusCheckbox.setSelected(true);
            if(provider == Provider.AMAZON_PRIME) this.amazonPrimeCheckbox.setSelected(true);
            if(provider == Provider.APPLE_PLUS) this.applePlusCheckbox.setSelected(true);
        }

        this.languageComboBox.setSelectedItem(data.getLanguage());
    }

    private void saveSettings(){
        ArrayList<Provider> providers = new ArrayList<>();
        if(this.netflixCheckbox.isSelected()) providers.add(Provider.NETFLIX);
        if(this.disneyPlusCheckbox.isSelected()) providers.add(Provider.DISNEY_PLUS);
        if(this.amazonPrimeCheckbox.isSelected()) providers.add(Provider.AMAZON_PRIME);
        if(this.applePlusCheckbox.isSelected()) providers.add(Provider.APPLE_PLUS);

        CustomData customData = new CustomData(
                Language.valueOf(this.languageComboBox.getSelectedItem().toString()),
                providers.toArray(Provider[]::new)
        );

        //Set and save custom data
        this.appView.getAppController().getAppModel().setCustomData(customData);

        //Set translations again in real time
        this.appView.setAllTranslations();

        if(!this.appView.getAppController().getAppModel().hasMediums())
            this.appView.disableTabs();

        AppModel appModel = this.appView.getAppController().getAppModel();
        this.appView.showDialog(appModel.getTranslation("dialog.title.settings.save"),
                appModel.getTranslation("dialog.body.settings.save"), JOptionPane.INFORMATION_MESSAGE);
    }
}
