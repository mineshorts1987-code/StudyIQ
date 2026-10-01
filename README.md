# StudyIQ

Android-first Material 3 study companion that turns a source into useful study material. This repository contains the original StudyIQ client foundation: a responsive Compose UI, source-to-format flow, library, progress dashboard, profile/settings, and interactive study previews.

## Run

Open in Android Studio Ladybug or newer and run the `app` configuration. The app uses Kotlin, Jetpack Compose, Material 3, and Navigation Compose.

The current client uses a local demo repository so every visible interaction is functional without exposing AI credentials. Production integrations should be connected through a server-side API for authentication, ingestion/OCR/transcription, generation jobs, exports, subscriptions, and Razorpay verification. No API keys belong in the Android client.

## Product boundaries

- Original StudyIQ identity and UI; no third-party branding or proprietary artwork.
- Demo content is explicitly labeled in the UI and isolated in `DemoStudyRepository`.
- Generation, file picking, recording, export, authentication, and payments have clear extension points and human-readable states.
- Backend should enforce ownership, signed file URLs, rate limits, validation, encryption, and payment signature verification.
