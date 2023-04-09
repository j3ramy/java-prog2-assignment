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
        GridBagConstraints dataViewConstraints = new GridBagConstraints();
        dataViewConstraints.weightx = 1f;
        dataViewConstraints.weighty = 1f;
        dataViewConstraints.fill = GridBagConstraints.BOTH;

        this.titleLabel = new JLabel("", SwingConstants.CENTER);
        dataViewConstraints.gridx = 0;
        dataViewConstraints.gridy = 0;
        dataViewConstraints.gridwidth = 2;
        this.add(this.titleLabel, dataViewConstraints);

        this.typeLabel = new JLabel("");
        this.typeLabel.setVerticalAlignment(SwingConstants.TOP);
        dataViewConstraints.gridy = 1;
        dataViewConstraints.gridwidth = 1;
        this.add(this.typeLabel, dataViewConstraints);

        this.providerLabel = new JLabel("");
        dataViewConstraints.gridy = 2;
        this.add(this.providerLabel, dataViewConstraints);

        this.genreTextArea = new JTextArea(50, 100);
        this.genreTextArea.setEditable(false);
        this.genreTextArea.setFocusable(false);
        this.genreTextArea.setLineWrap(true);
        this.genreTextArea.setWrapStyleWord(true);
        this.genreTextArea.setBackground(null);
        dataViewConstraints.gridy = 3;
        this.add(this.genreTextArea, dataViewConstraints);

        this.durationLabel = new JLabel("");
        dataViewConstraints.gridy = 4;
        this.add(this.durationLabel, dataViewConstraints);

        this.seasonLabel = new JLabel("");
        dataViewConstraints.gridy = 5;
        this.add(this.seasonLabel, dataViewConstraints);

        this.descriptionTextArea = new JTextArea();
        this.descriptionTextArea.setEditable(false);
        this.descriptionTextArea.setFocusable(false);
        this.descriptionTextArea.setLineWrap(true);
        this.descriptionTextArea.setWrapStyleWord(true);
        this.descriptionTextArea.setBackground(null);
        dataViewConstraints.gridx = 1;
        dataViewConstraints.gridy = 1;
        this.add(this.descriptionTextArea, dataViewConstraints);

        this.ageRatingLabel = new JLabel("");
        dataViewConstraints.gridy = 2;
        this.add(this.ageRatingLabel, dataViewConstraints);

        this.castTextArea = new JTextArea(50, 100);
        this.castTextArea.setEditable(false);
        this.castTextArea.setFocusable(false);
        this.castTextArea.setLineWrap(true);
        this.castTextArea.setWrapStyleWord(true);
        this.castTextArea.setBackground(null);
        dataViewConstraints.gridy = 3;
        this.add(this.castTextArea, dataViewConstraints);

        this.countryTextArea = new JTextArea(50, 100);
        this.countryTextArea.setEditable(false);
        this.countryTextArea.setFocusable(false);
        this.countryTextArea.setLineWrap(true);
        this.countryTextArea.setWrapStyleWord(true);
        this.countryTextArea.setBackground(null);
        dataViewConstraints.gridy = 4;
        this.add(this.countryTextArea, dataViewConstraints);

        this.addedAtLabel = new JLabel("");
        dataViewConstraints.gridy = 5;
        this.add(this.addedAtLabel, dataViewConstraints);
    }

    @Override
    public void initStyles() {
        this.setVisible(false);

        this.titleLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createLineBorder(Colors.BORDER)));

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

        this.typeLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.type"))));
        this.providerLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.provider"))));
        this.genreTextArea.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.genre"))));
        this.durationLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.duration"))));
        this.seasonLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.season"))));
        this.descriptionTextArea.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.description"))));
        this.ageRatingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.age"))));
        this.castTextArea.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.cast"))));
        this.countryTextArea.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.country"))));
        this.addedAtLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.random.added_at"))));
    }

    public void fillDataView(Medium medium){
        this.titleLabel.setText(medium.getTitle() + " (" + medium.getReleaseYear() + ")");

        this.typeLabel.setText(Util.uppercaseAll(medium.getType().name()));
        this.providerLabel.setText(Util.joinList(medium.getProviders(), ", "));
        this.genreTextArea.setText(medium.getGenres());
        this.durationLabel.setText(medium.getDuration());
        this.seasonLabel.setText(medium.getSeasons());
        this.descriptionTextArea.setText(medium.getDescription());
        this.ageRatingLabel.setText(medium.getAgeRating());
        this.castTextArea.setText(Util.joinArray(medium.getCast(), ", "));
        this.countryTextArea.setText(medium.getCountries());
        this.addedAtLabel.setText(medium.getAddedAt());

        this.setVisible(true);
    }
}
