package mvc.view.tab;

import mvc.AppModel;
import mvc.AppView;
import mvc.view.widget.AllReviewsDialog;
import mvc.view.widget.MetadataPanel;
import mvc.view.widget.ReviewPanel;
import util.data.AudienceReview;
import util.data.Medium;
import util.interfaces.IViewInit;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class SearchMediumTab extends JPanel implements IViewInit {
    private final AppView appView; //AppView reference

    private JPanel radioButtonContainer; //Container for the radio buttons
    private JRadioButton titleRadioButton, genreRadioButton, castRadioButton; //Radio buttons for different search types
    private JTextField inputTextField; //Input text field for searching by type
    private JButton getMediumsButton, allReviewsButton;
    private MetadataPanel metadataPanel; //Shows the metadata of the current medium
    private ReviewPanel bestReviewPanel, worstReviewPanel; //Shows the worst and best audience review
    private Medium currentMedium; //Represents the current visible medium
    private ArrayList<Medium> mediums; //List of all mediums that were searched for
    private int currentIndex = 0; //Current index of medium list
    private String lastSearch; //Last search string for checking if it is the same search

    //Constructor
    public SearchMediumTab(AppView appView){
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

        //Container for holding the radio buttons container and later the show all reviews button
        JPanel widgetContainer = new JPanel();
        widgetContainer.setLayout(new BoxLayout(widgetContainer, BoxLayout.Y_AXIS)); //Set alignment for radio buttons to vertical
        constraints.gridx = 0; //Grid x index
        constraints.gridy = 0; //Grid y index
        componentPanel.add(widgetContainer, constraints);

        //Initialize the radio button container and add it to the widget container
        this.radioButtonContainer = new JPanel(new GridLayout(3, 1));
        widgetContainer.add(this.radioButtonContainer);

        //Initialize radio buttons
        this.titleRadioButton = new JRadioButton();
        this.titleRadioButton.setSelected(true); //Set the title search as default option
        this.radioButtonContainer.add(this.titleRadioButton);

        this.genreRadioButton = new JRadioButton();
        this.radioButtonContainer.add(this.genreRadioButton);

        this.castRadioButton = new JRadioButton();
        this.radioButtonContainer.add(this.castRadioButton);

        //Create radio button group and add radio buttons to make only one of them selectable at the same time
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(this.titleRadioButton);
        radioButtonGroup.add(this.genreRadioButton);
        radioButtonGroup.add(this.castRadioButton);

        widgetContainer.add(Box.createRigidArea(new Dimension(0, 10))); //Add space between radio buttons and input text field

        //Add input text field
        this.inputTextField = new JFormattedTextField();
        widgetContainer.add(this.inputTextField);

        widgetContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        //Add search medium button
        this.getMediumsButton = new JButton();
        this.getMediumsButton.setEnabled(false);
        getMediumsButton.setHorizontalAlignment(JButton.CENTER); // Center button, the following line is needed to actually center it
        getMediumsButton.setAlignmentX(CENTER_ALIGNMENT); // Center button
        widgetContainer.add(this.getMediumsButton);

        //Initialize metadata view panel
        this.metadataPanel = new MetadataPanel(this.appView);
        this.metadataPanel.init();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2; //Cell width
        constraints.fill = GridBagConstraints.BOTH; //Stretch content horizontal and vertical
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
        //Add radio button listener which will reset the current search when another radio button is selected
        this.titleRadioButton.addItemListener((e) -> this.reset());
        this.genreRadioButton.addItemListener((e) -> this.reset());
        this.castRadioButton.addItemListener((e) -> this.reset());

        //Add document listener or the input field that will check if it is empty. If so, then disable the search medium button. Otherwise enable it
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
                getMediumsButton.setEnabled(!inputTextField.getText().isEmpty());
            }
        });

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
        //Set text of radio button container title text
        this.radioButtonContainer.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.search.search_by"))));

        this.titleRadioButton.setText(this.appView.getAppModel().getTranslation("radio.title"));
        this.genreRadioButton.setText(this.appView.getAppModel().getTranslation("radio.genre"));
        this.castRadioButton.setText(this.appView.getAppModel().getTranslation("radio.cast"));
        this.getMediumsButton.setText(this.appView.getAppModel().getTranslation("button.search"));
        this.allReviewsButton.setText(this.appView.getAppModel().getTranslation("button.all_reviews"));

        this.metadataPanel.setTranslations();
        this.bestReviewPanel.setTranslations();
        this.worstReviewPanel.setTranslations();
    }

    /**
     * Focuses the input text field when tab get active
     *
     * @BigO: O(1)
     * **/
    public void focusInputTextField(){
        this.inputTextField.requestFocusInWindow(); //Focus input text field
    }

    /**
     * Loads any medium by title, genre or cast and fills the panel components
     *
     * @BigO: O(n)
     * **/
    public void searchMedium(){
        //If current searched mediums list is null or the last search does not match the new search then reset and do a new search
        if(this.mediums == null || !this.lastSearch.equalsIgnoreCase(this.inputTextField.getText())){
            this.reset();

            this.lastSearch = this.inputTextField.getText(); //Set the last search string to the current search string from the input field

            //If search has no mediums found then open a new no medium found JDialog
            if(!this.getMediums()){
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
                this.currentIndex, this.mediums.size());
    }

    /**
     * Loads any medium by title, genres and cast from AppModel
     *
     * @return  true when mediums are found, otherwise false
     * @BigO: O(n)
     * **/
    private boolean getMediums(){
        this.appView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Set cursor to loading cursor

        AppModel appModel = this.appView.getAppController().getAppModel();

        //Check which radio button is selected and load the mediums depending on it
        //Search for mediums by title
        if(this.titleRadioButton.isSelected()){
            this.mediums = appModel.getMediumsByTitle(this.inputTextField.getText());
        }
        //Search for mediums by genre/s
        else if(this.genreRadioButton.isSelected()){
            this.mediums = appModel.getMediumsByGenres(this.inputTextField.getText().split(",")); //Split multiple genres
        }
        //Search for mediums by actor/s
        else{
            this.mediums = appModel.getMediumsByCast(this.inputTextField.getText().split(",")); //Split multiple actors
        }

        //Return if mediums are found
        return !this.mediums.isEmpty();
    }

    /**
     * Resets this panel
     *
     * @BigO: O(n)
     * **/
    private void reset(){
        //Clear search and reset current index
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
