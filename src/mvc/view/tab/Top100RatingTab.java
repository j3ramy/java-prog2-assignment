package mvc.view.tab;

import mvc.AppView;
import mvc.view.widget.AllReviewsDialog;
import mvc.view.widget.MetadataPanel;
import mvc.view.widget.ReviewPanel;
import util.data.AudienceReview;
import util.data.Medium;
import util.interfaces.IViewInit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class Top100RatingTab extends JPanel implements IViewInit {
    private final AppView appView; //AppView reference

    private JButton getMediumsButton, allReviewsButton;
    private MetadataPanel metadataPanel; //Shows the metadata of the current medium
    private ReviewPanel bestReviewPanel, worstReviewPanel; //Shows the worst and best audience review
    private Medium currentMedium; //Represents the current visible medium
    private ArrayList<Medium> mediums; //List of current search
    private int currentIndex; //Current index of rating list

    //Constructor
    public Top100RatingTab(AppView appView){
        this.appView = appView;
    }

    /**
     * Initializes this panel
     *
     * @BigO: O(n)
     * **/
    @Override
    public void init(){
        this.initComponents();
        this.initStyles();
        this.initListeners();
    }

    /**
     * Initializes panel components
     *
     * @BigO: O(n)
     * **/
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
        this.getMediumsButton = new JButton();
        this.getMediumsButton.setHorizontalAlignment(JButton.LEFT); //Positioning button to the left, the following line is needed to actually center it
        this.getMediumsButton.setAlignmentX(LEFT_ALIGNMENT); //Positioning button to the left
        widgetContainer.add(this.getMediumsButton);

        //Initialize metadata view panel
        this.metadataPanel = new MetadataPanel(this.appView);
        this.metadataPanel.init();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        componentPanel.add(this.metadataPanel, constraints);

        //Overwrite widget container to hold the show all reviews button
        widgetContainer = new JPanel();
        widgetContainer.setLayout(new BoxLayout(widgetContainer, BoxLayout.Y_AXIS)); //Set alignment for radio buttons to vertical
        constraints.gridx = 0; //Grid x index
        constraints.gridy = 2; //Grid y index
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        componentPanel.add(widgetContainer, constraints);

        //Initialize show all reviews button
        this.allReviewsButton = new JButton();
        this.allReviewsButton.setVisible(false); //Set visible when review is found
        widgetContainer.add(this.allReviewsButton);

        //Initialize best review panel
        this.bestReviewPanel = new ReviewPanel(this.appView);
        this.bestReviewPanel.init();
        constraints.gridx = 1;
        constraints.gridy = 2;
        componentPanel.add(this.bestReviewPanel, constraints);

        //Initialize worst review button
        this.worstReviewPanel = new ReviewPanel(this.appView);
        this.worstReviewPanel.init();
        constraints.gridx = 2;
        constraints.gridy = 2;
        componentPanel.add(this.worstReviewPanel, constraints);
    }

    /**
     * Initializes panel styles
     *
     * @BigO: O(n)
     * **/
    @Override
    public void initStyles() {
        //Set margin of the review view panels
        this.bestReviewPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.worstReviewPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
    }

    /**
     * Initializes panel event listeners
     *
     * @BigO: O(n)
     * **/
    @Override
    public void initListeners(){
        //Init action listener for load medium button to search for a random medium
        this.getMediumsButton.addActionListener((e) -> this.searchMedium());

        //Init action listener for show all reviews button
        this.allReviewsButton.addActionListener((e) -> {
            UIManager.put("OptionPane.minimumSize", new Dimension(600,400)); //Set size of following JDialog

            //Open message JDialog to show all reviews in a new window
            JOptionPane.showMessageDialog(null, new AllReviewsDialog(this.appView, this.currentMedium.getTitle()),
                    this.appView.getAppModel().getTranslation("dialog.title.all_reviews") + " " + this.currentMedium.getTitle(), JOptionPane.PLAIN_MESSAGE);

            UIManager.put("OptionPane.minimumSize", null); //Reset size of following JDialog
        });
    }

    /**
     * Sets translation of panel components
     *
     * @BigO: O(n)
     * **/
    @Override
    public void setTranslations(){
        this.getMediumsButton.setText(this.appView.getAppModel().getTranslation("button.load"));
        this.allReviewsButton.setText(this.appView.getAppModel().getTranslation("button.all_reviews"));

        this.metadataPanel.setTranslations();
        this.bestReviewPanel.setTranslations();
        this.worstReviewPanel.setTranslations();
    }

    /**
     * Loads the Top 100 mediums by rating and fills the panel components
     *
     * @BigO: O(n)
     * **/
    public void searchMedium(){
        //If current search list is null or the last search does not match the new search then reset and do a new search
        if(this.mediums == null){
            this.reset();

            this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Set cursor to loading cursor
            this.mediums = this.appView.getAppModel().getTop100ByReviews(); //Get top 100 mediums by rating by AppModel

            //If search has no mediums found then open a new no medium found JDialog
            if(this.mediums.isEmpty()){
                this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); //Set cursor to default cursor
                this.appView.getDialogHandler().showNoMediumFoundDialog();
                return;
            }
        }

        //If the current medium is not at the end set the current medium to the next one in the search results and increase the index. Otherwise reset the search
        if(!this.isAtEndOfResults()){
            this.currentMedium = this.mediums.get(this.currentIndex);
            this.currentIndex++;
        }
        else{
            this.reset();
            return;
        }

        this.metadataPanel.fillDataView(this.currentMedium); //Fill metadata view with the current medium data

        //Load reviews
        AudienceReview[] reviews = this.appView.getAppModel().getBestAndWorstReviewByTitle(this.currentMedium.getTitle());
        this.allReviewsButton.setVisible(true);
        this.allReviewsButton.setEnabled(reviews[0] != null && reviews[1] != null); //Enable show all reviews button when best and worst review exists
        this.worstReviewPanel.fillDataView(reviews[0]); //Fill worst review view panel
        this.bestReviewPanel.fillDataView(reviews[1]); //Fill best review view panel

        this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); //Set cursor to default cursor

        //Show the user the accept medium dialog where the user can decide to choose, skip or abort the current medium
        this.appView.getDialogHandler().showAcceptRecommendationDialog((e) -> this.appView.getDialogHandler().showCloseAppDialog(), (e) -> this.searchMedium(),
                this.currentIndex, this.mediums.size(),
                " " + this.appView.getAppModel().getTranslation("dialog.title.average_rating").replace("#", Float.toString(this.currentMedium.getAverageRating())));
    }

    /**
     * Resets this panel
     *
     * @BigO: O(n)
     * **/
    private void reset(){
        //Clear top 100 and reset current index
        this.mediums = null;
        this.currentIndex = 0;

        //Hide all panels
        this.allReviewsButton.setVisible(false);
        this.metadataPanel.setVisible(false);
        this.bestReviewPanel.setVisible(false);
        this.worstReviewPanel.setVisible(false);

        this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); //Set cursor to default cursor
    }

    /**
     * Checks if currentIndex is at the end of the list
     *
     * @return  true when at the end, otherwise false
     * @BigO: O(1)
     * **/
    private boolean isAtEndOfResults(){
        return this.currentIndex == this.mediums.size();
    }
}
