package org.sonatype.aether.impl.internal;

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

import static org.junit.Assert.*;
import static org.sonatype.aether.test.impl.RecordingRepositoryListener.Type.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonatype.aether.RepositoryEvent;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.installation.InstallRequest;
import org.sonatype.aether.installation.InstallResult;
import org.sonatype.aether.installation.InstallationException;
import org.sonatype.aether.metadata.Metadata;
import org.sonatype.aether.metadata.Metadata.Nature;
import org.sonatype.aether.test.impl.RecordingRepositoryListener;
import org.sonatype.aether.test.impl.RecordingRepositoryListener.EventWrapper;
import org.sonatype.aether.test.impl.TestFileProcessor;
import org.sonatype.aether.test.impl.TestRepositorySystemSession;
import org.sonatype.aether.test.util.TestFileUtils;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.metadata.DefaultMetadata;

public class DefaultInstallerTest
{

    private Artifact artifact;

    private DefaultMetadata metadata;

    private TestRepositorySystemSession session;

    private String localArtifactPath;

    private String localMetadataPath;

    private DefaultInstaller installer;

    private InstallRequest request;

    private RecordingRepositoryListener listener;

    @Before
    public void setup()
        throws IOException
    {
        artifact = new DefaultArtifact( "gid", "aid", "jar", "ver" );
        artifact = artifact.setFile( TestFileUtils.createTempFile( "artifact".getBytes(), 1 ) );
        metadata =
            new DefaultMetadata( "gid", "aid", "ver", "type", Nature.RELEASE_OR_SNAPSHOT,
                                 TestFileUtils.createTempFile( "metadata".getBytes(), 1 ) );

        session = new TestRepositorySystemSession();
        localArtifactPath = session.getLocalRepositoryManager().getPathForLocalArtifact( artifact );
        localMetadataPath = session.getLocalRepositoryManager().getPathForLocalMetadata( metadata );

        installer = new DefaultInstaller().setFileProcessor( TestFileProcessor.INSTANCE );
        request = new InstallRequest();
        listener = new RecordingRepositoryListener();
        session.setRepositoryListener( listener );
        TestFileUtils.delete( session.getLocalRepository().getBasedir() );
    }

    @After
    public void teardown()
        throws Exception
    {
        TestFileUtils.delete( session.getLocalRepository().getBasedir() );
    }

    @Test
    public void testSuccessfulInstall()
        throws InstallationException, UnsupportedEncodingException, IOException
    {
        File artifactFile =
            new File( session.getLocalRepositoryManager().getRepository().getBasedir(), localArtifactPath );
        File metadataFile =
            new File( session.getLocalRepositoryManager().getRepository().getBasedir(), localMetadataPath );

        artifactFile.delete();
        metadataFile.delete();

        request.addArtifact( artifact );
        request.addMetadata( metadata );

        InstallResult result = installer.install( session, request );

        assertTrue( artifactFile.exists() );
        TestFileUtils.assertContent( "artifact".getBytes( "UTF-8" ), artifactFile );

        assertTrue( metadataFile.exists() );
        TestFileUtils.assertContent( "metadata".getBytes( "UTF-8" ), metadataFile );

        assertEquals( result.getRequest(), request );

        assertEquals( result.getArtifacts().size(), 1 );
        assertTrue( result.getArtifacts().contains( artifact ) );

        assertEquals( result.getMetadata().size(), 1 );
        assertTrue( result.getMetadata().contains( metadata ) );
    }

    @Test( expected = InstallationException.class )
    public void testNullArtifactFile()
        throws InstallationException
    {
        InstallRequest request = new InstallRequest();
        request.addArtifact( artifact.setFile( null ) );

        DefaultInstaller installer = new DefaultInstaller();
        installer.install( session, request );
    }

    @Test( expected = InstallationException.class )
    public void testNullMetadataFile()
        throws InstallationException
    {
        InstallRequest request = new InstallRequest();
        request.addMetadata( metadata.setFile( null ) );

        DefaultInstaller installer = new DefaultInstaller();
        installer.install( session, request );
    }

    @Test( expected = InstallationException.class )
    public void testArtifactExistsAsDir()
        throws InstallationException
    {
        String path = session.getLocalRepositoryManager().getPathForLocalArtifact( artifact );
        File file = new File( session.getLocalRepository().getBasedir(), path );
        assertFalse( file.getAbsolutePath() + " is a file, not directory", file.isFile() );
        assertFalse( file.getAbsolutePath() + " already exists", file.exists() );
        assertTrue( "failed to setup test: could not create " + file.getAbsolutePath(),
                    file.mkdirs() || file.isDirectory() );

        request.addArtifact( artifact );
        new DefaultInstaller().install( session, request );
    }

