package uk.ac.tees.t7099806.mediatracker2.ui.Search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Search fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}