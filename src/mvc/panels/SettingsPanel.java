package mvc.panels;

import util.Util;
import util.enums.Language;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

public class SettingsPanel extends JPanel {
    private JComboBox languageComboBox;
    private JLabel languageLabel, providersLabel;
    private JCheckBox netflixCheckbox, disneyPlusCheckbox, amazonPrimeCheckbox, applePlusCheckbox;
    private JButton saveButton;

    public void init(){
        this.setLayout(new BorderLayout());

        JPanel componentPanel = new JPanel(new GridBagLayout());
        componentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.add(componentPanel, BorderLayout.WEST);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.1f;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        this.languageLabel = new JLabel("Sprache:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        componentPanel.add(this.languageLabel, constraints);

        this.languageComboBox = new JComboBox(Util.getLanguages());
        constraints.gridx = 1;
        componentPanel.add(this.languageComboBox, constraints);

        this.providersLabel = new JLabel("Deine Anbieter:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 0.1f;
        componentPanel.add(this.providersLabel, constraints);

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        constraints.gridx = 1;
        constraints.gridy = 1;
        componentPanel.add(checkboxPanel, constraints);

        this.netflixCheckbox = new JCheckBox("Netflix");
        checkboxPanel.add(this.netflixCheckbox);

        this.disneyPlusCheckbox = new JCheckBox("Disney Plus");
        checkboxPanel.add(this.disneyPlusCheckbox);

        this.amazonPrimeCheckbox = new JCheckBox("Amazon Prime");
        checkboxPanel.add(this.amazonPrimeCheckbox);

        this.applePlusCheckbox = new JCheckBox("Apple Plus");
        checkboxPanel.add(this.applePlusCheckbox);

        this.saveButton = new JButton("Speichern");
        constraints.gridx = 0;
        constraints.gridy = 2;
        componentPanel.add(this.saveButton, constraints);
    }

    public void setTranslations(HashMap<String, String> translations){
        //this.netflixCheckbox.setText("");
        //...
    }
}
