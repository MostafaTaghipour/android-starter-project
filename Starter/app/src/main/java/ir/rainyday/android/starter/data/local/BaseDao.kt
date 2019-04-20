package ir.rainyday.android.starter.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by taghipour on 04/02/2018.
 */

interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert
    fun insert(obj: T) : Long?
    /**
     * Insert an list in the database.
     *
     * @param list the list to be inserted.
     */
    @Insert
    fun insert(list: Collection<T>): List<Long?>
    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     */
    @Insert
    fun insert(vararg obj: T): List<Long?>




    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    fun update(obj: T)
    /**
     * Update an list from the database.
     *
     * @param list the list to be updated
     */
    @Update
    fun update(list: Collection<T>)
    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    fun update(vararg obj: T)




    /**
     * Upsert an object in the database.
     *
     * @param obj the object to be upserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(obj: T)  : Long?
    /**
     * Upsert an list in the database.
     *
     * @param list the list to be upserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(list: Collection<T>): List<Long?>
    /**
     * Upsert an array of objects in the database.
     *
     * @param obj the objects to be upserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg obj: T): List<Long?>




    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(obj: T)
    /**
     * Delete an list from the database
     *
     * @param list the list  to be deleted
     */
    @Delete
    fun delete(list: Collection<T>)
    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(vararg obj: T)
}