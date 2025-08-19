package com.example.spender.feature.auth

import com.example.spender.feature.auth.data.FirebaseAuthDataSource
import com.example.spender.feature.auth.data.KakaoDataSource
import com.example.spender.feature.auth.data.NaverDataSource
import com.example.spender.feature.auth.domain.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(): FirebaseAuthDataSource {
        return FirebaseAuthDataSource()
    }

    @Provides
    @Singleton
    fun provideKakaoDataSource(): KakaoDataSource {
        return KakaoDataSource()
    }

    @Provides
    @Singleton
    fun provideNaverDataSource(): NaverDataSource {
        return NaverDataSource()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuthDataSource: FirebaseAuthDataSource,
        naverDataSource: NaverDataSource,
        kakaoDataSource: KakaoDataSource
    ): AuthRepository {
        return AuthRepository(firebaseAuthDataSource, naverDataSource, kakaoDataSource)
    }
}