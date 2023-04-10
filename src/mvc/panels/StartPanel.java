package mvc.panels;

import mvc.AppModel;
import mvc.AppView;
import util.file.FilePath;
import util.interfaces.IViewPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StartPanel extends JPanel implements IViewPanel {
    private final AppView appView;

    private JLabel contentLabel, image;

    public StartPanel(AppView appView){
        this.appView = appView;
    }

    //Initialize all class components inside this panel
    @Override
    public void init(){
        this.initComponents();
        this.initStyles();
        this.initImages();
        this.initActionListeners();
    }

    @Override
    public void initComponents() {
        //Set layout
        this.setLayout(new BorderLayout());

        this.contentLabel = new JLabel();
        this.add(this.contentLabel, BorderLayout.NORTH); //Attach component panel on left side

        this.image = new JLabel();
        this.add(this.image, BorderLayout.CENTER);
    }

    @Override
    public void initStyles() {
        this.contentLabel.setBorder(new EmptyBorder(20, 0, 0, 0));
        this.contentLabel.setFont(new Font(null, Font.PLAIN, 14));
        this.contentLabel.setHorizontalAlignment(JLabel.CENTER);

        this.image.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public void initImages() {
        try {
            ImageIcon imageIcon = new ImageIcon(FilePath.POPCORN_IMAGE_PATH);
            Image image = imageIcon.getImage().getScaledInstance(281, 375,  Image.SCALE_SMOOTH);
            this.image.setIcon(new ImageIcon(image));
        }
        catch (Exception e){
            this.appView.showDialog("Error", "An error occurred:\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void initActionListeners(){

    }

    @Override
    public void setTranslations(){
        AppModel appModel = this.appView.getAppController().getAppModel();

        this.contentLabel.setText(appModel.getTranslation("label.main.start.content"));
    }
}
