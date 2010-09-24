package org.sonatype.aether.spi.connector;

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

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.transfer.ArtifactTransferException;

/**
 * A download/upload of an artifact.
 * 
 * @author Benjamin Bentmann
 */
public abstract class ArtifactTransfer
    extends Transfer
{

    private Artifact artifact;

    private File file;

    private ArtifactTransferException exception;

    /**
     * Gets the artifact being transferred.
     * 
     * @return The artifact being transferred or {@code null} if not set.
     */
    public Artifact getArtifact()
    {
        return artifact;
    }

    /**
     * Sets the artifact to transfer.
     * 
     * @param artifact The artifact, may be {@code null}.
     * @return This transfer for chaining, never {@code null}.
     */
    public ArtifactTransfer setArtifact( Artifact artifact )
    {
        this.artifact = artifact;
        return this;
    }

    /**
     * Gets the local file the artifact is downloaded to or uploaded from. In case of a download, a connector should
     * first transfer the bytes to a temporary file and only overwrite the target file once the entire download is
     * completed such that an interrupted/failed download does not corrupt the current file contents.
     * 
     * @return The local file or {@code null} if not set.
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Sets the local file the artifact is downloaded to or uploaded from.
     * 
     * @param file The local file, may be {@code null}.
     * @return This transfer for chaining, never {@code null}.
     */
    public ArtifactTransfer setFile( File file )
    {
        this.file = file;
        return this;
    }

    /**
     * Gets the exception that occurred during the transfer (if any).
     * 
     * @return The exception or {@code null} if the transfer was successful.
     */
    public ArtifactTransferException getException()
    {
        return exception;
    }

    /**
     * Sets the exception that occurred during the transfer.
     * 
     * @param exception The exception, may be {@code null} to denote a successful transfer.
     * @return This transfer for chaining, never {@code null}.
     */
    public ArtifactTransfer setException( ArtifactTransferException exception )
    {
        this.exception = exception;
        return this;
    }

}
