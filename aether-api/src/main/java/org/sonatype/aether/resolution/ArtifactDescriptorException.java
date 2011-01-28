package org.sonatype.aether.resolution;

/*******************************************************************************
 * Copyright (c) 2010-2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * The Apache License v2.0 is available at
 *   http://www.apache.org/licenses/LICENSE-2.0.html
 * You may elect to redistribute this code under either of these licenses.
 *******************************************************************************/

import org.sonatype.aether.RepositoryException;

/**
 * @author Benjamin Bentmann
 */
public class ArtifactDescriptorException
    extends RepositoryException
{

    private final ArtifactDescriptorResult result;

    public ArtifactDescriptorException( ArtifactDescriptorResult result, String message )
    {
        super( message, getCause( result ) );
        this.result = result;
    }

    public ArtifactDescriptorException( ArtifactDescriptorResult result )
    {
        super( "Failed to read artifact descriptor"
            + ( result != null ? " for " + result.getRequest().getArtifact() : "" ), getCause( result ) );
        this.result = result;
    }

    public ArtifactDescriptorResult getResult()
    {
        return result;
    }

    private static Throwable getCause( ArtifactDescriptorResult result )
    {
        Throwable cause = null;
        if ( result != null && !result.getExceptions().isEmpty() )
        {
            cause = result.getExceptions().get( 0 );
        }
        return cause;
    }

}
