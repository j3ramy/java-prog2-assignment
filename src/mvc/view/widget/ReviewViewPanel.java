package mvc.view.widget;

import mvc.AppModel;
import mvc.AppView;
import util.Colors;
import util.data.AudienceReview;
import util.data.CriticReview;
import util.data.Review;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReviewViewPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JLabel headingLabel, mediumTitleLabel, ratingLabel;
    private JScrollPane commentScrollPane;
    private JTextArea commentTextArea;

    public ReviewViewPanel(AppView appView){
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
        this.setLayout(new GridBagLayout());

        //Describe the cell behavior in GridLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        this.headingLabel = new JLabel("", SwingConstants.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(this.headingLabel, constraints);

        this.mediumTitleLabel = new JLabel("");
        constraints.gridy = 1;
        this.add(this.mediumTitleLabel, constraints);

        this.ratingLabel = new JLabel("");
        constraints.gridy = 2;
        this.add(this.ratingLabel, constraints);

        this.commentTextArea = new JTextArea();
        this.commentTextArea.setEditable(false);
        this.commentTextArea.setFocusable(false);
        this.commentTextArea.setLineWrap(true);
        this.commentTextArea.setWrapStyleWord(true);
        this.commentTextArea.setBackground(null);
        constraints.gridy = 3;

        this.commentScrollPane = new JScrollPane(this.commentTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.commentScrollPane.setMinimumSize(new Dimension(0, 100));
        this.commentScrollPane.setMaximumSize(new Dimension(0, 150));
        this.add(this.commentScrollPane, constraints);
    }

    @Override
    public void initStyles() {
        this.setVisible(false);

        this.headingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 20, 0), BorderFactory.createLineBorder(Colors.BORDER)));

        this.headingLabel.setFont(new Font(null, Font.BOLD, 14));
        this.mediumTitleLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.ratingLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.commentTextArea.setFont(new Font(null, Font.PLAIN, 12));
    }

    @Override
    public void initImages() {}

    @Override
    public void initActionListeners(){}

    @Override
    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.mediumTitleLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.reviewdata.medium_title"))));
        this.commentScrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.reviewdata.comment"))));
    }

    public void fillDataView(Review review){
        AppModel appModel = this.appView.getAppController().getAppModel();

        if(review == null){
            this.headingLabel.setText(appModel.getTranslation("label.reviewdata.no_comment"));
            this.clear();
        }
        else{
            if(review instanceof CriticReview){
                this.headingLabel.setText(appModel.getTranslation("label.reviewdata.critics"));

                this.ratingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                        BorderFactory.createTitledBorder(appModel.getTranslation("label.reviewdata.sentiment"))));

                String sentiment = ((CriticReview) review).getSentiment() ? appModel.getTranslation("label.yes") : appModel.getTranslation("label.no");
                this.ratingLabel.setText(sentiment);
            }
            else if(review instanceof AudienceReview){
                this.headingLabel.setText(appModel.getTranslation("label.reviewdata.audience"));
                switch (((AudienceReview) review).getClassification()){
                    case BEST -> this.headingLabel.setText(this.headingLabel.getText() + " (" + appModel.getTranslation("label.reviewdata.best") + ")");
                    case WORST -> this.headingLabel.setText(this.headingLabel.getText() + " (" + appModel.getTranslation("label.reviewdata.worst") + ")");
                }

                this.ratingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                        BorderFactory.createTitledBorder(appModel.getTranslation("label.reviewdata.rating"))));

                this.ratingLabel.setText(Float.toString(((AudienceReview) review).getRating()));
            }

            this.mediumTitleLabel.setText(review.getMediumTitle());
            this.commentTextArea.setText(review.getComment());

            this.commentTextArea.setCaretPosition(0);
        }

        this.setVisible(true);
    }

    private void clear(){
        this.mediumTitleLabel.setText("");
        this.ratingLabel.setText("");
        this.commentTextArea.setText("");
    }
}
