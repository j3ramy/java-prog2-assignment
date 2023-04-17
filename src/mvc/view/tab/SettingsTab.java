package mvc.view.tab;

import mvc.AppView;
import util.enums.Language;
import util.enums.Provider;
import util.file.CustomData;
import util.interfaces.IViewInit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class SettingsTab extends JPanel implements IViewInit {
    private final AppView appView;

    private JComboBox languageComboBox; //Dropdown for choosing the language
    private JLabel languageLabel;
    private JPanel checkboxContainer; //Container for the provider checkboxes
    private JCheckBox netflixCheckbox, disneyPlusCheckbox, amazonPrimeCheckbox, applePlusCheckbox; //Checkboxes for the providers
    private JButton saveButton;

    //Constructor
    public SettingsTab(AppView appView){
        this.appView = appView;
    }

    //Initialize all class components inside this panel
    @Override
    public void init(){
        this.initComponents();
        this.initListeners();
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

        //Initialize language label
        this.languageLabel = new JLabel("");
        constraints.gridx = 0; //Set cell x
        constraints.gridy = 0; //Set cell y
        componentPanel.add(this.languageLabel, constraints); //Add component with constraints

        //Initialize language dropdown
        this.languageComboBox = new JComboBox(Language.values());
        constraints.gridx = 1;
        componentPanel.add(this.languageComboBox, constraints);

        //Container for checkboxes to stack them above each other
        this.checkboxContainer = new JPanel();
        checkboxContainer.setLayout(new BoxLayout(this.checkboxContainer, BoxLayout.Y_AXIS)); //Stack vertically
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2; //Cell width
        componentPanel.add(this.checkboxContainer, constraints);

        //Initialize the checkboxes
        this.netflixCheckbox = new JCheckBox("Netflix");
        this.checkboxContainer.add(this.netflixCheckbox);

        this.disneyPlusCheckbox = new JCheckBox("Disney Plus");
        this.checkboxContainer.add(this.disneyPlusCheckbox);

        this.amazonPrimeCheckbox = new JCheckBox("Amazon Prime");
        this.checkboxContainer.add(this.amazonPrimeCheckbox);

        this.applePlusCheckbox = new JCheckBox("Apple Plus");
        this.checkboxContainer.add(this.applePlusCheckbox);

        //Initialize save button
        this.saveButton = new JButton("");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        componentPanel.add(this.saveButton, constraints);
    }

    @Override
    public void initStyles() {}

    @Override
    public void initListeners(){
        //Initialize action listener to save the settings when save button is clicked
        this.saveButton.addActionListener(e -> this.saveSettings());
    }

    @Override
    public void setTranslations(){
        this.languageLabel.setText(this.appView.getAppModel().getTranslation("label.settings.language"));

        this.checkboxContainer.setBorder(BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.settings.providers")));

        this.saveButton.setText(this.appView.getAppModel().getTranslation("button.settings.save"));
    }

    public void fillSettings(CustomData data){
        //Set the checkboxes selected when provider is saved in custom data
        for(Provider provider : data.getProviders()){
            if(provider == Provider.NETFLIX) this.netflixCheckbox.setSelected(true);
            if(provider == Provider.DISNEY_PLUS) this.disneyPlusCheckbox.setSelected(true);
            if(provider == Provider.AMAZON_PRIME) this.amazonPrimeCheckbox.setSelected(true);
            if(provider == Provider.APPLE_PLUS) this.applePlusCheckbox.setSelected(true);
        }

        //Set selected language from saved data
        this.languageComboBox.setSelectedItem(data.getLanguage());
    }

    private void saveSettings(){
        //Get all selected providers from checkboxes
        ArrayList<Provider> providers = new ArrayList<>();
        if(this.netflixCheckbox.isSelected()) providers.add(Provider.NETFLIX);
        if(this.disneyPlusCheckbox.isSelected()) providers.add(Provider.DISNEY_PLUS);
        if(this.amazonPrimeCheckbox.isSelected()) providers.add(Provider.AMAZON_PRIME);
        if(this.applePlusCheckbox.isSelected()) providers.add(Provider.APPLE_PLUS);

        //Create new custom data with the new language and selected providers
        CustomData customData = new CustomData(
                Language.valueOf(this.languageComboBox.getSelectedItem().toString()),
                providers.toArray(Provider[]::new)
        );

        //Set and save custom data
        this.appView.getAppController().getAppModel().setCustomData(customData);

        //Set translations again at runtime
        this.appView.setAllTranslations();

        //Disable tabs to force the user to restart the app
        this.appView.disableTabs();

        //Show save settings dialog where the user can restart the app
        this.appView.getDialogHandler().showSaveSettingsDialog();
    }
}
