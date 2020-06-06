package com.example.schedulertodo.ui.charts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonalViewModel : ViewModel(){
    val count: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}