package com.youngsik.jinada.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.youngsik.jinada.data.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.repository.MemoRepositoryImpl
import com.youngsik.jinada.presentation.viewmodel.MemoMapViewModel
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel

object ViewModelFactory : ViewModelProvider.Factory{
    private val memoDataSource = FirestoreMemoDataSourceImpl()

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return when {
            modelClass.isAssignableFrom(MemoViewModel::class.java) ->
                MemoViewModel(MemoRepositoryImpl(memoDataSource)) as T

            modelClass.isAssignableFrom(MemoMapViewModel::class.java) ->
                MemoMapViewModel(MemoRepositoryImpl(memoDataSource)) as T

            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}