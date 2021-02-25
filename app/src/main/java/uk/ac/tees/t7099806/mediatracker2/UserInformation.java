package uk.ac.tees.t7099806.mediatracker2;

import java.sql.Time;
import java.util.Date;

public class UserInformation {

    public String userName;
    public String phone;
    public String date;
    public int booksRead;
    public double bookScore;
    public int bookScoreCount;
    public int showsWatched;
    public Time timeWatched;
    public double showScore;
    public int showScoreCount;
    public double totalScore;
    public int totalScoreCount;



    public UserInformation()
    {

    }

    public UserInformation(String userName, String phone, String date)
    {
        this.userName = userName;
        this.phone = phone;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBooksRead() {
        return booksRead;
    }

    public void setBooksRead(int booksRead) {
        this.booksRead = booksRead;
    }

    public double getBookScore() {
        return bookScore;
    }

    public void setBookScore(double bookScore) {
        this.bookScore = bookScore;
    }

    public int getShowsWatched() {
        return showsWatched;
    }

    public void setShowsWatched(int showsWatched) {
        this.showsWatched = showsWatched;
    }

    public double getShowScore() {
        return showScore;
    }

    public void setShowScore(double showScore) {
        this.showScore = showScore;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public int getBookScoreCount() {
        return bookScoreCount;
    }

    public void setBookScoreCount(int bookScoreCount) {
        this.bookScoreCount = bookScoreCount;
    }

    public Time getTimeWatched() {
        return timeWatched;
    }

    public void setTimeWatched(Time timeWatched) {
        this.timeWatched = timeWatched;
    }

    public int getShowScoreCount() {
        return showScoreCount;
    }

    public void setShowScoreCount(int showScoreCount) {
        this.showScoreCount = showScoreCount;
    }

    public int getTotalScoreCount() {
        return totalScoreCount;
    }

    public void setTotalScoreCount(int totalScoreCount) {
        this.totalScoreCount = totalScoreCount;
    }
}
