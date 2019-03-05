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
    private static final Logger log =
        LogManager.getLogger(ForumMessageProcessorJ.class);

    /**
     * @param args
     */
    public static final void main(String[] args) {

        log.info("ForumMessageProcessor processing file: "
            + Constants.snitzDatabaseURL + "\n");

        try {
            fixTextIssues("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE");
            convertBBCodeToHtml("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE");
            revertHtml("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE");
            swapUrls("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE");
            // run AFTER swapUrls
            fixUpImgTags("FORUM_TOPICS", "TOPIC_ID", "T_MESSAGE");

             fixTextIssues("FORUM_REPLY", "REPLY_ID", "R_MESSAGE");
             convertBBCodeToHtml("FORUM_REPLY", "REPLY_ID", "R_MESSAGE");
             revertHtml("FORUM_REPLY", "REPLY_ID", "R_MESSAGE");
             swapUrls("FORUM_REPLY", "REPLY_ID", "R_MESSAGE");
             // run AFTER swapUrls
             fixUpImgTags("FORUM_REPLY", "REPLY_ID", "R_MESSAGE");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        log.info("\nForumMessageProcessor finished processing file: "
            + Constants.snitzDatabaseURL);

    } // main()

    /**
     * @param tableName
     * @param idColumnName
     * @param messageColumnName
     * @throws IOException
     */
    private static void convertBBCodeToHtml(String tableName,
        String idColumnName, String messageColumnName) throws IOException {

        log.info("convertBBCodeToHtml() for tableAndColumn " + tableName + "; "
            + messageColumnName);

        if (tableName == null || idColumnName == null
            || messageColumnName == null) {
            log.error("Parameters must not be null.  Cannot continue.");
            return;
        }

        // use Jackcess
        final Database db =
            DatabaseBuilder.open(new File(Constants.snitzDatabaseURL));
        final Table table = db.getTable(tableName);
        final IndexCursor cursor =
            CursorBuilder.createCursor(table.getPrimaryKeyIndex());

        // KefirBB processor for BBCode to HTML conversion
        final TextProcessor processor =
            BBProcessorFactory.getInstance().create();

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

            String fieldText = String.valueOf(row.get(messageColumnName));
            String revisedFieldText = fieldText;

            // replace BBCode markup with HTML
            // if there are any "[" characters then we will assume there is
            // BBCode markup

            // convert BBCode to HTML (KefirBB)
            revisedFieldText = processor.process(revisedFieldText);

            if (!revisedFieldText.equals(fieldText)) {
                // save and update row
                row.put(messageColumnName, revisedFieldText);

                ++updatedMessageCount;
                log.debug(revisedFieldText);
                log.debug(
                    "........................................................................................");
                table.updateRow(row);
            }
        }

        log.info("Number of BBCode Replacements (entire messages) = "
            + updatedMessageCount);

    } // convertBBCodeToHtml()

    /**
     * @param tableName
     * @param idColumnName
     * @param messageColumnName
     * @throws IOException
     */
    private static void fixTextIssues(String tableName, String idColumnName,
        String messageColumnName) throws IOException {

        log.info("fixTextIssues() for table and column " + tableName + "; "
            + messageColumnName);

        if (tableName == null || idColumnName == null
            || messageColumnName == null) {
            log.error("Parameters must not be null.  Cannot continue.");
            return;
        }

        // the spaces in names within image tags break the replacement process
        // we have to replace the spaces with underscores
        // we will also have to do the same in the image folders
        final HashMap<String, String> replacementPairs =
            new HashMap<String, String>();
        replacementPairs.put("/Aaron H/", "/Aaron-H/");
        replacementPairs.put("/Alan Buckley/", "/Alan-Buckley/");
        replacementPairs.put("/Alex E/", "/Alex-E/");
        replacementPairs.put("/Anthony Baron/", "/Anthony-Baron/");
        replacementPairs.put("/Art Love/", "/Art-Love/");
        replacementPairs.put("/Be Free/", "/Be-Free/");
        replacementPairs.put("/Bill Kaidbey/", "/Bill-Kaidbey/");
        replacementPairs.put("/chip pischel/", "/chip-pischel/");
        replacementPairs.put("/Chris Johnson/", "/Chris-Johnson/");
        replacementPairs.put("/Danny W/", "/Danny-W/");
        replacementPairs.put("/gary kuster/", "/gary-kuster/");
        replacementPairs.put("/Grant V/", "/Grant-V/");
        replacementPairs.put("/Jack English/", "/Jack-English/");
        replacementPairs.put("/james lawson/", "/james-lawson/");
        replacementPairs.put("/john erbe/", "/john-erbe/");
        replacementPairs.put("/Joseph Long/", "/Joseph-Long/");
        replacementPairs.put("/Kai McRae/", "/Kai-McRae/");
        replacementPairs.put("/Martin L/", "/Martin-L/");
        replacementPairs.put("/Michael Jekot/", "/Michael-Jekot/");
        replacementPairs.put("/Nick Papadakis/", "/Nick-Papadakis/");
        replacementPairs.put("/Phil OBrien/", "/Phil-OBrien/");
        replacementPairs.put("/Robert Jenkins/", "/Robert-Jenkins/");
        replacementPairs.put("/Robert Webster/", "/Robert-Webster/");
        replacementPairs.put("/Ron B/", "/Ron-B/");
        replacementPairs.put("/S class/", "/S-class/");
        replacementPairs.put("/Silver Baron/", "/Silver-Baron/");
        replacementPairs.put("/Squiggle Dog/", "/Squiggle-Dog/");
        replacementPairs.put("/Stefan Matthee/", "/Stefan-Matthee/");
        replacementPairs.put("/Stu Hammel/", "/Stu-Hammel/");
        replacementPairs.put("/T.J. Woods/", "/T.J.-Woods/");
        replacementPairs.put("/The Tsukiji Kid/", "/The-Tsukiji-Kid/");
        replacementPairs.put("/Wallace Wheeler/", "/Wallace-Wheeler/");

        int updatedMessageCount = doReplacements(tableName, idColumnName,
            messageColumnName, replacementPairs);
        log.info("Number of text fixes = " + updatedMessageCount);

    } // fixTextIssues()

    /**
     * @param tableName
     * @param idColumnName
     * @param messageColumnName
     * @throws IOException
     */
    private static void revertHtml(String tableName, String idColumnName,
        String messageColumnName) throws IOException {

        log.info("revertHtml() for table and column " + tableName + "; "
            + messageColumnName);

        if (tableName == null || idColumnName == null
            || messageColumnName == null) {
            log.error("Parameters must not be null.  Cannot continue.");
            return;
        }

        final HashMap<String, String> replacementPairs =
            new HashMap<String, String>();
        replacementPairs.put("&amp;", "&");
        replacementPairs.put("&gt;", ">");
        replacementPairs.put("&lt;", "<");
        replacementPairs.put("&apos;", "'");
        replacementPairs.put("&quot;", "\"");

        int updatedMessageCount = doReplacements(tableName, idColumnName,
            messageColumnName, replacementPairs);
        log.info("Number of html reversions = " + updatedMessageCount);

    } // revertHtml()

    /**
     * @param tableName
     * @param idColumnName
     * @param messageColumnName
     * @throws IOException
     */
    private static void swapUrls(String tableName, String idColumnName,
        String messageColumnName) throws IOException {

        log.info("swapUrls() for table and column " + tableName + "; "
            + messageColumnName);

        if (tableName == null || idColumnName == null
            || messageColumnName == null) {
            log.error("Parameters must not be null.  Cannot continue.");
            return;
        }

        // the spaces in names within image tags break the replacement process
        // we have to replace the spaces with underscores
        // we will also have to do the same in the image folders
        final HashMap<String, String> replacementPairs =
            new HashMap<String, String>();

        // change URL for legacy forum images
        String oldLegacyForumImagesLocation1 =
            Constants.oldLegacyForumImagesURL1 + "/forum/uploaded";
        String oldLegacyForumImagesLocation2 =
            Constants.oldLegacyForumImagesURL2 + "/forum/uploaded";
        String newLegacyForumImagesLocation =
            Constants.newLegacyForumImagesURL + "/forum/uploaded";
        replacementPairs.put(oldLegacyForumImagesLocation1,
            newLegacyForumImagesLocation);
        replacementPairs.put(oldLegacyForumImagesLocation2,
            newLegacyForumImagesLocation);

        int updatedMessageCount = doReplacements(tableName, idColumnName,
            messageColumnName, replacementPairs);
        log.info("Number of urls swapped = " + updatedMessageCount);

    } // swapUrls()

    /**
     * @param tableName
     * @param idColumnName
     * @param messageColumnName
     * @throws IOException
     */
    private static void fixUpImgTags(String tableName, String idColumnName,
        String messageColumnName) throws IOException {

        log.info("fixUpImgTags() for table and column " + tableName + "; "
            + messageColumnName);

        if (tableName == null || idColumnName == null
            || messageColumnName == null) {
            log.error("Parameters must not be null.  Cannot continue.");
            return;
        }

        // the spaces in names within image tags break the replacement process
        // we have to replace the spaces with underscores
        // we will also have to do the same in the image folders
        final HashMap<String, String> replacementPairs =
            new HashMap<String, String>();

        // fix up some leftover [img] tags

        // [img]http://legacy-forum-images.m-100.co
        String bad = "\\[img\\]http";
        String good = "<img src=\"http";
        replacementPairs.put(bad, good);

        // .jpg[/img]
        bad = "jpg\\[/img\\]";
        good = "jpg\"/>";
        replacementPairs.put(bad, good);

        bad = "JPG\\[/img\\]";
        good = "JPG\"/>";
        replacementPairs.put(bad, good);

        int updatedMessageCount = doReplacements(tableName, idColumnName,
            messageColumnName, replacementPairs);
        log.info("Number of img tags fixed = " + updatedMessageCount);

    } // fixUpImgTags()

    /**
     * @param tableName
     * @param idColumnName
     * @param messageColumnName
     * @return the number or replacements
     * @throws IOException
     */
    private static int doReplacements(String tableName, String idColumnName,
        String messageColumnName, HashMap<String, String> replacementPairs)
        throws IOException {

        log.info("doReplacements() for table and column " + tableName + "; "
            + messageColumnName);

        if (tableName == null || idColumnName == null
            || messageColumnName == null || replacementPairs == null) {
            log.error("Parameters must not be null.  Cannot continue.");
            return 0;
        }

        // use Jackcess
        final Database db =
            DatabaseBuilder.open(new File(Constants.snitzDatabaseURL));

        final Table table = db.getTable(tableName);
        final IndexCursor cursor =
            CursorBuilder.createCursor(table.getPrimaryKeyIndex());

        int replacementsDone = 0;

        // iterate over the rows
        Iterator<Row> cursorIterator = cursor.iterator();
        while (cursorIterator.hasNext()) {
            final Row row = (Row) cursorIterator.next();
            log.debug("Row ID: " + row.get(idColumnName));
            String fieldText = String.valueOf(row.get(messageColumnName));
            // boolean needsUpdate = false;

            // iterate over replacementPairs; do replacements
            Iterator<String> replacementPairsIterator =
                replacementPairs.keySet().iterator();
            String revisedFieldText = fieldText;
            while (replacementPairsIterator.hasNext()) {
                final String key = replacementPairsIterator.next();
                final String value = replacementPairs.get(key);

                revisedFieldText = revisedFieldText.replaceAll(key, value);
            }

            if (!revisedFieldText.equals(fieldText)) {
                row.put(messageColumnName, revisedFieldText);
                table.updateRow(row);
                ++replacementsDone;
                log.debug(revisedFieldText);
            }
        }

        return replacementsDone;

    } // doReplacements()

}
