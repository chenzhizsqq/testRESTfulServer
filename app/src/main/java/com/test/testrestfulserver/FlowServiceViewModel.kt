package com.test.testrestfulserver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 对应的ViewModel
 */
class FlowServiceViewModel : ViewModel() {
    var getFlowData = MutableLiveData<GetFlowData>()
}