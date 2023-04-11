package mvc;

import mvc.view.panel.RandomMediumPanel;
import mvc.view.panel.SearchMediumPanel;
import mvc.view.panel.SettingsPanel;
import mvc.view.panel.StartPanel;
import util.enums.AppState;
import util.file.FilePaths;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class AppView extends JFrame implements IViewPanel {
    private final AppController appController;

    private JPanel mainPanel, userFeedbackPanel, sidebarPanel, statusBarPanel;

    private StartPanel startPanel;
    private RandomMediumPanel randomMediumPanel;
    private SearchMediumPanel searchMediumPanel;
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

        /*
        this.tabbedPanel.addChangeListener(e -> {
            String tabName = tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex());
            if(tabName.equalsIgnoreCase(appController.getAppModel().getTranslation("label.main.random_medium")))
                randomMediumPanel.searchForRandomMedium();
        });

         */

        this.startPanel = new StartPanel(this);
        this.startPanel.init();

        this.randomMediumPanel = new RandomMediumPanel(this);
        this.randomMediumPanel.init();

        this.searchMediumPanel = new SearchMediumPanel(this);
        this.searchMediumPanel.init();

        this.settingsPanel = new SettingsPanel(this);
        this.settingsPanel.init();
    }

    @Override
    public void initStyles(){
        this.mainPanel.setBackground(Color.WHITE);

        this.userFeedbackPanel.setBackground(Color.WHITE);
        this.userFeedbackPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        this.sidebarPanel.setBackground(Color.WHITE);
        this.closeButton.setBackground(new Color(245, 245, 245));

        this.statusBarPanel.setBackground(Color.WHITE);
        this.statusBarPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
    }

    @Override
    public void initImages(){
        try {
            ImageIcon imageIcon = new ImageIcon(FilePaths.MENU_IMAGE_PATH);
            Image image = imageIcon.getImage().getScaledInstance(150, 150,  Image.SCALE_SMOOTH);
            this.menuImage.setIcon(new ImageIcon(image));
        }
        catch (Exception e){
            this.showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void initActionListeners(){
        this.closeButton.addActionListener(e -> this.showCloseAppDialog());
    }

    @Override
    public void setTranslations() {
        this.closeButton.setText(this.getAppController().getAppModel().getTranslation("button.main.close"));

        this.tabbedPanel.add(this.getAppController().getAppModel().getTranslation("label.main.start"), this.startPanel);
        this.tabbedPanel.add(this.getAppController().getAppModel().getTranslation("label.main.random_medium"), this.randomMediumPanel);
        this.tabbedPanel.add(this.getAppController().getAppModel().getTranslation("label.main.search_medium"), this.searchMediumPanel);
        this.tabbedPanel.add(this.getAppController().getAppModel().getTranslation("label.main.settings"), this.settingsPanel);

        this.setUserFeedbackText("label.user.welcome");
        this.setStatusBarText(AppState.READY);
    }

    public void enableTabs(){
        for(int i = 0; i < this.tabbedPanel.getTabCount(); i++){
            this.tabbedPanel.setEnabledAt(i, true);
        }
    }

    public void disableTabs(){
        for(int i = 0; i < this.tabbedPanel.getTabCount(); i++){
            //Only disable tabs which have to depend on data sources (which might not be loaded completely)
            //if(i != 0 && i != this.tabbedPanel.getTabCount() - 1) //Disable all tabs except start and settings tab
                //this.tabbedPanel.setEnabledAt(i, false);
        }
    }

    public void setSettings(){
        this.settingsPanel.loadSettings(this.appController.getAppModel().getCustomData());
    }

    public void setAllTranslations(){
        this.setTranslations();

        this.startPanel.setTranslations();
        this.randomMediumPanel.setTranslations();
        this.searchMediumPanel.setTranslations();
        this.settingsPanel.setTranslations();
    }

    public void setStatusBarText(AppState appState){
        JLabel statusBarLabel = (JLabel) this.statusBarPanel.getComponents()[0];

        switch (appState){
            case LOAD_MEDIUMS -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.load_mediums"));
            case LOAD_REVIEWS -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.load_reviews"));
            case LOAD_IMDB_RATING -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.load_imdb_rating"));
            case SEARCH -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.search"));
            case READY -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.ready"));
        }
    }

    public void setUserFeedbackText(String key){
        JLabel userFeedbackLabel = (JLabel) this.userFeedbackPanel.getComponents()[0];
        userFeedbackLabel.setText(this.getAppController().getAppModel().getTranslation(key));
        userFeedbackLabel.setFont(new Font(null, Font.BOLD, 14));
    }

    public void showDialog(String title, String message, int dialogType) {
        JOptionPane.showMessageDialog(this.mainPanel, message, title, dialogType);
    }

    public void showFirstAppStartDialog(){
        this.showDialog(this.getAppController().getAppModel().getTranslation("dialog.title.main.no_settings"),
                this.getAppController().getAppModel().getTranslation("dialog.body.main.no_settings"), JOptionPane.QUESTION_MESSAGE);
    }

    public void showCloseAppDialog(){
        this.showDialog(this.getAppController().getAppModel().getTranslation("dialog.title.main.close"),
                this.getAppController().getAppModel().getTranslation("dialog.body.main.close"), JOptionPane.INFORMATION_MESSAGE);
        this.appController.close();
    }

    public void showNoMediumFoundDialog(){
        this.showDialog(this.getAppController().getAppModel().getTranslation("dialog.title.main.no_result"),
                this.getAppController().getAppModel().getTranslation("dialog.body.main.no_result"), JOptionPane.ERROR_MESSAGE);
    }

    public void showAcceptRecommendationDialog(ActionListener confirmAction, ActionListener rejectAction, int currentIndex, int mediumCount){
        String title = this.getAppController().getAppModel().getTranslation("dialog.title.recommendation").
                replace("#", Integer.toString(currentIndex)).replace("+", Integer.toString(mediumCount));
        int input = JOptionPane.showConfirmDialog(null, this.getAppController().getAppModel().getTranslation("dialog.body.recommendation"),
                title, JOptionPane.YES_NO_CANCEL_OPTION);

        switch (input) {
            case 0 -> confirmAction.actionPerformed(null);
            case 1 -> rejectAction.actionPerformed(null);
        }
    }
}
