package com.nixesea.testvkr.fragments

import android.support.v4.app.Fragment
import com.nixesea.testvkr.network.CallBackKt
import retrofit2.Call

open class BaseFragment: Fragment() {
    fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
        val callBackKt = CallBackKt<T>()
        callback.invoke(callBackKt)
        this.enqueue(callBackKt)
    }
}