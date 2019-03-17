package com.m100;

import com.healthmarketscience.jackcess.*;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public final class DeletePreviouslyImportedRecords {
    private static final Logger log =
        LogManager.getLogger(DeletePreviouslyImportedRecords.class);

    private static final String workingDatabaseURL = Constants.snitzDatabaseURL;

    /**
     * @param args
     */
    public static final void main(String[] args) {

        log.info("DeletePreviouslyImportedRecords processing file: "
            + workingDatabaseURL + "\n");

        for (Triple<String, String, Integer> tableInfo : Constants.firstImportTableIdLimits) {

            String tableName = tableInfo.getLeft();
            String idColumnName = tableInfo.getMiddle();
            int idLimit = tableInfo.getRight();

            try {
                deleteRecords(tableName, idColumnName, idLimit);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

        log.info("\nDeletePreviouslyImportedRecords finished processing file: "
            + workingDatabaseURL);

    } // main()

    /**
     * @param tableName
     * @param idColumnName
     * @param idLimit
     * @throws IOException
     */
    private static void deleteRecords(String tableName, String idColumnName,
        int idLimit) throws IOException {

        log.info("deleteRecords() for table " + tableName);

        if (tableName == null || idColumnName == null || idLimit < 0) {
            log.error(
                "Parameters must not be null or less than zero.  Cannot continue.");
            return;
        }

        // use Jackcess
        final Database db = DatabaseBuilder.open(new File(workingDatabaseURL));
        final Table table = db.getTable(tableName);
        
        // if we tried to process a table name that does not exist
        if (table == null) {
            log.info("Table does not exist: " + tableName);
            return;
        }

        int deletedRecordCount = 0;

        for (Row row : table) {

            if (!idColumnName.equals("NA")) {

                log.debug("Row ID: " + row.get(idColumnName));

                String recordIdString = String.valueOf(row.get(idColumnName));
                int recordId = Integer.valueOf(recordIdString);

                if (recordId <= idLimit) {
                    // delete the row
                    log.debug("Deleting row with ID: " + recordIdString);
                    table.deleteRow(row);

                    ++deletedRecordCount;
                    log.debug(recordId);
                    log.debug(
                        "........................................................................................");
                }

            }
            else {
                ++deletedRecordCount;
                log.debug("Deleting each row in this table: deleted record # " + deletedRecordCount);
                // for tables with idColumnName = "NA" delete ALL rows
                table.deleteRow(row);
            }

        }

        log.info("Number of records deleted = " + deletedRecordCount);

    } // deleteRecords()

}
