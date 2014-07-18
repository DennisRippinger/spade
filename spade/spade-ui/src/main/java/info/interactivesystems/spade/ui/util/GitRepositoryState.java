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
package info.interactivesystems.spade.ui.util;

import java.util.Properties;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * The Class GitRepositoryState.
 * 
 * @author Dennis Rippinger
 */
@Data
@XmlRootElement
public class GitRepositoryState {

    private String branch;
    private String describe;
    private String shortDescribe;
    private String describeShort;
    private String commitId;
    private String commitIdAbbrev;
    private String buildUserName;
    private String buildUserEmail;
    private String buildTime;
    private String commitUserName;
    private String commitUserEmail;
    private String commitMessageFull;
    private String commitMessageShort;
    private String commitTime;

    public GitRepositoryState(Properties properties)
    {
        this.branch = properties.get("git.branch").toString();
        this.describe = properties.get("git.commit.id.describe").toString();
        this.describeShort = properties.get("git.commit.id.describe-short").toString();
        this.commitId = properties.get("git.commit.id").toString();
        this.commitIdAbbrev = properties.getProperty("git.commit.id.abbrev").toString();
        this.buildUserName = properties.get("git.build.user.name").toString();
        this.buildUserEmail = properties.get("git.build.user.email").toString();
        this.buildTime = properties.get("git.build.time").toString();
        this.commitUserName = properties.get("git.commit.user.name").toString();
        this.commitUserEmail = properties.get("git.commit.user.email").toString();
        this.commitMessageShort = properties.get("git.commit.message.short").toString();
        this.commitMessageFull = properties.get("git.commit.message.full").toString();
        this.commitTime = properties.get("git.commit.time").toString();
    }

}
