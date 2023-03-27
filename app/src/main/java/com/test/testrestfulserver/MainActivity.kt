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
        ViewModelProvider(this)[TestFlowViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ビューをインフレート
        binding.lifecycleOwner = this
        //绑定
        binding.viewModel = viewModel

        binding.btPost.setOnClickListener {
            binding.tvAnswer.text = "NG"
            viewModel.testFlowDataList.observe(binding.lifecycleOwner as MainActivity) {
                if (it != null) {
                    Toast.makeText(baseContext, "" + it, Toast.LENGTH_SHORT).show()
                }
            }
            flowViewModel()
        }
    }

    private fun flowViewModel() {
        runBlocking {
            flow {
                val service = testFlowService()
                val response = service?.getFlowGson()
                emit(response)
            }
                .onStart { Log.e(TAG, "flowViewModel: Starting flow") }
                .onEach {
                    Log.e(TAG, "flowViewModel: onEach : $it")
                    if (it != null) {
                        viewModel.testFlowDataList.value = it
                        binding.tvAnswer.text = it.toString()
                    }
                }
                .catch {
                    Log.e(TAG, "flowViewModel: it.message : "+it.message )
                }
                .onCompletion { if (it == null) Log.e(TAG, "Completed successfully") }
                .collect()
        }
    }

    private fun testFlowService(): TestFlowService? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create()) //把json转为gson，才可以直接用LiveData.postValue
            .build()
        return retrofit.create(TestFlowService::class.java)
    }
}