package gov.epa.owm.mtb.cwns.common.struts;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.portal.log.LogManager;
import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;
import oracle.portal.utils.v2.NameValuePair;
import oracle.webdb.utils.SimpleStringBuffer;

public class CWNSStrutsUtils {

	public CWNSStrutsUtils() {
	}

	public static String createHref(HttpServletRequest httpservletrequest,
			String s) throws JspException {
		PortletRenderRequest portletrenderrequest = (PortletRenderRequest) httpservletrequest
				.getAttribute("oracle.portal.PortletRenderRequest");
		String s1 = httpservletrequest.getContextPath();
		if (s.startsWith(s1))
			s = s.substring(s1.length());
		String s2 = PortletRendererUtil.portletParameter(portletrenderrequest,
				"strutsAction");
		NameValuePair anamevaluepair[] = { new NameValuePair(s2, s) };
		String s3 = null;
		try {
			s3 = CWNSUrlUtils.constructLink(portletrenderrequest, 0,
					anamevaluepair, true, false);
		} catch (IOException ioexception) {
			throw new JspException(ioexception);
		}
		if (LogManager.isDebugEnabled())
			LogManager.debug((new SimpleStringBuffer(50)).append("Page link: ")
					.append(s3).toString());
		return s3;
	}

	public static boolean isPortalRequest(HttpServletRequest httpservletrequest) {
		return httpservletrequest
				.getAttribute("oracle.portal.PortletRenderRequest") != null;
	}

	public static String createPDKTokenName(
			HttpServletRequest httpservletrequest, String s) {
		PortletRenderRequest portletrenderrequest = (PortletRenderRequest) httpservletrequest
				.getAttribute("oracle.portal.PortletRenderRequest");
		String s1 = portletrenderrequest.getPortletInstance().getInstanceName();
		String s2 = (new SimpleStringBuffer(60)).append(s1).append(s)
				.toString();
		return s2;
	}
}
