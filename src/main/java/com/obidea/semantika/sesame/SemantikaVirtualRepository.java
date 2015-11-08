/*
 * Copyright (c) 2013-2015 Obidea Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.obidea.semantika.sesame;

import java.io.File;
import java.util.logging.Logger;

import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import com.obidea.semantika.queryanswer.QueryEngineException;
import com.obidea.semantika.queryanswer.SparqlQueryEngine;

public class SemantikaVirtualRepository implements Repository
{
   private SparqlQueryEngine mQueryEngine;

   private static final Logger LOG = Logger.getLogger(SemantikaVirtualRepository.class.toString());

   public SemantikaVirtualRepository(SparqlQueryEngine queryEngine)
   {
      mQueryEngine = queryEngine;
   }

   @Override
   public RepositoryConnection getConnection() throws RepositoryException
   {
      return new SemantikaRepositoryConnection(this);
   }

   /* package */SparqlQueryEngine getQueryEngine()
   {
      return mQueryEngine;
   }

   @Override
   public File getDataDir()
   {
      return null; // no data directory
   }

   @Override
   public ValueFactory getValueFactory()
   {
      return SimpleValueFactory.getInstance();
   }

   @Override
   public void initialize() throws RepositoryException
   {
      try {
         if (!mQueryEngine.isStarted()) {
            LOG.info("Initializing SPARQL QueryEngine service"); //$NON-NLS-1$
            mQueryEngine.start();
         }
      }
      catch (QueryEngineException e) {
         throw new RepositoryException(e);
      }
   }

   @Override
   public boolean isInitialized()
   {
      return mQueryEngine.isStarted();
   }

   @Override
   public boolean isWritable() throws RepositoryException
   {
      return false;
   }

   @Override
   public void setDataDir(File arg0)
   {
      // NO-OP
   }

   @Override
   public void shutDown() throws RepositoryException
   {
      try {
         LOG.info("Stopping SPARQL QueryEngine service"); //$NON-NLS-1$
         mQueryEngine.stop();
      }
      catch (QueryEngineException e) {
         throw new RepositoryException(e);
      }
   }
}
