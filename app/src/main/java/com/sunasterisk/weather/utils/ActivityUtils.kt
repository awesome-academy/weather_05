package com.sunasterisk.weather.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object ActivityUtils {

    fun addFragmentToActivity(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        idRes: Int
    ) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .add(idRes, fragment)
            .commit()
    }

    fun replaceFragmentToActivity(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        idRes: Int
    ) {
       fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(idRes, fragment)
            .commit()
    }
}
