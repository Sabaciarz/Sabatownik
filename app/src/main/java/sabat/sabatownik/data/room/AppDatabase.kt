package sabat.sabatownik.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sabat.sabatownik.data.room.AnswerEntity
import sabat.sabatownik.data.room.QuestionEntity

@Database(
    entities = [
        QuestionEntity::class,
        AnswerEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sabatownik.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
