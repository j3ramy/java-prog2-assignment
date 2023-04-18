package util.data;

import util.Utils;
import util.enums.PersonRole;

public class Person {
    private final String name, character;
    private final PersonRole role;

    private String movieId;

    /**
     * Gets movie id
     * @return  movie id
     *
     * @BigO: O(1)
     * **/
    public String getMovieId() {
        return movieId;
    }

    /**
     * Sets movie id
     * @param movieId new movieId
     *
     * @BigO: O(1)
     * **/
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    /**
     * Gets name
     * @return  name
     *
     * @BigO: O(1)
     * **/
    public String getName() {
        return name;
    }

    /**
     * Gets character name
     * @return  character name
     *
     * @BigO: O(1)
     * **/
    public String getCharacter() {
        return character;
    }

    /**
     * Gets job as role
     * @return  role
     *
     * @BigO: O(1)
     * **/
    public PersonRole getRole() {
        return role;
    }

    public Person(String name, String character, PersonRole role){
        this.name = name;
        this.character = character == null || character.isEmpty() ? "N/A" : character;
        this.role = role;
    }

    /**
     * Override the toString()-method for displaying the persons
     *
     * @BigO: O(n)
     * **/
    @Override
    public String toString() {
        if(this.name == null || this.role == null)
            return "N/A";

        return Utils.uppercaseAll(this.role.name()) + ": " + this.name + " (" + this.character + ")";
    }
}
