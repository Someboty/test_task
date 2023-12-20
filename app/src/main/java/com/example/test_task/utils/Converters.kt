package com.example.test_task.utils

import android.util.Log
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
/**
 * Class that provides TypeConverters for converting between [String] and [LocalDate] using [DateTimeFormatter].
 *
 * The [Converters] class is responsible for converting [LocalDate] objects to [String] representations and vice versa.
 * It utilizes [DateTimeFormatter.ISO_LOCAL_DATE] as the default date formatter.
 *
 * @property formatter The [DateTimeFormatter] used for formatting [LocalDate] objects to [String].
 */
class Converters {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    /**
     * Converts a [String] representation of a date to a [LocalDate] object.
     *
     *
     * @param value The [String] representation of the date.
     * @return The corresponding [LocalDate] object, or `null` if the input [String] is `null`.
     */
    @TypeConverter
    fun fromDate(value: String?): LocalDate? {
        Log.i("Converters", "from date converter called")
        return value?.let { LocalDate.parse(it, formatter) }
    }
    /**
     * Converts a [LocalDate] object to its [String] representation.
     *
     * @param date The [LocalDate] object to be converted.
     * @return The [String] representation of the [LocalDate] object, or `null` if the input [LocalDate] is `null`.
     */
    @TypeConverter
    fun toDate(date: LocalDate?): String? {
        Log.i("Converters", "to date converter called")
        return date?.format(formatter)
    }
}