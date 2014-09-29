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

import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A service to 'break' a captcha provided by Amazon.
 * It uses the OCR Software 'Tesseract' to get a string from the captcha.
 * Works in 1 of 10 cases - which is sufficient.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Service
public class CaptchaService {

	@Resource
	private TiffConverter tiffConverter;

	@Resource
	private Tesseract tesseract;

	public String getValueFromCaptcha(HtmlPage page) {
		try {
			HtmlImage image = page.getFirstByXPath("//img");
			File imageFile = new File(FileNamer.getOutputName("jpeg"));
			image.saveAs(imageFile);

			String result = tesseract.recognizeCaptcha(imageFile);

			return result;

		} catch (NullPointerException e) {
			log.error("TIFF does not exist", e);
		} catch (IOException e) {
			log.error("Could not save/find the image file from file system", e);
		}
		return null;
	}

	public String getValueForCaptcha(URL captcha) {
		try {
			InputStream captchaStream = captcha.openStream();

			// TODO Maybe build an OS Switch, see TiffConverter for more information.
			// BufferedImage imageCaptcha = ImageIO.read(captchaStream);
			// File captchaImage = tiffConverter.saveTiff(getOutputName("tiff"), imageCaptcha);

			File captchaImage = tiffConverter.saveAsJPEG(FileNamer.getOutputName("jpeg"), captchaStream);

			return tesseract.recognizeCaptcha(captchaImage);

		} catch (MalformedURLException e) {
			log.error("URL to Captcha is malformed", e);
		} catch (IOException e) {
			log.error("Could not find or download the Captcha", e);
		} catch (NullPointerException e) {
			log.error("TIFF does not exist", e);
		}
		return null;
	}

	public String getValueForCaptcha(String captcha) {
		try {
			URL url = new URL(captcha);
			return getValueForCaptcha(url);
		} catch (MalformedURLException e) {
			log.error("Could not convert '{}' into URL Object", captcha, e);
		}
		return null;
	}
}
