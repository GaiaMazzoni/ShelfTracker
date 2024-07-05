package com.example.shelftracker.data.repositories

import com.example.shelftracker.data.database.Badge
import com.example.shelftracker.data.database.BadgesDAO

class BadgesRepository(
    private val badgesDAO: BadgesDAO
) {

    suspend fun getBadge(title: String) :Badge? {
        return badgesDAO.getBadge(title)
    }

    suspend fun upsert(badge: Badge) {
        badgesDAO.upsert(badge)
    }

    suspend fun delete(badge: Badge) = badgesDAO.delete(badge)


}