package util.file;

import mvc.AppModel;
import util.Utils;

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

    /**
     * Saves the custom data to file. Will be called after pressing the save button in the settings tab
     *
     * @BigO: O(n)
     * **/
    public void saveCustomData(){
        try{
            File file = new File(FilePaths.CUSTOM_DATA_PATH);
            if(!file.exists() && !file.createNewFile()){
                throw new FileAlreadyExistsException(file.getName());
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            bufferedWriter.write(this.appModel.getCustomData().getLanguage() + "," +
                    Utils.convertArrayToCsvString(this.appModel.getCustomData().getProviders()));
            bufferedWriter.close();
        }
        catch(Exception e){
            this.appModel.getAppView().getDialogHandler().showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }
}
