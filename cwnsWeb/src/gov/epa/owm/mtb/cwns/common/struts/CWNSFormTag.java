package gov.epa.owm.mtb.cwns.common.struts;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts.util.RequestUtils;

import oracle.portal.log.LogManager;
import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;
import oracle.portal.provider.v2.url.UrlUtils;
import oracle.webdb.provider.v2.taglib.struts.html.PDKFormTag;
import oracle.webdb.utils.HTTPUtils;
import oracle.webdb.utils.SimpleStringBuffer;
import org.apache.struts.taglib.html.FormTag;
import oracle.webdb.provider.v2.struts.StrutsUtils;
import org.apache.struts.util.ResponseUtils;
import javax.servlet.http.*;

public class CWNSFormTag extends FormTag {

	public CWNSFormTag()

	{

	}

	public int doStartTag()

	throws JspException

	{

		LogManager.debug("Entering PDKFormTag.doStartTag");

		if (!StrutsUtils.isPortalRequest((HttpServletRequest) pageContext
				.getRequest()))

		{

			LogManager
					.debug("Not a portal request so leaving PDKFormTag.doStartTag and calling org.apache.struts.taglib.html.FormTag#doStartTag");

			return super.doStartTag();

		} else

		{

			int i = super.doStartTag();

			ResponseUtils.write(pageContext, renderPDKHiddenFields());

			LogManager.debug("Leaving PDKFormTag.doStartTag");

			return i;

		}

	}

	protected String renderFormStartElement()

	{

		LogManager.debug("Entering PDKFormTag.renderFormStartElement");

		if (!StrutsUtils.isPortalRequest((HttpServletRequest) pageContext
				.getRequest()))

		{

			LogManager
					.debug("Not a portal request so leaving PDKFormTag.doStartTag and calling org.apache.struts.taglib.html.FormTag#renderFormStartElement");

			return super.renderFormStartElement();

		}

		HttpServletResponse httpservletresponse = (HttpServletResponse) pageContext
				.getResponse();

		SimpleStringBuffer simplestringbuffer = new SimpleStringBuffer("<form");

		simplestringbuffer.append(" name=\"");

		simplestringbuffer.append(beanName);

		simplestringbuffer.append("\"");

		simplestringbuffer.append(" method=\"");

		simplestringbuffer.append(method != null ? method : "post");

		simplestringbuffer.append("\" action=\"");

		HttpServletRequest httpservletrequest = (HttpServletRequest) pageContext
				.getRequest();

		PortletRenderRequest portletrenderrequest = (PortletRenderRequest) httpservletrequest
				.getAttribute("oracle.portal.PortletRenderRequest");

		simplestringbuffer.append(UrlUtils.htmlFormActionLink(
				portletrenderrequest, 0));

		simplestringbuffer.append("\"");

		if (styleClass != null)

		{

			simplestringbuffer.append(" class=\"");

			simplestringbuffer.append(styleClass);

			simplestringbuffer.append("\"");

		}

		if (enctype != null)

		{

			simplestringbuffer.append(" enctype=\"");

			simplestringbuffer.append(enctype);

			simplestringbuffer.append("\"");

		}

		if (onreset != null)

		{

			simplestringbuffer.append(" onreset=\"");

			simplestringbuffer.append(onreset);

			simplestringbuffer.append("\"");

		}

		if (onsubmit != null)

		{

			simplestringbuffer.append(" onsubmit=\"");

			simplestringbuffer.append(onsubmit);

			simplestringbuffer.append("\"");

		}

		if (style != null)

		{

			simplestringbuffer.append(" style=\"");

			simplestringbuffer.append(style);

			simplestringbuffer.append("\"");

		}

		if (styleId != null)

		{

			simplestringbuffer.append(" id=\"");

			simplestringbuffer.append(styleId);

			simplestringbuffer.append("\"");

		}

		if (target != null)

		{

			simplestringbuffer.append(" target=\"");

			simplestringbuffer.append(target);

			simplestringbuffer.append("\"");

		}

		simplestringbuffer.append(">");

		LogManager.debug("Leaving PDKFormTag.renderFormStartElement");

		return simplestringbuffer.toString();

	}

