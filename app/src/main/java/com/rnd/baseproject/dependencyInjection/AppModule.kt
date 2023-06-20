package com.rnd.baseproject.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.rnd.baseproject.database.LocalDataSource
import com.rnd.baseproject.retrofit_api.RemoteDataSource
import com.rnd.baseproject.retrofit_api.request.AuthApi
import com.rnd.baseproject.tools.ConstValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

@Singleton
@Provides
fun provideLocalDataSource(@ApplicationContext context: Context):LocalDataSource{
    return Room.databaseBuilder(context,LocalDataSource::class.java,ConstValue.APP_DATABASE_NAME).allowMainThreadQueries().build()
}
    @Singleton
    @Provides
    fun provideNoteDao(database:LocalDataSource) = database.getNoteDao()

    @Singleton
    @Provides
    fun provideAuthApi(remoteDataSource: RemoteDataSource):AuthApi{
        return remoteDataSource.buildApi(AuthApi::class.java)
    }

}