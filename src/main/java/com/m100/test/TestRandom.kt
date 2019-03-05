package com.m100.test

import com.healthmarketscience.jackcess.Column
import com.healthmarketscience.jackcess.CursorBuilder
import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.IndexCursor
import com.m100.util.FileUtilities
import java.io.File
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import org.kefirsf.bb.BBProcessorFactory
import org.kefirsf.bb.ConfigurationFactory
import org.kefirsf.bb.TextProcessor
import java.net.URI
import java.net.URL


class TestRandom {

	companion object {

		private val snitzDatabaseURL = "D:/dev/jls_projects/m-100_legacy_project/db/snitz_forums_2000_20171220.scratch.mdb"

		@JvmStatic
		public fun main(args: Array<String>) {

			//fixTextIssues()
			//convertBBCodeToHtml()

			filterHtml()


		} // main()


		fun filterHtml() {

			// KefirBB processor for BBCode to HTML conversion
			val processor = BBProcessorFactory.getInstance().create()

			val file = File("D:\\dev\\jls_projects\\m-100_legacy_project\\target\\classes\\test_html.html")

			val textLines: Array<String> = FileUtilities.readTextFile(file)
			//val text: String = textLines.get(0)

			//println(text)

			// convert BBCode to HTML (KefirBB)
			textLines.forEach { textLine ->
				var text: String = processor.process(textLine)

				// now convert back
				text = revertHtml(text)

				println(text)
			}
			// val output: String = processor.process(inputHtml)

			// println(output)

		} // filterHtml()


		fun revertHtml(textLine: String): String {


			val replacementPairs: LinkedHashSet<Pair<String, String>> = LinkedHashSet()
			replacementPairs.add(Pair("&amp;", "&"))
			replacementPairs.add(Pair("&gt;", ">"))
			replacementPairs.add(Pair("&lt;", "<"))
			replacementPairs.add(Pair("&apos;", "'"))
			replacementPairs.add(Pair("&quot;", "\""))

			var convertedTextLine: String = textLine
			replacementPairs.forEach { replacementPair ->
				//println(replacementPair.first)

				if (convertedTextLine.contains(replacementPair.first)) {
					//println("found " + replacementPair.first)
					convertedTextLine = convertedTextLine.replace(replacementPair.first, replacementPair.second)
					//println(convertedTextLine)
				}
			}

			return convertedTextLine

		} // revertHtml()


	} // companion object

}