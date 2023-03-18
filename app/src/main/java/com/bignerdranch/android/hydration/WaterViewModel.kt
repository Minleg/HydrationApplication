package com.bignerdranch.android.hydration

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class WaterViewModel(private val repository: WaterRepository, private val daysRepository: DaysRepository) : ViewModel() {

    val allRecords = repository.getAllRecords().asLiveData()

    fun insertNewRecord(record: WaterRecord) { // foreground
        viewModelScope.launch {
            // tells to run this in the background with launch
            repository.insert(record)
        }
    }

    fun updateRecord(record: WaterRecord) {
        viewModelScope.launch {
            repository.update(record)
        }
    }

    fun deleteRecord(record: WaterRecord) {
        viewModelScope.launch {
            repository.delete(record)
        }
    }

    fun getRecordForDay(day: String): LiveData<WaterRecord> {
        return repository.getRecordForDay(day).asLiveData()
    }

//    fun getAllRecords(): LiveData<List<WaterRecord>> {
//        return repository.getAllRecords().asLiveData()
//    }

    fun getWeekdays(): List<String> {
        return daysRepository.weekdays
    }

    fun getTodayIndex(): Int {
        return daysRepository.todayIndex
    }
}

class WaterViewModelFactory(private val waterRepository: WaterRepository, private val daysRepository: DaysRepository) : ViewModelProvider.Factory {
    // enables creating ViewModel with an argument: with WaterRepository in this case
    override fun <T : ViewModel> create(modelClass: Class<T>): T { // T is place holder for actual class definition - GenericType
        if (modelClass.isAssignableFrom(WaterViewModel::class.java)) {
            // checks if the class given to ViewModelProvider is a ViewModel class
            return WaterViewModel(waterRepository, daysRepository) as T
        }
        throw java.lang.IllegalArgumentException("$modelClass is not a WaterViewModel")
    }
}
