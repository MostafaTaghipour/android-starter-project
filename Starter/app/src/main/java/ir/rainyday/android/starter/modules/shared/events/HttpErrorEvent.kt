package ir.rainyday.android.starter.modules.shared.events


import ir.rainyday.android.starter.errors.HttpException
import ir.rainyday.android.starter.extras.Event

/**
 * Created by taghipour on 18/10/2017.
 */
class HttpErrorEvent(val error: HttpException?) : Event() {
}