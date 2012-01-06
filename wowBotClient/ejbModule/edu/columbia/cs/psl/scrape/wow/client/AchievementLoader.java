package edu.columbia.cs.psl.scrape.wow.client;

import java.util.HashSet;

import javax.naming.InitialContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

//import edu.columbia.cs.psl.scrape.wow.StaticDataLoaderRemote;
//import edu.columbia.cs.psl.scrape.wow.entity.Achievement;

public class AchievementLoader {
//	StaticDataLoaderRemote loader ;
//	public AchievementLoader()
//	{
//		try
//		{
//		InitialContext ctx = new InitialContext();
//		loader = (StaticDataLoaderRemote) ctx.lookup("edu.columbia.cs.psl.scrape.wow.StaticDataLoaderRemote");		
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}
//	}
//	public void go()
//	{
//		try{
//			
//
//			   HttpClient httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());
//		        HttpGet httpget = new HttpGet("http://us.battle.net/api/wow/data/character/achievements");
//				HttpResponse resp = httpclient.execute(httpget);
//				String data = EntityUtils.toString(resp.getEntity());
//				JSONObject o = JSONObject.fromObject(data);
//				JSONArray a = o.getJSONArray("achievements");
//				for(int i = 0;i<a.size();i++)
//				{
//					JSONObject ob = a.getJSONObject(i);
//					if(ob.containsKey("achievements"))
//					{
//						JSONArray ach = ob.getJSONArray("achievements");
//						processJSONAchievementList(ach);
//					}
//					if(ob.containsKey("categories"))
//					{
//						JSONArray cats = ob.getJSONArray("categories");
//						for(int j = 0; j< cats.size();j++)
//						{
//							JSONArray ach = cats.getJSONObject(j).getJSONArray("achievements");
//							processJSONAchievementList(ach);
//						}
//					}
//				}
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//		}
//	}
//	private HashSet<Integer> ids = new HashSet<Integer>();
//	private void processJSONAchievementList(JSONArray a)
//	{
//		for(int i = 0;i<a.size();i++)
//		{
//			JSONObject ac = a.getJSONObject(i);
//			if(ids.contains(ac.getInt("id")))
//				continue;
//			Achievement n = new Achievement();
//			n.setId(ac.getInt("id"));
//			n.setDescription(ac.getString("description"));
//			n.setPoints(ac.getInt("points"));
//			n.setTitle(ac.getString("title"));
//			loader.loadAchievement(n);
//			ids.add(n.getId());
//		}
//	}
//	public static void main(String[] args) {
//		new AchievementLoader().go();
//	}
}
