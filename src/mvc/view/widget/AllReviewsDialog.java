package mvc.view.widget;

import mvc.AppModel;
import mvc.AppView;
import util.Colors;
import util.data.Review;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class AllReviewsDialog extends JPanel implements IViewPanel {
    private final AppView appView;
    private final ArrayList<Review> reviews;

    private ReviewViewPanel reviewViewPanel;
    private JLabel indexFeedbackLabel;
    private JButton previousButton, nextButton;
    private int currentIndex = 0;

    public AllReviewsDialog(AppView appView, String title){
        this.appView = appView;
        this.reviews = this.appView.getAppController().getAppModel().getAllReviewsByTitle(title);

        this.init();
        this.setTranslations();

        this.reviewViewPanel.fillDataView(this.reviews.get(this.currentIndex));
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
        componentPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
        componentPanel.setBackground(Colors.BORDER);
        this.add(componentPanel, BorderLayout.CENTER);

        //Describe the cell behavior in GridLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        this.reviewViewPanel = new ReviewViewPanel(this.appView);
        this.reviewViewPanel.init();
        this.reviewViewPanel.setPreferredSize(new Dimension(0, 250));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        componentPanel.add(this.reviewViewPanel, constraints);

        this.indexFeedbackLabel = new JLabel("", JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 1;
        componentPanel.add(this.indexFeedbackLabel, constraints);

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

    @Override
    public void initStyles() {
        this.indexFeedbackLabel.setBorder(new EmptyBorder(5, 0 ,5, 0));
    }

    @Override
    public void initImages() {

    }

    @Override
    public void initActionListeners(){
        this.previousButton.addActionListener((e) -> {
            if(this.currentIndex > 0){
                this.currentIndex--;
                this.reviewViewPanel.fillDataView(this.reviews.get(this.currentIndex));
                this.setTranslations();
            }
        });

        this.nextButton.addActionListener((e) -> {
            if(this.currentIndex < this.reviews.size() - 1){
                this.currentIndex++;
                this.reviewViewPanel.fillDataView(this.reviews.get(this.currentIndex));
                this.setTranslations();
            }
        });
    }

    @Override
    public void setTranslations() {
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.indexFeedbackLabel.setText((this.currentIndex + 1) + " " + appModel.getTranslation("label.outof") + " " + this.reviews.size());
        this.previousButton.setText(appModel.getTranslation("button.previous"));
        this.nextButton.setText(appModel.getTranslation("button.next"));

        this.reviewViewPanel.setTranslations();
    }
}
