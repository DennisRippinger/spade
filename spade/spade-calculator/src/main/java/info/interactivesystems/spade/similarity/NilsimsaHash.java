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
package info.interactivesystems.spade.similarity;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * The Nilsimsa Hash</br>
 * 
 * Code is based on the the Python implementation of Michael Itz http://code.google.com/p/py-nilsimsa and the respective java
 * implementation of Albert Weichselbraun.
 * 
 * @author Dennis Rippinger
 * 
 */
@Slf4j
@Component
public class NilsimsaHash {

    private static final byte[] TRAN = NilsimsaHash.getByteArray(
        "02D69E6FF91D04ABD022161FD873A1AC" +
            "3B7062961E6E8F399D05144AA6BEAE0E" +
            "CFB99C9AC76813E12DA4EB518D646B50" +
            "23800341ECBB71CC7A867F98F2365EEE" +
            "8ECE4FB832B65F59DC1B314C7BF06301" +
            "6CBA07E81277493CDA46FE2F791C9B30" +
            "E300067E2E0F383321ADA554CAA729FC" +
            "5A47697DC595B5F40B90A3816D255535" +
            "F575740A26BF195C1AC6FF995D84AA66" +
            "3EAF78B32043C1ED24EAE63F18F3A042" +
            "57085360C3C0834082D709BD442A67A8" +
            "93E0C2569FD9DD8515B48A27289276DE" +
            "EFF8B2B7C93D45944B110D65D5348B91" +
            "0CFA87E97C5BB14DE5D4CB10A21789BC" +
            "DBB0E2978852F748D3612C3A2BD18CFB" +
            "F1CDE46AE7A9FDC437C8D2F6DF58724E");

    private static final byte[] POPC = NilsimsaHash.getByteArray(
        "00010102010202030102020302030304" +
            "01020203020303040203030403040405" +
            "01020203020303040203030403040405" +
            "02030304030404050304040504050506" +
            "01020203020303040203030403040405" +
            "02030304030404050304040504050506" +
            "02030304030404050304040504050506" +
            "03040405040505060405050605060607" +
            "01020203020303040203030403040405" +
            "02030304030404050304040504050506" +
            "02030304030404050304040504050506" +
            "03040405040505060405050605060607" +
            "02030304030404050304040504050506" +
            "03040405040505060405050605060607" +
            "03040405040505060405050605060607" +
            "04050506050606070506060706070708");

    private static byte[] getByteArray(String hexString) {
        try {
            return Hex.decodeHex(hexString.toCharArray());
        } catch (DecoderException e) {
            log.error("Could not decodeHex Value", e);
            return new byte[1];
        }
    }

    public String calculateNilsima(String input) {

        int count = 0; // num characters seen
        int[] acc = new int[256]; // accumulators for computing the digest
        int[] lastch = new int[4]; // the last four seen characters

        for (int ch : input.toCharArray()) {
            count++;

            // Accumulator for triplets
            if (lastch[1] > -1) {
                acc[tran3(ch, lastch[0], lastch[1], 0)]++;
            }
            if (lastch[2] > -1) {
                acc[tran3(ch, lastch[0], lastch[2], 1)]++;
                acc[tran3(ch, lastch[1], lastch[2], 2)]++;
            }
            if (lastch[3] > -1) {
                acc[tran3(ch, lastch[0], lastch[3], 3)]++;
                acc[tran3(ch, lastch[1], lastch[3], 4)]++;
                acc[tran3(ch, lastch[2], lastch[3], 5)]++;
                acc[tran3(lastch[3], lastch[0], ch, 6)]++;
                acc[tran3(lastch[3], lastch[2], ch, 7)]++;
            }

            // adjust lastch
            for (int i = 3; i > 0; i--) {
                lastch[i] = lastch[i - 1];
            }
            lastch[0] = ch;
        }

        int total = 0;
        int threshold;
        byte[] digest = new byte[32];
        Arrays.fill(digest, (byte) 0);

        if (count == 3) {
            total = 1;
        } else if (count == 4) {
            total = 4;
        } else if (count > 4) {
            total = 8 * count - 28;
        }
        threshold = total / 256;

        for (int i = 0; i < 256; i++) {
            if (acc[i] > threshold) {
                digest[i >> 3] += 1 << (i & 7);
            }
        }
        ArrayUtils.reverse(digest);
        return Hex.encodeHexString(digest);
    }

    public Integer compare(String nilsimaOne, String nilsimaTwo) {
        byte bits = 0;
        int j;
        byte[] n1 = null;
        byte[] n2 = null;
        try {
            n1 = nilsimaOne.getBytes("UTF-8");
            n2 = nilsimaTwo.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Encoding not supported");
            return 0;
        }

        for (int i = 0; i < 32; i++) {
            j = 255 & n1[i] ^ n2[i];
            bits += POPC[j];
        }
        return 128 - bits;
    }

    /**
     * Accumulator for a transition n between the chars a, b, c
     */
    private int tran3(int a, int b, int c, int n) {
        int i = (c) ^ TRAN[n];
        return (((TRAN[(a + n) & 255] ^ TRAN[b & 0xff] * (n + n + 1)) + TRAN[i & 0xff]) & 255);
    }
}
