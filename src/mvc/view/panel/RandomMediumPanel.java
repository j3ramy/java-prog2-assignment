package mvc.view.panel;

import mvc.AppModel;
import mvc.AppView;
import mvc.view.widget.AllReviewsDialog;
import mvc.view.widget.MetadataViewPanel;
import mvc.view.widget.ReviewViewPanel;
import util.data.AudienceReview;
import util.data.Medium;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RandomMediumPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JButton loadMediumButton, showAllReviewsButton;
    private MetadataViewPanel metadataViewPanel;
    private ReviewViewPanel bestReviewViewPanel, worstReviewViewPanel;
    private Medium medium;

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
        constraints.fill = GridBagConstraints.HORIZONTAL;

        this.loadMediumButton = new JButton();
        constraints.gridx = 0;
        constraints.gridy = 0;
        componentPanel.add(this.loadMediumButton, constraints);

        this.metadataViewPanel = new MetadataViewPanel(this.appView);
        this.metadataViewPanel.init();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        componentPanel.add(this.metadataViewPanel, constraints);

        JPanel buttonContainer = new JPanel(new GridLayout(1, 1));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        componentPanel.add(buttonContainer, constraints);

        this.showAllReviewsButton = new JButton();
        this.showAllReviewsButton.setVisible(false);
        buttonContainer.add(this.showAllReviewsButton);

        this.bestReviewViewPanel = new ReviewViewPanel(this.appView);
        this.bestReviewViewPanel.init();
        constraints.gridx = 1;
        constraints.gridy = 2;
        componentPanel.add(this.bestReviewViewPanel, constraints);

        this.worstReviewViewPanel = new ReviewViewPanel(this.appView);
        this.worstReviewViewPanel.init();
        constraints.gridx = 2;
        constraints.gridy = 2;
        componentPanel.add(this.worstReviewViewPanel, constraints);
    }

    @Override
    public void initStyles() {
        this.bestReviewViewPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.worstReviewViewPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
    }

    @Override
    public void initImages() {

    }

    @Override
    public void initActionListeners(){
        this.loadMediumButton.addActionListener((e) -> this.searchForRandomMedium());

        this.showAllReviewsButton.addActionListener((e) -> {
            AppModel appModel = this.appView.getAppController().getAppModel();
            UIManager.put("OptionPane.minimumSize", new Dimension(600,400));
            JOptionPane.showMessageDialog(null, new AllReviewsDialog(this.appView, this.medium.getTitle()),
                    appModel.getTranslation("dialog.title.all_reviews") + " " + this.medium.getTitle(), JOptionPane.PLAIN_MESSAGE);
            UIManager.put("OptionPane.minimumSize", null);
        });
    }

    @Override
    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.loadMediumButton.setText(appModel.getTranslation("button.random_medium"));
        this.showAllReviewsButton.setText(appModel.getTranslation("button.all_reviews"));

        this.metadataViewPanel.setTranslations();
        this.bestReviewViewPanel.setTranslations();
        this.worstReviewViewPanel.setTranslations();
    }

    public void searchForRandomMedium(){
        AppModel appModel = this.appView.getAppController().getAppModel();
        Medium medium = appModel.getRandomMedium();

        if(medium == null){
            this.appView.showNoMediumFoundDialog();
            return;
        }

        this.medium = medium;
        this.metadataViewPanel.fillDataView(medium);

        AudienceReview[] reviews = appModel.getBestAndWorstReviewByTitle(medium.getTitle());
        this.showAllReviewsButton.setVisible(true);
        this.showAllReviewsButton.setEnabled(reviews[0] != null && reviews[1] != null);
        this.worstReviewViewPanel.fillDataView(reviews[0]);
        this.bestReviewViewPanel.fillDataView(reviews[1]);

        this.appView.showAcceptRecommendationDialog((e) -> this.appView.showCloseAppDialog(), (e) -> this.searchForRandomMedium(), 1, 1);
    }
}
