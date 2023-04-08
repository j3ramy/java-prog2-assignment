package mvc;

import mvc.panels.SettingsPanel;
import util.file.FilePath;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class AppView extends JFrame {
    private static final int SETTINGS_TAB_ID = 5;

    private final AppController appController;

    private JPanel mainPanel, userFeedbackPanel, menuPanel, statusBarPanel;
    private SettingsPanel settingsPanel;
    private JTabbedPane tabbedPanel;
    private JButton closeButton;
    private JLabel menuImage;

    public AppView(AppController appController){
        this.appController = appController;
    }

    public void init(){
        this.mainPanel.setBackground(Color.WHITE);
        this.add(this.mainPanel);

        this.settingsPanel = new SettingsPanel();
        this.settingsPanel.init();

        this.initLayouts();
        this.initStyles();
        this.initImages();
        this.initActionListeners();

        /*
        this.showDialog("Willkommen!",
                "Hey User! Wir haben festgestellt, dass du diese App das erste Mal nutzt.\n" +
                        "Lege deine Sprache, sowie deine Provider fest (Dies kannst du immer anpassen)", JOptionPane.INFORMATION_MESSAGE);
        this.tabbedPanel.setSelectedIndex(SETTINGS_TAB_ID);

         */

        this.setAllTranslations(null);
    }

    public void setAllTranslations(HashMap<String, String> translations){
        //this.tabbedPanel.add("Start", null);
        //this.tabbedPanel.add("Zufällig", null);
        //this.tabbedPanel.add("Suche", null);
        //this.tabbedPanel.add("Top 100", null);
        //this.tabbedPanel.add("Bewertungen", null);
        this.tabbedPanel.add("Einstellungen", this.settingsPanel);
        this.settingsPanel.setTranslations(translations);
    }

    public void showDialog(String title, String message, int dialogType){
        JOptionPane.showMessageDialog(this.mainPanel, message, title, dialogType);
    }

    private void initLayouts(){
        this.settingsPanel.setLayout(new BoxLayout(this.settingsPanel, BoxLayout.Y_AXIS));
    }

    private void initStyles(){
        this.userFeedbackPanel.setBackground(Color.WHITE);
        this.menuPanel.setBackground(Color.WHITE);
        this.statusBarPanel.setBackground(Color.WHITE);

        this.closeButton.setBackground(new Color(245, 245, 245));
    }

    private void initImages(){
        try {
            ImageIcon imageIcon = new ImageIcon(FilePath.MENU_IMAGE_PATH);
            Image image = imageIcon.getImage().getScaledInstance(150, 150,  Image.SCALE_SMOOTH);
            this.menuImage.setIcon(new ImageIcon(image));
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private void initActionListeners(){
        this.closeButton.addActionListener(e -> {
            this.showDialog("Bye Bye!", "Danke, dass du die App benutzt hast! Bis bald :)", JOptionPane.INFORMATION_MESSAGE);
            this.appController.close();
        });
    }
}
