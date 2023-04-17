package mvc.view.util;

import javax.swing.*;
import java.awt.event.ActionListener;
import mvc.AppView;

public class DialogHandler {

    private final AppView appView;

    public DialogHandler(AppView appview){
        this.appView = appview;
    }

    public void showDialog(String title, String message, int dialogType) {
        JOptionPane.showMessageDialog(this.appView, message, title, dialogType);
    }

    public void showFirstAppStartDialog(){
        this.showDialog(this.appView.getAppController().getAppModel().getTranslation("dialog.title.main.no_settings"),
                this.appView.getAppController().getAppModel().getTranslation("dialog.body.main.no_settings"), JOptionPane.QUESTION_MESSAGE);

        this.appView.getTabbedPane().setSelectedIndex(this.appView.getTabbedPane().getTabCount() - 1);
    }

    public void showCloseAppDialog(){
        this.showDialog(this.appView.getAppController().getAppModel().getTranslation("dialog.title.main.close"),
                this.appView.getAppController().getAppModel().getTranslation("dialog.body.main.close"), JOptionPane.INFORMATION_MESSAGE);

        this.appView.getAppController().close();
    }

    public void showSaveSettingsDialog(){
        int input = JOptionPane.showConfirmDialog(this.appView, this.appView.getAppController().getAppModel().getTranslation("dialog.body.settings.save"),
                this.appView.getAppController().getAppModel().getTranslation("dialog.title.settings.save"), JOptionPane.YES_NO_OPTION);

        if(input == 0){
            this.showCloseAppDialog();
        }
    }

    public void showNoMediumFoundDialog(){
        this.showDialog(this.appView.getAppController().getAppModel().getTranslation("dialog.title.main.no_result"),
                this.appView.getAppController().getAppModel().getTranslation("dialog.body.main.no_result"), JOptionPane.ERROR_MESSAGE);
    }

    public void showAcceptRecommendationDialog(ActionListener confirmAction, ActionListener rejectAction, int currentIndex, int mediumCount){
        this.showAcceptRecommendationDialog(confirmAction, rejectAction, currentIndex, mediumCount, "");
    }

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
