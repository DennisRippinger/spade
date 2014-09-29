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
 * The Class SpadeException.
 *
 * @author Dennis Rippinger
 */
public class SpadeException extends Exception {

	private static final long serialVersionUID = 2637512426961963804L;

	/**
	 * Instantiates a new spade exception.
	 *
	 * @param message the message
	 */
	public SpadeException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new spade exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public SpadeException(String message, Throwable cause) {
		super(message, cause);
	}

}
