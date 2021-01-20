package uk.ac.tees.t7099806.mediatracker2.ui.Discover;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiscoverViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DiscoverViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Discover fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}