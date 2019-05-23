package com.nixesea.testvkr

import android.app.Application
import com.nixesea.testvkr.network.APIRequest

class App : Application() {

    companion object {
        var mAPIRequest: APIRequest? = null
    }
}