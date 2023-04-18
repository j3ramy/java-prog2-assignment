package util.data;

public class CriticReview extends Review {
    private final boolean sentiment;

    /**
     * Gets sentiment
     * @return  sentiment
     *
     * @BigO: O(1)
     * **/
    public boolean getSentiment(){
        return this.sentiment;
    }

    public CriticReview(String mediumTitle, String comment, boolean sentiment){
        super(mediumTitle, comment);

        this.sentiment = sentiment;
    }
}
