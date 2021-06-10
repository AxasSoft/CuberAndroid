package ru.wood.cuber.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.wood.cuber.room.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)!!
    }
    @Provides
    fun provideDaoTrees(appDatabase: AppDatabase): DaoTrees {
        return appDatabase.daoTrees()
    }
    @Provides
    fun provideDaoMyOrders(appDatabase: AppDatabase): DaoMyOrder {
        return appDatabase.daoOrder()
    }
    @Provides
    fun provideDaoContains(appDatabase: AppDatabase): DaoContains {
        return appDatabase.daoContains()
    }
    @Provides
    fun provideDaoResults(appDatabase: AppDatabase): DaoResults {
        return appDatabase.daoResults()
    }
    @Provides
    fun provideDaoContainRedact(appDatabase: AppDatabase): DaoContainRedact {
        return appDatabase.daoContainRedact()
    }
    @Provides
    fun provideDaoTreeRedact(appDatabase: AppDatabase): DaoTreeRedact {
        return appDatabase.daoTreeRedact()
    }
    @Provides
    fun provideDaoExcel(appDatabase: AppDatabase): DaoExcel {
        return appDatabase.daoExcel()
    }
}
