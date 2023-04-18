package util.data;

public class ImdbRating {
    private final Medium medium;
    private final String posterLink;
    private final float imdbRating;
    private final int metaScore, voteAmount, gross;
    private final Person[] stars;

    /**
     * Gets medium
     * @return  medium
     *
     * @BigO: O(1)
     * **/
    public Medium getMedium() {
        return medium;
    }

    /**
     * Gets poster link
     * @return  poster link
     *
     * @BigO: O(1)
     * **/
    public String getPosterLink() {
        return posterLink;
    }

    /**
     * Gets imdb rating
     * @return  imdb rating float
     *
     * @BigO: O(1)
     * **/
    public float getImdbRating() {
        return imdbRating;
    }

    /**
     * Gets meta score
     * @return  meta score
     *
     * @BigO: O(1)
     * **/
    public int getMetaScore() {
        return metaScore;
    }

    /**
     * Gets vote amount
     * @return  vote amount
     *
     * @BigO: O(1)
     * **/
    public int getVoteAmount() {
        return voteAmount;
    }

    /**
     * Gets gross
     * @return  gross
     *
     * @BigO: O(1)
     * **/
    public int getGross() {
        return gross;
    }

    /**
     * Gets stars
     * @return  stars
     *
     * @BigO: O(1)
     * **/
    public Person[] getStars() {
        return stars;
    }

    public ImdbRating(Medium medium, String posterLink, float imdbRating, int metaScore, int voteAmount, int gross, Person[] stars){
        this.medium = medium;
        this.posterLink = posterLink;
        this.imdbRating = imdbRating;
        this.metaScore = metaScore;
        this.voteAmount = voteAmount;
        this.gross = gross;
        this.stars = stars;
    }
}