    @Test( expected = InstallationException.class )
    public void testMetadataExistsAsDir()
        throws InstallationException
    {
        String path = session.getLocalRepositoryManager().getPathForLocalMetadata( metadata );
        assertTrue( "failed to setup test: could not create " + path,
                    new File( session.getLocalRepository().getBasedir(), path ).mkdirs() );

        request.addMetadata( metadata );
        new DefaultInstaller().install( session, request );
    }

    @Test
    public void testSuccessfulArtifactEvents()
        throws InstallationException
    {
        InstallRequest request = new InstallRequest();
        request.addArtifact( artifact );

        installer.install( session, request );
        checkEvents( "Repository Event problem", artifact, false );
    }

    @Test
    public void testSuccessfulMetadataEvents()
        throws InstallationException
    {
        InstallRequest request = new InstallRequest();
        request.addMetadata( metadata );

        installer.install( session, request );
        checkEvents( "Repository Event problem", metadata, false );
    }

    @Test
    public void testFailingEventsNullArtifactFile()
    {
        checkFailedEvents( "null artifact file", this.artifact.setFile( null ) );
    }

    @Test
    public void testFailingEventsNullMetadataFile()
    {
        checkFailedEvents( "null metadata file", this.metadata.setFile( null ) );
    }

    @Test
    public void testFailingEventsArtifactExistsAsDir()
    {
        String path = session.getLocalRepositoryManager().getPathForLocalArtifact( artifact );
        assertTrue( "failed to setup test: could not create " + path,
                    new File( session.getLocalRepository().getBasedir(), path ).mkdirs() );
        checkFailedEvents( "target exists as dir", artifact );
    }

    @Test
    public void testFailingEventsMetadataExistsAsDir()
    {
        String path = session.getLocalRepositoryManager().getPathForLocalMetadata( metadata );
        assertTrue( "failed to setup test: could not create " + path,
                    new File( session.getLocalRepository().getBasedir(), path ).mkdirs() );
        checkFailedEvents( "target exists as dir", metadata );
    }

    private void checkFailedEvents( String msg, Metadata metadata )
    {
        InstallRequest request = new InstallRequest().addMetadata( metadata );
        msg = "Repository events problem (case: " + msg + ")";

        try
        {
            installer.install( session, request );
            fail( "expected exception" );
        }
        catch ( InstallationException e )
        {
            checkEvents( msg, metadata, true );
        }

    }

    private void checkEvents( String msg, Metadata metadata, boolean failed )
    {
        List<EventWrapper> events = listener.getEvents();
        assertEquals( msg, 2, events.size() );
        EventWrapper wrapper = events.get( 0 );
        assertEquals( msg, METADATA_INSTALLING, wrapper.getType() );

        RepositoryEvent event = wrapper.getEvent();
        assertEquals( msg, metadata, event.getMetadata() );
        assertNull( msg, event.getException() );

        wrapper = events.get( 1 );
        assertEquals( msg, METADATA_INSTALLED, wrapper.getType() );
        event = wrapper.getEvent();
        assertEquals( msg, metadata, event.getMetadata() );
        if ( failed )
        {
            assertNotNull( msg, event.getException() );
        }
        else
        {
            assertNull( msg, event.getException() );
        }
    }

    private void checkFailedEvents( String msg, Artifact artifact )
    {
        InstallRequest request = new InstallRequest().addArtifact( artifact );
        msg = "Repository events problem (case: " + msg + ")";

        try
        {
            installer.install( session, request );
            fail( "expected exception" );
        }
        catch ( InstallationException e )
        {
            checkEvents( msg, artifact, true );
        }
    }

    private void checkEvents( String msg, Artifact artifact, boolean failed )
    {
        List<EventWrapper> events = listener.getEvents();
        assertEquals( msg, 2, events.size() );
        EventWrapper wrapper = events.get( 0 );
        assertEquals( msg, ARTIFACT_INSTALLING, wrapper.getType() );
        
        RepositoryEvent event = wrapper.getEvent();
        assertEquals( msg, artifact, event.getArtifact() );
        assertNull( msg, event.getException() );
        
        wrapper = events.get( 1 );
        assertEquals( msg, ARTIFACT_INSTALLED, wrapper.getType() );
        event = wrapper.getEvent();
        assertEquals( msg, artifact, event.getArtifact() );
        if ( failed )
        {
            assertNotNull( msg + " > expected exception", event.getException() );
        }
        else
        {
            assertNull( msg + " > " + event.getException(), event.getException() );
        }
    }
}
