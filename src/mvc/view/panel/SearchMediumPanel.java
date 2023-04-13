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
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class SearchMediumPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JPanel radioButtonContainer;
    private JRadioButton titleRadioButton, genreRadioButton, castRadioButton;
    private JTextField inputTextField;
    private JButton searchMediumButton, showAllReviewsButton;
    private MetadataViewPanel metadataViewPanel;
    private ReviewViewPanel bestReviewViewPanel, worstReviewViewPanel;
    private Medium medium;
    private ArrayList<Medium> currentSearchResults;
    private int currentSearchIndex = 0;
    private String lastSearch;

    public SearchMediumPanel(AppView appView){
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

        this.radioButtonContainer = new JPanel(new GridLayout(3, 1));
        widgetContainer.add(this.radioButtonContainer);

        this.titleRadioButton = new JRadioButton();
        this.titleRadioButton.setSelected(true);
        this.radioButtonContainer.add(this.titleRadioButton);

        this.genreRadioButton = new JRadioButton();
        this.radioButtonContainer.add(this.genreRadioButton);

        this.castRadioButton = new JRadioButton();
        this.radioButtonContainer.add(this.castRadioButton);

        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(this.titleRadioButton);
        radioButtonGroup.add(this.genreRadioButton);
        radioButtonGroup.add(this.castRadioButton);

        widgetContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        this.inputTextField = new JFormattedTextField();
        widgetContainer.add(this.inputTextField);

        widgetContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        this.searchMediumButton = new JButton();
        this.searchMediumButton.setEnabled(false);
        searchMediumButton.setHorizontalAlignment(JButton.CENTER);
        searchMediumButton.setAlignmentX(CENTER_ALIGNMENT);
        widgetContainer.add(this.searchMediumButton);

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
    public void initImages() {}

    @Override
    public void initActionListeners(){
        this.inputTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                this.changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                this.changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchMediumButton.setEnabled(!inputTextField.getText().isEmpty());
            }
        });

        this.searchMediumButton.addActionListener((e) -> this.searchMedium());

        this.showAllReviewsButton.addActionListener((e) -> {
            UIManager.put("OptionPane.minimumSize", new Dimension(600,400));
            JOptionPane.showMessageDialog(null, new AllReviewsDialog(this.appView, this.medium.getTitle()),
                    this.appView.getAppModel().getTranslation("dialog.title.all_reviews") + " " + this.medium.getTitle(), JOptionPane.PLAIN_MESSAGE);
            UIManager.put("OptionPane.minimumSize", null);
        });
    }

    @Override
    public void setTranslations(){
        this.radioButtonContainer.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.search.search_by"))));

        this.titleRadioButton.setText(this.appView.getAppModel().getTranslation("radio.title"));
        this.genreRadioButton.setText(this.appView.getAppModel().getTranslation("radio.genre"));
        this.castRadioButton.setText(this.appView.getAppModel().getTranslation("radio.cast"));
        this.searchMediumButton.setText(this.appView.getAppModel().getTranslation("button.search"));
        this.showAllReviewsButton.setText(this.appView.getAppModel().getTranslation("button.all_reviews"));

        this.metadataViewPanel.setTranslations();
        this.bestReviewViewPanel.setTranslations();
        this.worstReviewViewPanel.setTranslations();
    }

    public void focusInputTextField(){
        this.inputTextField.requestFocusInWindow();
    }

    public void searchMedium(){
        if(this.currentSearchResults == null || !this.lastSearch.equalsIgnoreCase(this.inputTextField.getText())){
            this.reset();

            if(!this.getMediums()){
                this.appView.showNoMediumFoundDialog();
                return;
            }
        }

        if(!this.isAtEndOfResults()){
            this.medium = this.currentSearchResults.get(this.currentSearchIndex);
            this.currentSearchIndex++;
        }
        else{
            this.reset();
            return;
        }

        this.metadataViewPanel.fillDataView(this.medium);

        AudienceReview[] reviews = this.appView.getAppModel().getBestAndWorstReviewByTitle(this.medium.getTitle());
        this.showAllReviewsButton.setVisible(true);
        this.showAllReviewsButton.setEnabled(reviews[0] != null && reviews[1] != null);
        this.worstReviewViewPanel.fillDataView(reviews[0]);
        this.bestReviewViewPanel.fillDataView(reviews[1]);

        this.appView.showAcceptRecommendationDialog((e) -> this.appView.showCloseAppDialog(), (e) -> this.searchMedium(),
                this.currentSearchIndex, this.currentSearchResults.size());
    }

    private boolean getMediums(){
        AppModel appModel = this.appView.getAppController().getAppModel();
        this.lastSearch = this.inputTextField.getText();

        if(this.titleRadioButton.isSelected()){
            this.currentSearchResults = appModel.getMediumsByTitle(this.inputTextField.getText());
        }
        else if(this.genreRadioButton.isSelected()){
            this.currentSearchResults = appModel.getMediumsByGenres(this.inputTextField.getText().split(","));
        }
        else{
            this.currentSearchResults = appModel.getMediumsByCast(this.inputTextField.getText().split(","));
        }

        return !this.currentSearchResults.isEmpty();
    }

    private void reset(){
        this.currentSearchResults = null;
        this.currentSearchIndex = 0;

        this.showAllReviewsButton.setVisible(false);
        this.metadataViewPanel.setVisible(false);
        this.bestReviewViewPanel.setVisible(false);
        this.worstReviewViewPanel.setVisible(false);
    }

    private boolean isAtEndOfResults(){
        return this.currentSearchIndex == this.currentSearchResults.size();
    }
}
