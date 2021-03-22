package uk.ac.tees.t7099806.mediatracker2;


//Used to get/set information of book information in user list stored in firebase
public class BookInfoFirebase {

    private String publisher;
    private String numberOfPages;
    private String publishDate;
    private String title;
    private String subTitle;
    private String description;
    private String bookImage;

    public BookInfoFirebase(String publisher, String numberOfPages, String publishDate, String title, String subTitle, String description, String bookImage) {
        this.publisher = publisher;
        this.numberOfPages = numberOfPages;
        this.publishDate = publishDate;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.bookImage = bookImage;
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


}
