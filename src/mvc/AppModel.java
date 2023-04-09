package mvc;

import util.Util;
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
        return this.mediums.get(Util.stringToKey(title));
    }

    public boolean isTitleInMediums(String title) {
        return this.mediums.containsKey(Util.stringToKey(title));
    }
}
