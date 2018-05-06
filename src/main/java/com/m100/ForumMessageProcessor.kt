package com.m100

import com.healthmarketscience.jackcess.CursorBuilder
import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.IndexCursor
import org.apache.logging.log4j.LogManager
import org.kefirsf.bb.BBProcessorFactory
import java.io.File
import java.util.*

class ForumMessageProcessor {

    companion object {

        val log = LogManager.getLogger(ForumMessageProcessor::class.java)

        private val snitzDatabaseURL = "D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.scratch.mdb"

        @JvmStatic
        fun main(args: Array<String>) {


            //fixTextIssues("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE")
            //convertBBCodeToHtml("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE")

            fixTextIssues("FORUM_REPLY", "REPLY_ID", "R_MESSAGE")
            //convertBBCodeToHtml("FORUM_REPLY", "REPLY_ID", "R_MESSAGE")


        } // main()


        fun convertBBCodeToHtml(tableName: String, idColumnName: String, messageColumnName: String) {

            log.debug("convertBBCodeToHtml() for tableAndColumn ")

            // use Jackcess
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

            val table = db.getTable(tableName)
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)

            // KefirBB processor for BBCode to HTML conversion
            val processor = BBProcessorFactory.getInstance().create()

            var updatedMessageCount = 0
            for (row in cursor) {

                log.debug("Row ID: " + row[idColumnName])

                // reply 15242 has a problem
                if (tableName.equals("FORUM_REPLY") && row[idColumnName]!!.equals(15242)) {
                    log.debug("***** Skipping Reply 15242")
                    continue
                }

                var message: String = row[messageColumnName].toString()

                // replace BBCode markup with HTML
                // if there are any "[" characters then we will assume there is BBCode markup
                if (message.indexOf("[") > -1) {

                    // convert BBCode to HTML (KefirBB)
                    message = processor.process(message)

                    // save and update row
                    row.set(messageColumnName, message)
                    updatedMessageCount++
                    log.debug(message)
                    log.debug("........................................................................................")
                    table.updateRow(row)
                }

            } // end iteration over rows

            log.debug("Number of updated messages = $updatedMessageCount")


        } // convertBBCodeToHtml()


        fun fixTextIssues(tableName: String, idColumnName: String, messageColumnName: String) {

            log.debug("fixTextIssues() for table " + tableName)

            // use Jackcess
            val db = DatabaseBuilder.open(File(snitzDatabaseURL))

            val table = db.getTable(tableName)
            val cursor: IndexCursor = CursorBuilder.createCursor(table.primaryKeyIndex)

            var updatedMessageCount = 0

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
            // other issues
            replacementPairs.add(Pair(" [/img]", "[/img]")) // space before close img tag
            replacementPairs.add(Pair("&amp;", "&")) // &amp;
            replacementPairs.add(Pair("&gt;", ">"))
            replacementPairs.add(Pair("&lt;", "<"))

            // iterate over the rows
            for (row in cursor) {

                log.debug("Row ID: " + row[idColumnName])

                var message: String = row[messageColumnName].toString()

                // do replacements
                var needsUpdate = false
                replacementPairs.forEach { replacementPair ->
                    if (message.indexOf(replacementPair.first) > -1) {
                        log.debug("found: " + replacementPair.first)
                        message = message.replace(replacementPair.first, replacementPair.second)
                        needsUpdate = true
                    }

                } // end iteration over replacement pairs

                // update after doing replacements
                if (needsUpdate) {
                    row.set(messageColumnName, message)
                    table.updateRow(row)
                    updatedMessageCount++
                    log.debug(message)
                }

            } // end iteration over rows

            log.debug("Number of fixed text issues = $updatedMessageCount")

        } // fixTextIssues()

    }

}
