package com.m100.service

import org.apache.logging.log4j.LogManager
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class AccessDBConnection {

    companion object {

        val log = LogManager.getLogger(AccessDBConnection::class.java)

        //val url = "jdbc:ucanaccess://D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.mdb"
        val url = "jdbc:ucanaccess://D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.accdb"

        @JvmStatic
        var conn: Connection = DriverManager.getConnection(url)
        var connTimestamp = LocalDateTime.now()

        @JvmStatic
        fun getConnection(): Connection {

            // validate connection
            if (connTimestamp.plusSeconds(60).isBefore(LocalDateTime.now()) && !conn.isValid(1)) {
                log.debug("AccessDBConnection.getConnection(): connection not valid; rebuilding connection.")
                conn = DriverManager.getConnection(url)
                connTimestamp = LocalDateTime.now()
            }

            return conn

        } // getConnection()

    } // companion object

}