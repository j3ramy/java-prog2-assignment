package mvc.view.widget;

import mvc.AppView;
import util.data.ImdbRating;
import util.interfaces.IViewInit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;

public class ImdbRatingPanel extends JPanel implements IViewInit {
    private final AppView appView; //AppView reference

    //Labels for displaying the imdb rating content
    private JLabel imdbRatingLabel, metaScoreLabel, voteAmountLabel, /*grossLabel,*/ star1Label, star2Label, star3Label, star4Label, posterImage;

    //Constructor
    public ImdbRatingPanel(AppView appView){
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
        constraints.anchor = GridBagConstraints.NORTH; //Alignment of components
        constraints.fill = GridBagConstraints.HORIZONTAL; //Stretch content horizontal

        //Initialize all labels and add them
        this.imdbRatingLabel = new JLabel("");
        constraints.gridy = 0; //Grid y index
        this.add(this.imdbRatingLabel, constraints);

        this.metaScoreLabel = new JLabel("");
        constraints.gridy = 1;
        this.add(this.metaScoreLabel, constraints);

        this.voteAmountLabel = new JLabel("");
        constraints.gridy = 2;
        this.add(this.voteAmountLabel, constraints);

        /*
        this.grossLabel = new JLabel("");
        constraints.gridy = 3;
        this.add(this.grossLabel, constraints);
         */

        this.star1Label = new JLabel("");
        //constraints.gridy = 4;
        constraints.gridy = 3;
        this.add(this.star1Label, constraints);

        this.star2Label = new JLabel("");
        //constraints.gridy = 5;
        constraints.gridy = 4;
        this.add(this.star2Label, constraints);

        this.star3Label = new JLabel("");
        //constraints.gridy = 6;
        constraints.gridy = 5;
        this.add(this.star3Label, constraints);

        this.star4Label = new JLabel("");
        //constraints.gridy = 7;
        constraints.gridy = 6;
        this.add(this.star4Label, constraints);

        //Initialize the poster image which displays the medium poster
        this.posterImage = new JLabel();
        this.posterImage.setHorizontalAlignment(JButton.CENTER); //Center
        this.posterImage.setAlignmentX(CENTER_ALIGNMENT); //Center
        constraints.gridy = 7;
        this.add(this.posterImage, constraints);
    }

    /**
     * Initializes panel styles
     *
     * @BigO: O(n)
     * **/
    @Override
    public void initStyles() {
        //Hide by default
        this.setVisible(false);

        //Set labels font to Java default font
        this.imdbRatingLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.metaScoreLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.voteAmountLabel.setFont(new Font(null, Font.PLAIN, 12));
        //this.grossLabel.setFont(new Font(null, Font.PLAIN, 12));
        this.star1Label.setFont(new Font(null, Font.PLAIN, 12));
        this.star2Label.setFont(new Font(null, Font.PLAIN, 12));
        this.star3Label.setFont(new Font(null, Font.PLAIN, 12));
        this.star4Label.setFont(new Font(null, Font.PLAIN, 12));
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
        this.imdbRatingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.imdb.rating"))));

        this.metaScoreLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.imdb.meta_score"))));

        this.voteAmountLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.imdb.votes"))));

        //this.grossLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
        // BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.gross"))));

        this.star1Label.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.imdb.star1"))));

        this.star2Label.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.imdb.star2"))));

        this.star3Label.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.imdb.star3"))));

        this.star4Label.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(this.appView.getAppModel().getTranslation("label.imdb.star4"))));
    }

    /**
     * Fills the DataViewPanel
     *
     * @param rating object of ImdbRating that contains the information which will be loaded into the GUI
     *
     * @BigO: O(n)
     * **/
    public void fillDataView(ImdbRating rating){
        //If rating is null the reset this panel otherwise fill it
        if(rating != null){
            //Set the label texts depending on the passed imdb rating
            this.imdbRatingLabel.setText(Float.toString(rating.getImdbRating()));
            this.metaScoreLabel.setText(Float.toString(rating.getMetaScore()));
            this.voteAmountLabel.setText(Integer.toString(rating.getVoteAmount()));
            //this.grossLabel.setText(Integer.toString(rating.getVoteAmount()));
            this.star1Label.setText(rating.getStars()[0].getName());
            this.star2Label.setText(rating.getStars()[1].getName());
            this.star3Label.setText(rating.getStars()[2].getName());
            this.star4Label.setText(rating.getStars()[3].getName());

            //Load the medium poster in a new thread
            new Thread(() -> this.setMediumPoster(rating.getPosterLink())).start();

            //Show this panel
            this.setVisible(true);
        }
        else{
            this. clear();
        }
    }

    /**
     * Sets the image of the medium poster
     *
     * @param url url to the image
     *
     * @BigO: O(n)
     * **/
    private void setMediumPoster(String url){
        try{
            //Get image from url and set the image/icon of the poster image container
            BufferedImage bufferedImage = ImageIO.read(new URL(url));
            this.posterImage.setIcon(new ImageIcon(bufferedImage));
        }
        catch(Exception ignored){}
    }

    /**
     * Clears this panel
     *
     * @BigO: O(n)
     * **/
    private void clear(){
        //Clear all labels and remove the poster
        this.imdbRatingLabel.setText("");
        this.metaScoreLabel.setText("");
        this.voteAmountLabel.setText("");
        //this.grossLabel.setText("");
        this.star1Label.setText("");
        this.star2Label.setText("");
        this.star3Label.setText("");
        this.star4Label.setText("");
        this.posterImage.setIcon(null);
    }
}
