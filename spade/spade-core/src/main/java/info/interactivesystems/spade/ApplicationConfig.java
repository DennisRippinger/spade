/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

@Configuration
@ComponentScan
@EnableMongoRepositories
public class ApplicationConfig extends AbstractMongoConfiguration {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#getDatabaseName()
     */
    @Override
    protected String getDatabaseName() {
        return "Spade";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#mongo()
     */
    @Override
    public Mongo mongo() throws Exception {

        @SuppressWarnings("deprecation")
        Mongo mongo = new Mongo();
        mongo.setWriteConcern(WriteConcern.SAFE);

        return mongo;
    }

}
