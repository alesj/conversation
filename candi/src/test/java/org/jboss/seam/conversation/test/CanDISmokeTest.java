/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.conversation.test;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.conversation.support.RealTestFilter;
import org.jboss.seam.conversation.support.SetupHttpSCCFilter;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Simple smoke test.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class CanDISmokeTest
{
   static String FILTER = 
         "<filter>" +
         "<filter-name>conversation</filter-name>" +
         "<filter-class>" + SetupHttpSCCFilter.class.getName() + "</filter-class>" +
         "</filter>" +
         "<filter>" +
         "<filter-name>test-filter</filter-name>" +
         "<filter-class>" + RealTestFilter.class.getName() + "</filter-class>" +
         "</filter>" +
         "<filter-mapping>" +
         "<filter-name>conversation</filter-name>" +
         "<url-pattern>/*</url-pattern>" +
         "</filter-mapping>" +
         "<filter-mapping>" +
         "<filter-name>test-filter</filter-name>" +
         "<url-pattern>/*</url-pattern>" +
         "</filter-mapping>";

   static Asset WEB_XML = new ByteArrayAsset(Deployments.extendDefaultWebXml(FILTER).getBytes());

   @Deployment
   public static WebArchive deployment()
   {
      return TomcatDeployments.tomcatfy(Deployments.baseDeployment(WEB_XML).addPackage(SetupHttpSCCFilter.class.getPackage()));
   }

   @Test
   public void testFactory() throws Exception
   {
      WebClient client = new WebClient();
      client.setThrowExceptionOnFailingStatusCode(false);
      Page page = client.getPage(Deployments.CONTEXT_PATH);
      Assert.assertEquals(200, page.getWebResponse().getStatusCode());
      page = client.getPage(Deployments.CONTEXT_PATH + "?cid=123");
      Assert.assertEquals(200, page.getWebResponse().getStatusCode());
   }
}
