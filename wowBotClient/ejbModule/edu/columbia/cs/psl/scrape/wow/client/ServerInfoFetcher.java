package edu.columbia.cs.psl.scrape.wow.client;

import java.net.URLEncoder;
import java.sql.PreparedStatement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

import edu.columbia.cs.psl.scrape.wow.DBUtils;

public class ServerInfoFetcher {
	public ServerInfoFetcher() {
		DBUtils.initDB();
	}
	public void go()
	{
//		getServerInfo("us"); 
//		getServerInfo("eu"); 
		getServerInfo("tw");
		getServerInfo("kr");
	}
	HttpClient httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());

	private void getServerInfo(String region)
	{
		try
		{
			HttpGet httpget = new HttpGet("http://"+region+
					".battle.net/api/wow/realm/status?locale=en_US");
			httpget.addHeader("Host",region+".battle.net");
			HttpResponse resp = httpclient.execute(httpget);
			String data = EntityUtils.toString(resp.getEntity());
			JSONObject ob = JSONObject.fromObject(data);
			JSONArray realms  = ob.getJSONArray("realms");
			for(int i = 0; i<realms.size();i++)
			{
				JSONObject s = realms.getJSONObject(i);
//				System.out.println(s);
//				System.exit(0);
				PreparedStatement ps = DBUtils.conn.prepareStatement("UPDATE SERVER set population=?,type=? where slug=? and region=?");
				
				ps.setString(1, s.getString("population"));
				ps.setString(2, s.getString("type"));
				ps.setString(3, s.getString("slug"));
				ps.setString(4, region);
				ps.executeUpdate();
				ps.close();

			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new ServerInfoFetcher().go();
	}
}
