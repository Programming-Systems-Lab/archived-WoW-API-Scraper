package edu.columbia.cs.psl.wowscrape;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.rpc.ServiceException;

import com.crowsoftware.jira.soap.JiraSoapService;
import com.crowsoftware.jira.soap.JiraSoapServiceServiceLocator;
import com.crowsoftware.jira.soap.RemoteAuthenticationException;
import com.crowsoftware.jira.soap.RemoteComponent;
import com.crowsoftware.jira.soap.RemoteException;
import com.crowsoftware.jira.soap.RemoteIssue;

public class Utils {
	private static JiraSoapService svc = null;
	public static String getHostName()
	{
		try {
		    return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return null;
	}
	public static void reportError(Exception ex)
	{
		reportError(ex,null);
	}
	public static void reportError(Exception ex, String message)
	{
		if(svc == null)
		{
			try {
				svc = new JiraSoapServiceServiceLocator().getJirasoapserviceV2(new URL("jiraWSDLURL"));
			} catch (ServiceException e) {
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			String token = svc.login("jirausername", "jirapassword");
			RemoteIssue issue = new RemoteIssue();
			
			StringWriter stackTrace = new StringWriter();
		    ex.printStackTrace(new PrintWriter(stackTrace));
		    issue.setSummary("Scraper: " + ex.getMessage() + " on " + getHostName());
			issue.setDescription("Error from machine: " + getHostName()+"\n"+stackTrace.getBuffer() + (message == null ? "" : "\nState: "+message));
			issue.setProject("OPS");
			issue.setType("1");
			RemoteComponent[] p = new RemoteComponent[1];
			p[0] = new RemoteComponent();
			p[0].setId(""+10100);
			issue.setComponents(p);
			svc.createIssue(token, issue);
			svc.logout(token);
		} catch (RemoteAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.rmi.RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
