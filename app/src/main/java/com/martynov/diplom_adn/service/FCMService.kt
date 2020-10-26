package com.martynov.diplom_adn.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.martynov.diplom_adn.NotifictionHelper
import splitties.toast.toast

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val recipientId = message.data["recipientId"]
        val title = message.data["title"]
        val body = message.data["body"]


        println(body)
        println(recipientId)
        println(title)
        try {
            if (title != null) {
                if (recipientId != null) {
                    NotifictionHelper.postIsLike(this, title, recipientId.toLong(), body)


                }
            }
        } catch (e: Exception) {
            this.toast("Ошибка")
        }

    }

    override fun onNewToken(token: String) {
        println(token)
    }
}