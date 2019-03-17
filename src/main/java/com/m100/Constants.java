package com.m100;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Triple;

public class Constants {

    static final String snitzDatabaseURL =
        "D:/ping23_projects/m-100/db/snitz_forums_2000.2018-09-04.dummy.mdb";

    // static final String snitzDatabaseURL =
    // "D:/ping23_projects/m-100/db/snitz_forums_2000_20171220.scratch.mdb";

    static final String tmpDatabaseURL =
        "D:/ping23_projects/m-100/db/scratch/snitz_forums_2000.2018-09-04.scratch.trash.mdb";

    static final String newLegacyForumImagesURL =
        "legacy-forum-images.m-100.co";
    static final String oldLegacyForumImagesURL1 = "www.m-100.co";
    static final String oldLegacyForumImagesURL2 = "www.m-100.cc";

    // for the second import we need a "diff" between the two database states
    // we need to delete all of the previously imported records for the second import
    // this data structure tells us which records to delete (last record id's from previous import)
    static final ArrayList<Triple<String, String, Integer>> firstImportTableIdLimits =
        new ArrayList<>();
    static {
 
        firstImportTableIdLimits.add(Triple.of("TEST_DUMMY", "NA", 0));

        firstImportTableIdLimits.add(Triple.of("FORUM_FORUM", "FORUM_ID", 15));
        firstImportTableIdLimits.add(Triple.of("FORUM_MEMBERS", "MEMBER_ID", 1427));
        firstImportTableIdLimits.add(Triple.of("FORUM_MEMBERS_PENDING", "MEMBER_ID", 39496 ));
        firstImportTableIdLimits.add(Triple.of("FORUM_MODERATOR", "MOD_ID", 353));
        firstImportTableIdLimits.add(Triple.of("FORUM_REPLY", "REPLY_ID", 76551));
        firstImportTableIdLimits.add(Triple.of("FORUM_SUBSCRIPTIONS", "SUBSCRIPTION_ID", 2697));
        firstImportTableIdLimits.add(Triple.of("FORUM_TOPICS", "TOPIC_ID", 9844));
        
        // delete all rows from the rest of the tables
        firstImportTableIdLimits.add(Triple.of("FORUM_A_REPLY", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_A_TOPICS", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_ALLOWED_MEMBERS", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_BADWORDS", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_CATEGORY", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_CONFIG_NEW", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_GROUP_NAMES", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_GROUPS", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_IPLIST", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_IPLOG", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_NAMEFILTER", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_PAGEKEYS", "NA", 0));
        firstImportTableIdLimits.add(Triple.of("FORUM_TOTALS", "NA", 0));
        
     }

}
