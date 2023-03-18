package com.bignerdranch.android.hydration

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WaterRecord(
    @PrimaryKey
    @NonNull
    val day: String,
    @NonNull
    var glasses: Int,
) // data class get free toString functions and more
