package util.data;

import util.Utils;
import util.enums.MediumType;
import util.enums.Provider;

import java.util.ArrayList;
import java.util.List;

public class Medium {
    private final MediumType type;
    private final List<Provider> providers = new ArrayList<>();
    private final String id, title, description, ageRating, addedAt, genres, countries;
    private final int releaseYear, seasons, duration;
    private final Person[] cast;

    private float averageRating = 0f;

    /**
     * Gets id
     * @return  id
     *
     * @BigO: O(1)
     * **/
    public String getId() {
        return id;
    }

    /**
     * Gets type
     * @return  type
     *
     * @BigO: O(1)
     * **/
    public MediumType getType() {
        return type;
    }

    /**
     * Gets providers
     * @return  providers
     *
     * @BigO: O(1)
     * **/
    public List<Provider> getProviders() {
        return providers;
    }

    /**
     * Gets title
     * @return  title
     *
     * @BigO: O(1)
     * **/
    public String getTitle() {
        return title;
    }

    /**
     * Gets description
     * @return  description
     *
     * @BigO: O(1)
     * **/
    public String getDescription() {
        return description;
    }

    /**
     * Gets genres
     * @return  genres
     *
     * @BigO: O(1)
     * **/
    public String getGenres() {
        return genres;
    }

    /**
     * Gets duration
     * @return  duration
     *
     * @BigO: O(1)
     * **/
    public int getDuration() {
        return duration;
    }

    /**
     * Gets release year
     * @return  release year
     *
     * @BigO: O(1)
     * **/
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Gets cast
     * @return cast
     *
     * @BigO: O(1)
     * **/
    public Person[] getCast() {
        return cast;
    }

    /**
     * Gets countries
     * @return  countries
     *
     * @BigO: O(1)
     * **/
    public String getCountries() {
        return countries;
    }

    /**
     * Gets age rating
     * @return  age rating
     *
     * @BigO: O(1)
     * **/
    public String getAgeRating() {
        return ageRating;
    }

    /**
     * Gets added at
     * @return  added at
     *
     * @BigO: O(1)
     * **/
    public String getAddedAt() {
        return addedAt;
    }

    /**
     * Gets seasons
     * @return  seasons
     *
     * @BigO: O(1)
     * **/
    public int getSeasons() {
        return seasons;
    }

    /**
     * Gets average rating
     * @return  average rating
     *
     * @BigO: O(1)
     * **/
    public float getAverageRating() {
        return averageRating;
    }

    /**
     * Sets average rating for this medium
     * @param averageRating new language
     *
     * @BigO: O(1)
     * **/
    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public Medium(String id, MediumType type, Provider provider, String title, String description, String genres, int duration, int seasons,
                  String releaseYear, Person[] cast, String countries, String ageRating, String addedAt){
        this.id = id;
        this.type = type;
        this.providers.add(provider);
        this.title = title.isEmpty() ? "N/A" : title;
        this.description = description.isEmpty() ? "N/A" : description;
        this.genres = genres.isEmpty() ? "N/A" : genres;
        this.duration = duration;
        this.seasons = seasons;
        this.releaseYear = !Utils.isNumeric(releaseYear) ? 0 : Integer.parseInt(releaseYear);
        this.cast = cast == null || cast.length == 0 ? new Person[]{new Person("N/A", null, null)} : cast;
        this.countries = countries.isEmpty() ? "N/A" : countries;
        this.ageRating = ageRating.isEmpty() ? "N/A" : ageRating;
        this.addedAt = addedAt.isEmpty() ? "N/A" : addedAt;
    }
}
