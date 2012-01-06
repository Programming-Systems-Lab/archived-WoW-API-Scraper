package edu.columbia.cs.psl.scrape.wow.client;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.Header;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

import ca.wowapi.utils.Base64Converter;

import sun.misc.BASE64Encoder;

import edu.columbia.cs.psl.scrape.wow.Achievement;
import edu.columbia.cs.psl.scrape.wow.AchievementProgress;
import edu.columbia.cs.psl.scrape.wow.Constants;
import edu.columbia.cs.psl.scrape.wow.DBUtils;
import edu.columbia.cs.psl.scrape.wow.Profession;
import edu.columbia.cs.psl.scrape.wow.Quest;
import edu.columbia.cs.psl.scrape.wow.Raid;
import edu.columbia.cs.psl.scrape.wow.RaidBoss;
import edu.columbia.cs.psl.scrape.wow.RaidBossProgress;
import edu.columbia.cs.psl.scrape.wow.RaidProgress;
import edu.columbia.cs.psl.scrape.wow.ReputationSink;
import edu.columbia.cs.psl.scrape.wow.ReputationStanding;
import edu.columbia.cs.psl.scrape.wow.ScrapeMaster;
import edu.columbia.cs.psl.scrape.wow.ScrapeMasterService;
import edu.columbia.cs.psl.scrape.wow.Utils;

public class QuestDataSCraper {
	private int requestsToday = 0;
	private boolean signRequests =false;
	private int limit = 3000;
	private long IP;
	
