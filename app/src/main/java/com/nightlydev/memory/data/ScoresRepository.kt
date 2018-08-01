package com.nightlydev.memory.data

import android.arch.lifecycle.LiveData
import com.nightlydev.memory.App
import com.nightlydev.memory.data.db.ScoreDao
import com.nightlydev.memory.model.Difficulty
import com.nightlydev.memory.model.Score
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 1-8-18
 */
class ScoresRepository {

    private val scoreDao: ScoreDao by lazy { App.gameDatabase.scoreDao() }

    fun getScores(difficulty: Difficulty): LiveData<List<Score>> {
        return scoreDao.getScores(difficulty.name)
    }

    fun saveScore(score: Score) {
        Single.fromCallable {
            scoreDao.insert(score)
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe()

    }

    fun deleteScore(score: Score) {
        scoreDao.delete(score)
    }
}