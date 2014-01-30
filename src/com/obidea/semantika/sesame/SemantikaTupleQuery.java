package com.obidea.semantika.sesame;

import info.aduna.iteration.CloseableIteration;

import java.util.ArrayList;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.impl.AbstractQuery;
import org.openrdf.query.impl.TupleQueryResultImpl;

import com.obidea.semantika.exception.QueryException;
import com.obidea.semantika.queryanswer.SelectQuery;
import com.obidea.semantika.queryanswer.result.IQueryResult;

public class SemantikaTupleQuery extends AbstractQuery implements TupleQuery
{
   private SelectQuery mSelectQuery;

   public SemantikaTupleQuery(SelectQuery selectQuery)
   {
      mSelectQuery = selectQuery;
   }

   @Override
   public TupleQueryResult evaluate() throws QueryEvaluationException
   {
      try {
         IQueryResult result = mSelectQuery.evaluate();
         CloseableIteration<? extends BindingSet, QueryEvaluationException> bindingsIter = createBindingsIter(result);
         return new TupleQueryResultImpl(new ArrayList<String>(result.getSelectNames()), bindingsIter);
      }
      catch (QueryException e) {
         throw new QueryEvaluationException(e);
      }
   }

   private CloseableIteration<? extends BindingSet, QueryEvaluationException> createBindingsIter(IQueryResult result)
   {
      return new SemantikaQueryResultIteration(result);
   }

   @Override
   public void evaluate(TupleQueryResultHandler handler) throws QueryEvaluationException, TupleQueryResultHandlerException
   {
      throw new UnsupportedOperationException("Operation is not supported"); //$NON-NLS-1$
   }
}
