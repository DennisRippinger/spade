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
package info.interactivesystems.spade.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class HashUtil offers methods to calculate a hash for a given string.
 * 
 * @author Dennis Rippinger
 */
public final class HashUtil {

    /**
     * Calculates the SHA-1 value for the given input.
     * 
     * @param input the original String.
     * @return the the SHA-1 Value.
     * @throws NoSuchAlgorithmException thrown if the SHA-1 Hash is not within the Java Runtime.
     * @see Source: http://www.sha1-online.com/sha1-java/
     */
    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    /**
     * Private Constructor.
     */
    private HashUtil() {

    }
}
