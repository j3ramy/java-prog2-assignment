package mvc.view.widget;

import mvc.AppModel;
import mvc.AppView;
import util.Colors;
import util.data.AudienceReview;
import util.data.CriticReview;
import util.data.ImdbRating;
import util.data.Review;
import util.file.FilePaths;
import util.interfaces.IViewPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class ImdbRatingViewPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JLabel imdbRatingLabel, metaScoreLabel, voteAmountLabel, /*grossLabel,*/ star1Label, star2Label, star3Label, star4Label, posterImage;

    public ImdbRatingViewPanel(AppView appView){
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

        this.imdbRatingLabel = new JLabel("");
        constraints.gridy = 0;
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

        this.posterImage = new JLabel();
        this.posterImage.setHorizontalAlignment(JButton.CENTER);
        this.posterImage.setAlignmentX(CENTER_ALIGNMENT);
        constraints.gridy = 7;
        this.add(this.posterImage, constraints);
    }

    @Override
    public void initStyles() {
        this.setVisible(false);

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
    public void initImages() {}

    @Override
    public void initActionListeners(){}

    @Override
    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.imdbRatingLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.rating"))));
        this.metaScoreLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.meta_score"))));
        this.voteAmountLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.votes"))));
        //this.grossLabel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.gross"))));
        this.star1Label.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.star1"))));
        this.star2Label.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.star2"))));
        this.star3Label.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.star3"))));
        this.star4Label.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 10, 0), BorderFactory.createTitledBorder(appModel.getTranslation("label.imdb.star4"))));
    }

    public void fillDataView(ImdbRating rating){
        if(rating != null){
            this.imdbRatingLabel.setText(Float.toString(rating.getImdbRating()));
            this.metaScoreLabel.setText(Float.toString(rating.getMetaScore()));
            this.voteAmountLabel.setText(Integer.toString(rating.getVoteAmount()));
            //this.grossLabel.setText(Integer.toString(rating.getVoteAmount()));
            this.star1Label.setText(rating.getStars()[0].getName());
            this.star2Label.setText(rating.getStars()[1].getName());
            this.star3Label.setText(rating.getStars()[2].getName());
            this.star4Label.setText(rating.getStars()[3].getName());

            new Thread(() -> this.setMediumPoster(rating.getPosterLink())).start();

            this.setVisible(true);
        }
        else{
            this. clear();
        }
    }

    private void setMediumPoster(String url){
        try{
            BufferedImage bufferedImage = ImageIO.read(new URL(url));
            this.posterImage.setIcon(new ImageIcon(bufferedImage));
        }
        catch(Exception ignored){}
    }

    private void clear(){
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
