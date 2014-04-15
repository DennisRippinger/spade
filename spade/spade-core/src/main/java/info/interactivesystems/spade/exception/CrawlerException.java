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
package info.interactivesystems.spade.exception;

/**
 * The Class CrawlerException.
 * 
 * @author Dennis Rippinger
 */
public class CrawlerException extends Exception {

    private static final long serialVersionUID = 8307212821761023653L;

    /**
     * Instantiates a new crawler exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public CrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

}
