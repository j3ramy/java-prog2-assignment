package util.data;

import util.enums.AudienceReviewType;

public class AudienceReview extends Review {
    private final float rating;
    private AudienceReviewType audienceReviewType = AudienceReviewType.DEFAULT;

    public float getRating() {
        return rating;
    }

    public AudienceReviewType getAudienceReviewType() {
        return audienceReviewType;
    }

    public void setAudienceReviewType(AudienceReviewType audienceReviewType) {
        this.audienceReviewType = audienceReviewType;
    }

    public AudienceReview(String mediumTitle, String comment, float rating){
        super(mediumTitle, comment);

        this.rating = rating;
    }
}
