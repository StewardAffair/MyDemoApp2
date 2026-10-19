package com.roman.mydemoapp.settings

import androidx.annotation.StringRes
import com.roman.mydemoapp.R

enum class RoastLevel(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val example: Int
) {
    Rare(R.string.settings_level_rare, R.string.settings_level_rare_description, R.string.settings_example_rare),
    MediumRare(R.string.settings_level_medium_rare, R.string.settings_level_medium_rare_description, R.string.settings_example_medium_rare),
    Medium(R.string.settings_level_medium, R.string.settings_level_medium_description, R.string.settings_example_medium),
    MediumWell(R.string.settings_level_medium_well, R.string.settings_level_medium_well_description, R.string.settings_example_medium_well),
    WellDone(R.string.settings_level_well_done, R.string.settings_level_well_done_description, R.string.settings_example_well_done)
}
