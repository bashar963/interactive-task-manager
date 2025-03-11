package tech.devbashar.interactivetaskmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.devbashar.interactivetaskmanager.domain.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("Select * From tasks Where id = :id")
    fun getById(id: Int): Flow<TaskEntity?>

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Query(
        """
    SELECT * FROM tasks
        WHERE 
            (:filter = 'All' OR (isCompleted = 1 AND :filter = 'Completed') OR (isCompleted = 0 AND :filter = 'Pending'))
        ORDER BY 
            CASE WHEN :sort = 'Title' THEN title END ASC,
            CASE WHEN :sort = 'Date' THEN createdAt END DESC,
            CASE WHEN :sort = 'Priority' THEN 
                CASE priority 
                    WHEN 'HIGH' THEN 1 
                    WHEN 'MEDIUM' THEN 2 
                    WHEN 'LOW' THEN 3 
                END 
            END ASC
    """,
    )
    fun getAll(
        filter: String,
        sort: String,
    ): Flow<List<TaskEntity>>
}
