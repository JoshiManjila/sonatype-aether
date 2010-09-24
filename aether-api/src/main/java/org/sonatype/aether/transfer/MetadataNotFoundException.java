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

import org.sonatype.aether.metadata.Metadata;
import org.sonatype.aether.repository.RemoteRepository;

/**
 * @author Benjamin Bentmann
 */
public class MetadataNotFoundException
    extends MetadataTransferException
{

    public MetadataNotFoundException( Metadata metadata, RemoteRepository repository )
    {
        super( metadata, repository, "Could not find metadata " + metadata + getString( " in ", repository ) );
    }

    public MetadataNotFoundException( Metadata metadata, RemoteRepository repository, String message )
    {
        super( metadata, repository, message );
    }

}