	protected String renderToken()

	{

		LogManager.debug("Entering PDKFormTag.renderToken");

		HttpServletRequest httpservletrequest = (HttpServletRequest) pageContext
				.getRequest();

		if (!StrutsUtils.isPortalRequest(httpservletrequest))

		{

			LogManager
					.debug("Not a portal request so leaving PDKFormTag.doStartTag and calling org.apache.struts.taglib.html.FormTag#renderToken");

			return super.renderToken();

		}

		SimpleStringBuffer simplestringbuffer = new SimpleStringBuffer(100);

		HttpSession httpsession = pageContext.getSession();

		String s = (String) httpsession.getAttribute(StrutsUtils
				.createPDKTokenName(httpservletrequest,
						"org.apache.struts.action.TOKEN"));

		if (LogManager.isDebugEnabled())

		{

			SimpleStringBuffer simplestringbuffer1 = new SimpleStringBuffer(50);

			simplestringbuffer1.append("Session synchronizer token name: ")
					.append(s);

			LogManager.debug(simplestringbuffer1.toString());

		}

		if (s != null)

		{

			simplestringbuffer.append("<input type=\"hidden\" name=\"");

			String s1 = null;

			PortletRenderRequest portletrenderrequest = (PortletRenderRequest) httpservletrequest
					.getAttribute("oracle.portal.PortletRenderRequest");

			String s2 = StrutsUtils.createPDKTokenName(httpservletrequest,
					"org.apache.struts.taglib.html.TOKEN");

			if (portletrenderrequest.getPortletDefinition()
					.getPassAllUrlParams())

				s1 = s2;

			else

				s1 = PortletRendererUtil.portletParameter(portletrenderrequest,
						s2);

			if (LogManager.isDebugEnabled())

			{

				SimpleStringBuffer simplestringbuffer2 = (new SimpleStringBuffer(
						100)).append(
						"Setting form synchronizer token name to: ").append(s1)
						.append("  value to: ").append(s);

				LogManager.debug(simplestringbuffer2.toString());

			}

			simplestringbuffer.append(s1);

			simplestringbuffer.append("\" value=\"");

			simplestringbuffer.append(s);

			if (RequestUtils.isXhtml(pageContext))

				simplestringbuffer.append("\" />");

			else

				simplestringbuffer.append("\">");

		}

		LogManager.debug("Leaving PDKFormTag.renderToken()");

		return simplestringbuffer.toString();

	}

	private String renderPDKHiddenFields()

	throws JspException

	{

		SimpleStringBuffer simplestringbuffer;

		HttpServletRequest httpservletrequest = (HttpServletRequest) pageContext
				.getRequest();

		PortletRenderRequest portletrenderrequest = (PortletRenderRequest) httpservletrequest
				.getAttribute("oracle.portal.PortletRenderRequest");

		simplestringbuffer = new SimpleStringBuffer(100);

		String s = CWNSUrlUtils.htmlFormHiddenFields(portletrenderrequest, 0);

		simplestringbuffer.append(s);

		String s1 = PortletRendererUtil.portletParameter(portletrenderrequest,
				"strutsAction");

		String s2 = RequestUtils.getActionMappingURL(action, pageContext);

		int i = httpservletrequest.getContextPath().length();

		String s3 = s2.substring(i + 1);

		String s4;

		try {
			s4 = HTTPUtils.encode(s3, portletrenderrequest.getURLCharSet());
		} catch (UnsupportedEncodingException unsupportedencodingexception) {

			JspException jspexception = new JspException(
					unsupportedencodingexception);

			LogManager.severe("Problem with encoding Struts action parameter",
					unsupportedencodingexception);

			throw jspexception;
		}

		String s5 = UrlUtils.emitHiddenField(s1, s4);

		simplestringbuffer.append(s5);

		return simplestringbuffer.toString();

	}

}
