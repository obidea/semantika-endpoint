/*
 * Copyright (c) 2013-2014 Obidea
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
import com.obidea.semantika.exception.SemantikaException;
import com.obidea.semantika.queryanswer.internal.ISelectQuery;
import com.obidea.semantika.queryanswer.result.IQueryResult;

public class SemantikaTupleQuery extends AbstractQuery implements TupleQuery
{
   private ISelectQuery mSelectQuery;

   public SemantikaTupleQuery(ISelectQuery selectQuery)
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
      catch (SemantikaException e) {
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
