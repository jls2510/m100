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
import org.kefirsf.bb.BBProcessorFactory
import org.kefirsf.bb.TextProcessor


class TestQueries {

    companion object {

        private val snitzDatabaseURL = "D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.scratch.mdb"

        @JvmStatic
        fun main(args: Array<String>) {

            query6()

        } // main()


        fun query6() {

            // use Jackcess
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

            val table = db.getTable("FORUM_TOPICS")
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)

            // KefirBB processor for BBCode to HTML conversion
            val processor = BBProcessorFactory.getInstance().create()

            var updatedMessageCount = 0
            for (row in cursor) {

                val forumImgTag_1: String = "[img]http://www.m-100.co/forum/uploaded"
                val forumImgTag_2: String = "[img]http://www.m-100.cc/forum/uploaded"
                val forumImgTagTestReplacement: String = "[xxxTestingxxx]"
                val forumImgTagEnd: String = "[/img]"

                var message: String = row["T_MESSAGE"].toString()

                var messageUpdated = false

                // replace BBCode markup with HTML
                // if there are any "[" characters then we will assume there is BBCode markup
                if (message.indexOf("[") > 0) {

                    // convert BBCode to HTML
                    message = processor.process(message)

                    // save and update row
                    row.set("T_MESSAGE", message)
                    updatedMessageCount++
                    println(message)
                    //table.updateRow(row)
                }

            } // end iteration over rows

            println("Number of updated messages = $updatedMessageCount")


        } // query6()


        fun query5() {

            // use Jackcess
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

            val table = db.getTable("FORUM_TOPICS")
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)

            var updatedMessageCount = 0
            for (row in cursor) {

                val forumImgTag_1: String = "[img]http://www.m-100.co/forum/uploaded"
                val forumImgTag_2: String = "[img]http://www.m-100.cc/forum/uploaded"
                val forumImgTagTestReplacement: String = "[xxxTestingxxx]"
                val forumImgTagEnd: String = "[/img]"

                var message: String = row["T_MESSAGE"].toString()

                var messageUpdated = false

                // check for first string
                if (message.indexOf(forumImgTag_1) > 0) {
                    message = message.replace(forumImgTag_1, forumImgTagTestReplacement)
                    messageUpdated = true
                    row.set("T_MESSAGE", message)
                }
                // check for second string
                if (message.indexOf(forumImgTag_2) > 0) {
                    message = message.replace(forumImgTag_2, forumImgTagTestReplacement)
                    messageUpdated = true
                    row.set("T_MESSAGE", message)
                }

                // update the row if necessary
                if (messageUpdated) {
                    updatedMessageCount++
                    //println(message)
                    table.updateRow(row)
                }

            } // end iteration over rows

            println("Number of updated messages = $updatedMessageCount")


        } // query5()


        fun query4() {

            // use Jackcess
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

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
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

            val table = db.getTable("FORUM_TOPICS")
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)
            //for (row in cursor) {
            for (index: Int in 1..100) {
                cursor.findNextRow(Collections.singletonMap("T_AUTHOR", 42))
                val row = cursor.getCurrentRow()
//                println(String.format("T_SUBJECT=%s, T_MESSAGE='%s'.",
//                        row["T_SUBJECT"], row["T_MESSAGE"]))

                var message: String = row["T_MESSAGE"].toString()

                val forumImgTagBegin: String = "[img]http://www.m-100.cc/forum/uploaded"
                val forumImgTagTestReplacement: String = "[xxxTestingxxx]"
                val forumImgTagEnd: String = "[/img]"

                if (message.indexOf(forumImgTagBegin) > 0) {
                    val topicIdCol: Column = table.getColumn("TOPIC_ID")
                    message = message.replace(forumImgTagBegin, forumImgTagTestReplacement)
                    println(message)
                    return // only do it once
                }

                //cursor.updateCurrentRow()
            }

        } // query3()


        fun query2() {

            // use Jackcess
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

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