	SimpleDateFormat fmt;
	public QuestDataSCraper(int limit, boolean useKey) {
		fmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		rfcFmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		rfcFmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.limit = limit;
		this.signRequests = useKey;
		DBUtils.initDB();
	}
	private String getHostName()
	{
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return null;
	}

	
	private void updateSlaveCount(int n)
	{
		try
		{
			PreparedStatement s = DBUtils.conn.prepareStatement("UPDATE SLAVE set REQUESTSTODAY=?,LASTUPDATED=NOW() WHERE NAME=?");
			s.setInt(1, n);
			s.setString(2, getHostName());
			s.executeUpdate();
			s.close();
		}
		catch(Exception ex)
		{
			Utils.reportError(ex);
		}
	}
	private String padInt(int n)
	{
		if(n < 10)
			return "0"+n;
		else
			return ""+n;
	}
	public void go()
	{
		while(true)
		{
			doAHScrape();
		}
	}
	public static String getSignature(String request, String date)
	{
		String pubKey = null;
		String privKey = null;

		String toSign = "GET\n"+	date
				+"\n"+request+"\n";
//		System.out.println(toSign);
		try
		{
			return "BNET" + " " + pubKey + ":" + calculateRFC2104HMAC(toSign, privKey);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	public static String calculateRFC2104HMAC(String data, String key)
			throws java.security.SignatureException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException
			{
				//Marooned202's wowapi
				byte[] hmacData = null;
				SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"),
				"HmacSHA1");
				Mac mac = Mac.getInstance("HmacSHA1");
				mac.init(secretKey);
				hmacData = mac.doFinal(data.getBytes("UTF-8"));
				return Base64Converter.encode(hmacData);
			}
	HttpClient httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());
	private int decodeTimeLeft(String s)
	{
		if(s.equals("VERY_LONG"))
			return 5;
		if(s.equals("LONG"))
			return 4;
		if(s.equals("MEDIUM"))
			return 3;
		if(s.equals("SHORT"))
			return 1;
		if(s.equals("VERY_SHORT"))
			return 1;
		return 0;
	}
	private SimpleDateFormat rfcFmt;
	public void doAHScrape()
	{
		int quest =0;
		try{
			quest = getServerToScrape();
		}
		catch(Exception ex)
		{
			Utils.reportError(ex);
		}
		System.out.println(quest);
		if(quest <= 0)
			System.exit(0); //TODO do something else here involving sleeping
		SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));

		try
		{
			HttpGet httpget = new HttpGet("http://us.battle.net/api/wow/quest/"+quest);
			if(signRequests)
			{
				
					String date = fmt.format(new Date());
				String signature = getSignature("/api/wow/quest/"+quest,date);

				httpget.addHeader("Host","us.battle.net");
				httpget.addHeader("Date",date);
//				httpget.addHeader("User-Agent","Jonathan Bell (jbell@cs.columbia.edu)");
				httpget.addHeader("Authorization",signature);
			}
			
			HttpResponse resp = httpclient.execute(httpget);
			
			String data = EntityUtils.toString(resp.getEntity());
			if(resp.getStatusLine().getStatusCode() == 503 && resp.getStatusLine().getReasonPhrase().equals("Service Temporarily Unavailable"))
			{
				System.out.println(resp.getStatusLine());
				Thread.sleep(1000*10);//hold off for 10 seconds
				return;
			}
			JSONObject ob = JSONObject.fromObject(data);
			if(ob.containsKey("status"))
			{
				System.out.println(ob);
				Utils.reportError(new Exception("Server error: " + ob.getString("status")),data);
				System.exit(0);
			}
			else
			{
				System.out.println(ob);
				PreparedStatement ps = DBUtils.conn.prepareStatement("UPDATE QUEST set HAVEDATA=1, title=?, reqLevel=?,suggestedPartyMembers=?,category=?,level=? where BNET_ID=?");
				ps.setString(1, ob.getString("title"));
				ps.setInt(2, ob.getInt("reqLevel"));
				ps.setInt(3, ob.getInt("suggestedPartyMembers"));
				if(ob.has("category"))
					ps.setString(4, ob.getString("category"));
				else
					ps.setString(4, null);
				ps.setInt(5, ob.getInt("level"));
				ps.setInt(6, quest);
				ps.executeUpdate();
				ps.close();
//				System.exit(0);
			}
//				long lastMod = ob.getJSONArray("files").getJSONObject(0).getLong("lastModified")/1000;
//				String modDate = "";
//				
//				modDate = fmt.format(new Date(lastMod * 1000));
//				lastID = server.getId();
//				if(lastMod > server.getLastUpdated())
//				{
//					System.out.println("Fetching new data");
//					String file = ob.getJSONArray("files").getJSONObject(0).getString("url");
//					httpget = new HttpGet(file);
//					resp = httpclient.execute(httpget);
//					data = EntityUtils.toString(resp.getEntity());
//					
//					ob = JSONObject.fromObject(data);
//					String[] houses = {"alliance","horde","neutral"};
//					
//					Statement s = DBUtils.conn.createStatement();
//					int nAuctions = 0;
//					for(String house : houses)
//					{
//						String query = "INSERT INTO AUCTION_LOAD (ID, ITEM, OWNER, BID, BUYOUT, QUANTITY,TIMELEFT,SERVER_ID,HOUSE) VALUES ";
//						JSONArray auctions = ob.getJSONObject(house).getJSONArray("auctions");
//						boolean nothingNew = false;
//						for(int i = 0; i<auctions.size();i++)
//						{
//							nothingNew = false;
//							JSONObject auction = auctions.getJSONObject(i);
//
//							query += "("+auction.getLong("auc")+","+auction.getInt("item")+",'"+auction.getString("owner")+"',"+auction.getInt("bid")+","+auction.getInt("buyout")+","+auction.getInt("quantity")+","+decodeTimeLeft(auction.getString("timeLeft"))+","+server.getId()+",'"+house.toUpperCase()+"'), ";
//							if(i % 1000 == 0 && i > 0)
//							{
//								s.executeUpdate(query.substring(0,query.length()-2)+";");
//								query = "INSERT INTO AUCTION_LOAD (ID, ITEM, OWNER, BID, BUYOUT, QUANTITY,TIMELEFT,SERVER_ID,HOUSE) VALUES ";
//								nothingNew = true;
//							}
//						}
//						nAuctions += auctions.size();
//						if(!nothingNew)
//							s.executeUpdate(query.substring(0,query.length()-2)+";");
//					}
//					s.execute("CALL load_auction_data("+server.getId()+",'"+modDate+"');");
//					int duration = (int) ((System.currentTimeMillis() - start)/1000);
//					s.executeUpdate("INSERT INTO AH_SLAVE_HISTORY (IP,time,server_id,auctions,duration) VALUES ("+IP+",NOW(),"+server.getId()+","+nAuctions+","+duration+");");
//					s.executeUpdate("UPDATE SERVER SET AH_UPDATING=0, LAST_AH_MOD="+lastMod+", LAST_AH_UPDATER="+IP+" WHERE ID="+server.getId());
//					s.close();
//				}
//				else
//				{
//					System.out.println("Nothing new here");
//					updateServerStatus(server.getId(), 0);
//				}
//			}
//			requestsToday++;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
	
		}
	}

	private void updateServerStatus(int id, int i) {
		PreparedStatement s;
		try {
			s = DBUtils.conn.prepareStatement("UPDATE SERVER set AH_UPDATING=? WHERE id=?");
			s.setInt(1, i);
			s.setInt(2, id);
			s.executeUpdate();
			s.executeUpdate("INSERT INTO SLAVE_HISTORY (IP,time,toon_id,status) VALUES ("+IP+",NOW(),"+id+","+i+");");
			s.close();

		} catch (SQLException e) {
			Utils.reportError(e);
		}
	}
	
	Random rand = new Random();
	private int lastID = 0;
	private int getServerToScrape() {
		try
		{
		
			Statement s = DBUtils.conn.createStatement();
			s.execute("select BNET_ID from QUEST where havedata=0");
			ResultSet rs = s.getResultSet();
			if(rs.next())
			{
				int ret = rs.getInt(1);
				rs.close();
				s.close();
				return ret;
			}			
			return -1;
		}
		catch(Exception x)
		{
			Utils.reportError(x);
			return -1;
		}
	}
	public static void main(String[] args) {
		
		System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "9000");
//		
//		ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager();
//		HttpClient httpclient = new DefaultHttpClient(mgr);
//try{
//	SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
//	fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
//	
//		String date = fmt.format(new Date());
//		HttpGet httpget = new HttpGet("http://us.battle.net/api/wow/quest/2");
//			String signature = getSignature("/api/wow/quest/2",date);
//			httpget.addHeader("Host","us.battle.net");
//			httpget.addHeader("Date",date);
//			httpget.addHeader("User-Agent","Jonathan Bell (jbell@cs.columbia.edu)");
//			httpget.addHeader("Authorization",signature);
//		HttpResponse resp = httpclient.execute(httpget);
//		for(org.apache.http.Header h : resp.getAllHeaders())
//		{
//			System.out.println(h);
//		}
//		String data = EntityUtils.toString(resp.getEntity());
//
//		System.out.println(data);
//}
//catch(Exception ex)
//{
//	ex.printStackTrace();
//}
//		System.exit(0);
		int limit = -1;
		boolean signRequests = true;
		new QuestDataSCraper(limit, signRequests).go();
		try {
			DBUtils.conn.close();
		} catch (SQLException e) {
			Utils.reportError(e);
		}
	}
}
