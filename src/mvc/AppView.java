package mvc;

import mvc.view.tab.*;
import mvc.view.util.DialogHandler;
import util.enums.LoadingState;
import util.file.FilePaths;
import util.interfaces.IViewInit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppView extends JFrame implements IViewInit {
    private final AppController appController;

    private final DialogHandler dialogHandler;
    private JPanel mainPanel, headerPanel, sidebarPanel, statusBarPanel;

    private RandomMediumTab randomMediumTab;
    private SearchMediumTab searchMediumTab;
    private Top100ImdbTab top100ImdbTab;
    private Top100RatingTab top100RatingTab;
    private SettingsTab settingsTab;

    private JTabbedPane tabbedPane;
    private JButton exitButton;
    private JLabel sidebarImage;

    public AppController getAppController() {
        return appController;
    }

    public AppModel getAppModel(){
        return this.appController.getAppModel();
    }

    public DialogHandler getDialogHandler() {
        return dialogHandler;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public AppView(AppController appController){
        this.appController = appController;
        this.dialogHandler = new DialogHandler(this);
    }

    @Override
    public void init(){
        this.initComponents();
        this.initStyles();
        this.initImages();
        this.initListeners();
    }

    @Override
    public void initComponents(){
        this.add(this.mainPanel);

        this.randomMediumTab = new RandomMediumTab(this);
        this.randomMediumTab.init();

        this.searchMediumTab = new SearchMediumTab(this);
        this.searchMediumTab.init();

        this.top100ImdbTab = new Top100ImdbTab(this);
        this.top100ImdbTab.init();

        this.top100RatingTab = new Top100RatingTab(this);
        this.top100RatingTab.init();

        this.settingsTab = new SettingsTab(this);
        this.settingsTab.init();
    }

    @Override
    public void initStyles(){
        this.mainPanel.setBackground(Color.WHITE);

        this.headerPanel.setBackground(Color.WHITE);
        this.headerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        this.sidebarPanel.setBackground(Color.WHITE);
        this.exitButton.setBackground(new Color(245, 245, 245));

        this.statusBarPanel.setBackground(Color.WHITE);
        this.statusBarPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
    }

    public void initImages(){
        try {
            ImageIcon imageIcon = new ImageIcon(FilePaths.MENU_IMAGE_PATH);

            Image image = imageIcon.getImage().getScaledInstance(200, this.getHeight(),  Image.SCALE_SMOOTH);
            this.sidebarImage.setIcon(new ImageIcon(image));
        }
        catch (Exception e){
            this.dialogHandler.showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void initListeners(){
        this.tabbedPane.addChangeListener(e -> {
            if(this.tabbedPane.getSelectedIndex() == 1)
            {
                this.searchMediumTab.focusInputTextField();
            }
        });

        this.exitButton.addActionListener(e -> this.dialogHandler.showCloseAppDialog());
    }

    @Override
    public void setTranslations() {
        this.exitButton.setText(this.getAppController().getAppModel().getTranslation("button.main.close"));

        this.tabbedPane.add(this.getAppController().getAppModel().getTranslation("label.main.random_medium"), this.randomMediumTab);
        this.tabbedPane.add(this.getAppController().getAppModel().getTranslation("label.main.search_medium"), this.searchMediumTab);
        this.tabbedPane.add(this.getAppController().getAppModel().getTranslation("label.main.top_100_imdb"), this.top100ImdbTab);
        this.tabbedPane.add(this.getAppController().getAppModel().getTranslation("label.main.top_100_rating"), this.top100RatingTab);
        this.tabbedPane.add(this.getAppController().getAppModel().getTranslation("label.main.settings"), this.settingsTab);

        this.disableTabs();
        this.tabbedPane.setSelectedIndex(this.tabbedPane.getTabCount() - 1);

        this.setHeaderText("label.user.welcome");
        this.setStatusBarText(LoadingState.READY);
    }

    public void setSettings(){
        this.settingsTab.fillSettings(this.appController.getAppModel().getCustomData());
    }

    public void setAllTranslations(){
        this.setTranslations();

        this.randomMediumTab.setTranslations();
        this.searchMediumTab.setTranslations();
        this.top100ImdbTab.setTranslations();
        this.top100RatingTab.setTranslations();
        this.settingsTab.setTranslations();
    }

    public void setStatusBarText(LoadingState loadingState){
        JLabel statusBarLabel = (JLabel) this.statusBarPanel.getComponents()[0];

        switch (loadingState){
            case LOAD_MEDIUMS -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.load_mediums"));
            case LOAD_REVIEWS -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.load_reviews"));
            case LOAD_IMDB_RATING -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.load_imdb_rating"));
            case SEARCH -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.search"));
            case READY -> statusBarLabel.setText(this.getAppController().getAppModel().getTranslation("label.status.ready"));
        }
    }

    public void setHeaderText(String translationKey){
        JLabel userFeedbackLabel = (JLabel) this.headerPanel.getComponents()[0];
        userFeedbackLabel.setText(this.getAppController().getAppModel().getTranslation(translationKey));
        userFeedbackLabel.setFont(new Font(null, Font.BOLD, 14));
    }

    public void enableTabs(){
        for(int i = 0; i < this.tabbedPane.getTabCount(); i++){
            this.tabbedPane.setEnabledAt(i, true);
        }
    }

    public void disableTabs(){
        for(int i = 0; i < this.tabbedPane.getTabCount(); i++){
            //Only disable tabs which have to depend on data sources (which might not be loaded completely)
            if(i != this.tabbedPane.getTabCount() - 1) //Disable all tabs except start and settings tab
                this.tabbedPane.setEnabledAt(i, false);
        }
    }
}
