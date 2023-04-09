package util.data;

import util.enums.MediumType;
import util.enums.Provider;

import java.util.Arrays;

public class Medium {
    private final MediumType type;
    private final Provider provider;
    private final String title, description, ageRating, duration, releaseYear, addedAt, genres, countries, seasons;
    private final Person[] cast;

    public MediumType getType() {
        return type;
    }

    public Provider getProvider() {
        return provider;
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
    public Medium(MediumType type, Provider provider, String title, String description, String genres, String duration, String seasons,
                  String releaseYear, Person[] cast, String countries, String ageRating, String addedAt){
        this.type = type;
        this.provider = provider;
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
                ", provider=" + provider +
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
