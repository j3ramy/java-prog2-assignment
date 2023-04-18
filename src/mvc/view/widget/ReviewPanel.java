package mvc.view.widget;

import mvc.AppModel;
import mvc.AppView;
import util.CustomColors;
import util.data.AudienceReview;
import util.data.CriticReview;
import util.data.Review;
import util.interfaces.IViewInit;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReviewPanel extends JPanel implements IViewInit {
    private final AppView appView; //AppView reference

    private JLabel headingLabel, mediumTitleLabel, ratingLabel; //Labels for displaying the content
    private JTextArea commentTextArea; //Text area for longer review comments
    private JScrollPane commentScrollPane; //Scroll pane to make scrolling in upper text area possible


    public ReviewPanel(AppView appView){
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
    }

    /**
     * Initializes panel components
     *
     * @BigO: O(n)
     * **/
    @Override
    public void initComponents() {
        //Set layout of this panel
        this.setLayout(new GridBagLayout());

        //Describe the cell behavior in GridLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.anchor = GridBagConstraints.NORTH; //Position all components to the top
        constraints.fill = GridBagConstraints.HORIZONTAL; //Stretch content horizontal

        //Add labels
        this.headingLabel = new JLabel("", SwingConstants.CENTER); //Initialize and center
        constraints.gridx = 0; //Grid x index
        constraints.gridy = 0; //Grid y index
        this.add(this.headingLabel, constraints);

        this.mediumTitleLabel = new JLabel("");
        constraints.gridy = 1;
        this.add(this.mediumTitleLabel, constraints);

        this.ratingLabel = new JLabel("");
        constraints.gridy = 2;
        this.add(this.ratingLabel, constraints);

        this.commentTextArea = new JTextArea(3, 1); //Width and height
        this.commentTextArea.setEditable(false); //Set editable to false so the user cannot edit it
        this.commentTextArea.setFocusable(false); //Set focusable to false so the user cannot focus it
        this.commentTextArea.setLineWrap(true); //Create line breaks
        this.commentTextArea.setWrapStyleWord(true); //Words that does not fit in the line anymore will be moved to the next line
        this.commentTextArea.setBackground(null); //Remove background
        constraints.gridy = 3;

        //Initialize scroll panes for the text areas where the horizontal scroll bar is hidden and the vertical scroll bar is only shown when needed
        this.commentScrollPane = new JScrollPane(this.commentTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.commentScrollPane.setMinimumSize(new Dimension(0, 100));
        this.commentScrollPane.setMaximumSize(new Dimension(0, 150));
        this.add(this.commentScrollPane, constraints);
    }

    /**
     * Initializes panel styles
     *
     * @BigO: O(n)
     * **/
    @Override
    public void initStyles() {
        this.setVisible(false); //Hide by default

        //Create titled border for title label and add margin
        this.headingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 20, 0), BorderFactory.createLineBorder(CustomColors.LIGHT_BLUE)));

        //Set labels font to Java default font
        this.headingLabel.setFont(new Font(null, Font.BOLD, 14));
        this.mediumTitleLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.ratingLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.commentTextArea.setFont(new Font(null, Font.PLAIN, 12));

        //Set vertical alignment to top to display it in the upper-left
        this.mediumTitleLabel.setVerticalTextPosition(JLabel.TOP);
        this.mediumTitleLabel.setVerticalAlignment(JLabel.TOP);
        this.ratingLabel.setVerticalTextPosition(JLabel.TOP);
        this.ratingLabel.setVerticalAlignment(JLabel.TOP);
    }

    @Override
    public void initListeners(){}

    /**
     * Sets translation of panel components
     *
     * @BigO: O(n)
     * **/
    @Override
    public void setTranslations(){
        //Create titled border around all labels and set the text to the correct translation and add margin
        this.mediumTitleLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.reviewdata.medium_title"))));

        this.commentScrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.reviewdata.comment"))));
    }

    /**
     * Fills the DataViewPanel
     *
     * @param review object of type Review that contains the information which will be loaded into the GUI
     *
     * @BigO: O(n)
     * **/
    public void fillDataView(Review review){
        AppModel appModel = this.appView.getAppController().getAppModel(); //Get app model as cached variable

        //If review is null then inform the user that there is no review and clear this panel.
        if(review == null){
            this.headingLabel.setText(appModel.getTranslation("label.reviewdata.no_comment"));
            this.clear();
        }
        else{
            //Check if review is a critics review or an audience review
            if(review instanceof CriticReview){
                //Set heading
                this.headingLabel.setText(appModel.getTranslation("label.reviewdata.critics"));

                //Add titled border to the rating label and add margin
                this.ratingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                        BorderFactory.createTitledBorder(appModel.getTranslation("label.reviewdata.sentiment"))));

                //Check for the sentiment and display yes or no
                String sentiment = ((CriticReview) review).getSentiment() ? appModel.getTranslation("label.yes") : appModel.getTranslation("label.no");
                this.ratingLabel.setText(sentiment);
            }
            else if(review instanceof AudienceReview){
                //Set heading containing the information if it is the best or worst review by the review classification
                this.headingLabel.setText(appModel.getTranslation("label.reviewdata.audience"));
                switch (((AudienceReview) review).getAudienceReviewType()){
                    case BEST -> this.headingLabel.setText(this.headingLabel.getText() + " (" + appModel.getTranslation("label.reviewdata.best") + ")");
                    case WORST -> this.headingLabel.setText(this.headingLabel.getText() + " (" + appModel.getTranslation("label.reviewdata.worst") + ")");
                }

                //Add titled border to the rating label and add margin
                this.ratingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                        BorderFactory.createTitledBorder(appModel.getTranslation("label.reviewdata.rating"))));

                this.ratingLabel.setText(Float.toString(((AudienceReview) review).getRating()));
            }

            //Set title and comment
            this.mediumTitleLabel.setText(review.getMediumTitle());
            this.commentTextArea.setText(review.getComment());

            //Move the scroll pane scroll bar to top
            this.commentTextArea.setCaretPosition(0);
        }

        //Show this panel
        this.setVisible(true);
    }

    /**
     * Clears this panel
     *
     * @BigO: O(n)
     * **/
    private void clear(){
        //Clear all content
        this.mediumTitleLabel.setText("");
        this.ratingLabel.setText("");
        this.commentTextArea.setText("");
    }
}
