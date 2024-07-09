package com.example.shelftracker.data.repositories

import com.example.shelftracker.data.database.Badge
import com.example.shelftracker.data.database.BadgesDAO
import kotlinx.coroutines.flow.Flow

class BadgesRepository(
    private val badgesDAO: BadgesDAO
) {

    suspend fun getAllFromUser(user: String): Flow<List<Badge>> {
        return badgesDAO.getAllFromUser(user)
    }

    suspend fun getBadge(title: String, user: String) :Badge? {
        return badgesDAO.getBadge(title, user)
    }

    suspend fun upsert(badge: Badge) {
        badgesDAO.upsert(badge)
    }

    suspend fun delete(badge: Badge) = badgesDAO.delete(badge)


}