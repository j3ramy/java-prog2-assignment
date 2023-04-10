package util.data;

public class AudienceReview extends Review {
    private final float rating;
    private boolean isBest = false;

    public float getRating() {
        return rating;
    }

    public boolean isBest() {
        return isBest;
    }

    public void setBest(boolean best) {
        isBest = best;
    }

    public AudienceReview(String mediumTitle, String comment, float rating){
        super(mediumTitle, comment);

        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Title: " + this.mediumTitle + ", Rating: " + this.rating + ", Comment: " + this.comment;
    }
}
