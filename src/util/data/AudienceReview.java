package util.data;

import util.enums.AudienceReviewType;

public class AudienceReview extends Review {
    private final float rating;
    private AudienceReviewType audienceReviewType = AudienceReviewType.DEFAULT;

    /**
     * Gets review rating
     * @return  review rating
     *
     * @BigO: O(1)
     * **/
    public float getRating() {
        return rating;
    }

    /**
     * Gets audience review type
     * @return  audience review type
     *
     * @BigO: O(1)
     * **/
    public AudienceReviewType getAudienceReviewType() {
        return audienceReviewType;
    }

    /**
     * Sets audience review type
     * @param audienceReviewType new audience review type
     *
     * @BigO: O(1)
     * **/
    public void setAudienceReviewType(AudienceReviewType audienceReviewType) {
        this.audienceReviewType = audienceReviewType;
    }

    public AudienceReview(String mediumTitle, String comment, float rating){
        super(mediumTitle, comment);

        this.rating = rating;
    }
}
