package com.example.guitartuner
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.* //pozwala korzystac ze wszystkich activity typu button itp bez koniecznosci tworzenia zmiennych
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    //lateinit - pozwala uniknąć inizjowania właściwości gdy kontruujemy obiekt
    //MediaRecorder - klasa służąca do nagrywania dzwięku i obrazu
    lateinit var recorder:MediaRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //define path for record file
//        var recording_path = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"
        recorder = MediaRecorder()

        val recording_path = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        //turn off button if user dont give permission
        StartButton.isEnabled = false //start
        StopButton.isEnabled = false //stop

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),111)
        StartButton.isEnabled = true //start button on if user give permission

        //if user click on start button - start recording
        StartButton.setOnClickListener{
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder.setOutputFile(recording_path)
            recorder.prepare()
            recorder.start()
            StopButton.isEnabled = true
            StartButton.isEnabled = false
        }
        //if user click on stop button - stop recording
        StopButton.setOnClickListener{
            recorder.stop()
            StartButton.isEnabled = true
            StopButton.isEnabled = false
        }
        //if user click on play button - play recording
        PlayButton.setOnClickListener{
            val player = MediaPlayer()
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
        val button = findViewById<Button>(R.id.StartButton)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            button.isEnabled = true

    }
}
