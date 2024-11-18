package com.mnrf.ejajan.view.main.parent.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Setting2ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Setting Fragment"
    }
    val text: LiveData<String> = _text
}