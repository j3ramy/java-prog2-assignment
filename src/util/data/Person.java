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
        this.character = character == null || character.isEmpty() ? "N/A" : character;
        this.role = role;
    }

    @Override
    public String toString() {
        if(this.name == null || this.role == null)
            return "N/A";

        return Util.uppercaseAll(this.role.name()) + ": " + this.name + " (" + this.character + ")";
    }
}
