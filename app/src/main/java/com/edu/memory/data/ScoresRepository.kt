package com.edu.memory.data

import android.arch.lifecycle.LiveData
import com.edu.memory.App
import com.edu.memory.data.db.ScoreDao
import com.edu.memory.model.Difficulty
import com.edu.memory.model.Score
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by edu
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