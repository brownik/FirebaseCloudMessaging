package com.brownik.firebasecloudmessaging

data class MessageData(
    val to: String = "",
    val data: MessageInfo,
) {
    data class MessageInfo(
        val title: String = "",
        val message: String = "",
    )
}