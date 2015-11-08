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

import org.openrdf.model.IRI;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.util.Models;
import org.openrdf.repository.config.AbstractDelegatingRepositoryImplConfig;
import org.openrdf.repository.config.RepositoryConfigException;

import com.obidea.semantika.util.StringUtils;

public class SemantikaRepositoryConfig extends AbstractDelegatingRepositoryImplConfig
{
   public static final String NAMESPACE = "http://www.obidea.com/semantika#"; //$NON-NLS-1$

   private static ValueFactory sValueFactory =  SimpleValueFactory.getInstance();

   /** <tt>http://www.obidea.com/semantika#cfgpath</tt> */
   public final static IRI SEMANTIKA_CONFIG_PATH = sValueFactory.createIRI(NAMESPACE, "cfgpath"); //$NON-NLS-1$

   private String mConfigurationPath = ""; //$NON-NLS-1$

   public SemantikaRepositoryConfig()
   {
      super(SemantikaRepositoryFactory.REPOSITORY_TYPE);
   }

   public String getConfigurationPath()
   {
      return mConfigurationPath;
   }

   void setConfigurationPath(String path)
   {
      mConfigurationPath = path;
   }

   @Override
   public void validate() throws RepositoryConfigException
   {
       if (StringUtils.isEmpty(mConfigurationPath)) {
           throw new RepositoryConfigException("Configuration path is empty"); //$NON-NLS-1$
       }
   }

   @Override
   public Resource export(Model model)
   {
      Resource implNode = super.export(model);

      if (!StringUtils.isEmpty(mConfigurationPath)) {
         model.add(implNode, SEMANTIKA_CONFIG_PATH, sValueFactory.createLiteral(mConfigurationPath));
      }
      return implNode;
   }

   @Override
   public void parse(Model model, Resource implNode) throws RepositoryConfigException
   {
      super.parse(model, implNode);
      Models.objectLiteral(model.filter(implNode, SEMANTIKA_CONFIG_PATH, null)).ifPresent(
            resource -> setConfigurationPath(resource.getLabel()));
   }
}
