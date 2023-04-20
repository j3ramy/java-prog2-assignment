package mvc;

import util.Algorithms;
import util.Utils;
import util.data.*;
import util.enums.AudienceReviewType;
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
    private final HashMap<String, Medium> mediums = new HashMap<>();
    private final ArrayList<Review> reviews = new ArrayList<>();
    private final LinkedList<ImdbRating> imdbRatings = new LinkedList<>();

    private CustomData customData;

    /**
     * Gets app view
     * @return  app view
     *
     * @BigO: O(1)
     * **/
    public AppView getAppView(){
        return this.appController.getAppView();
    }

    /**
     * Gets custom data
     * @return  custom data
     *
     * @BigO: O(1)
     * **/
    public CustomData getCustomData() {
        return customData;
    }

    /**
     * Sets custom data
     * @param  customData new custom data
     *
     * @BigO: O(n)
     * **/
    public void setCustomData(CustomData customData) {
        this.customData = customData;
        this.fileSaver.saveCustomData();
    }

    /**
     * Gets translations
     * @return  translations
     *
     * @BigO: O(1)
     * **/
    public HashMap<String, Translation> getTranslations() {
        return translations;
    }

    /**
     * Gets mediums
     * @return  mediums
     *
     * @BigO: O(1)
     * **/
    public HashMap<String, Medium> getMediums() {
        return mediums;
    }

    /**
     * Gets reviews
     * @return  reviews
     *
     * @BigO: O(1)
     * **/
    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /**
     * Gets IMDB ratings
     * @return  IMDB ratings
     *
     * @BigO: O(1)
     * **/
    public LinkedList<ImdbRating> getImdbRatings() {
        return imdbRatings;
    }

    public AppModel(AppController appController){
        this.appController = appController;
    }

    /**
     * Init the app model
     *
     * @BigO: O(1)
     * **/
    public void init(){
        this.fileLoader.loadFiles();
    }

    /**
     * Get translation as string depending on set language
     *
     * @BigO: O(1)
     * **/
    public String getTranslation(String key){
        return this.customData.getLanguage() == Language.DE ? this.translations.get(key).getTranslationDe() : this.translations.get(key).getTranslationEn();
    }

    /**
     * Returns a random medium
     *
     * @return random medium
     *
     * @BigO: O(n)
     * **/
    public Medium getRandomMedium(){
        ArrayList<String> keys = new ArrayList<>(this.mediums.keySet());
        Random random = new Random();

        if(keys.isEmpty())
            return null;

        return this.mediums.get(keys.get(random.nextInt(keys.size())));
    }

    /**
     * Returns a medium by title
     *
     * @param title title that should be looked for
     * @return medium
     *
     * @BigO: O(1)
     * **/
    public Medium getMediumByTitle(String title){
        return this.mediums.get(Utils.stringToKeyFormat(title));
    }

    /**
     * Returns a list of mediums by title
     *
     * @param title title that should be looked for
     * @return array list of mediums matching the title
     *
     * @BigO: O(n)
     * **/
    public ArrayList<Medium> getMediumsByTitle(String title){
        ArrayList<Medium> mediums = new ArrayList<>();
        for(String keys : this.mediums.keySet()){
            if(keys.contains(Utils.removeForbiddenChars(Utils.stringToKeyFormat(title))))
                mediums.add(this.mediums.get(keys));
        }

        Algorithms.sortMediumsByRating(mediums, this);
        Algorithms.sortMediumsByReleaseYear(mediums);
        Collections.reverse(mediums);

        return mediums;
    }

    /**
     * Returns a list of mediums by genres
     *
     * @param genres genres that should be looked for
     * @return array list of mediums matching the genres
     *
     * @BigO: O(n)
     * **/
    public ArrayList<Medium> getMediumsByGenres(String[] genres){
        ArrayList<Medium> mediums = new ArrayList<>();
        for(Medium medium : this.mediums.values()){
            if(this.getGenreMatchingCount(genres, medium.getGenres()) == genres.length)
                mediums.add(medium);
        }

        Algorithms.sortMediumsByRating(mediums, this);
        Algorithms.sortMediumsByReleaseYear(mediums);
        Collections.reverse(mediums);

        return mediums;
    }

    /**
     * Returns a list of mediums by actors
     *
     * @param searchedActors actors that should be looked for
     * @return array list of mediums matching the cast
     *
     * @BigO: O(n)
     * **/
    public ArrayList<Medium> getMediumsByCast(String[] searchedActors){
        ArrayList<Medium> mediums = new ArrayList<>();
        for(Medium medium : this.mediums.values()){
            int actorMatchCounter = this.getActorMatchingCount(medium.getCast(), searchedActors);

            if(actorMatchCounter == searchedActors.length)
                mediums.add(medium);
        }

        Algorithms.sortMediumsByRating(mediums, this);
        Algorithms.sortMediumsByReleaseYear(mediums);
        Collections.reverse(mediums);

        return mediums;
    }

    /**
     * Returns a list of top 100 imdb ratings
     *
     * @return array list of IMDB rating
     *
     * @BigO: O(n)
     * **/
    public ArrayList<ImdbRating> getTop100Imdb(){
        ArrayList<ImdbRating> ratings = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            if(i < this.imdbRatings.size() - 1)
                ratings.add(this.imdbRatings.get(i));
        }

        return ratings;
    }

    /**
     * Returns a list of top 100 mediums by audience review rating
     *
     * @return array list of mediums matching the cast
     *
     * @BigO: O(n)
     * **/
    public ArrayList<Medium> getTop100ByReviews(){
        HashMap<Float, ArrayList<Medium>> mediums = new HashMap<>();
        for(Medium medium : this.mediums.values()){
            ArrayList<AudienceReview> reviews = this.getAudienceReviewsByTitle(medium.getTitle());
            if(!reviews.isEmpty()){
                float average = Algorithms.getAverageRating(reviews);
                if(mediums.containsKey(average)){
                    mediums.get(average).add(medium);
                }
                else{
                    mediums.put(average, new ArrayList<>(List.of(medium)));
                }
            }
        }

        TreeMap<Float, ArrayList<Medium>> map = new TreeMap<>(mediums);
        ArrayList<Medium> sortedMediums = new ArrayList<>();
        for(Map.Entry<Float, ArrayList<Medium>> entry : map.descendingMap().entrySet()){
            entry.getValue().forEach((medium) -> medium.setAverageRating(entry.getKey()));

            /*
            if(sortedMediums.size() >= 100)
                return sortedMediums;


             */
            sortedMediums.addAll(entry.getValue());
        }

        //TODO: List is 102 elements big and not 100 for some reason
        System.out.println(sortedMediums.size());

        return (ArrayList<Medium>) sortedMediums.subList(0, 100);
    }

    /**
     * Returns an audience review array with size of 2 with the best and worst review of any title
     *
     * @param title title where reviews should be searched for
     *
     * @return array with best and worst review
     *
     * @BigO: O(n)
     * **/
    public AudienceReview[] getBestAndWorstReviewByTitle(String title){
        AudienceReview[] reviews = new AudienceReview[2];
        for(AudienceReview review : this.getAudienceReviewsByTitle(title)){
            if(reviews[1] == null || review.getRating() >= reviews[1].getRating())
                reviews[1] = review;

            if(reviews[0] == null || review.getRating() <= reviews[0].getRating())
                reviews[0] = review;
        }

        if(reviews[0] != null && reviews[1] != null){
            reviews[0].setAudienceReviewType(AudienceReviewType.WORST);
            reviews[1].setAudienceReviewType(AudienceReviewType.BEST);
        }

        return reviews;
    }

    /**
     * Returns all reviews  of any title
     *
     * @param title title where reviews should be searched for
     *
     * @return list of all reviews
     *
     * @BigO: O(n)
     * **/
    public ArrayList<Review> getAllReviewsByTitle(String title){
        ArrayList<Review> reviews = new ArrayList<>();

        if(this.isTitleInMediums(title)){
            for(Review review : this.reviews){
                if(review.getMediumTitle().equalsIgnoreCase(title)){
                    reviews.add(review);
                }
            }

            ArrayList<AudienceReview> audienceReviews = this.getAudienceReviewsOnly(reviews);
            Algorithms.sortAudienceReviewsByRating(audienceReviews);

            Collections.reverse(audienceReviews);
            ArrayList<CriticReview> criticReviews = this.getCriticsReviewsOnly(reviews);

            reviews.clear();
            reviews.addAll(audienceReviews);
            reviews.addAll(criticReviews);
        }

        return reviews;
    }

    /**
     * Returns all audience reviews of any title
     *
     * @param title title where reviews should be searched for
     *
     * @return list of all audience reviews
     *
     * @BigO: O(n)
     * **/
    public ArrayList<AudienceReview> getAudienceReviewsByTitle(String title){
        ArrayList<AudienceReview> reviews = new ArrayList<>();

        if(this.isTitleInMediums(title)){
            for(Review review : this.reviews){
                if(review.getMediumTitle().equalsIgnoreCase(title) && review instanceof AudienceReview)
                    reviews.add((AudienceReview) review);
            }
        }

        return reviews;
    }

    /**
     * Returns if there are any mediums loaded/found
     *
     * @return true if mediums are found otherwise false
     *
     * @BigO: O(1)
     * **/
    public boolean hasMediums(){
        return !this.mediums.isEmpty();
    }

    /**
     * Returns if medium with passed title exists
     *
     * @param title title to be searched for
     *
     * @return true if mediums is found otherwise false
     *
     * @BigO: O(1)
     * **/
    public boolean isTitleInMediums(String title) {
        return this.mediums.containsKey(Utils.stringToKeyFormat(title));
    }

    /**
     * Returns a list of critics reviews from passed reviews
     *
     * @param reviews all reviews of title
     *
     * @return list of critics reviews
     *
     * @BigO: O(n)
     * **/
    private ArrayList<CriticReview> getCriticsReviewsOnly(ArrayList<Review> reviews){
        ArrayList<CriticReview> criticReviews = new ArrayList<>();
        for(Review review : reviews){
            if(review instanceof CriticReview){
                criticReviews.add(((CriticReview) review));
            }
        }

        return criticReviews;
    }

    /**
     * Returns a list of audience reviews from passed reviews
     *
     * @param reviews all reviews of title
     *
     * @return list of audience reviews
     *
     * @BigO: O(n)
     * **/
    private ArrayList<AudienceReview> getAudienceReviewsOnly(ArrayList<Review> reviews){
        ArrayList<AudienceReview> audienceReviews = new ArrayList<>();
        for(Review review : reviews){
            if(review instanceof AudienceReview){
                audienceReviews.add(((AudienceReview) review));
            }
        }

        return audienceReviews;
    }

    /**
     * Returns count of how many entered genres match the medium genres
     *
     * @param genres genres of one medium
     * @param mediumGenres genres entered by user
     *
     * @return number of matching count
     *
     * @BigO: O(n)
     * **/
    private int getGenreMatchingCount(String[] genres, String mediumGenres){
        int matchCounter = 0;
        for(String genre : genres){
            String formattedGenre = Utils.removeForbiddenChars(genre.toLowerCase().strip());
            if(mediumGenres.toLowerCase().contains(formattedGenre))
                matchCounter++;
        }

        return matchCounter;
    }

    /**
     * Returns count of how many entered actors match the medium cast
     *
     * @param cast cast/actors of any medium
     * @param searchedActors actors entered by user
     *
     * @return number of matching count
     *
     * @BigO: O(n)
     * **/
    private int getActorMatchingCount(Person[] cast, String[] searchedActors){
        int matchCounter = 0;
        for(String actor : searchedActors){
            String formattedActor = Utils.removeForbiddenChars(actor.toLowerCase().strip());
            if(this.containsCastActor(cast, formattedActor))
                matchCounter++;
        }

        return matchCounter;
    }

    /**
     * Returns if the passed cast contains the searched actor
     *
     * @param cast cast/actors of any medium
     * @param actorName actor to search
     *
     * @return true if medium cast contains searched actor name
     *
     * @BigO: O(n)
     * **/
    private boolean containsCastActor(Person[] cast, String actorName){
        for(Person person : cast){
            if(person.getName().toLowerCase().contains(actorName))
                return true;
        }

        return false;
    }
}
