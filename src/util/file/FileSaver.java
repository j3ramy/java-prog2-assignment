package util.file;

import mvc.AppModel;
import util.Util;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.FileAlreadyExistsException;

public class FileSaver {
    private final AppModel appModel;

    public FileSaver(AppModel appModel){
        this.appModel = appModel;
    }

    public void saveCustomData(){
        try{
            File file = new File(FilePath.CUSTOM_DATA_PATH);
            if(!file.exists() && !file.createNewFile()){
                throw new FileAlreadyExistsException(file.getName());
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            bufferedWriter.write(this.appModel.getCustomData().getLanguage() + "," +
                    Util.getArrayAsCsvString(this.appModel.getCustomData().getProvidersAsString()));
            bufferedWriter.close();
        }
        catch(Exception e){
            this.appModel.getAppView().showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }
}
