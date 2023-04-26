package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.infra.Clock

interface CurrentPeriodProvider {
    fun currentPeriod(periodDays: Habits.HabitPeriodDays): Long
}

class CurrentPeriodProviderImpl(private val clock: Clock): CurrentPeriodProvider {
    override fun currentPeriod(periodDays: Habits.HabitPeriodDays): Long {
        val curTime = clock.currentTime()
        return (curTime / 86_400_000) / periodDays.periodDays
    }

}