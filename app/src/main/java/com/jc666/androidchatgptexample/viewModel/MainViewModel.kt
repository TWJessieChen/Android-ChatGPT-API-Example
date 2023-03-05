package com.jc666.androidchatgptexample.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jc666.androidchatgptexample.database.entity.ContentEntity
import com.jc666.androidchatgptexample.repository.DatabaseRepository
import com.jc666.androidchatgptexample.utils.xLog
import com.nohjunh.test.repository.NetWorkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException


class MainViewModel : ViewModel() {

    private val databaseRepository = DatabaseRepository()
    private val netWorkRepository = NetWorkRepository()

    val contentList : LiveData<List<ContentEntity>> get() = _contentList
    private var _contentList = MutableLiveData<List<ContentEntity>>()

    val deleteCheck : LiveData<Boolean> get() = _deleteCheck
    private var _deleteCheck = MutableLiveData<Boolean>(false)

    val gptInsertCheck : LiveData<Boolean> get() = _gptInsertCheck
    private var _gptInsertCheck = MutableLiveData<Boolean>(false)

    fun postResponse(query : String) = viewModelScope.launch {
        val jsonObject: JsonObject? = JsonObject().apply{
            // params
            addProperty("model", "gpt-3.5-turbo")
            val jsonArray = JSONArray()
            val jsonObject = JSONObject()
            jsonObject.put("role", "user")
            jsonObject.put("content", query)
            jsonArray.put(jsonObject)
            addProperty("messages", jsonArray.toString())
        }
        try {
            val response = netWorkRepository.postResponse(jsonObject!!)
            var responseMsg = ""
            for (iChoices in response.choices) {
                responseMsg += iChoices.message.content
            }
            insertContent(responseMsg, 1, Gson().toJson(response))
        } catch (e: HttpException) {
            xLog.e("Error Code : ${e.code() } Error Message : ${e.message()}")
            _gptInsertCheck.postValue(true)
            //這段可以彈跳視窗顯示錯誤訊息
        } catch (e: Throwable) {
            xLog.e("Error Message : ${e.message}")
        }
    }

    fun getContentDataFromDatabase() = viewModelScope.launch(Dispatchers.IO) {
        _contentList.postValue(databaseRepository.getContentData())
        _deleteCheck.postValue(false)
        _gptInsertCheck.postValue(false)
    }

    fun insertContent(content : String, gptOrUser : Int, originalJson : String) = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.insertContent(content, gptOrUser, originalJson)
        if (gptOrUser == 1) {
            _gptInsertCheck.postValue(true)
        }
    }

    fun deleteSelectedContent(id : Int) = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.deleteSelectedContent(id)
        _deleteCheck.postValue(true)
    }

}