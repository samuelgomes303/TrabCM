package com.example.trabalho.MOD
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trabalho.DAO.ProjectDao
import com.example.trabalho.DAO.TaskDao
import com.example.trabalho.DAO.TaskIndicationDao
import com.example.trabalho.DAO.UserDao
import com.example.trabalho.data.ProjectEntity
import com.example.trabalho.data.TaskEntity
import com.example.trabalho.data.TaskIndicationEntity
import com.example.trabalho.data.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProjectEntity::class,
        TaskEntity::class,
        TaskIndicationEntity::class
    ],
    version = 1
)
abstract class BaseDados : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun taskIndicationDao(): TaskIndicationDao

    companion object {
        const val NOME_BASE_DADOS = "gestor_projetos_db"

        @Volatile
        private var INSTANCE: BaseDados? = null

        fun getInstance(context: Context): BaseDados {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDados::class.java,
                    NOME_BASE_DADOS
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}