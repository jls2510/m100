package com.m100.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AccessDBConnection {

    private static final Logger log =
        LogManager.getLogger(AccessDBConnection.class);

    // private static String url =
    // "jdbc:ucanaccess://D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.mdb";
    private static String url =
        "jdbc:ucanaccess://D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.accdb";

    private static Connection conn = null;
    private static LocalDateTime connTimestamp = null;

    public static Connection getConnection() throws SQLException {

        // validate connection
        if ((conn == null || connTimestamp == null)
            || (connTimestamp.plusSeconds(60).isBefore(LocalDateTime.now())
                && !conn.isValid(1))) {
            log.debug(
                "AccessDBConnection.getConnection(): connection not valid; rebuilding connection.");
            conn = DriverManager.getConnection(url);
            connTimestamp = LocalDateTime.now();
        }

        return conn;

    } // getConnection()

}
