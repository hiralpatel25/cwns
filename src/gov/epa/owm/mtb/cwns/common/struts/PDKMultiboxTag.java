package gov.epa.owm.mtb.cwns.common.struts;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;
import oracle.webdb.provider.v2.struts.StrutsUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.taglib.html.MultiboxTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;


/**
 * This class overrides the default struts MultiboxTag implementation to work with oracle PDK
 * @author Raj Lingam
 *
 */
public class PDKMultiboxTag extends MultiboxTag {

	public PDKMultiboxTag() {
	}

	public int doEndTag() throws JspException {
		if(!StrutsUtils.isPortalRequest((HttpServletRequest)pageContext.getRequest()))
            return super.doEndTag();		
		StringBuffer results = new StringBuffer("<input type=\"checkbox\"");
		results.append(" name=\"");
        if(indexed)
            prepareIndex(results, name);
        HttpServletRequest httpservletrequest = (HttpServletRequest)pageContext.getRequest();
        PortletRenderRequest portletrenderrequest = (PortletRenderRequest)httpservletrequest.getAttribute("oracle.portal.PortletRenderRequest");
        String s = PortletRendererUtil.portletParameter(portletrenderrequest, property);
        results.append(s);
		results.append("\"");
		if (accesskey != null) {
			results.append(" accesskey=\"");
			results.append(accesskey);
			results.append("\"");
		}
		if (tabindex != null) {
			results.append(" tabindex=\"");
			results.append(tabindex);
			results.append("\"");
		}
		results.append(" value=\"");
		String value = this.value;
		if (value == null)
			value = constant;
		if (value == null) {
			JspException e = new JspException(messages
					.getMessage("multiboxTag.value"));
			pageContext
					.setAttribute("org.apache.struts.action.EXCEPTION", e, 2);
			throw e;
		}
		results.append(ResponseUtils.filter(value));
		results.append("\"");
		Object bean = RequestUtils.lookup(pageContext, name, null);
		String values[] = null;
		if (bean == null)
			throw new JspException(messages.getMessage("getter.bean", name));
		try {
			values = BeanUtils.getArrayProperty(bean, property);
			if (values == null)
				values = new String[0];
		} catch (IllegalAccessException e) {
			throw new JspException(messages.getMessage("getter.access",
					property, name));
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			throw new JspException(messages.getMessage("getter.result",
					property, t.toString()));
		} catch (NoSuchMethodException e) {
			throw new JspException(messages.getMessage("getter.method",
					property, name));
		}
		for (int i = 0; i < values.length; i++) {
			if (!value.equals(values[i]))
				continue;
			results.append(" checked=\"checked\"");
			break;
		}

		results.append(prepareEventHandlers());
		results.append(prepareStyles());
		results.append(getElementClose());
		ResponseUtils.write(pageContext, results.toString());
		return 6;
	}
}
