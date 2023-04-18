package mvc.view.widget;

import mvc.AppModel;
import mvc.AppView;
import util.CustomColors;
import util.data.Review;
import util.interfaces.IViewInit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class AllReviewsDialog extends JPanel implements IViewInit {
    private final AppView appView; //AppView reference
    private final ArrayList<Review> reviews; //Current reviews of passed title

    private ReviewPanel reviewViewPanel; //Panel for showing the reviews
    private JLabel indexFeedbackLabel; //Label for showing the user on which review it is
    private JButton previousButton, nextButton; //Buttons to go through the review list
    private int currentIndex; //Current index of review list

    //Constructor
    public AllReviewsDialog(AppView appView, String title){
        this.appView = appView;
        this.reviews = this.appView.getAppController().getAppModel().getAllReviewsByTitle(title); //Get all reviews by title by AppModel

        this.init(); //Initialize components depending on the loaded reviews
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

        this.setTranslations();

        this.reviewViewPanel.fillDataView(this.reviews.get(this.currentIndex)); //Fill review panel
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
        componentPanel.setBorder(new EmptyBorder(20, 20, 0, 20)); //Set margin
        componentPanel.setBackground(CustomColors.LIGHT_BLUE); //Set background color
        this.add(componentPanel, BorderLayout.CENTER); //Add component panel to the center of the view

        //Describe the cell behavior in GridLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.anchor = GridBagConstraints.NORTH; //Alignment of components
        constraints.fill = GridBagConstraints.HORIZONTAL; //Stretch content horizontal

        //Initialize review panel
        this.reviewViewPanel = new ReviewPanel(this.appView);
        this.reviewViewPanel.init();
        this.reviewViewPanel.setPreferredSize(new Dimension(0, 250)); //Set preferred display size
        constraints.gridx = 0; //Grid x index
        constraints.gridy = 0; //Grid y index
        constraints.gridwidth = 2; //Set cell width
        componentPanel.add(this.reviewViewPanel, constraints);

        //Initialize feedback label adn center it
        this.indexFeedbackLabel = new JLabel("", JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 1;
        componentPanel.add(this.indexFeedbackLabel, constraints);

        //Initialize buttons
        this.previousButton = new JButton();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        componentPanel.add(this.previousButton, constraints);

        this.nextButton = new JButton();
        constraints.gridx = 1;
        constraints.gridy = 2;
        componentPanel.add(this.nextButton, constraints);
    }

    /**
     * Initializes panel styles
     *
     * @BigO: O(n)
     * **/
    @Override
    public void initStyles() {
        //Set margin
        this.indexFeedbackLabel.setBorder(new EmptyBorder(5, 0 ,5, 0));
    }

    /**
     * Initializes panel event listeners
     *
     * @BigO: O(n)
     * **/
    @Override
    public void initListeners(){
        //Initialize action listener to go back and forth through all the reviews
        this.previousButton.addActionListener((e) -> {
            //If current index is bigger than 0 then go back
            if(this.currentIndex > 0){
                this.currentIndex--; //Decrease current index
                this.reviewViewPanel.fillDataView(this.reviews.get(this.currentIndex)); //Load previous review
                this.setTranslations(); //Reload text
            }
        });

        this.nextButton.addActionListener((e) -> {
            //If current index is smaller than the list size then go forth
            if(this.currentIndex < this.reviews.size() - 1){
                this.currentIndex++; //Increase current index
                this.reviewViewPanel.fillDataView(this.reviews.get(this.currentIndex)); //Load next review
                this.setTranslations(); //Reload text
            }
        });
    }

    /**
     * Sets translation of panel components
     *
     * @BigO: O(n)
     * **/
    @Override
    public void setTranslations() {
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.indexFeedbackLabel.setText((this.currentIndex + 1) + " " + appModel.getTranslation("label.outof") + " " + this.reviews.size());
        this.previousButton.setText(appModel.getTranslation("button.previous"));
        this.nextButton.setText(appModel.getTranslation("button.next"));

        this.reviewViewPanel.setTranslations();
    }
}
