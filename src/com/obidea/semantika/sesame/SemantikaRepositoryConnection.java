package com.obidea.semantika.sesame;

import info.aduna.iteration.EmptyIteration;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
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
import org.openrdf.repository.base.RepositoryConnectionBase;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;

import com.obidea.semantika.exception.QueryException;
import com.obidea.semantika.queryanswer.SelectQuery;
import com.obidea.semantika.queryanswer.SparqlQueryEngine;

public class SemantikaRepositoryConnection extends RepositoryConnectionBase
{
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
      getRepository().initialize();
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
         SelectQuery selectQuery = queryEngine.createQuery(query);
         return new SemantikaTupleQuery(selectQuery);
      }
      catch (QueryException e) {
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
   public RepositoryResult<Statement> getStatements(Resource subj, URI pred, Value obj,
         boolean includeInferred, Resource... contexts) throws RepositoryException
   {
      return null;
   }

   @Override
   public void exportStatements(Resource subj, URI pred, Value obj, boolean includeInferred,
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
   protected void addWithoutCommit(Resource subject, URI predicate, Value object,
         Resource... contexts) throws RepositoryException
   {
      // NO-OP
   }

   @Override
   protected void removeWithoutCommit(Resource subject, URI predicate, Value object,
         Resource... contexts) throws RepositoryException
   {
      // NO-OP
   }
}
