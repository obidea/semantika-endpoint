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

import java.util.logging.Logger;

import org.openrdf.model.IRI;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.Query;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.Update;
import org.openrdf.query.parser.ParsedBooleanQuery;
import org.openrdf.query.parser.ParsedGraphQuery;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.ParsedTupleQuery;
import org.openrdf.query.parser.QueryParserUtil;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.UnknownTransactionStateException;
import org.openrdf.repository.base.AbstractRepositoryConnection;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;

import com.obidea.semantika.queryanswer.SparqlQueryEngine;
import com.obidea.semantika.queryanswer.exception.QueryAnswerException;
import com.obidea.semantika.queryanswer.internal.SelectQuery;

import info.aduna.iteration.EmptyIteration;

public class SemantikaRepositoryConnection extends AbstractRepositoryConnection
{
   private static final Logger LOG = Logger.getLogger(SemantikaRepositoryConnection.class.toString());

   public SemantikaRepositoryConnection(Repository repository)
   {
      super(repository);
   }

   @Override
   public SemantikaVirtualRepository getRepository()
   {
      return (SemantikaVirtualRepository) super.getRepository();
   }

   @Override
   public void begin() throws RepositoryException
   {
      if (!getRepository().isInitialized()) {
         LOG.info("Initializing service connection"); //$NON-NLS-1$
         getRepository().initialize();
      }
   }

   @Override
   public Query prepareQuery(QueryLanguage ql, String query, String baseURI)
         throws RepositoryException, MalformedQueryException
   {
      validateQueryLanguage(ql);
      ParsedQuery parsedQuery = QueryParserUtil.parseQuery(QueryLanguage.SPARQL, query, baseURI);

      if (parsedQuery instanceof ParsedTupleQuery) {
         return prepareTupleQuery(ql, query, baseURI);
      }
      else if (parsedQuery instanceof ParsedBooleanQuery) {
         return prepareBooleanQuery(ql, query, baseURI);
      }
      else if (parsedQuery instanceof ParsedGraphQuery) {
         return prepareGraphQuery(ql, query, baseURI);
      }
      throw new MalformedQueryException("Unrecognized query type:\n" + query); //$NON-NLS-1$
   }

   @Override
   public TupleQuery prepareTupleQuery(QueryLanguage ql, String query, String baseURI)
         throws RepositoryException, MalformedQueryException
   {
      validateQueryLanguage(ql);
      try {
         SparqlQueryEngine queryEngine = getRepository().getQueryEngine();
         SelectQuery selectQuery = queryEngine.createSelectQuery(query);
         return new SemantikaTupleQuery(selectQuery);
      }
      catch (QueryAnswerException e) {
         throw new MalformedQueryException(e);
      }
   }

   @Override
   public GraphQuery prepareGraphQuery(QueryLanguage ql, String query, String baseURI)
         throws RepositoryException, MalformedQueryException
   {
      validateQueryLanguage(ql);
      throw new UnsupportedOperationException("Graph query is not supported"); //$NON-NLS-1$
   }

   @Override
   public BooleanQuery prepareBooleanQuery(QueryLanguage ql, String query, String baseURI)
         throws RepositoryException, MalformedQueryException
   {
      validateQueryLanguage(ql);
      throw new UnsupportedOperationException("Boolean query is not supported"); //$NON-NLS-1$
   }

   @Override
   public Update prepareUpdate(QueryLanguage ql, String update, String baseURI)
         throws RepositoryException, MalformedQueryException
   {
      validateQueryLanguage(ql);
      throw new UnsupportedOperationException("Update query is not supported"); //$NON-NLS-1$
   }

   private static void validateQueryLanguage(QueryLanguage ql) throws MalformedQueryException
   {
      if (!ql.equals(QueryLanguage.SPARQL)) {
         throw new MalformedQueryException("Support only SPARQL language"); //$NON-NLS-1$
      }
   }

   @Override
   public RepositoryResult<Resource> getContextIDs() throws RepositoryException
   {
      return null;
   }

   @Override
   public RepositoryResult<Statement> getStatements(Resource subj, IRI pred, Value obj,
         boolean includeInferred, Resource... contexts) throws RepositoryException
   {
      return null;
   }

   @Override
   public void exportStatements(Resource subj, IRI pred, Value obj, boolean includeInferred,
         RDFHandler handler, Resource... contexts) throws RepositoryException, RDFHandlerException
   {
      throw new UnsupportedOperationException("Update query is not supported"); //$NON-NLS-1$
   }

   @Override
   public long size(Resource... contexts) throws RepositoryException
   {
      return -1;
   }

   @Override
   public void commit() throws RepositoryException
   {
      // NO-OP
   }

   @Override
   public void rollback() throws RepositoryException
   {
      // NO-OP
   }

   @Override
   public RepositoryResult<Namespace> getNamespaces() throws RepositoryException
   {
      return new RepositoryResult<Namespace>(new EmptyIteration<Namespace, RepositoryException>());
   }

   @Override
   public String getNamespace(String prefix) throws RepositoryException
   {
      return null;
   }

   @Override
   public void setNamespace(String prefix, String name) throws RepositoryException
   {
      // NO-OP
   }

   @Override
   public void removeNamespace(String prefix) throws RepositoryException
   {
      // NO-OP
   }

   @Override
   public void clearNamespaces() throws RepositoryException
   {
      // NO-OP
   }

   @Override
   public boolean isActive() throws UnknownTransactionStateException, RepositoryException
   {
      return getRepository().isInitialized();
   }

   @Override
   protected void addWithoutCommit(Resource subject, IRI predicate, Value object,
         Resource... contexts) throws RepositoryException
   {
      // NO-OP
   }

   @Override
   protected void removeWithoutCommit(Resource subject, IRI predicate, Value object,
         Resource... contexts) throws RepositoryException
   {
      // NO-OP
   }
}
