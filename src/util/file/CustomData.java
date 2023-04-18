package util.file;

import util.enums.Language;
import util.enums.Provider;

public class CustomData {
    private Language language;
    private Provider[] providers;

    /**
     * Sets language
     * @param language new language
     *
     * @BigO: O(1)
     * **/
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Gets language
     * @return  language
     *
     * @BigO: O(1)
     * **/
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets providers
     * @param providers providers
     *
     * @BigO: O(1)
     * **/
    public void setProviders(Provider[] providers) {
        this.providers = providers;
    }

    /**
     * Gets providers
     * @return  providers
     *
     * @BigO: O(1)
     * **/
    public Provider[] getProviders() {
        return providers;
    }

    public CustomData(Language language, Provider[] providers){
        this.language = language;
        this.providers = providers;
    }

    /**
     * @return if custom data contains providers
     *
     * @BigO: O(n)
     * **/
    public boolean hasProvider(Provider provider){
        for(Provider p : this.providers)
            if(p == provider) return true;

        return false;
    }
}
