package uk.ac.tees.t7099806.mediatracker2;

public class Book {

    private String mTitle;
    private String mAuthors;
    private float mRating;
    

    Book(String title, String authors, float rating) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mRating = rating;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }


    public float getRating() {
        return mRating;
    }

}
