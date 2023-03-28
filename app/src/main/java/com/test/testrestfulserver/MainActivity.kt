package com.test.testrestfulserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.test.testrestfulserver.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    // Obtain ViewModel from ViewModelProviders
    private val viewModel by lazy {
        ViewModelProvider(this)[FlowServiceViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ビューをインフレート
        binding.lifecycleOwner = this
        //绑定
        binding.viewModel = viewModel

        binding.btGetAllFlowData.setOnClickListener {
            binding.tvAnswer.text = "NG"
            viewModel.getFlowData.observeForever {
                if (it !=null){
                    binding.tvAnswer.text = it.toString()
                }
            }
            restfulFlowViewModelGetAll()
        }
    }

    private fun restfulFlowService(): FlowService? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.3:8080/")
            .addConverterFactory(GsonConverterFactory.create()) //把json转为gson，才可以直接用LiveData.postValue
            .build()
        return retrofit.create(FlowService::class.java)
    }

    private fun restfulFlowViewModelGetAll() {
        runBlocking {
            flow {
                val service = restfulFlowService()
                val response = service?.getFlowGson()
                if (response?.isSuccessful == true) {
                    Log.e(TAG, "" + response.code())
                    Toast.makeText(baseContext, "" + response?.code(), Toast.LENGTH_SHORT)
                        .show()
                    emit(response?.body())
                } else {
                    if (response != null) {
                        Log.e(TAG, "" + response.code())
                    }
                    Toast.makeText(baseContext, "" + response?.code(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
                .onStart { Log.e(TAG, "flowViewModel: Starting flow") }
                .onEach {
                    Log.e(TAG, "flowViewModel: onEach : $it")
                    if (it != null) {
                        viewModel.getFlowData.value = it
                    }
                }
                .catch {
                    Log.e(TAG, "flowViewModel: !!! catch : "+it.message )
                    binding.tvAnswer.text = it.toString()
                }
                .onCompletion {
                    Log.e(TAG, "onCompletion")
                }
                .collect()
        }
    }
}