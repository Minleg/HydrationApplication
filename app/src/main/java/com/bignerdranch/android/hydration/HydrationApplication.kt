package com.bignerdranch.android.hydration

import android.app.Application

class HydrationApplication :
    Application() { // Application - initial setup before any activity or fragment is created
    private val database by lazy { WaterDatabase.getDatabase(this) }

    // activity can access this repository
    val waterRepository by lazy { WaterRepository(database.waterDao()) }

    // refernce for day repository
    val daysRepository = DaysRepository()
}
