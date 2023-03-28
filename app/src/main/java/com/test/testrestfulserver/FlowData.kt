package com.test.testrestfulserver

data class GetFlowDataItem(
    val emailId: String,
    val firstName: String,
    val id: Int,
    val lastName: String
)
class GetFlowData : ArrayList<GetFlowDataItem>()