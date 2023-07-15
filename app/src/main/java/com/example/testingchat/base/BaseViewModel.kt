package com.example.testingchat.base

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.testingchat.data.local.PreferenceManager
import javax.inject.Inject

abstract class BaseViewModel: ViewModel() {

    @Inject
    protected lateinit var preferenceManager: PreferenceManager
    @Inject
    protected lateinit var application: Application

    open class NonNullLiveData<T: Any>(value: T): LiveData<T>(value) {
        override fun getValue(): T {
            return super.getValue() as T
        }

        inline fun observe(owner: LifecycleOwner, crossinline observer: (t: T) -> Unit) {
            this.observe(owner, Observer {
                it?.let(observer)
            })
        }
    }

    class NonNullMutableLiveData<T: Any>(value: T): NonNullLiveData<T>(value) {
        public override fun setValue(value: T) {
            super.setValue(value)
        }

        public override fun postValue(value: T) {
            super.postValue(value)
        }
    }
}