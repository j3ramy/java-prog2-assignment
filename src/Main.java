import mvc.AppController;

public class Main {
    /**
     * Entry point of this program
     *
     * @param args arguments passed by the system
     *
     * @BigO: O(n)
     * **/
    public static void main(String[] args) {
        //Create instance of app controller as an entry point for the MVC Pattern and init
        AppController appController = new AppController();
        appController.init();
    }
}