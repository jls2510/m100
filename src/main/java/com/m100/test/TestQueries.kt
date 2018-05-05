package com.m100.test

import com.healthmarketscience.jackcess.Column
import com.healthmarketscience.jackcess.CursorBuilder
import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.IndexCursor
import com.m100.service.AccessDBConnection
import java.io.File
import java.sql.Connection
import java.sql.SQLException
import java.util.*


class TestQueries {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            query4()

        } // main()


        fun query4() {

            // use Jackcess
            val url = "D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.mdb"
            val db = DatabaseBuilder.open(File(url))

            val table = db.getTable("FORUM_TOPICS")
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)
            //for (row in cursor) {
            cursor.findFirstRow(Collections.singletonMap("TOPIC_ID", 9696))

            val row = cursor.getCurrentRow()
//                println(String.format("T_SUBJECT=%s, T_MESSAGE='%s'.",
//                        row["T_SUBJECT"], row["T_MESSAGE"]))

            val message: String = row["T_MESSAGE"].toString()

            val forumImgTagBegin: String = "[img]http://www.m-100.cc/forum/uploaded"
            val forumImgTagEnd: String = "[/img]"

//            for (column in table.columns) {
//                println(column.columnIndex.toString() + ": " + column.name + ": " + column.type)
//            }

            // this works!
            row.set("T_LAST_EDIT", "Hello")
            table.updateRow(row)

            //cursor.updateCurrentRow()

        } // query4()


        fun query3() {

            // use Jackcess
            val url = "D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.accdb"
            val db = DatabaseBuilder.open(File(url))

            val table = db.getTable("FORUM_TOPICS")
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)
            //for (row in cursor) {
            for (index: Int in 1..100) {
                cursor.findNextRow(Collections.singletonMap("T_AUTHOR", 42))
                val row = cursor.getCurrentRow()
//                println(String.format("T_SUBJECT=%s, T_MESSAGE='%s'.",
//                        row["T_SUBJECT"], row["T_MESSAGE"]))

                val message: String = row["T_MESSAGE"].toString()

                val forumImgTagBegin: String = "[img]http://www.m-100.cc/forum/uploaded"
                val forumImgTagEnd: String = "[/img]"

                if (message.indexOf(forumImgTagBegin) > 0) {
                    val topicIdCol: Column = table.getColumn("TOPIC_ID")
                    println(message)
                }

                //cursor.updateCurrentRow()
            }

        } // query3()


        fun query2() {

            // use Jackcess
            val url = "D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.accdb"
            val db = DatabaseBuilder.open(File(url))

            val table = db.getTable("FORUM_MEMBERS")

            for (row in table) {
                for (column in table.columns) {
                    val columnName = column.name
                    val value = row[columnName]
                    if (value != null) {
                        println("Column " + columnName + "(" + column.type + "): "
                                + value + " (" + value::class.java + ")")
                    }
                }
            }

        }


        fun query1() {
            val conn: Connection
            try {
                conn = AccessDBConnection.getConnection()

                val s = conn.createStatement()

                val query: String
                // query = "SELECT [T_MESSAGE] FROM [FORUM_TOPICS] WHERE [T_AUTHOR]
                // = 12 LIMIT 10";
                query = "SELECT * FROM FORUM_TOPICS LIMIT 10"
                val rs = s.executeQuery(query)
                val rsmd = rs.metaData
                val columnsNumber = rsmd.columnCount
                while (rs.next()) {
                    for (index in 1..columnsNumber) {
                        print(rs.getString(index) + "; ")
                    }
                    println()
                }
            } catch (e: SQLException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }


        }

    } // companion object
}