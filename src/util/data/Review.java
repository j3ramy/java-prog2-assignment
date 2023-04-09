package util.data;

public abstract class Review {
    protected final String mediumTitle, comment;

    public String getMediumTitle() {
        return mediumTitle;
    }

    public String getComment() {
        return comment;
    }

    public Review(String mediumTitle, String comment){
        this.mediumTitle = mediumTitle;
        this.comment = comment;
    }
}
