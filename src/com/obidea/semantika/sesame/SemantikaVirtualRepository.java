package com.obidea.semantika.sesame;

import java.io.File;

import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import com.obidea.semantika.queryanswer.QueryEvaluationException;
import com.obidea.semantika.queryanswer.SparqlQueryEngine;

public class SemantikaVirtualRepository implements Repository
{
   private SparqlQueryEngine mQueryEngine;

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
      return ValueFactoryImpl.getInstance();
   }

   @Override
   public void initialize() throws RepositoryException
   {
      try {
         mQueryEngine.start();
      }
      catch (QueryEvaluationException e) {
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
      throw new UnsupportedOperationException("Setting data directory is not supported"); //$NON-NLS-1$
   }

   @Override
   public void shutDown() throws RepositoryException
   {
      try {
         mQueryEngine.stop();
      }
      catch (QueryEvaluationException e) {
         throw new RepositoryException(e);
      }
   }
}
