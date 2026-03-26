package com.wilson.focusmode.core.sensors

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import com.wilson.focusmode.core.interfaces.DistractionSensor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MicrophoneSensor(
    private val context: Context
): DistractionSensor<Int> {
    private var recorder: MediaRecorder? = null
    private var isRecording = false

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
            Log.e("MicrophoneSensor", "Erro ao iniciar captura: ${e.message}")
            stopListening()
        }
    }

    private fun setupRecorder() {
        releaseRecorder()
        try {
            recorder = MediaRecorder(context).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile("/dev/null")
                prepare()
            }
        } catch (e: Exception) {
            Log.e("MicrophoneSensor", "Falha ao inicializar MediaRecorder: ${e.message}")
            recorder = null
        }
    }

    private fun releaseRecorder() {
        try {
            recorder?.stop()
        } catch (e: Exception){
            Log.e("MicrophoneSensor", "Falha ao liberar o MediaRecorder: ${e.message}")
        }
        recorder?.release()
        recorder = null
    }

    override fun stopListening() {
        try {
            if (isRecording) {
                recorder?.stop()
            }
        } catch (e: IllegalStateException) {
            Log.w("MicrophoneSensor", "Recorder já estava em estado inválido")
        } finally {
            isRecording = false
            recorder?.reset()
            recorder?.release()
            recorder = null
        }
    }
}