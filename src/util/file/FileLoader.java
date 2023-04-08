package util.file;

import mvc.AppModel;
import util.Util;
import util.enums.Language;
import util.enums.Provider;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class FileLoader {
    private final AppModel appModel;

    public FileLoader(AppModel appModel){
        this.appModel = appModel;
    }

    public void loadFiles(){
        this.loadCustomData();
        this.loadTranslations();

        new Thread(() -> {
            loadMediums();
            loadReviews();
            loadTop100();
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

            String line = "";
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

    }

    private void loadReviews(){

    }

    private void loadTop100(){

    }
}
