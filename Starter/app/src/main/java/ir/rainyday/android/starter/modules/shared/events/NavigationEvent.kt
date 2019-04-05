package ir.rainyday.android.starter.modules.shared.events

import android.os.Bundle
import ir.rainyday.android.starter.extras.Event

/**
 * Created by taghipour on 18/10/2017.
 */
class NavigationEvent(val destination: Class<*>, val extras: Bundle? = null) : Event() {
}

