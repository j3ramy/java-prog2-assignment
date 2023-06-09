package mvc.view.util;

import javax.swing.*;
import java.awt.event.ActionListener;
import mvc.AppView;

public class DialogHandler {

    private final AppView appView;

    public DialogHandler(AppView appview){
        this.appView = appview;
    }

    /**
     * Shows JOptionPane dialog
     *
     * @param title title of the dialog window
     * @param message message of the dialog window
     * @param dialogType type of the dialog from JOptionPane
     *
     * @BigO: O(1)
     * **/
    public void showDialog(String title, String message, int dialogType) {
        JOptionPane.showMessageDialog(this.appView, message, title, dialogType);
    }

    /**
     * Shows JOptionPane dialog when app is started the first time for advice to set the settings
     *
     * @BigO: O(n)
     * **/
    public void showFirstAppStartDialog(){
        this.showDialog(this.appView.getAppController().getAppModel().getTranslation("dialog.title.main.no_settings"),
                this.appView.getAppController().getAppModel().getTranslation("dialog.body.main.no_settings"), JOptionPane.QUESTION_MESSAGE);

        this.appView.getTabbedPane().setSelectedIndex(this.appView.getTabbedPane().getTabCount() - 1);
    }

    /**
     * Shows JOptionPane dialog when app closes
     *
     * @BigO: O(n)
     * **/
    public void showCloseAppDialog(){
        this.showDialog(this.appView.getAppController().getAppModel().getTranslation("dialog.title.main.close"),
                this.appView.getAppController().getAppModel().getTranslation("dialog.body.main.close"), JOptionPane.INFORMATION_MESSAGE);

        this.appView.getAppController().close();
    }

    /**
     * Shows JOptionPane dialog when settings are saved with the advice to restart the app
     *
     * @BigO: O(n)
     * **/
    public void showSaveSettingsDialog(){
        int input = JOptionPane.showConfirmDialog(this.appView, this.appView.getAppController().getAppModel().getTranslation("dialog.body.settings.save"),
                this.appView.getAppController().getAppModel().getTranslation("dialog.title.settings.save"), JOptionPane.YES_NO_OPTION);

        if(input == 0){
            this.showCloseAppDialog();
        }
    }

    /**
     * Shows JOptionPane dialog that informs the user that no medium has been found
     *
     * @BigO: O(1)
     * **/
    public void showNoMediumFoundDialog(){
        this.showDialog(this.appView.getAppController().getAppModel().getTranslation("dialog.title.main.no_result"),
                this.appView.getAppController().getAppModel().getTranslation("dialog.body.main.no_result"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows JOptionPane dialog where the user can decide whether to accept or reject the recommendation
     *
     * @param confirmAction action interface that will be executed when the user presses 'Yes'
     * @param rejectAction action interface that will be executed when the user presses 'No'
     * @param currentIndex the current list index of the current search for displaying how far the user is
     * @param mediumCount the max list index or medium count for displaying how many results in total exist
     *
     * @BigO: O(1)
     * **/
    public void showAcceptRecommendationDialog(ActionListener confirmAction, ActionListener rejectAction, int currentIndex, int mediumCount){
        this.showAcceptRecommendationDialog(confirmAction, rejectAction, currentIndex, mediumCount, "");
    }

    /**
     * Shows JOptionPane dialog where the user can decide whether to accept or reject the recommendation
     *
     * @param confirmAction action interface that will be executed when the user presses 'Yes'
     * @param rejectAction action interface that will be executed when the user presses 'No'
     * @param currentIndex the current list index of the current search for displaying how far the user is
     * @param mediumCount the max list index or medium count for displaying how many results in total exist
     * @param addable addable text that will be added in the end of the dialog title
     *
     * @BigO: O(1)
     * **/
    public void showAcceptRecommendationDialog(ActionListener confirmAction, ActionListener rejectAction, int currentIndex, int mediumCount, String addable){
        String title = this.appView.getAppController().getAppModel().getTranslation("dialog.title.recommendation").
                replace("#", Integer.toString(currentIndex)).replace("+", Integer.toString(mediumCount)) + addable;

        int input = JOptionPane.showConfirmDialog(this.appView, this.appView.getAppController().getAppModel().getTranslation("dialog.body.recommendation"),
                title, JOptionPane.YES_NO_CANCEL_OPTION);

        switch (input) {
            case 0 -> confirmAction.actionPerformed(null);
            case 1 -> rejectAction.actionPerformed(null);
        }
    }
}
