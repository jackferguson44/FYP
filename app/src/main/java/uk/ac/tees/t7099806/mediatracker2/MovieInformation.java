package uk.ac.tees.t7099806.mediatracker2;

public class MovieInformation {

    String id;
    String name;
    String image;
    String overview;
    String releaseDate;
    String language;
    String genre;
    String backDropPath;

    public MovieInformation(String id, String name, String image, String overview, String releaseDate, String language, String genre, String backDropPath) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.language = language;
        this.genre = genre;
        this.backDropPath = backDropPath;
    }

    public MovieInformation()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }
}
