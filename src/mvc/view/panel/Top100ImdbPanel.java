package mvc.view.panel;

import mvc.AppView;
import mvc.view.widget.AllReviewsDialog;
import mvc.view.widget.ImdbRatingViewPanel;
import mvc.view.widget.MetadataViewPanel;
import mvc.view.widget.ReviewViewPanel;
import util.data.AudienceReview;
import util.data.ImdbRating;
import util.data.Medium;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class Top100ImdbPanel extends JPanel implements IViewPanel {
    private final AppView appView; //AppView reference

    private JButton searchMediumButton, showAllReviewsButton;
    private ImdbRatingViewPanel imdbRatingViewPanel; //Panel for showing the imdb data
    private MetadataViewPanel metadataViewPanel; //Shows the metadata of the current medium
    private ReviewViewPanel bestReviewViewPanel, worstReviewViewPanel; //Shows the worst and best audience review
    private Medium currentMedium; //Represents the current visible medium
    private ArrayList<ImdbRating> imdbRatings; //List of all top 100 imdb ratings
    private int currentIndex = 0; //Current index of rating list

    //Constructor
    public Top100ImdbPanel(AppView appView){
        this.appView = appView;
    }

    //Initialize all class components inside this panel
    @Override
    public void init(){
        this.initComponents();
        this.initStyles();
        this.initListeners();
    }

    @Override
    public void initComponents() {
        //Set layout of this panel
        this.setLayout(new BorderLayout());

        //Create component panel as a component inside of this panel for holding the following components
        JPanel componentPanel = new JPanel(new GridBagLayout());
        componentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); //Set margin
        this.add(componentPanel, BorderLayout.CENTER); //Add component panel to the center of the view

        //Describe the cell behavior in GridLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.anchor = GridBagConstraints.NORTH; //Alignment of components
        constraints.fill = GridBagConstraints.HORIZONTAL; //Stretch content horizontal

        //Container for holding the imdb data panel, load mediums button and show all reviews button
        JPanel widgetContainer = new JPanel();
        widgetContainer.setLayout(new BoxLayout(widgetContainer, BoxLayout.Y_AXIS)); //Set alignment for radio buttons to vertical
        constraints.gridx = 0; //Grid x index
        constraints.gridy = 0; //Grid y index
        componentPanel.add(widgetContainer, constraints);

        //Add search medium button
        this.searchMediumButton = new JButton();
        this.searchMediumButton.setHorizontalAlignment(JButton.LEFT); //Positioning button to the left, the following line is needed to actually center it
        this.searchMediumButton.setAlignmentX(LEFT_ALIGNMENT); //Positioning button to the left
        widgetContainer.add(this.searchMediumButton);

        widgetContainer.add(Box.createRigidArea(new Dimension(0, 10))); //Add space between radio buttons and input text field

        //Initialize imdb rating panel and add it
        this.imdbRatingViewPanel = new ImdbRatingViewPanel(this.appView);
        this.imdbRatingViewPanel.init();
        this.imdbRatingViewPanel.setAlignmentX(LEFT_ALIGNMENT); //Positioning button to the left
        widgetContainer.add(this.imdbRatingViewPanel);

        //Initialize metadata view panel
        this.metadataViewPanel = new MetadataViewPanel(this.appView);
        this.metadataViewPanel.init();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2; //Set cell width
        constraints.fill = GridBagConstraints.BOTH;
        componentPanel.add(this.metadataViewPanel, constraints);

        //Overwrite widget container to hold the show all reviews button
        widgetContainer = new JPanel();
        widgetContainer.setLayout(new BoxLayout(widgetContainer, BoxLayout.Y_AXIS)); //Set alignment for radio buttons to vertical
        constraints.gridx = 0; //Grid x index
        constraints.gridy = 2; //Grid y index
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        componentPanel.add(widgetContainer, constraints);

        //Initialize show all reviews button
        this.showAllReviewsButton = new JButton();
        this.showAllReviewsButton.setVisible(false); //Set visible when review is found
        widgetContainer.add(this.showAllReviewsButton);

        //Initialize best review panel
        this.bestReviewViewPanel = new ReviewViewPanel(this.appView);
        this.bestReviewViewPanel.init();
        constraints.gridx = 1;
        constraints.gridy = 2;
        componentPanel.add(this.bestReviewViewPanel, constraints);

        //Initialize worst review button
        this.worstReviewViewPanel = new ReviewViewPanel(this.appView);
        this.worstReviewViewPanel.init();
        constraints.gridx = 2;
        constraints.gridy = 2;
        componentPanel.add(this.worstReviewViewPanel, constraints);
    }

    @Override
    public void initStyles() {
        //Set margin of the review view panels
        this.bestReviewViewPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.worstReviewViewPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
    }

    @Override
    public void initListeners(){
        //Init action listener for load medium button to search for a random medium
        this.searchMediumButton.addActionListener((e) -> this.searchMedium());

        //Init action listener for show all reviews button
        this.showAllReviewsButton.addActionListener((e) -> {
            UIManager.put("OptionPane.minimumSize", new Dimension(600,400)); //Set size of following JDialog

            //Open message JDialog to show all reviews in a new window
            JOptionPane.showMessageDialog(null, new AllReviewsDialog(this.appView, this.currentMedium.getTitle()),
                    this.appView.getAppModel().getTranslation("dialog.title.all_reviews") + " " + this.currentMedium.getTitle(), JOptionPane.PLAIN_MESSAGE);

            UIManager.put("OptionPane.minimumSize", null); //Reset size of following JDialog
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
        //If current search list is null or the last search does not match the new search then reset and do a new search
        if(this.imdbRatings == null){
            this.reset();

            this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Set cursor to loading cursor
            this.imdbRatings = this.appView.getAppModel().getTop100Imdb(); //Get top 100 imdb by AppModel

            //If search has no mediums found then open a new no medium found JDialog
            if(this.imdbRatings.isEmpty()){
                this.appView.showNoMediumFoundDialog();
                return;
            }
        }

        //If the current medium is not at the end set the current medium to the next one in the search results and increase the index. Otherwise reset the search
        if(!this.isAtEndOfResults()){
            this.currentMedium = this.imdbRatings.get(this.currentIndex).getMedium();
            this.currentIndex++;
        }
        else{
            this.reset();
            return;
        }

        this.imdbRatingViewPanel.fillDataView(this.imdbRatings.get(this.currentIndex - 1)); //Fill imdb rating view with the current medium data
        this.metadataViewPanel.fillDataView(this.currentMedium); //Fill metadata view with the current medium data

        //Load reviews
        AudienceReview[] reviews = this.appView.getAppModel().getBestAndWorstReviewByTitle(this.currentMedium.getTitle());
        this.showAllReviewsButton.setVisible(true);
        this.showAllReviewsButton.setEnabled(reviews[0] != null && reviews[1] != null); //Enable show all reviews button when best and worst review exists
        this.worstReviewViewPanel.fillDataView(reviews[0]); //Fill worst review view panel
        this.bestReviewViewPanel.fillDataView(reviews[1]); //Fill best review view panel

        this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); //Set cursor to default cursor

        //Show the user the accept medium dialog where the user can decide to choose, skip or abort the current medium
        this.appView.showAcceptRecommendationDialog((e) -> this.appView.showCloseAppDialog(), (e) -> this.searchMedium(),
                this.currentIndex, this.imdbRatings.size());
    }

    private void reset(){
        //Clear top 100 and reset current index
        this.imdbRatings = null;
        this.currentIndex = 0;

        //Hide all panels
        this.imdbRatingViewPanel.setVisible(false);
        this.showAllReviewsButton.setVisible(false);
        this.metadataViewPanel.setVisible(false);
        this.bestReviewViewPanel.setVisible(false);
        this.worstReviewViewPanel.setVisible(false);

        this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); //Set cursor to default cursor
    }

    private boolean isAtEndOfResults(){
        return this.currentIndex == this.imdbRatings.size();
    }
}
