package util.data;

public class ImdbRating {
    private final Medium medium;
    private final String posterLink;
    private final float imdbRating;
    private final int metaScore, voteAmount, gross;
    private final Person[] stars;

    public Medium getMedium() {
        return medium;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public int getMetaScore() {
        return metaScore;
    }

    public int getVoteAmount() {
        return voteAmount;
    }

    public int getGross() {
        return gross;
    }

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
