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

public class RandomMediumTab extends JPanel implements IViewInit {
    private final AppView appView; //AppView reference

    private JButton getMediumButton, allReviewsButton;
    private MetadataPanel metadataPanel; //Shows the metadata of the current medium
    private ReviewPanel bestReviewPanel, worstReviewPanel; //Shows the worst and best audience review
    private Medium currentMedium; //Represents the current visible medium

    //Constructor
    public RandomMediumTab(AppView appView){
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
        this.getMediumButton = new JButton();
        this.getMediumButton.setHorizontalAlignment(JButton.LEFT); //Positioning button to the left, the following line is needed to actually center it
        this.getMediumButton.setAlignmentX(LEFT_ALIGNMENT); //Positioning button to the left
        widgetContainer.add(this.getMediumButton);

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
        this.getMediumButton.addActionListener((e) -> this.searchMedium());

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
        this.getMediumButton.setText(this.appView.getAppModel().getTranslation("button.load"));
        this.allReviewsButton.setText(this.appView.getAppModel().getTranslation("button.all_reviews"));

        this.metadataPanel.setTranslations();
        this.bestReviewPanel.setTranslations();
        this.worstReviewPanel.setTranslations();
    }

    /**
     * Loads any random medium and fills the panel components
     *
     * @BigO: O(n)
     * **/
    public void searchMedium(){
        this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Set cursor to loading cursor

        //Load random medium from AppModel
        this.currentMedium = this.appView.getAppModel().getRandomMedium();

        this.metadataPanel.fillDataView(this.currentMedium); //Fill metadata view with the current medium data

        //Load reviews
        AudienceReview[] reviews = this.appView.getAppModel().getBestAndWorstReviewByTitle(this.currentMedium.getTitle());
        this.allReviewsButton.setVisible(true);
        this.allReviewsButton.setEnabled(reviews[0] != null && reviews[1] != null); //Enable show all reviews button when best and worst review exists
        this.worstReviewPanel.fillDataView(reviews[0]); //Fill worst review view panel
        this.bestReviewPanel.fillDataView(reviews[1]); //Fill best review view panel

        this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); //Set cursor to default cursor

        //Show the user an accept medium dialog where the user can decide to choose, skip or abort the current medium
        this.appView.getDialogHandler().showAcceptRecommendationDialog((e) -> this.appView.getDialogHandler().showCloseAppDialog(), (e) -> this.searchMedium(), 1, 1);
    }
}
