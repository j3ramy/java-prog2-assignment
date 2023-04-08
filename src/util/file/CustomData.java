package util.file;

import util.enums.Language;
import util.enums.Provider;

import java.util.ArrayList;

public class CustomData {
    private Language language;
    private Provider[] providers;

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public void setProviders(Provider[] providers) {
        this.providers = providers;
    }

    public Provider[] getProviders() {
        return providers;
    }

    public String[] getProvidersAsString(){
        ArrayList<String> providers = new ArrayList<>();
        for(Provider provider : this.providers)
            providers.add(provider.name());

        return providers.toArray(String[]::new);
    }

    public CustomData(Language language, Provider[] providers){
        this.language = language;
        this.providers = providers;
    }

    public boolean isSet(){
        return this.providers != null;
    }
}
