package ir.rainyday.android.starter.modules.shared.events

import android.widget.Toast
import ir.rainyday.android.starter.errors.HttpException
import ir.rainyday.android.starter.extras.Event

/**
 * Created by taghipour on 18/10/2017.
 */
class MessageEvent(val message: String, val type:MessageType=MessageType.REGULAR, val duration:Int= Toast.LENGTH_SHORT ) : Event()

enum class MessageType {
    REGULAR,
    SUCCESS,
    ERROR,
    INFO,
    WARNING
}