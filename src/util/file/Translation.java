package util.file;

public class Translation {
    private final String translationDe, translationEn;

    /**
     * Gets german translation
     * @return  german translation string
     *
     * @BigO: O(1)
     * **/
    public String getTranslationDe() {
        return translationDe;
    }

    /**
     * Gets english translation
     * @return  english translation string
     *
     * @BigO: O(1)
     * **/
    public String getTranslationEn() {
        return translationEn;
    }

    public Translation(String translationDe, String translationEn){
         this.translationDe = translationDe;
         this.translationEn = translationEn;
     }
}
