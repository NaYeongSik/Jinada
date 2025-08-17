package com.youngsik.jinada.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.repository.NaverRepository
import com.youngsik.jinada.data.repository.UserRepository
import com.youngsik.jinada.presentation.viewmodel.MemoMapViewModel
import com.youngsik.jinada.presentation.viewmodel.MemoViewModel
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel

class ViewModelFactory(private val userRepository: UserRepository, private val repository: MemoRepository, private val locationRepository: CurrentLocationRepository, private val dataStoreRepository: DataStoreRepository, private val naverRepository: NaverRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]?: throw IllegalStateException("Application context not found.")

        return when {
            modelClass.isAssignableFrom(MemoViewModel::class.java) ->
                MemoViewModel(repository, dataStoreRepository) as T

            modelClass.isAssignableFrom(MemoMapViewModel::class.java) ->
                MemoMapViewModel(
                    application,
                    repository,
                    locationRepository,
                    naverRepository,
                    dataStoreRepository
                ) as T

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(userRepository, dataStoreRepository) as T

            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}