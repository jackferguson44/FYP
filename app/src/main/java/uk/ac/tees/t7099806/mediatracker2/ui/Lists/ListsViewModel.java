package uk.ac.tees.t7099806.mediatracker2.ui.Lists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public ListsViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is List fragment");
    }

    public LiveData<String> getText() {return mText;}
}