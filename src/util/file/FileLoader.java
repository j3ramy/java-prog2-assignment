package util.file;

import mvc.AppModel;
import util.Util;
import util.data.Medium;
import util.data.Person;
import util.enums.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class FileLoader {
    private final AppModel appModel;

    private Person[] applePlusCredits;

    public FileLoader(AppModel appModel){
        this.appModel = appModel;
    }

    public void loadFiles(){
        this.loadCustomData();
        this.loadTranslations();

        new Thread(() -> {
            this.appModel.getAppView().setStatusBarText(AppState.LOAD);
            loadMediums();
            loadReviews();
            loadTop100();
            this.appModel.getAppView().setStatusBarText(AppState.READY);
        }).start();
    }

    private void loadCustomData(){
        try{
            //Check if file exists
            File file = new File(FilePath.CUSTOM_DATA_PATH);
            if(!file.exists()){
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
                Object[] data = Util.splitCsvLine(bufferedReader.readLine(), true);
                Util.removeQuotes(data);

                //Convert string providers into Provider list when providers are chosen (exist)
                ArrayList<String> providersAsStringList = (ArrayList<String>) data[1];
                ArrayList<Provider> providersList = new ArrayList<>();
                if(!providersAsStringList.isEmpty()){
                    for (String s : providersAsStringList)
                        if(Util.containsEnum(s, Provider.class))
                            providersList.add(Provider.valueOf(s));
                }

                //Set custom data and pass language and providers
                this.appModel.setCustomData(new CustomData(Language.valueOf(data[0].toString()), providersList.toArray(Provider[]::new)));
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
        this.appModel.setCustomData(new CustomData(Language.EN, new Provider[]{}));
    }

    private void loadTranslations(){
        try{
            //Check if file exists
            File file = new File(FilePath.TRANSLATIONS_PATH);
            if(!file.exists()){
                throw new FileNotFoundException();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //Split line and normalize it
                Object[] data = Util.splitCsvLine(line, false);
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
        this.loadMediumsFromProvider(FilePath.NETFLIX_TITLES_PATH, Provider.NETFLIX);
        this.loadMediumsFromProvider(FilePath.DISNEY_PLUS_TITLES_PATH, Provider.DISNEY_PLUS);
        this.loadMediumsFromProvider(FilePath.AMAZON_PRIME_TITLES_PATH, Provider.AMAZON_PRIME);

        this.loadApplePlusCredits();
        this.loadMediumsFromProvider(FilePath.APPLE_PLUS_TITLES_PATH, Provider.APPLE_PLUS);
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
                Object[] data = Util.splitCsvLine(line, false);
                Util.removeQuotes(data);

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

                this.appModel.getMediums().add(medium);
            }

            this.showSkippedMediumsDialog(provider, skipCounter);
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
        return new Medium(
                this.convertMediumType(data[1]),
                provider,
                this.convertTitles(data[2]),
                this.convertDescription(data[data.length - 1]),
                this.convertGenres(data[10]),
                (String) data[9],
                "N/A",
                (String) data[7],
                this.convertCast(data[3], data[4]),
                this.convertCountries(data[5]),
                (String) data[8],
                this.convertDateAdded(data[6])
        );
    }

    //Apple T: 0 id, 1 title, 2 type, 3 description, 4 release_year, 5 age_certification, 6 runtime, 7 genres, 8 production_countries, 9 seasons
    //Apple C: id,name,character,role
    private Medium createApplePlusMedium(Object[] data, Provider provider){
        MediumType mediumType = this.convertMediumType(data[2]);
        return new Medium(
                mediumType,
                provider,
                this.convertTitles(data[1]),
                this.convertDescription(data[3]),
                this.convertGenres(data[7]),
                (String) data[6],
                mediumType == MediumType.TVSHOW ? (String) data[9] : "N/A",
                (String) data[4],
                this.getApplePlusCastByMovie((String) data[0]),
                this.convertCountries(data[8]),
                (String) data[5],
                "N/A"
        );
    }

    private Person[] getApplePlusCastByMovie(String movieId){
        ArrayList<Person> cast = new ArrayList<>();
        if(this.applePlusCredits != null){
            for(Person person : this.applePlusCredits){
                if(person.getMovieId().equalsIgnoreCase(movieId))
                    cast.add(person);
            }
        }

        return cast.toArray(Person[]::new);
    }

    private void loadApplePlusCredits(){
        try{
            //Check if file exists
            File file = new File(FilePath.APPLE_PLUS_CREDITS_PATH);
            if(!file.exists()){
                throw new FileNotFoundException();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            ArrayList<Person> credits = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                //Split line and normalize it
                Object[] data = Util.splitCsvLine(line, false);

                if(!this.isApplePlusCreditsFormattingValid(data))
                    continue;

                if(Util.containsEnum((String) data[4], PersonRole.class)){
                    Person person = new Person(this.convertName(data[2]), this.convertName(data[3]), PersonRole.valueOf((String) data[4]));
                    person.setMovieId((String) data[1]);

                    credits.add(person);
                }
            }

            bufferedReader.close();

            this.applePlusCredits = credits.toArray(Person[]::new);
        }
        catch(Exception e){
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReviews(){

    }

    private void loadTop100(){

    }

    //TODO: Sum up convert methods, that do the same
    private MediumType convertMediumType(Object data){
        MediumType mediumType = MediumType.TVSHOW;
        if(Util.containsEnum((String) data, MediumType.class)){
            mediumType = MediumType.valueOf(((String) data).toUpperCase().replace(" ", ""));
        }

        return mediumType;
    }

    private String convertTitles(Object data){
        String titles;
        if(data instanceof ArrayList<?>)
            titles = Util.joinArray((ArrayList<String>) data, "/");
        else
            titles = (String) data;

        return titles;
    }

    private String convertDescription(Object data){
        String description;
        if(data instanceof ArrayList<?>)
            description = Util.joinArray((ArrayList<String>) data);
        else
            description = (String) data;

        return description;
    }

    private String convertGenres(Object data){
        String genres;
        if(data instanceof ArrayList<?>)
            genres = Util.joinArray((ArrayList<String>) data, ", ");
        else
            genres = (String) data;

        return genres;
    }

    private String convertCountries(Object data){
        String countries;
        if(data instanceof ArrayList<?>)
            countries = Util.joinArray((ArrayList<String>) data, ", ");
        else
            countries = (String) data;

        return countries;
    }

    private String convertDateAdded(Object data){
        String dateAdded;
        if(data instanceof ArrayList<?>)
            dateAdded = Util.joinArray((ArrayList<String>) data, ", ");
        else
            dateAdded = (String) data;

        return dateAdded;
    }

    private Person[] convertCast(Object directorData, Object actorData){
        ArrayList<Person> cast = new ArrayList<>();

        //Add directors
        if(directorData instanceof ArrayList<?>){
            for(String s : (ArrayList<String>)directorData){
                cast.add(new Person(s, null, PersonRole.DIRECTOR));
            }
        }
        else
            cast.add(new Person((String) directorData, null, PersonRole.DIRECTOR));

        //Add actors
        if(actorData instanceof ArrayList<?>){
            for(String s : (ArrayList<String>)actorData){
                cast.add(new Person(s, null, PersonRole.ACTOR));
            }
        }
        else
            cast.add(new Person((String) actorData, null, PersonRole.ACTOR));

        return cast.toArray(Person[]::new);
    }

    private String convertName(Object data){
        String character;
        if(data instanceof ArrayList<?>){
            character = Util.joinArray((ArrayList<String>) data, " ");
        }
        else{
            character = (String) data;
        }

        return character;
    }

    //TODO: Show skipped counter of all provider in one dialog
    private void showSkippedMediumsDialog(Provider provider, int skipCounter){
        this.appModel.getAppView().showDialog(this.appModel.getTranslation("dialog.title.init.dataloading"),
                skipCounter + " " + Util.capitalize(provider.name(), "_") + this.appModel.getTranslation("dialog.body.init.dataloading"),
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

    private final String[] blacklist = new String[]{"s7384"};
    private boolean isBlacklisted(String id){
        for(String s : blacklist)
            if(s.equalsIgnoreCase(id))
                return true;

        return false;
    }
}
