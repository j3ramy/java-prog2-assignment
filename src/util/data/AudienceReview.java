package util.data;

public class AudienceReview extends Review {
    private final float rating;

    public float getRating() {
        return rating;
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
