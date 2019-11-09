package com.yakgwa.kullow.ui.goose;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GooseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GooseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is goose fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
