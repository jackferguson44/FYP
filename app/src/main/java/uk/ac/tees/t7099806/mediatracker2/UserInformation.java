package uk.ac.tees.t7099806.mediatracker2;

import java.sql.Time;
import java.util.Date;

public class UserInformation {

    public String userName;
    public String phone;
    public Date date;
    public int booksRead;
    public double bookScore;
    public int showsWatched;
    public Time timeWatched;
    public double showScore;
    public double totalScore;


    public UserInformation()
    {

    }

    public UserInformation(String userName, String phone)
    {
        this.userName = userName;
        this.phone = phone;
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



}
