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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;

/**
 * Converts a {@link BufferedImage} to a TIFF on the filesystem.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class TiffConverter {

	/**
	 * A TIFF is desired to run with Tesseract, but the image library TIFFImageWriter seems to be not available on Mac-Java.
	 * However, it does run also with the original JPEG. Therefore it is also possible to just store the file on the filesystem.
	 *
	 * @param inputStream captcha from URL
	 * @return the File on the Filesystem.
	 */
	public File saveAsJPEG(String filename, InputStream inputStream) {

		try (FileOutputStream fos = new FileOutputStream(filename);) {
			IOUtils.copy(inputStream, fos);
		} catch (IOException e) {
			log.error("IO Error processing incoming image", e);
		}

		File result = new File(filename);
		return result;

	}

	/**
	 * Saves {@link BufferedImage} as a TIFF on the filesystem.
	 *
	 * @param filename the output filename
	 * @param image    the input image
	 * @return true, if successful
	 * @see Original ist from: http://log.robmeek.com/2005/08/write-tiff-in-java.html
	 */
	public File saveTiff(String filename, BufferedImage image) {

		File tiffFile = new File(filename);
		ImageOutputStream ios = null;
		ImageWriter writer = null;

		try {

			// find an appropriate writer
			Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("tiff");
			if (it.hasNext()) {
				writer = (ImageWriter) it.next();
			} else {
				return null;
			}

			// Setup writer
			ios = ImageIO.createImageOutputStream(tiffFile);
			writer.setOutput(ios);
			ImageWriteParam writeParam = new ImageWriteParam(Locale.ENGLISH);
			writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			// see writeParam.getCompressionTypes() for available compression type strings
			writeParam.setCompressionType("PackBits");

			// Convert to an IIOImage
			IIOImage iioImage = new IIOImage(image, null, null);

			writer.write(null, iioImage, writeParam);

		} catch (IOException e) {
			log.error("Could not save JPEG as TIFF", e);
			return null;
		}
		return tiffFile;

	}
}
