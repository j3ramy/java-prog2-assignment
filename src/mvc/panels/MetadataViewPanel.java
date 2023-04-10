package mvc.panels;

import mvc.AppModel;
import mvc.AppView;
import util.Colors;
import util.Util;
import util.data.Medium;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MetadataViewPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JLabel titleLabel, typeLabel, providerLabel, durationLabel, seasonLabel, ageRatingLabel, addedAtLabel;
    private JTextArea genreTextArea, descriptionTextArea, castTextArea, countryTextArea;
    private JScrollPane descriptionScrollPane, castScrollPane;

    public MetadataViewPanel(AppView appView){
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
        constraints.fill = GridBagConstraints.BOTH;

        this.titleLabel = new JLabel("", SwingConstants.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        this.add(this.titleLabel, constraints);

        this.typeLabel = new JLabel("");
        this.typeLabel.setVerticalAlignment(SwingConstants.TOP);
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        this.add(this.typeLabel, constraints);

        this.providerLabel = new JLabel("");
        constraints.gridy = 2;
        this.add(this.providerLabel, constraints);

        this.genreTextArea = new JTextArea(50, 100);
        this.genreTextArea.setEditable(false);
        this.genreTextArea.setFocusable(false);
        this.genreTextArea.setLineWrap(true);
        this.genreTextArea.setWrapStyleWord(true);
        this.genreTextArea.setBackground(null);
        constraints.gridy = 3;
        this.add(this.genreTextArea, constraints);

        this.durationLabel = new JLabel("");
        constraints.gridy = 4;
        this.add(this.durationLabel, constraints);

        this.seasonLabel = new JLabel("");
        constraints.gridy = 5;
        this.add(this.seasonLabel, constraints);

        this.descriptionTextArea = new JTextArea();
        this.descriptionTextArea.setEditable(false);
        this.descriptionTextArea.setFocusable(false);
        this.descriptionTextArea.setLineWrap(true);
        this.descriptionTextArea.setWrapStyleWord(true);
        this.descriptionTextArea.setBackground(null);
        constraints.gridx = 1;
        constraints.gridy = 1;

        this.descriptionScrollPane = new JScrollPane(this.descriptionTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.descriptionScrollPane.setMinimumSize(new Dimension(0, 100));
        this.descriptionScrollPane.setMaximumSize(new Dimension(0, 150));
        this.add(this.descriptionScrollPane, constraints);

        this.ageRatingLabel = new JLabel("");
        constraints.gridy = 2;
        this.add(this.ageRatingLabel, constraints);

        this.castTextArea = new JTextArea(50, 100);
        this.castTextArea.setEditable(false);
        this.castTextArea.setFocusable(false);
        this.castTextArea.setLineWrap(true);
        this.castTextArea.setWrapStyleWord(true);
        this.castTextArea.setBackground(null);
        constraints.gridy = 3;

        //TODO: Bugfix cast textarea white lines in the end
        this.castScrollPane = new JScrollPane(this.castTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.castScrollPane.setMinimumSize(new Dimension(0, 100));
        this.castScrollPane.setMaximumSize(new Dimension(0, 150));
        this.add(this.castScrollPane, constraints);

        this.countryTextArea = new JTextArea(50, 100);
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

    @Override
    public void initStyles() {
        this.setVisible(false);

        this.setBorder(new EmptyBorder(0, 20, 20, 0));

        this.titleLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 20, 0), BorderFactory.createLineBorder(Colors.BORDER)));

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
    }

    @Override
    public void initImages() {}

    @Override
    public void initActionListeners(){}

    @Override
    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.typeLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.type"))));
        this.providerLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.provider"))));
        this.genreTextArea.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.genre"))));
        this.durationLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.duration"))));
        this.seasonLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.season"))));
        this.descriptionScrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.description"))));
        this.ageRatingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.age"))));
        this.castScrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.cast"))));
        this.countryTextArea.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.country"))));
        this.addedAtLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.metadata.added_at"))));
    }

    public void fillDataView(Medium medium){
        this.titleLabel.setText(medium.getTitle() + " (" + medium.getReleaseYear() + ")");

        this.typeLabel.setText(Util.uppercaseAll(medium.getType().name()));
        this.providerLabel.setText(Util.joinList(medium.getProviders(), ", "));
        this.genreTextArea.setText(medium.getGenres());
        this.durationLabel.setText(medium.getDuration());
        this.seasonLabel.setText(medium.getSeasons());
        this.descriptionTextArea.setText(medium.getDescription().trim());
        this.ageRatingLabel.setText(medium.getAgeRating());
        this.castTextArea.setText(Util.joinArray(medium.getCast(), ", "));
        this.countryTextArea.setText(medium.getCountries());
        this.addedAtLabel.setText(medium.getAddedAt());

        this.descriptionTextArea.setCaretPosition(0);
        this.castTextArea.setCaretPosition(0);

        this.setVisible(true);
    }
}
