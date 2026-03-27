# Context-Aware-Focus-Mode
Senior Android Technical Assessment MVP
Focus Mode - Sensory Monitoring Application

A native Android application designed to enhance user productivity by monitoring environmental distractions (noise and movement) using real-time sensory data.

1. Architecture Explanation
The project follows Clean Architecture principles combined with MVVM (Model-View-ViewModel) and UDF (Unidirectional Data Flow).

Presentation Layer: Built with Jetpack Compose, observing state through StateFlow.

Domain/Core Layer: Contains the business logic for distraction detection and sensor abstractions. It uses a Foreground Service to ensure session persistence.

Data Layer: Utilizes Room Database for persistence and a Repository pattern acting as a Single Source of Truth (SSoT).

Dependency Injection: Managed by Koin, enabling a decoupled architecture and easier testing.

Reactive Data Flow
Sensors emit raw data through Kotlin Flows, which are combined in the FocusForegroundService. The service updates a shared activeSessionState in the Repository, which is then mapped to the UI state.

2. Native Resource Handling
Handling hardware sensors (Microphone and Accelerometer) requires strict lifecycle management to avoid memory leaks and battery drain.
Idempotent Resource Release: The MicrophoneSensor implementation uses a synchronized lock to ensure MediaRecorder is stopped, reset, and released exactly once, even during concurrent scope cancellations.
State Machine Safety: Defensive programming was applied to the MediaRecorder lifecycle to prevent IllegalStateException during rapid start/stop transitions.

4. OS-Level Integration
Foreground Service (FGS): Implemented with foregroundServiceType="microphone" to maintain monitoring integrity when the app is backgrounded.
SDK 36 Compatibility: Declarations for FOREGROUND_SERVICE_MICROPHONE and runtime checks for POST_NOTIFICATIONS ensure compliance with latest Android security standards.
Notification Utilization: Used persistent notifications for FGS compliance and high-priority channels for real-time distraction alerts.

4. Trade-offs and Prioritization
Intentional Deprioritization:
Complex Sensory Calibration: The threshold values (Noise/Movement) are currently static. Implementing an auto-calibration phase was deprioritized to focus on architecture stability.
UI Polish: Following the "What We Are NOT Evaluating" guideline, complex animations and pixel-perfect styling were traded for robust background execution and data persistence.

Improvements with More Time:
WorkManager Integration: Implementing a periodic worker to sync session history with a remote backend.
Advanced Audio Analysis: Using Fast Fourier Transform (FFT) to distinguish between background ambient noise and specific human speech distractions.

5. Testability and Scaling
Ensuring Testability:
Context-Free ViewModels: The ViewModels do not hold references to the Android Context. Navigation and Service control are abstracted through interfaces, allowing for pure JUnit tests.
Sensor Abstraction: DistractionSensor<T> is a generic interface, enabling the use of Mock/Fake sensors for automated UI testing without requiring physical movement or noise.

Scaling for Production:
Modularization: Splitting the project into :core, :data, and :feature modules to reduce build times and improve team parallelization.
Remote Configuration: Moving sensor thresholds to a Firebase Remote Config to allow real-time calibration without app updates.
Analytics Pipeline: Implementing an event-tracking layer to monitor user focus trends at scale.
