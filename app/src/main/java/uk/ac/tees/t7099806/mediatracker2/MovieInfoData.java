package uk.ac.tees.t7099806.mediatracker2;

//Used to get/set information of movie stored in firebase
public class MovieInfoData {

    String name;
    String image;
    String overview;
    String releaseDate;
    String language;
    String genre;
    String backDropPath;
    String rating;
    String ratingCount;

    public MovieInfoData(String name, String image, String overview, String releaseDate, String language, String genre, String backDropPath, String rating, String ratingCount ) {
        this.name = name;
        this.image = image;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.language = language;
        this.genre = genre;
        this.backDropPath = backDropPath;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public MovieInfoData()
    {

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }
}
