import mvc.AppController;

public class Main {
    public static void main(String[] args) {
        AppController appController = new AppController();

        appController.init();
        appController.run();
    }
}