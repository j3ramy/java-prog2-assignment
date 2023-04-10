package mvc;

import util.Util;
import util.data.AudienceReview;
import util.data.ImdbRating;
import util.data.Medium;
import util.data.Review;
import util.enums.Language;
import util.file.CustomData;
import util.file.FileLoader;
import util.file.FileSaver;
import util.file.Translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

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
        return this.mediums.get(Util.stringToKeyFormat(title));
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
            reviews[0].setBest(false);
            reviews[1].setBest(true);
        }

        return reviews;
    }

    public ArrayList<Review> getAllReviewsByTitle(String title){
        ArrayList<Review> reviews = new ArrayList<>();

        if(isTitleInMediums(title)){
            for(Review review : this.reviews){
                if(review.getMediumTitle().equalsIgnoreCase(title))
                    reviews.add(review);
            }
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

    public boolean hasMediums(){
        return !this.mediums.isEmpty();
    }

    public boolean hasTitleReviews(String title){
        return !this.getAllReviewsByTitle(title).isEmpty();
    }

    public boolean isTitleInMediums(String title) {
        return this.mediums.containsKey(Util.stringToKeyFormat(title));
    }
}
