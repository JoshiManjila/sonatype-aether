package org.sonatype.aether.transfer;

/*
 * Copyright (c) 2010 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0, 
 * and you may not use this file except in compliance with the Apache License Version 2.0. 
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the Apache License Version 2.0 is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

import java.io.File;

/**
 * Describes a resource being uploaded or downloaded by the repository system.
 * 
 * @author Benjamin Bentmann
 */
public interface TransferResource
{

    /**
     * The base URL of the repository, e.g. "http://repo1.maven.org/maven2/". Unless the URL is unknown, it will be
     * terminated by a trailing slash.
     * 
     * @return The base URL of the repository or an empty string if unknown, never {@code null}.
     */
    String getRepositoryUrl();

    /**
     * The path of the resource relative to the repository's base URL, e.g. "org/apache/maven/maven/3.0/maven-3.0.pom".
     * 
     * @return The path of the resource, never {@code null}.
     */
    String getResourceName();

    /**
     * Gets the local file being uploaded or downloaded. When the repository system merely checks for the existence of a
     * remote resource, no local file will be involved in the transfer.
     * 
     * @return The source/target file involved in the transfer or {@code null} if none.
     */
    File getFile();

    /**
     * The size of the resource in bytes.
     * 
     * @return The size of the resource in bytes or a negative value if unknown.
     */
    long getContentLength();

    /**
     * Gets the timestamp when the transfer of this resource was started.
     * 
     * @return The timestamp when the transfer of this resource was started.
     */
    long getTransferStartTime();

}
