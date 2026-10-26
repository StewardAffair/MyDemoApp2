package com.roman.mydemoapp.settings.presentation

import androidx.annotation.StringRes
import com.roman.mydemoapp.R
import com.roman.mydemoapp.settings.domain.model.RoastLevel

@get:StringRes
val RoastLevel.title: Int
    get() = when (this) {
        RoastLevel.Rare -> R.string.settings_level_rare
        RoastLevel.MediumRare -> R.string.settings_level_medium_rare
        RoastLevel.Medium -> R.string.settings_level_medium
        RoastLevel.MediumWell -> R.string.settings_level_medium_well
        RoastLevel.WellDone -> R.string.settings_level_well_done
    }

@get:StringRes
val RoastLevel.description: Int
    get() = when (this) {
        RoastLevel.Rare -> R.string.settings_level_rare_description
        RoastLevel.MediumRare -> R.string.settings_level_medium_rare_description
        RoastLevel.Medium -> R.string.settings_level_medium_description
        RoastLevel.MediumWell -> R.string.settings_level_medium_well_description
        RoastLevel.WellDone -> R.string.settings_level_well_done_description
    }

@get:StringRes
val RoastLevel.example: Int
    get() = when (this) {
        RoastLevel.Rare -> R.string.settings_example_rare
        RoastLevel.MediumRare -> R.string.settings_example_medium_rare
        RoastLevel.Medium -> R.string.settings_example_medium
        RoastLevel.MediumWell -> R.string.settings_example_medium_well
        RoastLevel.WellDone -> R.string.settings_example_well_done
    }
