package util.data;

public class CriticReview extends Review {
    private final boolean sentiment;

    public boolean getSentiment(){
        return this.sentiment;
    }

    public CriticReview(String mediumTitle, String comment, boolean sentiment){
        super(mediumTitle, comment);

        this.sentiment = sentiment;
    }
}
