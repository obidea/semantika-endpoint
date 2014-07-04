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

import java.util.List;

import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.impl.MapBindingSet;

import info.aduna.iteration.CloseableIteration;

import com.obidea.semantika.queryanswer.result.ILiteral;
import com.obidea.semantika.queryanswer.result.IQueryResult;
import com.obidea.semantika.queryanswer.result.IUri;
import com.obidea.semantika.queryanswer.result.IValue;
import com.obidea.semantika.queryanswer.result.IValueList;
import com.obidea.semantika.util.StringUtils;

public class SemantikaQueryResultIteration implements CloseableIteration<BindingSet, QueryEvaluationException>
{
   private IQueryResult mResult;

   private ValueFactory mValueFactory = ValueFactoryImpl.getInstance();

   public SemantikaQueryResultIteration(IQueryResult result)
   {
      mResult = result;
   }

   @Override
   public boolean hasNext() throws QueryEvaluationException
   {
      return (mResult.next()) ? true : false;
   }

   @Override
   public BindingSet next() throws QueryEvaluationException
   {
      IValueList valueList = mResult.getValueList();
      return convert(valueList);
   }

   @Override
   public void remove() throws QueryEvaluationException
   {
      throw new UnsupportedOperationException("Remove operation is not supported"); //$NON-NLS-1$
   }

   @Override
   public void close() throws QueryEvaluationException
   {
      // NO-OP
   }

   private BindingSet convert(IValueList valueList)
   {
      List<String> selectNames = valueList.getSelectNames();
      MapBindingSet result = new MapBindingSet(selectNames.size());
      for (String name : selectNames) {
         Value value = createValue(valueList.get(name));
         result.addBinding(name, value);
      }
      return result;
   }

   private Value createValue(IValue value)
   {
      if (value == null) {
         return null;
      }
      
      if (value instanceof ILiteral) {
         ILiteral literal = (ILiteral) value;
         String label = literal.stringValue();
         String lang = literal.getLanguage();
         if (!StringUtils.isEmpty(lang)) {
            return mValueFactory.createLiteral(label, lang);
         }
         else {
            String datatype = literal.getDatatype();
            return mValueFactory.createLiteral(label, mValueFactory.createURI(datatype));
         }
      }
      else if (value instanceof IUri) {
         IUri uri = (IUri) value;
         return mValueFactory.createURI(uri.stringValue());
      }
      return null;
   }
}
