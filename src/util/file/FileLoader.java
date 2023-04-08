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

    private void loadMediums(){
        this.loadMediumsFromProvider(FilePath.NETFLIX_TITLES_PATH, Provider.NETFLIX);
        this.loadMediumsFromProvider(FilePath.DISNEY_PLUS_TITLES_PATH, Provider.DISNEY_PLUS);
        this.loadMediumsFromProvider(FilePath.AMAZON_PRIME_TITLES_PATH, Provider.AMAZON_PRIME);
    }

    //Netflix: 1 type, 2 title, 3 director, 4 cast, 5 country, 6 date_added, 7 release_year, 8 rating, 9 duration, 10 listed_in, 11 description
    //Disney: type,title,director,cast,country,date_added,release_year,rating,duration,listed_in,description
    //Amazon: type,title,director,cast,country,date_added,release_year,rating,duration,listed_in,description

    //Apple T: id,title,type,description,release_year,age_certification,runtime,genres,production_countries,seasons
    //Apple C: id,name,character,role

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

                //Check if data/line is well formatted regarding the default column amount, otherwise skip it
                if(!this.isFormattingValid(data) || this.isBlacklisted((String) data[0])){
                    skipCounter++;
                    continue;
                }

                Medium medium = new Medium(
                        this.convertMediumType(data),
                        provider,
                        this.convertTitles(data),
                        this.convertDescription(data),
                        this.convertGenres(data),
                        (String) data[9],
                        (String) data[7],
                        this.convertCast(data),
                        this.convertCountries(data),
                        (String) data[8],
                        this.convertDateAdded(data)
                );

                this.appModel.getMediums().put(medium.getTitle(), medium);
            }

            this.showSkippedMediumsDialog(provider, skipCounter);
            bufferedReader.close();
        }
        catch(Exception e){
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e + Arrays.toString(e.getStackTrace()), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReviews(){

    }

    private void loadTop100(){

    }

    private MediumType convertMediumType(Object[] data){
        MediumType mediumType = MediumType.TVSHOW;
        if(Util.containsEnum((String) data[1], MediumType.class)){
            mediumType = MediumType.valueOf(((String) data[1]).toUpperCase().replace(" ", ""));
        }

        return mediumType;
    }

    private String convertTitles(Object[] data){
        String titles;
        if(data[2] instanceof ArrayList<?>)
            titles = Util.joinArray((ArrayList<String>) data[2], "/");
        else
            titles = (String) data[2];

        return titles;
    }

    private String convertDescription(Object[] data){
        String description;
        if(data[data.length - 1] instanceof ArrayList<?>)
            description = Util.joinArray((ArrayList<String>) data[data.length - 1]);
        else
            description = (String) data[data.length - 1];

        return description;
    }

    private String convertGenres(Object[] data){
        String genres;
        if(data[10] instanceof ArrayList<?>)
            genres = Util.joinArray((ArrayList<String>) data[10], ", ");
        else
            genres = (String) data[10];

        return genres;
    }

    private String convertCountries(Object[] data){
        String countries;
        if(data[5] instanceof ArrayList<?>)
            countries = Util.joinArray((ArrayList<String>) data[5], ", ");
        else
            countries = (String) data[5];

        return countries;
    }

    private String convertDateAdded(Object[] data){
        String dateAdded;
        if(data[6] instanceof ArrayList<?>)
            dateAdded = Util.joinArray((ArrayList<String>) data[6], ", ");
        else
            dateAdded = (String) data[6];

        return dateAdded;
    }

    private Person[] convertCast(Object[] data){
        ArrayList<Person> cast = new ArrayList<>();

        //Add directors
        if(data[3] instanceof ArrayList<?>){
            for(String s : (ArrayList<String>)data[3]){
                cast.add(new Person(s, null, PersonRole.DIRECTOR));
            }
        }
        else
            cast.add(new Person((String) data[3], null, PersonRole.DIRECTOR));

        //Add actors
        if(data[4] instanceof ArrayList<?>){
            for(String s : (ArrayList<String>)data[4]){
                cast.add(new Person(s, null, PersonRole.ACTOR));
            }
        }
        else
            cast.add(new Person((String) data[4], null, PersonRole.ACTOR));

        return cast.toArray(Person[]::new);
    }

    private void showSkippedMediumsDialog(Provider provider, int skipCounter){
        this.appModel.getAppView().showDialog(this.appModel.getTranslation("dialog.title.init.dataloading"),
                skipCounter + " " + Util.capitalize(provider.name(), "_") + this.appModel.getTranslation("dialog.body.init.dataloading"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isFormattingValid(Object[] data){
        return data.length == 12;
    }

    private final String[] blacklist = new String[]{"s7384"};
    private boolean isBlacklisted(String id){
        for(String s : blacklist)
            if(s.equalsIgnoreCase(id))
                return true;

        return false;
    }
}
