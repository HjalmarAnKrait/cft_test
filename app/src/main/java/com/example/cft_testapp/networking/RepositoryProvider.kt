package com.example.cft_testapp.networking

object RepositoryProvider {
    fun provideRepository(): Repository{
        return Repository(ApiService.create())
    }
}