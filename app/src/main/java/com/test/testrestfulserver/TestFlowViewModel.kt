package com.test.testrestfulserver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 对应的ViewModel
 */
class TestFlowViewModel : ViewModel() {
    //专门对应json数据中的TestFlowData数据List
    var testFlowDataList = MutableLiveData<List<TestFlowData>>()
}