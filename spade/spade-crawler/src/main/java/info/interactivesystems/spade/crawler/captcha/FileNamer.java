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

/**
 * The Class FileNamer provides static methods to create a thread depended name for each CAPTCHA file and Tesseract output.
 * 
 * @author Dennis Rippinger
 */
public final class FileNamer {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    /**
     * Gets the output name for the CAPTCHA image.
     * 
     * @param type the type of image
     * @return the output name with a patch in $OS tempdir.
     */
    public static String getOutputName(String type) {
        return String.format("%s%s.%s", TEMP_DIR, Thread.currentThread().getName(), type);
    }

    /**
     * Gets the output Name for the text file.
     * 
     * @return the output name with a patch in $OS tempdir.
     */
    static String getTextOutputName() {
        return String.format("%s%s", TEMP_DIR, Thread.currentThread().getName());
    }

    /**
     * Gets the output Name for the text file. Tesseract adds a "txt" in every case.
     * 
     * @return the output name with a patch in $OS tempdir.
     */
    static String getActualTextOutputName() {
        return String.format("%s%s.txt", TEMP_DIR, Thread.currentThread().getName());
    }

    /**
     * Private Constructor.
     */
    private FileNamer() {

    }

}
