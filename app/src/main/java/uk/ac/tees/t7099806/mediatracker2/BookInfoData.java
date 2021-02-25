package uk.ac.tees.t7099806.mediatracker2;

public class BookInfoData {


    private String publisher;
    private String numberOfPages;
    private String publishDate;
    private String title;
    private String subTitle;
    private String description;
    private String bookImage;
    private String rating;
    private String ratingCount;

    public BookInfoData(String publisher, String numberOfPages, String publishDate, String title, String subTitle, String description, String bookImage, String rating, String ratingCount) {
        this.publisher = publisher;
        this.numberOfPages = numberOfPages;
        this.publishDate = publishDate;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.bookImage = bookImage;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getRating() { return rating; }

    public void setRating(String rating) { this.rating = rating;}

    public String getRatingCount() {return ratingCount;}

    public void setRatingCount(String ratingCount) {this.ratingCount = ratingCount;}
}
