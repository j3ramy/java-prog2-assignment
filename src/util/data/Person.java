package util.data;

import util.Util;
import util.enums.PersonRole;

public class Person {
    private final String name, character;
    private final PersonRole role;

    private String movieId;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public PersonRole getRole() {
        return role;
    }

    public Person(String name, String character, PersonRole role){
        this.name = name;
        this.character = character == null ? "N/A" : character;
        this.role = role;
    }

    @Override
    public String toString() {
        return Util.capitalize(this.role.name(), null) + ": " + this.name + " (" + this.character + ")" + " (" + this.movieId + ")";
    }
}
