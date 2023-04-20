package mvc.view.widget;

import mvc.AppView;
import util.CustomColors;
import util.Utils;
import util.data.Medium;
import util.interfaces.IViewInit;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MetadataPanel extends JPanel implements IViewInit {
    private final AppView appView; //AppView reference

    //Labels for displaying the content
    private JLabel titleLabel, typeLabel, providerLabel, durationLabel, seasonLabel, ageRatingLabel, addedAtLabel;
    private JTextArea genreTextArea, descriptionTextArea, castTextArea, countryTextArea; //Text areas for longer texts
    private JScrollPane descriptionScrollPane, castScrollPane; //Scroll panes to make scrolling in upper text areas possible

    //Constructor
    public MetadataPanel(AppView appView){
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
        constraints.fill = GridBagConstraints.BOTH; //Stretch content horizontal and vertical

        //Initialize labels,  text areas and scroll panes
        this.titleLabel = new JLabel("", SwingConstants.CENTER); //Center
        constraints.gridx = 0; //Grid x index
        constraints.gridy = 0; //Grid y index
        constraints.gridwidth = 2; //Cell width
        this.add(this.titleLabel, constraints);

        this.typeLabel = new JLabel("");
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        this.add(this.typeLabel, constraints);

        this.providerLabel = new JLabel("");
        constraints.gridy = 2;
        this.add(this.providerLabel, constraints);

        this.genreTextArea = new JTextArea(3, 1);
        this.genreTextArea.setEditable(false); //Set editable to false so the user cannot edit it
        this.genreTextArea.setFocusable(false); //Set focusable to false so the user cannot focus it
        this.genreTextArea.setLineWrap(true); //Create line breaks
        this.genreTextArea.setWrapStyleWord(true); //Words that does not fit in the line anymore will be moved to the next line
        this.genreTextArea.setBackground(null); //Remove background
        constraints.gridy = 3;
        this.add(this.genreTextArea, constraints);

        this.durationLabel = new JLabel("");
        constraints.gridy = 4;
        this.add(this.durationLabel, constraints);

        this.seasonLabel = new JLabel("");
        constraints.gridy = 5;
        this.add(this.seasonLabel, constraints);

        this.descriptionTextArea = new JTextArea(3, 1);
        this.descriptionTextArea.setEditable(false);
        this.descriptionTextArea.setFocusable(false);
        this.descriptionTextArea.setLineWrap(true);
        this.descriptionTextArea.setWrapStyleWord(true);
        this.descriptionTextArea.setBackground(null);
        constraints.gridx = 1;
        constraints.gridy = 1;

        //Initialize scroll panes for the text areas where the horizontal scroll bar is hidden and the vertical scroll bar is only shown when needed
        this.descriptionScrollPane = new JScrollPane(this.descriptionTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.descriptionScrollPane.setMinimumSize(new Dimension(0, 100)); //Set minimum size
        this.descriptionScrollPane.setMaximumSize(new Dimension(0, 150)); //Set maximum size
        this.add(this.descriptionScrollPane, constraints);

        this.ageRatingLabel = new JLabel("");
        constraints.gridy = 2;
        this.add(this.ageRatingLabel, constraints);

        this.castTextArea = new JTextArea(3, 1);
        this.castTextArea.setEditable(false);
        this.castTextArea.setFocusable(false);
        this.castTextArea.setLineWrap(true);
        this.castTextArea.setWrapStyleWord(true);
        this.castTextArea.setBackground(null);
        constraints.gridy = 3;

        this.castScrollPane = new JScrollPane(this.castTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.castScrollPane.setMinimumSize(new Dimension(0, 100));
        this.castScrollPane.setMaximumSize(new Dimension(0, 150));
        this.add(this.castScrollPane, constraints);

        this.countryTextArea = new JTextArea(3, 1);
        this.countryTextArea.setEditable(false);
        this.countryTextArea.setFocusable(false);
        this.countryTextArea.setLineWrap(true);
        this.countryTextArea.setWrapStyleWord(true);
        this.countryTextArea.setBackground(null);
        constraints.gridy = 4;
        this.add(this.countryTextArea, constraints);

        this.addedAtLabel = new JLabel("");
        constraints.gridy = 5;
        this.add(this.addedAtLabel, constraints);
    }

    /**
     * Initializes panel styles
     *
     * @BigO: O(n)
     * **/
    @Override
    public void initStyles() {
        this.setVisible(false); //Hide by default

        this.setBorder(new EmptyBorder(0, 20, 20, 0)); //Set margin

        //Create titled border for title label and add margin
        this.titleLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 20, 0), BorderFactory.createLineBorder(CustomColors.LIGHT_BLUE)));

        //Set labels font to Java default font
        this.titleLabel.setFont(new Font(null, Font.BOLD, 14));
        this.typeLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.providerLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.genreTextArea.setFont(new Font(null, Font.PLAIN, 12));
        this.durationLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.seasonLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.descriptionTextArea.setFont(new Font(null, Font.PLAIN, 12));
        this.ageRatingLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.castTextArea.setFont(new Font(null, Font.PLAIN, 12));
        this.countryTextArea.setFont(new Font(null, Font.PLAIN, 12));
        this.addedAtLabel.setFont(new Font(null, Font.PLAIN, 12));

        //Set vertical alignment to top to display it in the upper-left
        this.typeLabel.setVerticalTextPosition(JLabel.TOP);
        this.typeLabel.setVerticalAlignment(JLabel.TOP);
        this.providerLabel.setVerticalTextPosition(JLabel.TOP);
        this.providerLabel.setVerticalAlignment(JLabel.TOP);
        this.ageRatingLabel.setVerticalTextPosition(JLabel.TOP);
        this.ageRatingLabel.setVerticalAlignment(JLabel.TOP);
        this.durationLabel.setVerticalTextPosition(JLabel.TOP);
        this.durationLabel.setVerticalAlignment(JLabel.TOP);
        this.seasonLabel.setVerticalTextPosition(JLabel.TOP);
        this.seasonLabel.setVerticalAlignment(JLabel.TOP);
        this.addedAtLabel.setVerticalTextPosition(JLabel.TOP);
        this.addedAtLabel.setVerticalAlignment(JLabel.TOP);
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
        this.typeLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.type"))));

        this.providerLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.provider"))));

        this.genreTextArea.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.genre"))));

        this.durationLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.duration"))));

        this.seasonLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.season"))));

        this.descriptionScrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.description"))));

        this.ageRatingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.age"))));

        this.castScrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.cast"))));

        this.countryTextArea.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.country"))));

        this.addedAtLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.metadata.added_at"))));
    }

    /**
     * Fills the DataViewPanel
     *
     * @param medium object of Medium that contains the information which will be loaded into the GUI
     *
     * @BigO: O(n)
     * **/
    public void fillDataView(Medium medium){
        //Set title label with title and release year. If release year is 0 then replace it by N/A because then it cannot be loaded
        this.titleLabel.setText(medium.getTitle() + " (" + (medium.getReleaseYear() == 0 ? "N/A" : medium.getReleaseYear()) + ")");

        //Set medium type and format it to correct uppercase
        this.typeLabel.setText(Utils.uppercaseAll(medium.getType().name()));

        //Format providers text and set the label
        String formattedKey = Utils.uppercaseAll(Utils.joinArray(Utils.joinList(medium.getProviders(), ", ").split("_"), " "));
        this.providerLabel.setText(formattedKey);

        //Set the label texts depending on the passed medium
        this.genreTextArea.setText(medium.getGenres());
        this.durationLabel.setText(medium.getDuration() == -1 ? "N/A" : Integer.toString(medium.getDuration()));
        this.seasonLabel.setText(medium.getSeasons() == -1 ? "N/A" : Integer.toString(medium.getSeasons()));
        this.descriptionTextArea.setText(medium.getDescription().trim());
        this.ageRatingLabel.setText(medium.getAgeRating());
        this.castTextArea.setText(Utils.joinArray(medium.getCast(), ", "));
        this.countryTextArea.setText(medium.getCountries());
        this.addedAtLabel.setText(medium.getAddedAt());

        //Move the scroll pane scroll bar to top
        this.descriptionTextArea.setCaretPosition(0);
        this.castTextArea.setCaretPosition(0);

        //Show this panel
        this.setVisible(true);
    }
}
