package com.m100.util;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class FileUtilities
{
    // non-instantiable
    private FileUtilities()
    {
    }

    /**
     * return the lines of the given text file as a Collection of Strings.
     * @param file
     * @return
     */
    public static String[] readTextFile(File file)
    {
        ArrayList<String> fileLinesArrayList = new ArrayList<String>();

        try
        {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                fileLinesArrayList.add(line);
            }

            bufferedReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String[] fileLines = new String[fileLinesArrayList.size()];
        fileLinesArrayList.toArray(fileLines);

        return fileLines;

    }

    /**
     * append the given string to the end of the given text file
     * @param lineOfText
     */
    public static boolean appendToTextFile(File file, String lineOfText)
    {

        boolean successValue = false;

        successValue = writeToTextFile2(file, lineOfText, true);

        return successValue;

    }

    /**
     * overwrite the given string to the beginning of the given text file
     * @param lineOfText
     */
    public static boolean overwriteToTextFile(File file, String lineOfText)
    {

        boolean successValue = false;

        successValue = writeToTextFile2(file, lineOfText, false);

        return successValue;

    }

    /**
     * write text to file
     * @param file
     * @param lineOfText
     * @return
     */
    public static boolean writeToTextFile(File file, String lineOfText)
    {

        boolean successValue = false;

        // create FileOutputStreams and redirect output
        try
        {
            PrintStream printStream =
                new PrintStream(new FileOutputStream(file, true));

            printStream.printf("%s" + "%n", lineOfText);

            printStream.close();

            successValue = true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return successValue;

    }

    /**
     * write to file
     * @param file is the File object
     * @param lineOfText
     * @param shouldAppend
     */
    public static boolean writeToTextFile2(File file, String lineOfText,
        boolean shouldAppend)
    {
        boolean successValue = false;

        try
        {
            FileWriter fileWriter = new FileWriter(file, shouldAppend);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.printf("%s" + "%n", lineOfText);
            printWriter.close();

            // if we got to here then we were successful
            successValue = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return successValue;

    }

    /**
     * write multiple lines to file
     * @param file is the File object
     * @param linesOfText
     * @param shouldAppend
     */
    public static boolean writeLinesToTextFile(File file, String[] linesOfText,
        boolean shouldAppend)
    {
        boolean successValue = false;

        try
        {
            FileWriter fileWriter = new FileWriter(file, shouldAppend);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (String lineOfText : linesOfText)
            {
                printWriter.printf("%s" + "%n", lineOfText);
            }
            printWriter.close();

            // if we got to here then we were successful
            successValue = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return successValue;

    }

    /**
     * get a File object
     * (file need not already exist)
     */
    public static File getFileInUserHome(String filename)
    {
        String folderPath =
            (String) System.getProperties().getProperty("user.home");

        File file = getFileInFolder(folderPath, filename);

        return file;

    }

    /**
     * get a File object
     * (file need not already exist)
     */
    public static File getFileInFolder(String folderPath, String filename)
    {
        File file = new File(folderPath, filename);

        return file;
    }

    /**
     * get a File object
     * (file need not already exist)
     */
    public static File getFileFromAbsoluteFullPath(String absoluteFullPath)
        throws IllegalStateException
    {
        File file = new File(absoluteFullPath);

        // check to see that parent folder exists; create if not
        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs())
        {
            throw new IllegalStateException(
                "Couldn't create parent directories.");
        }

        return file;
    }

    /**
     * get a File object
     * (NOTE that the file must already exist)
     */
    public static File getFileFromRelativeFullPath(String relativeFullPath)
        throws IllegalStateException
    {
        URL url = FileUtilities.class.getResource(relativeFullPath);

        if (url == null)
        {
            throw new IllegalStateException(
                "FileUtilities.getFileFromRelativeFullPath(): file does not, but must already exist.");
        }

        String absoluteFullPath = url.getPath();

        File file = new File(absoluteFullPath);

        return file;
    }

    public static String[] getFirstFiveLinesOfAliceInWonderland()
    {
        String[] stringArray = {
            "Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, ‘and what is the use of a book,’ thought Alice ‘without pictures or conversations?’",

            "So she was considering in her own mind (as well as she could, for the hot day made her feel very sleepy and stupid), whether the pleasure of making a daisy-chain would be worth the trouble of getting up and picking the daisies, when suddenly a White Rabbit with pink eyes ran close by her.",

            "There was nothing so very remarkable in that; nor did Alice think it so very much out of the way to hear the Rabbit say to itself, ‘Oh dear! Oh dear! I shall be late!’ (when she thought it over afterwards, it occurred to her that she ought to have wondered at this, but at the time it all seemed quite natural); but when the Rabbit actually took a watch out of its waistcoat-pocket, and looked at it, and then hurried on, Alice started to her feet, for it flashed across her mind that she had never before seen a rabbit with either a waistcoat-pocket, or a watch to take out of it, and burning with curiosity, she ran across the field after it, and fortunately was just in time to see it pop down a large rabbit-hole under the hedge.",

            "In another moment down went Alice after it, never once considering how in the world she was to get out again.",

            "The rabbit-hole went straight on like a tunnel for some way, and then dipped suddenly down, so suddenly that Alice had not a moment to think about stopping herself before she found herself falling down a very deep well."

        };

        return stringArray;

    }

}
