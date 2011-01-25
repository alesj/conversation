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

package org.jboss.seam.conversation.plugins.weld;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.Instance;

import org.jboss.seam.conversation.spi.ConversationManager;
import org.jboss.weld.Container;
import org.jboss.weld.context.http.HttpConversationContext;

/**
 * Weld based conversation manager.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author Shane Bryzak
 */
public class WeldConversationManager implements ConversationManager
{
   private static Instance<Context> instance()
   {
      return Container.instance().deploymentManager().instance().select(Context.class);
   }

   public Conversation restoreConversationContext(String conversationId)
   {
      Instance<Context> instance = instance();
      HttpConversationContext conversationContext = instance.select(HttpConversationContext.class).get();

      if (conversationId != null && isEmpty(conversationId) == false)
      {
         conversationContext.activate(conversationId);
      }
      else
      {
         conversationContext.activate(null);
      }
      return conversationContext.getConversation(conversationId);
   }

   static boolean isEmpty(String string)
   {
      int len;
      if (string == null || (len = string.length()) == 0)
      {
         return true;
      }

      for (int i = 0; i < len; i++)
      {
         if ((Character.isWhitespace(string.charAt(i)) == false))
         {
            return false;
         }
      }
      return true;
   }
}
