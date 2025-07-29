package com.youngsik.jinada.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.youngsik.jinada.data.remote.FirestoreMemoDataSourceImpl
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.repository.MemoRepositoryImpl
import com.youngsik.jinada.presentation.viewmodel.MemoMapViewModel
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel

class ViewModelFactory(private val repository: MemoRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]?: throw IllegalStateException("Application context not found.")

        return when {
            modelClass.isAssignableFrom(MemoViewModel::class.java) ->
                MemoViewModel(repository) as T

            modelClass.isAssignableFrom(MemoMapViewModel::class.java) ->
                MemoMapViewModel(application,repository) as T

            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}