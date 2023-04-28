package com.github.orkest.data

import java.time.LocalDateTime

data class OrkestDate (var year:Int = 0,
                       var month:Int = 0,
                       var dayOfMonth: Int = 0,
                       var hour: Int = 0,
                       var minute: Int = 0,
                       var second: Int = 0) :Comparable<OrkestDate> {

    /**
     * Constructor for OrkestDate, takes a LocalDateTime and converts it to an OrkestDate
     */
    constructor(date: LocalDateTime) :
            this(date.year, date.monthValue, date.dayOfMonth, date.hour, date.minute, date.second)

    /**
     * Converts an OrkestDate to a LocalDateTime
     */
    fun toLocalDateTime(): LocalDateTime {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second)
    }

    /**
     * Converts an OrkestDate to a String representation, same as the LocalDateTime string representation
     */
    override fun toString(): String {
        return toLocalDateTime().toString()
    }

    /**
     * Compares two objects,
     * Uses the localDateTime representation of the OrkestDate to compare
     * @param other the other object to compare to
     * @return 0 if they are equal, 1 if this is greater than other, -1 if this is less than other
     */
    override fun compareTo(other: OrkestDate): Int {
        return toLocalDateTime().compareTo(other.toLocalDateTime())
    }
}


