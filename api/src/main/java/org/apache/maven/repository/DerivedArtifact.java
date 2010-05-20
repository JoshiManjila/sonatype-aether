package org.apache.maven.repository;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.util.Map;

/**
 * @author Benjamin Bentmann
 */
public abstract class DerivedArtifact
    implements Artifact, Cloneable
{

    protected Artifact mainArtifact;

    private File file;

    protected DerivedArtifact( Artifact mainArtifact )
    {
        if ( mainArtifact == null )
        {
            throw new IllegalArgumentException( "main artifact not specified" );
        }
        this.mainArtifact = mainArtifact;
    }

    public String getGroupId()
    {
        return mainArtifact.getGroupId();
    }

    public String getArtifactId()
    {
        return mainArtifact.getArtifactId();
    }

    public String getBaseVersion()
    {
        return mainArtifact.getBaseVersion();
    }

    public String getVersion()
    {
        return mainArtifact.getVersion();
    }

    public Artifact setVersion( String version )
    {
        mainArtifact.setVersion( version );
        return this;
    }

    public boolean isSnapshot()
    {
        return mainArtifact.isSnapshot();
    }

    public String getClassifier()
    {
        return mainArtifact.getClassifier();
    }

    public String getType()
    {
        return mainArtifact.getType();
    }

    public File getFile()
    {
        return file;
    }

    public Artifact setFile( File file )
    {
        this.file = file;
        return this;
    }

    public <T> T getProperty( String key, Class<T> type, T defaultValue )
    {
        return mainArtifact.getProperty( key, type, defaultValue );
    }

    public Map<String, Object> getProperties()
    {
        return mainArtifact.getProperties();
    }

    @Override
    public Artifact clone()
    {
        try
        {
            DerivedArtifact clone = (DerivedArtifact) super.clone();

            clone.mainArtifact = mainArtifact.clone();

            return clone;
        }
        catch ( CloneNotSupportedException e )
        {
            throw new UnsupportedOperationException( e );
        }
    }

}
