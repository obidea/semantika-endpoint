package com.obidea.semantika.sesame;

import java.util.Map;
import java.util.WeakHashMap;

import org.openrdf.repository.Repository;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.config.RepositoryFactory;
import org.openrdf.repository.config.RepositoryImplConfig;

import com.obidea.semantika.app.ApplicationFactory;
import com.obidea.semantika.app.ApplicationManager;
import com.obidea.semantika.exception.SemantikaException;
import com.obidea.semantika.queryanswer.SparqlQueryEngine;

public class SemantikaRepositoryFactory implements RepositoryFactory
{
   public static final String REPOSITORY_TYPE = "sma:SemantikaVRepo"; //$NON-NLS-1$

   Map<RepositoryImplConfig, ApplicationManager> mAppCache = new WeakHashMap<RepositoryImplConfig, ApplicationManager>();

   @Override
   public RepositoryImplConfig getConfig()
   {
      return new SemantikaRepositoryConfig();
   }

   @Override
   public Repository getRepository(RepositoryImplConfig config) throws RepositoryConfigException
   {
      if (mAppCache.containsKey(config)) {
         ApplicationManager appManager = mAppCache.get(config);
         return createSemantikaVirtualRepository(appManager);
      }
      if (config instanceof SemantikaRepositoryConfig) {
         try {
            ApplicationManager appManager = setupApplicationManager((SemantikaRepositoryConfig) config);
            mAppCache.put(config, appManager);
            return createSemantikaVirtualRepository(appManager);
         }
         catch (SemantikaException e) {
            throw new RepositoryConfigException(e); //$NON-NLS-1$
         }
      }
      else {
         throw new RepositoryConfigException("Invalid configuration class"); //$NON-NLS-1$
      }
   }

   private static SemantikaVirtualRepository createSemantikaVirtualRepository(ApplicationManager appManager)
   {
      SparqlQueryEngine queryEngine = appManager.createQueryEngine();
      return new SemantikaVirtualRepository(queryEngine);
   }

   private ApplicationManager setupApplicationManager(SemantikaRepositoryConfig config) throws SemantikaException, RepositoryConfigException
   {
      config.validate();
      String cfgPath = config.getConfigurationPath();
      ApplicationManager appManager = new ApplicationFactory().configure(cfgPath).createApplicationManager();
      return appManager;
   }

   @Override
   public String getRepositoryType()
   {
      return REPOSITORY_TYPE;
   }
}
