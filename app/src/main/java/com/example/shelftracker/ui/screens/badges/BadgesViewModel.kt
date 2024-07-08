package com.example.shelftracker.ui.screens.badges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftracker.data.database.Badge
import com.example.shelftracker.data.repositories.BadgesRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class BadgesViewModel (
    private val badgesRepository: BadgesRepository
) : ViewModel() {

    fun getBadge(title: String, user: String) : Deferred<Badge?> = viewModelScope.async {
        return@async badgesRepository.getBadge(title, user)
    }
}