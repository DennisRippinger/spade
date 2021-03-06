/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade.crawler.captcha;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Wrapper for Tesseract OCR CLI.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class Tesseract {

	public static final String TESSERACT_LOCATION = System.getProperty("ocr.tesseract.location");

	public String recognizeCaptcha(File tiffImage) {

		try {
			Runtime runtime = Runtime.getRuntime();
			Process exec = runtime.exec(createCommand(tiffImage));

			Integer returnValue = exec.waitFor();

			boolean delete = tiffImage.delete();

			if (delete && returnValue.equals(0)) {
				return readTesseractOutput();
			} else {
				log.warn("Tesseract couldn't process the CAPTCHA, return code '{}'", returnValue);
			}

		} catch (IOException | InterruptedException e) {
			log.error("Could not run Tesseract process", e);
		}
		return "";
	}

	/**
	 * Creates the command.
	 *
	 * @param tiffImage the tiff image
	 * @return the list
	 */
	private String createCommand(File tiffImage) {
		StringBuilder cmd = new StringBuilder();
		if (!TESSERACT_LOCATION.isEmpty()) {
			// On a mac installed with brew a location is needed (or seems to be).
			cmd.append(TESSERACT_LOCATION);
		}
		cmd.append("tesseract ");
		cmd.append(tiffImage.getAbsolutePath());
		cmd.append(" ");
		cmd.append(FileNamer.getTextOutputName());
		cmd.append(" -l eng");

		return cmd.toString();
	}

	private String readTesseractOutput() {
		File outputFile = new File(FileNamer.getActualTextOutputName());
		try {
			List<String> readLines = Files.readLines(outputFile, Charsets.UTF_8);
			if (!readLines.isEmpty()) {

				if (outputFile.delete()) {
					return readLines.get(0);
				}
			}
		} catch (IOException e) {
			log.error("Could not find or read Tesseract Output File", e);
		}
		return "";
	}
}