package org.sonatype.aether.util.listener;

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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.sonatype.aether.AbstractRepositoryListener;
import org.sonatype.aether.RepositoryEvent;
import org.sonatype.aether.RepositoryListener;

/**
 * A repository listener that delegates to zero or more other listeners (multicast). The list of target listeners is
 * thread-safe, i.e. target listeners can be added or removed by any thread at any time.
 * 
 * @author Benjamin Bentmann
 */
public class ChainedRepositoryListener
    extends AbstractRepositoryListener
{

    private final List<RepositoryListener> listeners = new CopyOnWriteArrayList<RepositoryListener>();

    /**
     * Creates a new multicast listener that delegates to the specified listeners.
     * 
     * @param listeners The listeners to delegate to, may be {@code null} or empty.
     */
    public ChainedRepositoryListener( RepositoryListener... listeners )
    {
        if ( listeners != null )
        {
            add( Arrays.asList( listeners ) );
        }
    }

    /**
     * Creates a new multicast listener that delegates to the specified listeners.
     * 
     * @param listeners The listeners to delegate to, may be {@code null} or empty.
     */
    public ChainedRepositoryListener( Collection<RepositoryListener> listeners )
    {
        add( listeners );
    }

    /**
     * Adds the specified listeners to the end of the multicast chain.
     * 
     * @param listeners The listeners to add, may be {@code null} or empty.
     */
    public void add( Collection<RepositoryListener> listeners )
    {
        if ( listeners != null )
        {
            for ( RepositoryListener listener : listeners )
            {
                add( listener );
            }
        }
    }

    /**
     * Adds the specified listener to the end of the multicast chain.
     * 
     * @param listener The listener to add, may be {@code null}.
     */
    public void add( RepositoryListener listener )
    {
        if ( listener != null )
        {
            listeners.add( listener );
        }
    }

    /**
     * Removes the specified listener from the multicast chain. Trying to remove a non-existing listener has no effect.
     * 
     * @param listener The listener to remove, may be {@code null}.
     */
    public void remove( RepositoryListener listener )
    {
        if ( listener != null )
        {
            listeners.remove( listener );
        }
    }

    protected void handleError( RepositoryEvent event, RepositoryListener listener, RuntimeException error )
    {
        // default just swallows errors
    }

    @Override
    public void artifactDeployed( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.artifactDeployed( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void artifactDeploying( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.artifactDeploying( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void artifactDescriptorInvalid( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.artifactDescriptorInvalid( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void artifactDescriptorMissing( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.artifactDescriptorMissing( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void artifactInstalled( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.artifactInstalled( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void artifactInstalling( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.artifactInstalling( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void artifactResolved( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.artifactResolved( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void artifactResolving( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.artifactResolving( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void metadataDeployed( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.metadataDeployed( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void metadataDeploying( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.metadataDeploying( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void metadataInstalled( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.metadataInstalled( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void metadataInstalling( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.metadataInstalling( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void metadataInvalid( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.metadataInvalid( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void metadataResolved( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.metadataResolved( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

    @Override
    public void metadataResolving( RepositoryEvent event )
    {
        for ( RepositoryListener listener : listeners )
        {
            try
            {
                listener.metadataResolving( event );
            }
            catch ( RuntimeException e )
            {
                handleError( event, listener, e );
            }
        }
    }

}
