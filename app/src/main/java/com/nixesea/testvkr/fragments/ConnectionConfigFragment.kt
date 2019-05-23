package com.nixesea.testvkr.fragments

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation
import com.nixesea.testvkr.R
import com.nixesea.testvkr.network.APIBase
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import com.nixesea.testvkr.App


class ConnectionConfigFragment : BaseFragment(), View.OnClickListener {

    lateinit var ipEdit: TextInputEditText
    lateinit var confirm: Button
    lateinit var progress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.connection_config_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progress = view.findViewById(R.id.progress)

        ipEdit = view.findViewById(R.id.ip_edit)

        confirm = view.findViewById(R.id.confirm)
        confirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (!ipEdit.text.toString().contains("http://")) {
            Toast.makeText(context, "Unsupported format", Toast.LENGTH_LONG).show()
            return
        }
        progress.visibility = View.VISIBLE

        APIBase.BASE_URL = ipEdit.text.toString()
        App.mAPIRequest = APIBase.apiRequest

        App.mAPIRequest?.checkConnection()?.enqueue {
            onResponse = {
                Log.i("Response", "onResponse:$it")
                val imm =
                    activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)

                Navigation.findNavController(confirm)
                    .navigate(R.id.action_connectionConfigFragment_to_simulateFragment)
                progress.visibility = View.GONE
            }

            onFailure = {
                Log.i("Response", "onFailure:$it")
                Toast.makeText(context, "onFailure:$it", Toast.LENGTH_LONG).show()
                progress.visibility = View.GONE
            }
        }
    }
}
