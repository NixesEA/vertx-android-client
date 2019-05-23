package com.nixesea.testvkr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
/*
    private fun callOntology() {

                // create tdb dataset
                val directory = File(filesDir, "tdb_dataset")
                directory.delete()
                directory.mkdir()
                val dataset = TDBFactory.createDataset(directory.absolutePath)
                        val model = dataset.defaultModel
                val skos_ttl = assets.open("rootTTL.ttl")
//        RDFDataMgr.read(model, skos_ttl, Lang.TURTLE)

    }
    */
}