package edu.columbia.cs.psl.scrape.wow.client;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;


import edu.columbia.cs.psl.scrape.wow.Constants;
import edu.columbia.cs.psl.scrape.wow.Guild;
import edu.columbia.cs.psl.scrape.wow.ScrapeMaster;
import edu.columbia.cs.psl.scrape.wow.ScrapeMasterService;
import edu.columbia.cs.psl.scrape.wow.Toon;
import edu.columbia.cs.psl.scrape.wow.Utils;

public class GuildScraper {
	private int requestsToday = 0;
	private Calendar requestsTomorrowsDate;
	public GuildScraper() {

	}
	private String getHostName()
	{
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return null;
	}
	ScrapeMaster master;


	public void go()
	{
		String hostname = getHostName();
		master = (new ScrapeMasterService()).getScrapeMasterPort();
		requestsToday = master.register(Constants.PASSWORD, hostname);
		Timer t = new Timer(true);
		
		requestsTomorrowsDate= new GregorianCalendar();
		requestsTomorrowsDate.clear(Calendar.HOUR_OF_DAY);
		requestsTomorrowsDate.clear(Calendar.MINUTE);
		requestsTomorrowsDate.clear(Calendar.SECOND);
		requestsTomorrowsDate.clear(Calendar.MILLISECOND);
		requestsTomorrowsDate.add(Calendar.DAY_OF_MONTH, 1);
		
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				master.updateSlaveCount(requestsToday);
			}
		}, 5000, 5000);

		while(requestsToday < 3000)
		{
				doGuildScrape();
//			else if(requestsTomorrowsDate.before(new GregorianCalendar()))
//			{
//				requestsToday = 0;
//				master.updateSlaveCount(0);
//			}
//			else
//			{
//				try {
//					Thread.sleep(10800000);
//				} catch (InterruptedException e) {
//					Utils.reportError(e);
//				} //three hours
//			}
		}
	}
	public void doGuildScrape()
	{
		Guild toScrape = master.getGuildToScrape(Constants.PASSWORD);
		try
		{
			HttpClient httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());
			HttpGet httpget = new HttpGet("http://"+toScrape.getServer().getRegion()+
					".battle.net/api/wow/guild/"+toScrape.getServer().getSlug()+"/"+URLEncoder.encode(toScrape.getName(),"utf-8").replace("+", "%20")+"?fields=members");
			HttpResponse resp = httpclient.execute(httpget);
			String data = EntityUtils.toString(resp.getEntity());
			JSONObject ob = JSONObject.fromObject(data);
			if(ob.containsKey("status"))
			{
				if(ob.getString("reason").equals("Guild not found."))
					master.updateGuildStatus(toScrape.getId(), -1);
				else
					master.updateGuildStatus(toScrape.getId(), 0);
				System.out.println(toScrape.getName() +" " +ob);
			}
			else
			{
				JSONArray ar = ob.getJSONArray("members");
				ArrayList<Toon> toons = new ArrayList<Toon>();
				for(int i=0;i<ar.size();i++)
				{
					JSONObject toond= ar.getJSONObject(i).getJSONObject("character");
					int level = ar.getJSONObject(i).getInt("rank");
					Toon toon = new Toon();
					toon.setName(toond.getString("name"));
					if(toond.containsKey("class"))
						toon.setPClass(toond.getInt("class"));
					toon.setPRace(toond.getInt("race"));
					toon.setPLevel(toond.getInt("level"));
					toon.setGRank(level);
					toon.setExplored(0);
					toons.add(toon);
				}
				master.processGuild(toons, toScrape.getId());
				master.updateGuildStatus(toScrape.getId(), 1);
			}
			requestsToday++;
		}
		catch(Exception ex)
		{
			Utils.reportError(ex, "Working on guild " + toScrape.getId() + " (" + toScrape.getServer().getFullName() +" / " + toScrape.getName()+")");
		}
	}

	public static void main(String[] args) {
		new GuildScraper().go();
	}
}
