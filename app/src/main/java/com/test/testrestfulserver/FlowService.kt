package com.test.testrestfulserver

import retrofit2.Response
import retrofit2.http.GET

interface FlowService {

    //http://192.168.1.3:8080/api/v1/employees
    /*"post":
{
    "id": 36,
    "firstName": "88",
    "lastName": "99",
    "emailId": "777"
}
    */


    @GET("/api/v1/employees")
    suspend fun getFlowGson(): Response<GetFlowData>


    companion object {
        private const val TAG = "FlowService"
    }
}