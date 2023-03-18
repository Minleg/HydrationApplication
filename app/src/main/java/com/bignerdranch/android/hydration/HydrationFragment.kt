package com.bignerdranch.android.hydration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RatingBar
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_DAY_OF_WEEK = "day_of_week"

/**
 * A simple [Fragment] subclass.
 * Use the [HydrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HydrationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var dayOfWeek: String

    private lateinit var waterRatingBar: RatingBar
    private lateinit var addGlassButton: ImageButton
    private lateinit var resetGlassesButton: ImageButton

    private lateinit var waterRecord: WaterRecord

    private val waterViewModel by lazy {
        val app = requireActivity().application as HydrationApplication
        WaterViewModelFactory(app.waterRepository, app.daysRepository)
            .create(WaterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dayOfWeek = it.getString(ARG_DAY_OF_WEEK)
                ?: // !! // !! non null assertion operator can be used but better way is using elvis operator
                throw java.lang.IllegalArgumentException("No day of week provided to HydrationFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hydration, container, false)

        waterRatingBar = view.findViewById(R.id.water_rating_bar)
        addGlassButton = view.findViewById(R.id.add_glass_button)
        resetGlassesButton = view.findViewById(R.id.reset_count_button)

        waterViewModel.getRecordForDay(dayOfWeek)
            .observe(requireActivity()) { wr -> // wr from database
                // if the record changes, also called the first time
                if (wr == null) {
                    waterViewModel.insertNewRecord(WaterRecord(dayOfWeek, 0))
                } else {
                    waterRecord = wr
                    waterRatingBar.progress = waterRecord.glasses

                    addGlassButton.setOnClickListener {
                        addGlass()
                    }

                    resetGlassesButton.setOnClickListener {
                        resetGlasses()
                    }
                }
            }
        return view
    }

    private fun addGlass() {
        // add 1 to total glasses - up to max of 5
        if (waterRecord.glasses < resources.getInteger(R.integer.max_glasses)) {
            waterRecord.glasses++
            waterViewModel.updateRecord(waterRecord)
        }
    }

    private fun resetGlasses() {
        waterRecord.glasses = 0
        waterViewModel.updateRecord(waterRecord)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HydrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(dayOfWeek: String) =
            // This newInstance fragment function can be invoked without having to make a Fragment first
            HydrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DAY_OF_WEEK, dayOfWeek)
                }
            }
    }
}
