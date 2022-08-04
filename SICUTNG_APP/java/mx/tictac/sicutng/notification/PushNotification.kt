package mx.tictac.sicutng.notification

import mx.tictac.sicutng.notification.NotificationData

data class PushNotification (
    val data: NotificationData,
    val to: String
)