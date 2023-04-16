package util.file;

import mvc.AppModel;
import util.Utils;
import util.data.*;
import util.enums.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class FileLoader {
    private final AppModel appModel;
    private final LinkedHashMap<String, Integer> loadingSkips = new LinkedHashMap<>();

    public FileLoader(AppModel appModel){
        this.appModel = appModel;
    }

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
                    this.appModel.getAppView().showNoMediumFoundDialog();
                }

                this.appModel.getAppView().setStatusBarText(LoadingState.READY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void loadCustomData(){
        try{
            //Check if file exists
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
                this.appModel.getAppView().showFirstAppStartDialog();
            }
            else{
                //Split line and normalize it
                Object[] data = Utils.splitCsvLine(bufferedReader.readLine(), true);
                Utils.removeQuotes(data);

                //Convert string providers into Provider list when providers are chosen (exist)
                ArrayList<String> providersAsStringList = (ArrayList<String>) data[1];
                ArrayList<Provider> providersList = new ArrayList<>();
                if(!providersAsStringList.isEmpty()){
                    for (String s : providersAsStringList)
                        if(Utils.containsEnumValue(s, Provider.class))
                            providersList.add(Provider.valueOf(s));
                }

                //Set custom data and pass language and providers
                this.appModel.setCustomData(new CustomData(Language.valueOf(data[0].toString()), providersList.toArray(Provider[]::new)));
                this.loadTranslations();
            }

            bufferedReader.close();

            //Tell the view to load the custom data settings
            this.appModel.getAppView().setSettings();
        }
        catch(Exception e){
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createDefaultCustomData(){
        this.appModel.setCustomData(new CustomData(Language.EN, Provider.values()));
    }

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
                //Split line and normalize it
                Object[] data = Utils.splitCsvLine(line, false);
                //Add it to the translation hash map
                this.appModel.getTranslations().put(data[0].toString(), new Translation(data[1].toString(), data[2].toString()));
            }

            bufferedReader.close();

            //Tell the view to load the correct translations
            this.appModel.getAppView().setAllTranslations();
        }
        catch(Exception e){
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    //Total records data source: 20.118
    //Total records loaded: 19.640; difference because Netflix (154), DP (33), Amazon (287), Apple (4) haven't been loaded
    private void loadMediums(){
        if(this.appModel.getCustomData().hasProvider(Provider.NETFLIX)) this.loadMediumsFromProvider(FilePaths.NETFLIX_TITLES_PATH, Provider.NETFLIX);
        if(this.appModel.getCustomData().hasProvider(Provider.DISNEY_PLUS)) this.loadMediumsFromProvider(FilePaths.DISNEY_PLUS_TITLES_PATH, Provider.DISNEY_PLUS);
        if(this.appModel.getCustomData().hasProvider(Provider.AMAZON_PRIME)) this.loadMediumsFromProvider(FilePaths.AMAZON_PRIME_TITLES_PATH, Provider.AMAZON_PRIME);

        if(this.appModel.getCustomData().hasProvider(Provider.APPLE_PLUS)){
            this.loadApplePlusCredits();
            this.loadMediumsFromProvider(FilePaths.APPLE_PLUS_TITLES_PATH, Provider.APPLE_PLUS);
        }
    }

    private void loadMediumsFromProvider(String path, Provider provider){
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
                //Split line and normalize it
                Object[] data = Utils.splitCsvLine(line, false);
                Utils.removeQuotes(data);

                Medium medium;
                if(provider != Provider.APPLE_PLUS){
                    //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                    if(!this.isNDAFormattingValid(data) || this.isBlacklisted((String) data[0])){
                        skipCounter++;
                        continue;
                    }

                    medium = this.createNDAMedium(data, provider);
                }
                else{
                    //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                    if(!this.isApplePlusTitlesFormattingValid(data) || this.isBlacklisted((String) data[0])){
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
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }
    }

    //Netflix: 1 type, 2 title, 3 director, 4 cast, 5 country, 6 date_added, 7 release_year, 8 rating, 9 duration, 10 listed_in, 11 description
    //Disney: type,title,director,cast,country,date_added,release_year,rating,duration,listed_in,description
    //Amazon: type,title,director,cast,country,date_added,release_year,rating,duration,listed_in,description
    private Medium createNDAMedium(Object[] data, Provider provider){
        MediumType mediumType = this.convertMediumType((String) data[1]);
        return new Medium(
                (String) data[0],
                mediumType,
                provider,
                this.convertObjectToString(data[2], "/"),
                this.convertObjectToString(data[data.length - 1], ", ", false, false),
                this.convertObjectToString(data[10], ", "),
                mediumType == MediumType.MOVIE ? (String) data[9] : "",
                mediumType == MediumType.SHOW ? (String) data[9] : "",
                (String) data[7],
                this.getCast(data[3], data[4]),
                this.convertObjectToString(data[5], ", "),
                (String) data[8],
                this.convertObjectToString(data[6], ", ")
        );
    }

    //Apple T: 0 id, 1 title, 2 type, 3 description, 4 release_year, 5 age_certification, 6 runtime, 7 genres, 8 production_countries, 9 seasons
    //Apple C: id,name,character,role
    private Medium createApplePlusMedium(Object[] data, Provider provider){
        MediumType mediumType = this.convertMediumType((String) data[2]);
        return new Medium(
                (String) data[0],
                mediumType,
                provider,
                this.convertObjectToString(data[1], "/"),
                this.convertObjectToString(data[3], ", ", false, false),
                this.convertObjectToString(data[7], ", "),
                (String) data[6],
                mediumType == MediumType.SHOW ? (String) data[9] : "",
                (String) data[4],
                this.getApplePlusCastByMovie((String) data[0]),
                this.convertObjectToString(data[8], ", "),
                (String) data[5],
                ""
        );
    }

    private Person[] getApplePlusCastByMovie(String movieId){
        ArrayList<Person> cast = new ArrayList<>();
        for(Person person : this.loadApplePlusCredits()){
            if(person.getMovieId().equalsIgnoreCase(movieId))
                cast.add(person);
        }

        return cast.toArray(Person[]::new);
    }

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
                //Split line and normalize it
                Object[] data = Utils.splitCsvLine(line, false);

                if(!this.isApplePlusCreditsFormattingValid(data)){
                    skipCounter++;
                    continue;
                }

                if(Utils.containsEnumValue((String) data[4], PersonRole.class)){
                    Person person = new Person(this.convertObjectToString(data[2], " "), this.convertObjectToString(data[3], " "), PersonRole.valueOf((String) data[4]));
                    person.setMovieId((String) data[1]);

                    credits.add(person);
                }
            }

            bufferedReader.close();

            this.loadingSkips.put("APPLE_PLUS_CREDITS", skipCounter);
            return credits.toArray(Person[]::new);
        }
        catch(Exception e){
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }

        return new Person[]{};
    }

    private void loadReviews(){
        this.loadReviewsByType(FilePaths.CRITIC_REVIEWS_PATH, ReviewType.CRITICS);
        this.loadReviewsByType(FilePaths.AUDIENCE_REVIEWS_PATH, ReviewType.AUDIENCE);
    }

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
                //Split line and normalize it
                Object[] data = Utils.splitCsvLine(line, false);

                //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                if(!this.isReviewFormattingValid(data)){
                    skipCounter++;
                    continue;
                }

                String title = data[0] instanceof ArrayList<?> ? Utils.joinList((List<?>) data[0], ", ") : (String) data[0];
                if(!this.appModel.isTitleInMediums(title)){
                    continue;
                }

                Review review;
                if(type == ReviewType.CRITICS){
                    boolean sentiment = !data[1].equals("0");
                    review = new CriticReview(this.convertObjectToString(data[0], "/"), this.convertObjectToString(data[2], " ", false, false),
                            sentiment);
                }
                else{
                    review = new AudienceReview(this.convertObjectToString(data[0], "/"), this.convertObjectToString(data[2], " ", false, false),
                             Float.parseFloat((String) data[1]));
                }

                this.appModel.getReviews().add(review);
            }

            this.loadingSkips.put(type.name(), skipCounter);
            bufferedReader.close();
        }
        catch(Exception e){
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }
    }

    // 0 Poster_Link, 1 Series_Title, 2 Released_Year, 3 Certificate, 4 Runtime, 5 Genre, 6 IMDB_Rating, 7 Overview, 8 Meta_score, 9 Director, 10 Star1,
    // 11 Star2, 12 Star3, 13 Star4, 14 No_of_Votes, 15 Gross
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
                Object[] data = Utils.splitCsvLine(line, false);
                Utils.removeQuotes(data);

                //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                if(!this.isImdbRatingFormattingValid(data)){
                    skipCounter++;
                    continue;
                }

                String title = data[1] instanceof ArrayList<?> ? Utils.joinList((List<?>) data[1], ", ") : (String) data[1];
                if(!this.appModel.isTitleInMediums(title)){
                    continue;
                }

                Medium imdbMedium = this.appModel.getMediumByTitle(title);
                if(imdbMedium != null){
                    ArrayList<String> starsAsString = new ArrayList<>();
                    starsAsString.add(this.convertObjectToString(data[10], " "));
                    starsAsString.add(this.convertObjectToString(data[11], " "));
                    starsAsString.add(this.convertObjectToString(data[12], " "));
                    starsAsString.add(this.convertObjectToString(data[13], " "));

                    ImdbRating rating = new ImdbRating(
                            imdbMedium,
                            this.convertObjectToString(data[0], ",", false, true),
                            ((String) data[6]).isBlank() ? 0f : Float.parseFloat((String) data[6]),
                            ((String) data[8]).isBlank() ? 0 : Integer.parseInt((String) data[8]),
                            ((String) data[14]).isBlank() ? 0 : Integer.parseInt((String) data[14]),
                            Integer.parseInt(this.convertObjectToString(data[15], "")),
                            this.getCast(null, starsAsString)
                            );

                    this.appModel.getImdbRatings().add(rating);
                }
            }

            this.loadingSkips.put("IMDB_RATING", skipCounter);
            bufferedReader.close();
        }
        catch(Exception e){
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }
    }

    private MediumType convertMediumType(String type){
        return type.toLowerCase().contains("show") ? MediumType.SHOW : MediumType.MOVIE;
    }

    private String convertObjectToString(Object data, String separator){
        return this.convertObjectToString(data, separator, true, false);
    }

    private String convertObjectToString(Object data, String separator, boolean uppercaseAll, boolean isLink) {
        String string = "";
        if(data instanceof ArrayList<?>)
            string = Utils.joinList((List<?>) data, separator);
        else if(!((String) data).isEmpty())
            string = (String) data;

        if(uppercaseAll)
            return Utils.uppercaseAll(Utils.removeForbiddenChars(string));
        else if(!isLink)
            return Utils.removeForbiddenChars(string);
        else
            return string;
    }

    private Person[] getCast(Object directorData, Object actorData){
        ArrayList<Person> cast = new ArrayList<>();

        //Add directors
        if(directorData != null){
            if(directorData instanceof ArrayList<?>){
                for(String s : (ArrayList<String>)directorData){
                    cast.add(new Person(s, null, PersonRole.DIRECTOR));
                }
            }
            else if(!((String) directorData).isEmpty())
                cast.add(new Person((String) directorData, null, PersonRole.DIRECTOR));
        }

        //Add actors
        if(actorData != null){
            if(actorData instanceof ArrayList<?>){
                for(String s : (ArrayList<String>)actorData){
                    cast.add(new Person(s, null, PersonRole.ACTOR));
                }
            }
            else if(!((String) actorData).isEmpty()){
                cast.add(new Person((String) actorData, null, PersonRole.ACTOR));
            }
        }

        return cast.toArray(Person[]::new);
    }

    private void showSkipStats(){
        StringBuilder skippedStats = new StringBuilder();
        int totalSkips = 0;
        for (Map.Entry<String, Integer> entry : this.loadingSkips.entrySet()) {
            String formattedKey = Utils.uppercaseAll(Utils.joinArray(entry.getKey().split("_"), " "));
            skippedStats.append(formattedKey).append(": ").append(entry.getValue()).append('\n');
            totalSkips += entry.getValue();
        }

        this.appModel.getAppView().showDialog(this.appModel.getTranslation("dialog.title.init.dataloading"),
                this.appModel.getTranslation("dialog.body.init.dataloading").replace("#", Integer.toString(totalSkips)) + "\n\n" + skippedStats,
                JOptionPane.INFORMATION_MESSAGE);
    }

    //NDA = Netflix, Disney Plus, Amazon Prime
    private boolean isNDAFormattingValid(Object[] data){
        return data.length == 12;
    }

    private boolean isApplePlusTitlesFormattingValid(Object[] data){
        return data.length == 15;
    }

    private boolean isApplePlusCreditsFormattingValid(Object[] data){
        return data.length == 5;
    }

    private boolean isReviewFormattingValid(Object[] data){
        return data.length == 3 && !(data[1] instanceof ArrayList<?>) && Utils.isNumeric((String) data[1]);
    }

    private boolean isImdbRatingFormattingValid(Object[] data){
        return data.length == 16;
    }

    private final String[] blacklist = new String[]{"s7384"};
    private boolean isBlacklisted(String id){
        for(String s : blacklist)
            if(s.equalsIgnoreCase(id))
                return true;

        return false;
    }
}
