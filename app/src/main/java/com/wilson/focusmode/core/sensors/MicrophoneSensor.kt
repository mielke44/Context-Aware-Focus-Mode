package com.wilson.focusmode.core.sensors

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import com.wilson.focusmode.core.interfaces.DistractionSensor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

class MicrophoneSensor(
    private val context: Context
): DistractionSensor<Int> {

    private var recorder: MediaRecorder? = null
    private var isRecording = false

    private val lock = Any()

    override fun startListening(): Flow<Int> = flow {
        setupRecorder()
        try {
            recorder?.start()
            isRecording = true
            while (isRecording) {
                val amplitude = recorder?.maxAmplitude ?: 0
                emit(amplitude)
                delay(1000)
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                Log.d(microphoneSensorTag, "Data collection scope canceled")
            } else {
                Log.e(microphoneSensorTag, "Capture error: ${e.message}")
            }
        } finally {
            stopListening()
        }
    }.flowOn(Dispatchers.IO)


    private fun setupRecorder() {
        releaseRecorder()
        try {
            val tempFile = File(context.cacheDir, "temp_mic_sensor.amr")
            if (tempFile.exists()) tempFile.delete()

            recorder = MediaRecorder(context).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(tempFile.absolutePath)
                prepare()
            }
        } catch (e: Exception) {
            Log.e(microphoneSensorTag, "MediaRecorder failed to start: ${e.message}")
            recorder = null
        }
    }

    private fun releaseRecorder() {
        try {
            recorder?.stop()
        } catch (e: Exception){
            Log.e(microphoneSensorTag, "MediaRecorder failed to release: ${e.message}")
        }
        recorder?.release()
        recorder = null
    }

    override fun stopListening() {
        synchronized(lock) {
            try {
                if (isRecording) {
                    isRecording = false
                    recorder?.stop()
                }
            } catch (e: Exception) {
                Log.w(microphoneSensorTag, "Microphone sensor Exception: ${e.message}")
            } finally {
                try {
                    recorder?.reset()
                    recorder?.release()
                } catch (e: Exception) {
                }
                recorder = null
            }
        }
    }

    companion object{
        const val microphoneSensorTag = "MicrophoneSensor"
    }
}