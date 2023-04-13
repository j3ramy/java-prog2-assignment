package mvc.view.panel;

import mvc.AppModel;
import mvc.AppView;
import mvc.view.widget.AllReviewsDialog;
import mvc.view.widget.ImdbRatingViewPanel;
import mvc.view.widget.MetadataViewPanel;
import mvc.view.widget.ReviewViewPanel;
import util.data.AudienceReview;
import util.data.ImdbRating;
import util.data.Medium;
import util.file.FilePaths;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

public class Top100Panel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JButton searchMediumButton, showAllReviewsButton;
    private ImdbRatingViewPanel imdbRatingViewPanel;
    private MetadataViewPanel metadataViewPanel;
    private ReviewViewPanel bestReviewViewPanel, worstReviewViewPanel;
    private Medium medium;
    private ArrayList<ImdbRating> imdbRatings;
    private int currentListIndex = 0;

    public Top100Panel(AppView appView){
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

        JPanel widgetContainer = new JPanel();
        widgetContainer.setLayout(new BoxLayout(widgetContainer, BoxLayout.Y_AXIS));
        constraints.gridx = 0;
        constraints.gridy = 0;
        componentPanel.add(widgetContainer, constraints);

        this.searchMediumButton = new JButton();
        searchMediumButton.setHorizontalAlignment(JButton.CENTER);
        searchMediumButton.setAlignmentX(CENTER_ALIGNMENT);
        widgetContainer.add(this.searchMediumButton);

        widgetContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        this.imdbRatingViewPanel = new ImdbRatingViewPanel(this.appView);
        this.imdbRatingViewPanel.init();
        widgetContainer.add(this.imdbRatingViewPanel);

        widgetContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        this.metadataViewPanel = new MetadataViewPanel(this.appView);
        this.metadataViewPanel.init();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        componentPanel.add(this.metadataViewPanel, constraints);

        this.showAllReviewsButton = new JButton();
        this.showAllReviewsButton.setVisible(false);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        widgetContainer = new JPanel(new GridLayout(1, 1));
        componentPanel.add(widgetContainer, constraints);
        widgetContainer.add(this.showAllReviewsButton);

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
        this.searchMediumButton.addActionListener((e) -> this.searchMedium());

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
        this.searchMediumButton.setText(this.appView.getAppModel().getTranslation("button.load"));
        this.showAllReviewsButton.setText(this.appView.getAppModel().getTranslation("button.all_reviews"));

        this.imdbRatingViewPanel.setTranslations();
        this.metadataViewPanel.setTranslations();
        this.bestReviewViewPanel.setTranslations();
        this.worstReviewViewPanel.setTranslations();
    }

    public void searchMedium(){
        if(this.imdbRatings == null){
            this.reset();

            this.imdbRatings = this.appView.getAppModel().getTop100();
            if(this.imdbRatings.isEmpty()){
                this.appView.showNoMediumFoundDialog();
                return;
            }
        }

        if(!this.isAtEndOfResults()){
            this.medium = this.imdbRatings.get(this.currentListIndex).getMedium();
            this.currentListIndex++;
        }
        else{
            this.reset();
            return;
        }

        this.imdbRatingViewPanel.fillDataView(this.imdbRatings.get(this.currentListIndex - 1));
        this.metadataViewPanel.fillDataView(this.medium);

        AudienceReview[] reviews = this.appView.getAppModel().getBestAndWorstReviewByTitle(this.medium.getTitle());
        this.showAllReviewsButton.setVisible(true);
        this.showAllReviewsButton.setEnabled(reviews[0] != null && reviews[1] != null);
        this.worstReviewViewPanel.fillDataView(reviews[0]);
        this.bestReviewViewPanel.fillDataView(reviews[1]);

        this.appView.showAcceptRecommendationDialog((e) -> this.appView.showCloseAppDialog(), (e) -> this.searchMedium(),
                this.currentListIndex, this.imdbRatings.size());
    }

    private void reset(){
        this.imdbRatings = null;
        this.currentListIndex = 0;

        this.imdbRatingViewPanel.setVisible(false);
        this.showAllReviewsButton.setVisible(false);
        this.metadataViewPanel.setVisible(false);
        this.bestReviewViewPanel.setVisible(false);
        this.worstReviewViewPanel.setVisible(false);
    }

    private boolean isAtEndOfResults(){
        return this.currentListIndex == this.imdbRatings.size();
    }
}
