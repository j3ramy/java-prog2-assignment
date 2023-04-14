package util.data;

import util.enums.AudienceReviewClassification;

public class AudienceReview extends Review {
    private final float rating;
    private AudienceReviewClassification classification = AudienceReviewClassification.NONE;

    public float getRating() {
        return rating;
    }

    public AudienceReviewClassification getClassification() {
        return classification;
    }

    public void setClassification(AudienceReviewClassification classification) {
        this.classification = classification;
    }

    public AudienceReview(String mediumTitle, String comment, float rating){
        super(mediumTitle, comment);

        this.rating = rating;
    }
}
