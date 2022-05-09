package com.example.guitartuner
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.activity.contextaware.ContextAware
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.PrintWriter
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    //lateinit - pozwala uniknąć inizjowania właściwości gdy kontruujemy obiekt
    //MediaRecorder - klasa służąca do nagrywania dzwięku i obrazu
    lateinit var recorder:MediaRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //define path for record file
        var recording_path = Environment.getExternalStorageDirectory().toString()+"/record.3gp"
        recorder = MediaRecorder()



        //define button to use them
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)

        //turn off button if user dont give permission
        button.isEnabled = false //start
        button2.isEnabled = false //stop

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),111)
            button.isEnabled = true //start button on if user give permission

        //if user click on start button - start recording
        button.setOnClickListener{
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder.setOutputFile(recording_path)
            recorder.prepare()
            recorder.start()
            button2.isEnabled = true
            button.isEnabled = false
        }
        //if user click on stop button - stop recording
        button2.setOnClickListener{
            recorder.stop()
            button.isEnabled = true
            button2.isEnabled = false
        }
        //if user click on play button - play recording
        button3.setOnClickListener{
            var player = MediaPlayer()
            player.setDataSource(recording_path)
            player.prepare()
            player.start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val button = findViewById<Button>(R.id.button)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            button.isEnabled = true

    }
}
