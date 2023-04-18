package util.data;

public abstract class Review {
    protected final String mediumTitle, comment;

    /**
     * Gets medium title
     * @return  medium title
     *
     * @BigO: O(1)
     * **/
    public String getMediumTitle() {
        return mediumTitle;
    }

    /**
     * Gets comment
     * @return  comment
     *
     * @BigO: O(1)
     * **/
    public String getComment() {
        return comment;
    }

    public Review(String mediumTitle, String comment){
        this.mediumTitle = mediumTitle;
        this.comment = comment;
    }
}
