package gov.epa.owm.mtb.cwns.common.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;
import oracle.webdb.provider.v2.struts.StrutsUtils;

import org.apache.struts.taglib.html.TextareaTag;

/**
 *
 * This class overrides the default struts TextareaTag implementation to work with oracle PDK
 *
 * @author Raj Lingam
 *
 */
public class PDKTextareaTag extends TextareaTag {

    protected String renderTextareaElement()
        throws JspException
    {
    	if(!StrutsUtils.isPortalRequest((HttpServletRequest)pageContext.getRequest()))
            return super.renderTextareaElement();	
        StringBuffer results = new StringBuffer("<textarea");
        results.append(" name=\"");
        if(indexed)
            prepareIndex(results, name);
        HttpServletRequest httpservletrequest = (HttpServletRequest)pageContext.getRequest();
        PortletRenderRequest portletrenderrequest = (PortletRenderRequest)httpservletrequest.getAttribute("oracle.portal.PortletRenderRequest");
        String s = PortletRendererUtil.portletParameter(portletrenderrequest, property);
        results.append(s);
		results.append("\"");        
        if(accesskey != null)
        {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if(tabindex != null)
        {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }
        if(cols != null)
        {
            results.append(" cols=\"");
            results.append(cols);
            results.append("\"");
        }
        if(rows != null)
        {
            results.append(" rows=\"");
            results.append(rows);
            results.append("\"");
        }
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(">");
        results.append(renderData());
        results.append("</textarea>");
        return results.toString();
    }
}
