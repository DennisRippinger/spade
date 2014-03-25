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
package info.interactivesystems.spade.semantic;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import de.linguatools.disco.DISCO;

/**
 * Basic factory to create a {@link DISCO} instance.
 * 
 * 
 * @author Dennis Rippinger
 * 
 */
@Slf4j
public final class DiscoFactory {

    private static final Boolean LOAD_TO_RAM = false;

    private static PropertiesConfiguration configuration;
    private static String discoDir;

    static {
        try {
            configuration = new PropertiesConfiguration("disco.properties");
            discoDir = configuration.getString("disco.english.wikipedia");
        } catch (ConfigurationException e) {
            log.error("Could not load Disco Properties", e);
        }
    }

    /**
     * Creates an {@link DICO} Instance with English co-occurence data.
     * 
     * @return the english co-ocurence data as {@link DISCO} instance.
     */
    public static DISCO getEnglishCoOcurenceData() {
        DISCO disco = null;
        try {

            log.debug("Loading English word coocurence File from '{}'",
                discoDir);
            disco = new DISCO(discoDir, LOAD_TO_RAM);
            log.debug("Loaded English word coocurence into RAM = '{}'",
                LOAD_TO_RAM);

        } catch (IOException e) {
            log.error("Could not load Disco folder", e);
        }

        return disco;
    }

    /**
     * Private Constructor
     */
    private DiscoFactory() {

    }

}
