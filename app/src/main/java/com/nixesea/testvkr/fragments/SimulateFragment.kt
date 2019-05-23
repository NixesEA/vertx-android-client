package com.nixesea.testvkr.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.nixesea.testvkr.App
import com.nixesea.testvkr.R
import com.nixesea.testvkr.network.SimulatedData
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import com.hp.hpl.jena.rdf.model.Literal
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryExecution
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.util.FileManager


class SimulateFragment : BaseFragment(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    //    lateinit var connect: Button
    lateinit var query: Button
    lateinit var text: TextView
    lateinit var radioGr: RadioGroup

    lateinit var ledServer: ImageView
    lateinit var ledSimulation: ImageView

    var simulateFlag = false
    val handler = Handler()
    val checkHandler = Handler()

    var simulatedDeviceType = 0

    var deviceList = arrayOf("Термометр", "СО2", "Барометр", "Влажность")
    var sb: StringBuilder = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.simulate_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ledServer = view.findViewById(R.id.led_connect_status)
        ledSimulation = view.findViewById(R.id.led_sim_status)

        radioGr = view.findViewById(R.id.device_radio_gr)
        radioGr.setOnCheckedChangeListener(this)
        text = view.findViewById(R.id.home)

        query = view.findViewById(R.id.simulation)
        query.setOnClickListener(this)

        for (device: String in deviceList) {
            val newRadioButton = RadioButton(context)
            newRadioButton.text = device
            radioGr.addView(newRadioButton)
        }
        radioGr.check(1)
    }

    override fun onResume() {
        super.onResume()
        startServerMonitoring()
    }

    private fun startServerMonitoring() {
        checkHandler.postDelayed(object : Runnable {
            override fun run() {
                checkConnect()
                checkHandler.postDelayed(this, 7000)
            }
        }, 7000)
    }

    private fun checkConnect() {
        App.mAPIRequest?.checkConnection()?.enqueue {
            onResponse = {
                Log.i("Response", "onResponse:$it")
                ledServer.setImageDrawable(context?.resources?.getDrawable(R.drawable.simulator_on))
            }

            onFailure = {
                Log.i("Response", "onFailure:$it")
                ledServer.setImageDrawable(resources.getDrawable(R.drawable.simulator_off))
                Toast.makeText(context, "onFailure:$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        simulatedDeviceType = checkedId
        Log.i("TEST", "selected type: $simulatedDeviceType")
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(view: View) {
        when (view) {
            query -> {
//                callOntology()
                simulateFlag = !simulateFlag

                if (simulateFlag) {
                    ledSimulation.setImageDrawable(context?.resources?.getDrawable(R.drawable.simulator_on))

                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            if (simulateFlag) {
                                simulateDevice()
                                handler.postDelayed(this, 20000)
                            }
                        }
                    }, 20000)
                } else {
                    ledSimulation.setImageDrawable(context?.resources?.getDrawable(R.drawable.simulator_off))
                }

            }
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun simulateDevice() {
        val calendar: Calendar = Calendar.getInstance()

        val sim = SimulatedData()
        val time = SimpleDateFormat("dd:MM:yyyy HH:mm.ss").format(calendar.time)
        sim.storingTime = time
        when (simulatedDeviceType) {
            //Термометр
            1 -> {
                sim.deviceGroup = "bedroom"
                sim.deviceName = "smartThermometer"

                sim.valueMap.put("pressure", (Math.random() * 30 + 740).toString())
                sim.valueMap.put("temperature", (Math.random() * 15 + 15).toString())
                Log.i("TEST", "create thermometer")
            }
            //СО2
            2 -> {
                sim.deviceGroup = "bedroom"
                sim.deviceName = "smartCO2"

                sim.valueMap.put("CO %", (Math.random() * 10 + 10).toString())
                sim.valueMap.put("CO2 %", (Math.random() * 40).toString())
                Log.i("TEST", "create CO2")
            }
            //Барометр
            3 -> {
                sim.deviceGroup = "livingroom"
                sim.deviceName = "smartBarometer"

                sim.valueMap.put("wet", (Math.random() * 40 + 15).toString())
                sim.valueMap.put("pressure", (Math.random() * 30 + 740).toString())
                Log.i("TEST", "create barometer")
            }
            //Влажность
            else -> {
                sim.deviceGroup = "livingroom"
                sim.deviceName = "smartGigrometer"

                sim.valueMap.put("wet", (Math.random() * 40 + 15).toString())
                sim.valueMap.put("light", (Math.random() * 200 + 300).toString())
                Log.i("TEST", "create gigrometer")
            }
        }

        sb.append(Gson().toJson(sim))
        sb.append("\n")
        text.text = sb.toString()

        sendData(sim)
    }

    @SuppressLint("SetTextI18n")
    private fun sendData(sim: SimulatedData) {
        App.mAPIRequest?.saveSimulateData(sim)?.enqueue {
            onResponse = {
                Log.i("Response", "onResponse:$it")
                ledServer.setImageDrawable(context?.resources?.getDrawable(R.drawable.simulator_on))

                sb.append("Responce:${it.body()?.response}")
                sb.append("\n")
                text.text = sb.toString()
            }

            onFailure = {
                Log.i("Response", "onFailure:$it")
                ledServer.setImageDrawable(context?.resources?.getDrawable(R.drawable.simulator_off))

                sb.clear()
                text.text = "Error:$it"
            }
        }
    }

}