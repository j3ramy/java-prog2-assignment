package util.data;

import util.enums.MediumType;
import util.enums.Provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Medium {
    private final MediumType type;
    private final List<Provider> providers = new ArrayList<>();
    private final String id, title, description, ageRating, duration, releaseYear, addedAt, genres, countries, seasons;
    private final Person[] cast;

    public String getId() {
        return id;
    }

    public MediumType getType() {
        return type;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        return genres;
    }

    public String getDuration() {
        return duration;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public Person[] getCast() {
        return cast;
    }

    public String getCountries() {
        return countries;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public String getSeasons() {
        return seasons;
    }

    //TODO: Replace some parameter with better data type
    public Medium(String id, MediumType type, Provider provider, String title, String description, String genres, String duration, String seasons,
                  String releaseYear, Person[] cast, String countries, String ageRating, String addedAt){
        this.id = id;
        this.type = type;
        this.providers.add(provider);
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.duration = duration;
        this.seasons = seasons;
        this.releaseYear = releaseYear;
        this.cast = cast;
        this.countries = countries;
        this.ageRating = ageRating;
        this.addedAt = addedAt;

    }

    @Override
    public String toString() {
        return "Medium{" +
                "type=" + type +
                ", providers=" + providers +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ageRating='" + ageRating + '\'' +
                ", duration='" + duration + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", addedAt='" + addedAt + '\'' +
                ", genres='" + genres + '\'' +
                ", countries='" + countries + '\'' +
                ", cast=" + Arrays.toString(cast) +
                '}';
    }
}
