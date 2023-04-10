package mvc;

import util.Algorithms;
import util.Utils;
import util.data.*;
import util.enums.AudienceReviewClassification;
import util.enums.Language;
import util.file.CustomData;
import util.file.FileLoader;
import util.file.FileSaver;
import util.file.Translation;

import java.util.*;

public class AppModel {
    private final AppController appController;
    private final FileSaver fileSaver = new FileSaver(this);
    private final FileLoader fileLoader = new FileLoader(this);
    private final HashMap<String, Translation> translations = new HashMap<>();
    private final HashMap<String, Medium> mediums = new HashMap<>(); //Use ArrayList instead of HashMap because some titles are available at multiple providers and storing the movie id wouldn't be as helpful
    private final ArrayList<Review> reviews = new ArrayList<>();
    private final LinkedList<ImdbRating> imdbRatings = new LinkedList<>();

    private CustomData customData;

    public AppView getAppView(){
        return this.appController.getAppView();
    }

    public CustomData getCustomData() {
        return customData;
    }

    public void setCustomData(CustomData customData) {
        this.customData = customData;
        this.fileSaver.saveCustomData();
    }

    public HashMap<String, Translation> getTranslations() {
        return translations;
    }

    public HashMap<String, Medium> getMediums() {
        return mediums;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public LinkedList<ImdbRating> getImdbRatings() {
        return imdbRatings;
    }

    public AppModel(AppController appController){
        this.appController = appController;
    }

    public void init(){
        this.fileLoader.loadFiles();
    }

    public String getTranslation(String key){
        return this.customData.getLanguage() == Language.DE ? this.translations.get(key).getTranslationDe() : this.translations.get(key).getTranslationEn();
    }

    public Medium getMediumByTitle(String title){
        return this.mediums.get(Utils.stringToKeyFormat(title));
    }

    public Medium getRandomMedium(){
        ArrayList<String> keys = new ArrayList<>(this.mediums.keySet());
        Random random = new Random();

        if(keys.isEmpty())
            return null;

        return this.mediums.get(keys.get(random.nextInt(keys.size())));
    }

    //Returns array of AudienceReview only, because CriticsReview does not have a rating as a number
    public AudienceReview[] getBestAndWorstReviewByTitle(String title){
        AudienceReview[] reviews = new AudienceReview[2];
        for(AudienceReview review : this.getAudienceReviewsByTitle(title)){
            if(reviews[1] == null || review.getRating() >= reviews[1].getRating())
                reviews[1] = review;

            if(reviews[0] == null || review.getRating() <= reviews[0].getRating())
                reviews[0] = review;
        }

        if(reviews[0] != null && reviews[1] != null){
            reviews[0].setClassification(AudienceReviewClassification.WORST);
            reviews[1].setClassification(AudienceReviewClassification.BEST);
        }

        return reviews;
    }

    public ArrayList<Review> getAllReviewsByTitle(String title){
        ArrayList<Review> reviews = new ArrayList<>();

        if(isTitleInMediums(title)){
            for(Review review : this.reviews){
                if(review.getMediumTitle().equalsIgnoreCase(title)){
                    reviews.add(review);;
                }
            }

            ArrayList<AudienceReview> audienceReviews = this.getAudienceReviewsOnly(reviews);
            this.sortAudienceReviewsByRatingDesc(audienceReviews);
            ArrayList<CriticReview> criticReviews = this.getCriticsReviewsOnly(reviews);

            reviews.clear();
            reviews.addAll(audienceReviews);
            reviews.addAll(criticReviews);
        }

        return reviews;
    }

    public ArrayList<AudienceReview> getAudienceReviewsByTitle(String title){
        ArrayList<AudienceReview> reviews = new ArrayList<>();

        if(isTitleInMediums(title)){
            for(Review review : this.reviews){
                if(review.getMediumTitle().equalsIgnoreCase(title) && review instanceof AudienceReview)
                    reviews.add((AudienceReview) review);
            }
        }

        return reviews;
    }

    private ArrayList<CriticReview> getCriticsReviewsOnly(ArrayList<Review> reviews){
        ArrayList<CriticReview> criticReviews = new ArrayList<>();
        for(Review review : reviews){
            if(review instanceof CriticReview){
                criticReviews.add(((CriticReview) review));
            }
        }

        return criticReviews;
    }

    private ArrayList<AudienceReview> getAudienceReviewsOnly(ArrayList<Review> reviews){
        ArrayList<AudienceReview> audienceReviews = new ArrayList<>();
        for(Review review : reviews){
            if(review instanceof AudienceReview){
                audienceReviews.add(((AudienceReview) review));
            }
        }

        return audienceReviews;
    }

    private void sortAudienceReviewsByRatingDesc(ArrayList<AudienceReview> reviews){
        Algorithms.quickSortAudienceReviews(reviews, 0, reviews.size() - 1);
        Collections.reverse(reviews);
    }

    public boolean hasMediums(){
        return !this.mediums.isEmpty();
    }

    public boolean isTitleInMediums(String title) {
        return this.mediums.containsKey(Utils.stringToKeyFormat(title));
    }
}
