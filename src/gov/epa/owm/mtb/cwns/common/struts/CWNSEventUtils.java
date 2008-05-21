/* Copyright (c) 2001, 2005 Oracle Corporation. All rights reserved. */

package gov.epa.owm.mtb.cwns.common.struts;

import oracle.portal.provider.v2.PortletException;
import oracle.portal.provider.v2.event.EventUtils;
import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.url.UrlUtils;
import oracle.portal.utils.NameValue;

/**
 * Contains utility methods and constants for events.
 */
public class CWNSEventUtils extends EventUtils
{
    /**
     * Create a parameterized link for a portlet event.
     *
     * Note the input event parameter names should be the base
     * name not an equivalent url parameter name obtained from
     * eventParameter(String name).
     *
     * Consequently if you want to render a link that both
     * contains event parameters and additional private portlet
     * parameters you will have to construct it manually using
     * the parameterizeLink method.
     *
     * @param pr              the render request.
     *
     * @param eventName       the event name.
     *
     * @param eventParameters an array of NameValue objects representing
     *                        the event parameter names and associated values.
     *                        Names cannot be NULL.
     *
     * @param encodeParams    flag indicating whether the parameter names and
     *                        values should be URL encoded using the multibyte
     *                        URL encoder.
     *
     * @param replaceParams   flag indicating whether parameters contained in
     *                        the params array should replace existing
     *                        parameters of the same name.
     *                        true = replace or overwrite existing parameters.
     *                        false = simply add parameters.
     */
    public static String constructEventLink(PortletRenderRequest pr,
                                            String eventName,
                                            NameValue[] eventParameters,
                                            boolean encodeParams,
                                            boolean replaceParams)
        throws PortletException
    {
        try
        {
            // must convert event name and event parameters
            // into url parameters

            NameValue[] urlParameters;
            if (eventParameters == null)
            {
                urlParameters = new NameValue[1];

                // add event name
                urlParameters[0] = new NameValue( eventName(eventName), "" );
            }
            else
            {
                urlParameters = new NameValue[eventParameters.length+1];

                // add event parameters
                int i;
                for ( i=0; i<eventParameters.length; i++)
                {
                    String name  = eventParameters[i].getName();
                    String value = eventParameters[i].getValue();
                    if (name != null)
                    {
                      urlParameters[i] = new NameValue( eventParameter(name), value );
                    }
                }

                // add event name
                urlParameters[i] = new NameValue( eventName(eventName), "" );
            }

            // return complete url
            return CWNSUrlUtils.constructLink(
                             pr,
                             UrlUtils.EVENT_LINK,
                             urlParameters,
                             encodeParams,
                             replaceParams);
        }
        catch (Exception e)
        {
            throw new PortletException(e);
        }
    }

}
