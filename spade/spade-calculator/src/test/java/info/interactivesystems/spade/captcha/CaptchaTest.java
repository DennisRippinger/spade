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
package info.interactivesystems.spade.captcha;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.crawler.captcha.CaptchaService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * The Class CaptchaTest.
 * 
 * @author Dennis Rippinger
 */
@ContextConfiguration(locations = { "classpath:lightweight-beans.xml" })
public class CaptchaTest extends AbstractTestNGSpringContextTests {

    @Resource
    private CaptchaService captchaService;

    @Test
    public void getValueForString() {
        URL captchaResource = this.getClass().getResource("/Captcha.jpg");

        String result = captchaService.getValueForCaptcha(captchaResource);

        assertThat(result).isEqualTo("BLJRNM");
    }

    @Test
    public void testCaptchaSite() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        final WebClient webClient = new WebClient();
        URL resource = this.getClass().getResource("/Captcha_Site.html");
        final HtmlPage page1 = webClient.getPage(resource);

        final HtmlForm form = page1.getFormByName("");

        DomElement captcha = (DomElement) page1.getByXPath("//img").get(0);
        String capchaURl = captcha.getAttribute("src");
        System.out.println(capchaURl);

        final HtmlTextInput textField = form.getInputByName("field-keywords");

        // Change the value of the text field
        textField.setValueAttribute("test");

        // Now submit the form by clicking the button and get back the second page.
        // final HtmlPage page2 = button.click();

        webClient.closeAllWindows();
    }
}
