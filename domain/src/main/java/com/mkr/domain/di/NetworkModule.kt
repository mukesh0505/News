package com.mkr.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Named(value = EndPoint.News)
    fun getNewsEndpoint(): String {
        return ""
    }

    @Provides
    @Named(value = EndPoint.User)
    fun getUserEndpoint(): String {
        return ""
    }
}

object EndPoint {
    const val News = "News"
    const val User = "User"
}