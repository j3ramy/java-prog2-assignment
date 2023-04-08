package mvc;

import mvc.panels.SettingsPanel;
import util.enums.AppState;
import util.file.FilePath;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppView extends JFrame implements IViewPanel {
    private static final int SETTINGS_TAB_ID = 0;

    private final AppController appController;

    private JPanel mainPanel, userFeedbackPanel, sidebarPanel, statusBarPanel;
    private SettingsPanel settingsPanel;
    private JTabbedPane tabbedPanel;
    private JButton closeButton;
    private JLabel menuImage;

    public AppController getAppController() {
        return appController;
    }

    public AppView(AppController appController){
        this.appController = appController;
    }

    @Override
    public void init(){
        this.initComponents();
        this.initStyles();
        this.initImages();
        this.initActionListeners();
    }

    @Override
    public void initComponents(){
        this.add(this.mainPanel);

        this.settingsPanel = new SettingsPanel(this);
        this.settingsPanel.init();

        this.tabbedPanel.add(this.settingsPanel);
    }

    @Override
    public void initStyles(){
        this.mainPanel.setBackground(Color.WHITE);

        this.userFeedbackPanel.setBackground(Color.WHITE);

        this.sidebarPanel.setBackground(Color.WHITE);

        this.statusBarPanel.setBackground(Color.WHITE);
        this.statusBarPanel.setBorder(new EmptyBorder(0, 5, 0, 0));

        this.closeButton.setBackground(new Color(245, 245, 245));
    }

    @Override
    public void initImages(){
        try {
            ImageIcon imageIcon = new ImageIcon(FilePath.MENU_IMAGE_PATH);
            Image image = imageIcon.getImage().getScaledInstance(150, 150,  Image.SCALE_SMOOTH);
            this.menuImage.setIcon(new ImageIcon(image));
        }
        catch (Exception e){
            this.showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void initActionListeners(){
        this.closeButton.addActionListener(e -> {

            this.showDialog(this.getAppController().getAppModel().getTranslation("dialog.title.main.close"),
                    this.getAppController().getAppModel().getTranslation("dialog.body.main.close"), JOptionPane.INFORMATION_MESSAGE);
            this.appController.close();
        });
    }

    public void setSettings(){
        this.settingsPanel.loadSettings(this.appController.getAppModel().getCustomData());
    }

    public void setAllTranslations(){
        this.closeButton.setText(this.getAppController().getAppModel().getTranslation("button.main.close"));
        this.tabbedPanel.add(this.getAppController().getAppModel().getTranslation("label.main.settings"), this.settingsPanel);

        this.setUserFeedbackText("label.user.welcome");
        this.setStatusBarText(AppState.READY);

        this.settingsPanel.setTranslations();
    }

    public void setStatusBarText(AppState appState){
        JLabel statusBarLabel = (JLabel) this.statusBarPanel.getComponents()[0];

        switch (appState){
            case LOAD -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.load"));
            case SEARCH -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.search"));
            case READY -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.ready"));
        }
    }

    public void setUserFeedbackText(String key){
        JLabel userFeedbackLabel = (JLabel) this.userFeedbackPanel.getComponents()[0];
        userFeedbackLabel.setText(this.getAppController().getAppModel().getTranslation(key));
    }

    public void showDialog(String title, String message, int dialogType) {
        JOptionPane.showMessageDialog(this.mainPanel, message, title, dialogType);
    }

    public void showFirstAppStartDialog(){
        this.showDialog(this.getAppController().getAppModel().getTranslation("dialog.title.main.no_settings"),
                this.getAppController().getAppModel().getTranslation("dialog.body.main.no_settings"), JOptionPane.QUESTION_MESSAGE);
        this.tabbedPanel.setSelectedIndex(SETTINGS_TAB_ID);
    }
}
