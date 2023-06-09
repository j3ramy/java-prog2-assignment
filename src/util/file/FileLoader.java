package util.file;

import mvc.AppModel;
import util.Utils;
import util.data.*;
import util.enums.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class FileLoader {
    private final AppModel appModel;
    private final LinkedHashMap<String, Integer> loadingSkips = new LinkedHashMap<>();
    private final HashMap<String, String> reviewTitles = new HashMap<>();

    public HashMap<String, String> getReviewTitles() {
        return reviewTitles;
    }

    public FileLoader(AppModel appModel){
        this.appModel = appModel;
    }

    /**
     * Loads custom data and mediums. Entry point for all file loadings
     *
     * @BigO: O(n)
     * **/
    public void loadFiles(){
        this.loadCustomData();

        new Thread(() -> {
            try {
                Thread.sleep(500);

                this.appModel.getAppView().setStatusBarText(LoadingState.LOAD_MEDIUMS);
                this.loadMediums();

                if(this.appModel.hasMediums()){
                    this.appModel.getAppView().setStatusBarText(LoadingState.LOAD_REVIEWS);
                    this.loadReviews();
                    this.appModel.getAppView().setStatusBarText(LoadingState.LOAD_IMDB_RATING);
                    this.loadImdbRatings();

                    this.appModel.getAppView().setStatusBarText(LoadingState.READY);
                    this.appModel.getAppView().enableTabs();

                    this.showSkipStats();
                }
                else{
                    this.appModel.getAppView().getDialogHandler().showNoMediumFoundDialog();
                }

                this.appModel.getAppView().setStatusBarText(LoadingState.READY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Loads custom data
     *
     * @BigO: O(n)
     * **/
    private void loadCustomData(){
        try{
            //Check if file exists, otherwise create default custom data file
            File file = new File(FilePaths.CUSTOM_DATA_PATH);
            if(!file.exists()){
                if(!new File(FilePaths.CUSTOM_DATA_PATH).createNewFile())
                    throw new FileNotFoundException();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            //Check if file is empty. If yes, then create default custom data and save it immediately. If no, read custom data from file
            if(file.length() == 0){
                this.createDefaultCustomData();
                this.loadTranslations();
                this.appModel.getAppView().getDialogHandler().showFirstAppStartDialog();
            }
            else{
                //Split line and normalize it
                String[] data = bufferedReader.readLine().split(";");
                Utils.removeQuotes(data);

                //Convert provider strings in Provider enum values
                Provider[] providers = new Provider[Provider.values().length];
                String[] stringProviders = Arrays.copyOfRange(data, 1, data.length);
                for(int i = 0; i < stringProviders.length; i++)
                    providers[i] = Provider.valueOf(stringProviders[i]);

                //Set custom data and pass language and providers
                this.appModel.setCustomData(new CustomData(Language.valueOf(data[0]), providers));
                this.loadTranslations();
            }

            bufferedReader.close();

            //Tell the view to load the custom data settings
            this.appModel.getAppView().setSettings();
        }
        catch(Exception e){
            this.appModel.getAppView().getDialogHandler().showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates default custom data when no custom data found
     *
     * @BigO: O(1)
     * **/
    private void createDefaultCustomData(){
        this.appModel.setCustomData(new CustomData(Language.EN, Provider.values()));
    }

    /**
     * Loads translations
     *
     * @BigO: O(n)
     * **/
    private void loadTranslations(){
        try{
            //Check if file exists
            File file = new File(FilePaths.TRANSLATIONS_PATH);
            if(!file.exists()){
                throw new FileNotFoundException();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //Split line
                Object[] data = line.split(";");
                //Add it to the translation hash map
                this.appModel.getTranslations().put(data[0].toString(), new Translation(data[1].toString(), data[2].toString()));
            }

            bufferedReader.close();

            //Tell the view to load the correct translations
            this.appModel.getAppView().setAllTranslations();
        }
        catch(Exception e){
            this.appModel.getAppView().getDialogHandler().showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads custom data
     *
     * @BigO: O(n)
     * **/
    private void loadMediums(){
        if(this.appModel.getCustomData().hasProvider(Provider.NETFLIX)) this.loadMediumsFromProvider(FilePaths.NETFLIX_TITLES_PATH, Provider.NETFLIX);
        if(this.appModel.getCustomData().hasProvider(Provider.DISNEY_PLUS)) this.loadMediumsFromProvider(FilePaths.DISNEY_PLUS_TITLES_PATH, Provider.DISNEY_PLUS);
        if(this.appModel.getCustomData().hasProvider(Provider.AMAZON_PRIME)) this.loadMediumsFromProvider(FilePaths.AMAZON_PRIME_TITLES_PATH, Provider.AMAZON_PRIME);

        if(this.appModel.getCustomData().hasProvider(Provider.APPLE_PLUS)){
            this.loadApplePlusCredits();
            this.loadMediumsFromProvider(FilePaths.APPLE_PLUS_TITLES_PATH, Provider.APPLE_PLUS);
        }
    }

    /**
     * Loads all mediums from file by passed provider
     *
     * @param path file path for the file that should be read
     * @param provider important for medium creating
     *
     * @BigO: O(n)
     * **/
    private void loadMediumsFromProvider(String path, Provider provider){
        try{
            //Check if file exists
            File file = new File(path);
            if(!file.exists()){
                throw new FileNotFoundException();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.readLine(); //Skips first line in while loop

            String line;
            int skipCounter = 0;
            while ((line = bufferedReader.readLine()) != null) {
                //Split line and normalize it
                String[] data = line.split(";");
                Utils.removeQuotes(data);

                Medium medium;
                if(provider != Provider.APPLE_PLUS){
                    //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                    if(!this.isNDAFormattingValid(data)){
                        skipCounter++;
                        continue;
                    }

                    medium = this.createNDAMedium(data, provider);
                }
                else{
                    //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                    if(!this.isApplePlusTitlesFormattingValid(data)){
                        skipCounter++;
                        continue;
                    }

                    medium = this.createApplePlusMedium(data, provider);
                }

                if(this.appModel.getMediums().containsKey(Utils.stringToKeyFormat(medium.getTitle()))){
                    this.appModel.getMediums().get(Utils.stringToKeyFormat(medium.getTitle())).getProviders().add(provider);
                }
                else{
                    this.appModel.getMediums().put(Utils.stringToKeyFormat(medium.getTitle()), medium);
                }
            }

            this.loadingSkips.put(provider.name(), skipCounter);
            bufferedReader.close();
        }
        catch(Exception e){
            this.appModel.getAppView().getDialogHandler().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates an instance of a Netflix, Disney or Amazon medium
     *
     * @param data split data from csv file
     * @param provider important for medium creating
     *
     * @return created medium
     *
     * @BigO: O(n)
     * **/
    private Medium createNDAMedium(String[] data, Provider provider){
        MediumType mediumType = this.convertMediumType(data[1]);
        int duration = -1;
        if(!data[9].isEmpty()) duration = Integer.parseInt(data[9].split(" ")[0]);

        return new Medium(
                data[0],
                mediumType,
                provider,
                this.convertObjectToString(data[2]),
                this.convertObjectToString(data[data.length - 1], false, false),
                this.convertObjectToString(data[10]),
                mediumType == MediumType.MOVIE ? duration : -1, //Duration contains duration in min
                mediumType == MediumType.SHOW ? duration : -1, //Duration contains duration in seasons
                data[7],
                this.getCast(data[3], data[4]),
                this.convertObjectToString(data[5]),
                data[8],
                this.convertObjectToString(data[6])
        );
    }

    /**
     * Creates an instance of an Apple Plus medium because the attributes of the file are reordered
     *
     * @param data split data from csv file
     * @param provider important for medium creating
     *
     * @return created medium
     *
     * @BigO: O(n)
     * **/
    private Medium createApplePlusMedium(String[] data, Provider provider){
        MediumType mediumType = this.convertMediumType(data[2]);
        int duration = -1;
        if(!data[6].isEmpty()) duration = Integer.parseInt(data[6]);

        int seasons = -1;
        if(!data[9].isEmpty()) seasons = Integer.parseInt(data[9].split("\\.")[0]);

        return new Medium(
                data[0],
                mediumType,
                provider,
                this.convertObjectToString(data[1]),
                this.convertObjectToString(data[3], false, false),
                this.convertObjectToString(data[7]),
                mediumType == MediumType.MOVIE ? duration : -1,
                mediumType == MediumType.SHOW ? seasons : -1,
                data[4],
                this.getApplePlusCastByMovie(data[0]),
                this.convertObjectToString(data[8]),
                data[5],
                ""
        );
    }

    /**
     * Returns apple plus cast by movie id after loading the apple plus credits
     *
     * @param movieId movie id which should be searched for
     *
     * @return cast
     *
     * @BigO: O(n)
     * **/
    private Person[] getApplePlusCastByMovie(String movieId){
        ArrayList<Person> cast = new ArrayList<>();
        for(Person person : this.loadApplePlusCredits()){
            if(person.getMovieId().equalsIgnoreCase(movieId))
                cast.add(person);
        }

        return cast.toArray(Person[]::new);
    }

    /**
     * Loads apple plus cast by movie id from file
     *
     * @return person array
     *
     * @BigO: O(n)
     * **/
    private Person[] loadApplePlusCredits(){
        try{
            //Check if file exists
            File file = new File(FilePaths.APPLE_PLUS_CREDITS_PATH);
            if(!file.exists()){
                throw new FileNotFoundException();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            int skipCounter = 0;
            ArrayList<Person> credits = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                //Split line
                String[] data = line.split(";");

                if(!this.isApplePlusCreditsFormattingValid(data)){
                    skipCounter++;
                    continue;
                }

                if(Utils.containsEnumValue(data[4], PersonRole.class)){
                    Person person = new Person(this.convertObjectToString(data[2]), this.convertObjectToString(data[3]), PersonRole.valueOf(data[4]));
                    person.setMovieId(data[1]);

                    credits.add(person);
                }
            }

            bufferedReader.close();

            this.loadingSkips.put("APPLE_PLUS_CREDITS", skipCounter);
            return credits.toArray(Person[]::new);
        }
        catch(Exception e){
            this.appModel.getAppView().getDialogHandler().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }

        return new Person[]{};
    }

    /**
     * Loads audience and critics reviews
     *
     * @BigO: O(n)
     * **/
    private void loadReviews(){
        this.loadReviewsByType(FilePaths.CRITIC_REVIEWS_PATH, ReviewType.CRITICS);
        this.loadReviewsByType(FilePaths.AUDIENCE_REVIEWS_PATH, ReviewType.AUDIENCE);
    }

    /**
     * Loads review by type
     *
     * @param path file path
     * @param type review type
     *
     * @BigO: O(n)
     * **/
    private void loadReviewsByType(String path, ReviewType type){
        try{
            //Check if file exists
            File file = new File(path);
            if(!file.exists()){
                throw new FileNotFoundException();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            int skipCounter = 0;
            while ((line = bufferedReader.readLine()) != null) {
                //Split line
                String[] data = line.split(";");

                //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                if(!this.isReviewFormattingValid(data)){
                    skipCounter++;
                    continue;
                }

                String title = data[0];
                if(!this.appModel.isTitleInMediums(title)){
                    continue;
                }

                Review review;
                if(type == ReviewType.CRITICS){
                    boolean sentiment = !data[1].equals("0");
                    review = new CriticReview(this.convertObjectToString(data[0]), this.convertObjectToString(data[2], false, false),
                            sentiment);
                }
                else{
                    review = new AudienceReview(this.convertObjectToString(data[0]), this.convertObjectToString(data[2], false, false),
                             Float.parseFloat(data[1]));
                }

                this.reviewTitles.put(review.getMediumTitle(), "");
                this.appModel.getReviews().add(review);
            }

            this.loadingSkips.put(type.name(), skipCounter);
            bufferedReader.close();
        }
        catch(Exception e){
            this.appModel.getAppView().getDialogHandler().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads IMDB ratings
     *
     * @BigO: O(n)
     * **/
    private void loadImdbRatings(){
        try{
            //Check if file exists
            File file = new File(FilePaths.IMDB_RANKING_PATH);
            if(!file.exists()){
                throw new FileNotFoundException();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            int skipCounter = 0;
            while ((line = bufferedReader.readLine()) != null) {
                //Split line and normalize it
                String[] data = line.split(";");
                Utils.removeQuotes(data);

                //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                if(!this.isImdbRatingFormattingValid(data)){
                    skipCounter++;
                    continue;
                }

                String title = data[1];
                if(!this.appModel.isTitleInMediums(title)){
                    continue;
                }

                Medium imdbMedium = this.appModel.getMediumByTitle(title);
                if(imdbMedium != null){
                    ArrayList<String> starsAsString = new ArrayList<>();
                    starsAsString.add(this.convertObjectToString(data[10]));
                    starsAsString.add(this.convertObjectToString(data[11]));
                    starsAsString.add(this.convertObjectToString(data[12]));
                    starsAsString.add(this.convertObjectToString(data[13]));

                    ImdbRating rating = new ImdbRating(
                            imdbMedium,
                            this.convertObjectToString(data[0], false, true),
                            data[6].isBlank() ? 0f : Float.parseFloat(data[6]),
                            data[8].isBlank() ? 0 : Integer.parseInt(data[8]),
                            data[14].isBlank() ? 0 : Integer.parseInt(data[14]),
                            Integer.parseInt( data[15].replace(",", "")),
                            this.getCast(null, String.valueOf(starsAsString))
                            );

                    this.appModel.getImdbRatings().add(rating);
                }
            }

            this.loadingSkips.put("IMDB_RATING", skipCounter);
            bufferedReader.close();
        }
        catch(Exception e){
            this.appModel.getAppView().getDialogHandler().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Helper method that converts a passed medium type string to a MediumType type
     *
     * @param type string type to convert
     *
     * @return MediumType type
     *
     * @BigO: O(n)
     * **/
    private MediumType convertMediumType(String type){
        return type.toLowerCase().contains("show") ? MediumType.SHOW : MediumType.MOVIE;
    }

    /**
     * Helper method that converts a data object to a string
     *
     * @param data data to be converted
     * @return converted string
     *
     * @BigO: O(n)
     * **/
    private String convertObjectToString(String data){
        return this.convertObjectToString(data, true, false);
    }

    /**
     * Helper method that converts a data object to a string
     *
     * @param data data to be converted
     * @param uppercaseAll should every word be uppercase
     * @param isLink is data any link or url
     *
     * @return converted string
     *
     * @BigO: O(n)
     * **/
    private String convertObjectToString(String data, boolean uppercaseAll, boolean isLink) {
        String string = "";
        if(!data.isEmpty())
            string = data;

        if(uppercaseAll)
            return Utils.uppercase(Utils.removeForbiddenChars(string));
        else if(!isLink)
            return Utils.removeForbiddenChars(string);
        else
            return string;
    }

    /**
     * Helper method that joins director data with actor data because the director is part of the cast itself
     *
     * @param directorData director data
     * @param actorData actor data
     *
     * @return joined cast
     *
     * @BigO: O(n)
     * **/
    private Person[] getCast(String directorData, String actorData){
        ArrayList<Person> cast = new ArrayList<>();

        //Add directors
        if(directorData != null){
            if(!directorData.isEmpty())
                for(String director : directorData.split(", "))
                    cast.add(new Person(Utils.removeForbiddenChars(director), null, PersonRole.DIRECTOR));
        }

        //Add actors
        if(actorData != null){
            if(!actorData.isEmpty()){
                for(String actor : actorData.split(", "))
                    cast.add(new Person(Utils.removeForbiddenChars(actor), null, PersonRole.ACTOR));
            }
        }

        return cast.toArray(Person[]::new);
    }

    /**
     * Evaluates how many read-ins have been failed and passes this statistics to the frontend where it will be displayed in a dialog
     *
     * @BigO: O(n)
     * **/
    private void showSkipStats(){
        StringBuilder skippedStats = new StringBuilder();
        int totalSkips = 0;
        for (Map.Entry<String, Integer> entry : this.loadingSkips.entrySet()) {
            String formattedKey = Utils.uppercase(Utils.joinArray(entry.getKey().split("_"), " "));
            if(entry.getValue() != 0){
                skippedStats.append(formattedKey).append(": ").append(entry.getValue()).append('\n');
                totalSkips += entry.getValue();
            }
        }

        this.appModel.getAppView().getDialogHandler().showDialog(this.appModel.getTranslation("dialog.title.init.dataloading"),
                this.appModel.getTranslation("dialog.body.init.dataloading").replace("#", Integer.toString(totalSkips)) + "\n\n" + skippedStats,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Checks if the data length equals the column amount from file. For Netflix, Disney and Amazon only
     *
     * @return true when column counts are equal otherwise false
     *
     * @BigO: O(1)
     * **/
    private boolean isNDAFormattingValid(String[] data){
        return data.length == 12;
    }

    /**
     * Checks if the data length equals the column amount from file. For Apple Plus only
     *
     * @return true when column counts are equal otherwise false
     *
     * @BigO: O(1)
     * **/
    private boolean isApplePlusTitlesFormattingValid(String[] data){
        return data.length == 15;
    }

    /**
     * Checks if the data length equals the column amount from file. For Apple Plus Credits only
     *
     * @return true when column counts are equal otherwise false
     *
     * @BigO: O(1)
     * **/
    private boolean isApplePlusCreditsFormattingValid(String[] data){
        return data.length == 5;
    }

    /**
     * Checks if the data length equals the column amount from file. For Reviews only
     *
     * @return true when column counts are equal otherwise false
     *
     * @BigO: O(n)
     * **/
    private boolean isReviewFormattingValid(Object[] data){
        return data.length == 3 && !(data[1] instanceof ArrayList<?>) && Utils.isNumeric((String) data[1]);
    }

    /**
     * Checks if the data length equals the column amount from file. For Imdb Rating only
     *
     * @return true when column counts are equal otherwise false
     *
     * @BigO: O(n)
     * **/
    private boolean isImdbRatingFormattingValid(String[] data){
        return data.length == 16;
    }
}
