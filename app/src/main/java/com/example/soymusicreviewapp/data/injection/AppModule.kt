package com.example.soymusicreviewapp.data.injection

import com.example.soymusicreviewapp.data.datasource.services.ReviewRetrofitService
import com.example.soymusicreviewapp.data.datasource.services.SongRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesReviewRetrofitService(retrofit: Retrofit): ReviewRetrofitService{
        return retrofit.create(ReviewRetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun providesSongRetrofitService(retrofit: Retrofit): SongRetrofitService {
        return retrofit.create(SongRetrofitService::class.java)
    }
}
