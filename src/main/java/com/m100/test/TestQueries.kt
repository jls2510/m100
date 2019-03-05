package com.m100.test

import com.healthmarketscience.jackcess.Column
import com.healthmarketscience.jackcess.CursorBuilder
import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.IndexCursor
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

            //fixTextIssues()
            convertBBCodeToHtml()

        } // main()


        fun convertBBCodeToHtml() {

            // use Jackcess
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

            val table = db.getTable("FORUM_TOPICS")
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)

            // KefirBB processor for BBCode to HTML conversion
            val processor = BBProcessorFactory.getInstance().create()

            var updatedMessageCount = 0
            for (row in cursor) {

                var message: String = row["T_MESSAGE"].toString()

                // replace BBCode markup with HTML
                // if there are any "[" characters then we will assume there is BBCode markup
                if (message.indexOf("[") > 0) {

                    // convert BBCode to HTML (KefirBB)
                    message = processor.process(message)

                    // save and update row
                    row.set("T_MESSAGE", message)
                    updatedMessageCount++
                    println(message)
                    println("........................................................................................")
                    //table.updateRow(row)
                }

            } // end iteration over rows

            println("Number of updated messages = $updatedMessageCount")


        } // convertBBCodeToHtml()


        fun fixNameSpaces() {

            // use Jackcess
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

            val table = db.getTable("FORUM_TOPICS")
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)

            var updatedMessageCount = 0

            for (row in cursor) {

                val replacementPairs: ArrayList<Pair<String, String>> = ArrayList()

                // the spaces in names within image tags break the replacement process
                // we have to replace the spaces with underscores
                // we will also have to do the same in the image folders
                replacementPairs.add(Pair("/Aaron H/", "/Aaron_H/"))
                replacementPairs.add(Pair("/Alan Buckley/", "/Alan_Buckley/"))
                replacementPairs.add(Pair("/Alex E/", "/Alex_E/"))
                replacementPairs.add(Pair("/Anthony Baron/", "/Anthony_Baron/"))
                replacementPairs.add(Pair("/Art Love/", "/Art_Love/"))
                replacementPairs.add(Pair("/Be Free/", "/Be_Free/"))
                replacementPairs.add(Pair("/Bill Kaidbey/", "/Bill_Kaidbey/"))
                replacementPairs.add(Pair("/chip pischel/", "/chip_pischel/"))
                replacementPairs.add(Pair("/Chris Johnson/", "/Chris_Johnson/"))
                replacementPairs.add(Pair("/Danny W/", "/Danny_W/"))
                replacementPairs.add(Pair("/gary kuster/", "/gary_kuster/"))
                replacementPairs.add(Pair("/Grant V/", "/Grant_V/"))
                replacementPairs.add(Pair("/Jack English/", "/Jack_English/"))
                replacementPairs.add(Pair("/james lawson/", "/james_lawson/"))
                replacementPairs.add(Pair("/john erbe/", "/john_erbe/"))
                replacementPairs.add(Pair("/Joseph Long/", "/Joseph_Long/"))
                replacementPairs.add(Pair("/Kai McRae/", "/Kai_McRae/"))
                replacementPairs.add(Pair("/Martin L/", "/Martin_L/"))
                replacementPairs.add(Pair("/Michael Jekot/", "/Michael_Jekot/"))
                replacementPairs.add(Pair("/Nick Papadakis/", "/Nick_Papadakis/"))
                replacementPairs.add(Pair("/Phil OBrien/", "/Phil_OBrien/"))
                replacementPairs.add(Pair("/Robert Jenkins/", "/Robert_Jenkins/"))
                replacementPairs.add(Pair("/Robert Webster/", "/Robert_Webster/"))
                replacementPairs.add(Pair("/Ron B/", "/Ron_B/"))
                replacementPairs.add(Pair("/S class/", "/S_class/"))
                replacementPairs.add(Pair("/Silver Baron/", "/Silver_Baron/"))
                replacementPairs.add(Pair("/Squiggle Dog/", "/Squiggle_Dog/"))
                replacementPairs.add(Pair("/Stefan Matthee/", "/Stefan_Matthee/"))
                replacementPairs.add(Pair("/Stu Hammel/", "/Stu_Hammel/"))
                replacementPairs.add(Pair("/T.J. Woods/", "/T.J._Woods/"))
                replacementPairs.add(Pair("/The Tsukiji Kid/", "/The_Tsukiji_Kid/"))
                replacementPairs.add(Pair("/Wallace Wheeler/", "/Wallace_Wheeler/"))

                var message: String = row["T_MESSAGE"].toString()

                // do replacements
                replacementPairs.forEach { replacementPair ->

                    if (message.indexOf(replacementPair.first) > 0) {

                        message = message.replace(replacementPair.first, replacementPair.second)
                        row.set("T_MESSAGE", message)
                        updatedMessageCount++
                        //println(message)
                        table.updateRow(row)
                    }
                }

            } // end iteration over rows

            println("Number of updated messages = $updatedMessageCount")


        } // fixTextIssues()


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


    } // companion object

}