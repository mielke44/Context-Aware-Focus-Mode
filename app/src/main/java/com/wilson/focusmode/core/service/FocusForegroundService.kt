package com.wilson.focusmode.core.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.wilson.focusmode.R
import com.wilson.focusmode.core.FocusRepository
import com.wilson.focusmode.core.interfaces.DistractionSensor
import com.wilson.focusmode.core.models.FocusSessionEntity
import com.wilson.focusmode.core.utils.NotificationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class FocusForegroundService : Service(), KoinComponent {

    private val notificationUtil: NotificationUtil by inject()
    private val accelSensor: DistractionSensor<Float> by inject(named("accelerometer"))
    private val micSensor: DistractionSensor<Int> by inject(named("microphone"))
    private val repository: FocusRepository by inject()

    private var lastAlertTime = 0L
    private val ALERT_COOLDOWN = 5000L

    private val NOISE_THRESHOLD = 20000
    private val MOVEMENT_THRESHOLD = 12f

    private var duration = 0L
    private var sounds = 0
    private var movements = 0

    private var serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startFocusService()
            ACTION_STOP -> stopFocusService()
        }
        return START_STICKY
    }

    private fun startFocusService() {
        val notification = notificationUtil.buildForegroundNotification(
            title = applicationContext.getString(R.string.notif_ongoing_fgs),
            body = applicationContext.getString(R.string.notif_ongoing_body),
            icon = R.drawable.ic_launcher_foreground
        )

        if (!serviceScope.isActive) {
            serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        }

        startForeground(NotificationUtil.ONGOING_NOTIFICATION_ID, notification)
        serviceScope.launch {
            while (isActive) {
                delay(1000)
                duration += 1000
                repository.updateActiveSession(duration, sounds, movements)
            }
        }
        serviceScope.launch {
            combine(accelSensor.startListening(), micSensor.startListening()) { accelValue, micValue ->
                processSensors(accelValue, micValue)
            }.collect()
        }
    }

    private fun processSensors(accel: Float, mic: Int) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAlertTime < ALERT_COOLDOWN) return
        when {
            mic > NOISE_THRESHOLD -> {
                handleDistraction(applicationContext.getString(R.string.notif_movement_distraction))
                sounds += 1
                lastAlertTime = currentTime
                repository.updateActiveSession(duration, sounds, movements)
            }
            accel > MOVEMENT_THRESHOLD -> {
                handleDistraction(applicationContext.getString(R.string.notif_sound_distraction))
                lastAlertTime = currentTime
                movements += 1
                repository.updateActiveSession(duration, sounds, movements)
            }
        }
    }

    private fun stopFocusService() {
        CoroutineScope(Dispatchers.IO).launch {
            if (serviceScope.isActive) {
                serviceScope.cancel()
            }
            duration = 0L
            sounds = 0
            movements = 0
            accelSensor.stopListening()
            micSensor.stopListening()

            val finalData = repository.activeSessionState.value
            if (finalData.durationMillis > 0.01) {
                val sessionEntity = FocusSessionEntity(
                    startTimestamp = System.currentTimeMillis() - finalData.durationMillis,
                    durationMillis = finalData.durationMillis,
                    soundDistractions = finalData.soundDistractions,
                    movementDistractions = finalData.movementDistractions
                )
                repository.saveSession(sessionEntity)
            }
            repository.resetActiveSession()
            withContext(Dispatchers.Main) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            serviceScope.cancel()
        }
    }

    private fun handleDistraction(message: String) {
        notificationUtil.postDistractionNotification(
            title = applicationContext.getString(R.string.notif_distraction_title),
            body = message,
            icon = R.drawable.ic_warning
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "ACTION_START_FOCUS"
        const val ACTION_STOP = "ACTION_STOP_FOCUS"
    }
}