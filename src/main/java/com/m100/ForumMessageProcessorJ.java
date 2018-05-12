package com.m100;

import com.healthmarketscience.jackcess.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.TextProcessor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public final class ForumMessageProcessorJ {
    private static final Logger log = LogManager.getLogger(ForumMessageProcessorJ.class);
    private static final String snitzDatabaseURL = "D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.scratch.mdb";

    /**
     * @param args
     */
    public static final void main(String[] args) {

        try {
            //fixTextIssues("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE");
            //convertBBCodeToHtml("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE");

            fixTextIssues("FORUM_REPLY", "REPLY_ID", "R_MESSAGE");
            //convertBBCodeToHtml("FORUM_REPLY", "REPLY_ID", "R_MESSAGE");
        } catch (Exception e) {
            e.printStackTrace();
        }

    } // main()

    /**
     * @param tableName
     * @param idColumnName
     * @param messageColumnName
     * @throws IOException
     */
    private static void convertBBCodeToHtml(String tableName, String idColumnName, String messageColumnName) throws IOException {
        
        if (tableName == null || idColumnName == null || messageColumnName == null) {
            log.error("Parameters must not be null.  Cannot continue.");
            return;
        }

        log.debug("convertBBCodeToHtml() for tableAndColumn " + tableName + "; " + messageColumnName);

        // use Jackcess
        final Database db = DatabaseBuilder.open(new File(snitzDatabaseURL));
        final Table table = db.getTable(tableName);
        final IndexCursor cursor = CursorBuilder.createCursor(table.getPrimaryKeyIndex());

        // KefirBB processor for BBCode to HTML conversion
        final TextProcessor processor = BBProcessorFactory.getInstance().create();
        int updatedMessageCount = 0;
        final Iterator<Row> cursorIterator = cursor.iterator();

        while (cursorIterator.hasNext()) {
            final Row row = (Row) cursorIterator.next();
            log.debug("Row ID: " + row.get(idColumnName));

            // reply 15242 has a problem
            if (tableName.equals("FORUM_REPLY")) {
                final Integer replyId = (Integer) row.get(idColumnName);

                if (replyId != null && replyId == 15242) {
                    log.debug("***** Skipping Reply 15242");
                    continue;
                }
            }

            String message = String.valueOf(row.get(messageColumnName));

            // replace BBCode markup with HTML
            // if there are any "[" characters then we will assume there is BBCode markup
            if (message.contains("[")) {

                // convert BBCode to HTML (KefirBB)
                message = processor.process(message);

                // save and update row
                row.put(messageColumnName, message);

                ++updatedMessageCount;
                log.debug(message);
                log.debug("........................................................................................");
                table.updateRow(row);
            }
        }

        log.debug("Number of updated messages = " + updatedMessageCount);
        return;
    } // convertBBCodeToHtml()


    /**
     * @param tableName
     * @param idColumnName
     * @param messageColumnName
     * @throws IOException
     */
    private static void fixTextIssues(String tableName, String idColumnName, String messageColumnName) throws IOException {
        if (tableName == null || idColumnName == null || messageColumnName == null) {
            log.error("Parameters must not be null.  Cannot continue.");
            return;
        }

        log.debug("fixTextIssues() for table and column " + tableName + "; " + messageColumnName);

        // use Jackcess
        final Database db = DatabaseBuilder.open(new File(snitzDatabaseURL));

        final Table table = db.getTable(tableName);
        final IndexCursor cursor = CursorBuilder.createCursor(table.getPrimaryKeyIndex());
        int updatedMessageCount = 0;

        // the spaces in names within image tags break the replacement process
        // we have to replace the spaces with underscores
        // we will also have to do the same in the image folders
        final HashMap<String, String> replacementPairs = new HashMap<String, String>();
        replacementPairs.put("/Aaron H/", "/Aaron_H/");
        replacementPairs.put("/Alan Buckley/", "/Alan_Buckley/");
        replacementPairs.put("/Alex E/", "/Alex_E/");
        replacementPairs.put("/Anthony Baron/", "/Anthony_Baron/");
        replacementPairs.put("/Art Love/", "/Art_Love/");
        replacementPairs.put("/Be Free/", "/Be_Free/");
        replacementPairs.put("/Bill Kaidbey/", "/Bill_Kaidbey/");
        replacementPairs.put("/chip pischel/", "/chip_pischel/");
        replacementPairs.put("/Chris Johnson/", "/Chris_Johnson/");
        replacementPairs.put("/Danny W/", "/Danny_W/");
        replacementPairs.put("/gary kuster/", "/gary_kuster/");
        replacementPairs.put("/Grant V/", "/Grant_V/");
        replacementPairs.put("/Jack English/", "/Jack_English/");
        replacementPairs.put("/james lawson/", "/james_lawson/");
        replacementPairs.put("/john erbe/", "/john_erbe/");
        replacementPairs.put("/Joseph Long/", "/Joseph_Long/");
        replacementPairs.put("/Kai McRae/", "/Kai_McRae/");
        replacementPairs.put("/Martin L/", "/Martin_L/");
        replacementPairs.put("/Michael Jekot/", "/Michael_Jekot/");
        replacementPairs.put("/Nick Papadakis/", "/Nick_Papadakis/");
        replacementPairs.put("/Phil OBrien/", "/Phil_OBrien/");
        replacementPairs.put("/Robert Jenkins/", "/Robert_Jenkins/");
        replacementPairs.put("/Robert Webster/", "/Robert_Webster/");
        replacementPairs.put("/Ron B/", "/Ron_B/");
        replacementPairs.put("/S class/", "/S_class/");
        replacementPairs.put("/Silver Baron/", "/Silver_Baron/");
        replacementPairs.put("/Squiggle Dog/", "/Squiggle_Dog/");
        replacementPairs.put("/Stefan Matthee/", "/Stefan_Matthee/");
        replacementPairs.put("/Stu Hammel/", "/Stu_Hammel/");
        replacementPairs.put("/T.J. Woods/", "/T.J._Woods/");
        replacementPairs.put("/The Tsukiji Kid/", "/The_Tsukiji_Kid/");
        replacementPairs.put("/Wallace Wheeler/", "/Wallace_Wheeler/");
        replacementPairs.put(" [/img]", "[/img]");
        // other issues
        replacementPairs.put("&amp;", "&");
        replacementPairs.put("&gt;", ">");
        replacementPairs.put("&lt;", "<");
        Iterator<Row> cursorIterator = cursor.iterator();

        // iterate over the rows
        while (cursorIterator.hasNext()) {
            final Row row = (Row) cursorIterator.next();
            log.debug("Row ID: " + row.get(idColumnName));
            String message = String.valueOf(row.get(messageColumnName));
            boolean needsUpdate = false;

            // iterate over replacementPairs; do replacements
            Iterator<String> replacementPairsIterator = replacementPairs.keySet().iterator();
            while (replacementPairsIterator.hasNext()) {
                final String key = replacementPairsIterator.next();
                final String value = replacementPairs.get(key);

                if (message.contains(key)) {
                    log.debug("found: " + key);
                    message = message.replaceAll(key, value);
                    needsUpdate = true;
                }
            }

            if (needsUpdate) {
                row.put(messageColumnName, message);
                table.updateRow(row);
                ++updatedMessageCount;
                log.debug(message);
            }
        }

        log.debug("Number of fixed text issues = " + updatedMessageCount);

    } // fixTextIssues()

}
