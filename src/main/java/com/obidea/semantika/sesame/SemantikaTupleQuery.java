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

import java.util.ArrayList;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.impl.AbstractQuery;
import org.openrdf.query.impl.IteratingTupleQueryResult;

import com.obidea.semantika.queryanswer.exception.QueryAnswerException;
import com.obidea.semantika.queryanswer.internal.SelectQuery;
import com.obidea.semantika.queryanswer.result.IQueryResult;

import info.aduna.iteration.CloseableIteration;

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
         return new IteratingTupleQueryResult(new ArrayList<String>(result.getSelectNames()), bindingsIter);
      }
      catch (QueryAnswerException e) {
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
