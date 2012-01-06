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

public class AuctionScraper {
	private int requestsToday = 0;
	private boolean signRequests =false;
	private int limit = 3000;
	private long IP;
	
	SimpleDateFormat fmt;
	public AuctionScraper(int limit, boolean useKey) {
		fmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		rfcFmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		rfcFmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.limit = limit;
		this.signRequests = useKey;
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
		ScrapeMaster master = new ScrapeMasterService().getScrapeMasterPort();
		DBUtils.initDB();
		String hostname = getHostName();
		requestsToday = master.register(Constants.PASSWORD,hostname);
		IP = master.getMyIP();
		
		Timer t = new Timer(true);
		
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				updateSlaveCount(requestsToday);
			}
		}, 5000, 5000);

		while(limit < 0 || requestsToday < limit)
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
		Server server = null;
		try{
			server = getServerToScrape();
		}
		catch(Exception ex)
		{
			Utils.reportError(ex);
		}
		System.out.println(server.getSlug());
		if(server == null)
			System.exit(0); //TODO do something else here involving sleeping
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			long start = System.currentTimeMillis();
			HttpGet httpget = new HttpGet("http://"+server.getRegion()+
					".battle.net/api/wow/auction/data/"+server.getSlug());
			if(signRequests)
			{
				String signature = getSignature("http://"+server.getRegion()+
						".battle.net/api/wow/auction/data/"+server.getSlug(),"");
			}
			
			httpget.addHeader("Last-Modified",rfcFmt.format(new Date(server.getLastUpdated() * 1000)));
			HttpResponse resp = httpclient.execute(httpget);
			String data = EntityUtils.toString(resp.getEntity());
			if(resp.getStatusLine().getStatusCode() == 503 && resp.getStatusLine().getReasonPhrase().equals("Service Temporarily Unavailable"))
			{
				System.out.println(resp.getStatusLine());
				updateServerStatus(server.getId(), 0);
				Thread.sleep(1000*10);//hold off for 10 seconds
				return;
			}
			JSONObject ob = JSONObject.fromObject(data);
			if(ob.containsKey("status"))
			{
				System.out.println(ob);
				updateServerStatus(server.getId(), 0);
				Statement s = DBUtils.conn.createStatement();
				s.executeUpdate("UPDATE SLAVE set LASTSTOP=NOW(), KILL_REQ=0 WHERE NAME='"+getHostName()+"';");
				s.close();
				Utils.reportError(new Exception("Server error: " + ob.getString("status")),data);
				System.exit(0);
			}
			else
			{
				long lastMod = ob.getJSONArray("files").getJSONObject(0).getLong("lastModified")/1000;
				String modDate = "";
				
				modDate = fmt.format(new Date(lastMod * 1000));
				lastID = server.getId();
				if(lastMod > server.getLastUpdated())
				{
					System.out.println("Fetching new data");
					String file = ob.getJSONArray("files").getJSONObject(0).getString("url");
					httpget = new HttpGet(file);
					resp = httpclient.execute(httpget);
					data = EntityUtils.toString(resp.getEntity());
					
					ob = JSONObject.fromObject(data);
					String[] houses = {"alliance","horde","neutral"};
					
					Statement s = DBUtils.conn.createStatement();
					int nAuctions = 0;
					for(String house : houses)
					{
						String query = "INSERT INTO AUCTION_LOAD (ID, ITEM, OWNER, BID, BUYOUT, QUANTITY,TIMELEFT,SERVER_ID,HOUSE) VALUES ";
						JSONArray auctions = ob.getJSONObject(house).getJSONArray("auctions");
						boolean nothingNew = false;
						for(int i = 0; i<auctions.size();i++)
						{
							nothingNew = false;
							JSONObject auction = auctions.getJSONObject(i);

							query += "("+auction.getLong("auc")+","+auction.getInt("item")+",'"+auction.getString("owner")+"',"+auction.getInt("bid")+","+auction.getInt("buyout")+","+auction.getInt("quantity")+","+decodeTimeLeft(auction.getString("timeLeft"))+","+server.getId()+",'"+house.toUpperCase()+"'), ";
							if(i % 1000 == 0 && i > 0)
							{
								s.executeUpdate(query.substring(0,query.length()-2)+";");
								query = "INSERT INTO AUCTION_LOAD (ID, ITEM, OWNER, BID, BUYOUT, QUANTITY,TIMELEFT,SERVER_ID,HOUSE) VALUES ";
								nothingNew = true;
							}
						}
						nAuctions += auctions.size();
						if(!nothingNew)
							s.executeUpdate(query.substring(0,query.length()-2)+";");
					}
					s.execute("CALL load_auction_data("+server.getId()+",'"+modDate+"');");
					int duration = (int) ((System.currentTimeMillis() - start)/1000);
					s.executeUpdate("INSERT INTO AH_SLAVE_HISTORY (IP,time,server_id,auctions,duration) VALUES ("+IP+",NOW(),"+server.getId()+","+nAuctions+","+duration+");");
					s.executeUpdate("UPDATE SERVER SET AH_UPDATING=0, LAST_AH_MOD="+lastMod+", LAST_AH_UPDATER="+IP+" WHERE ID="+server.getId());
					s.close();
				}
				else
				{
					System.out.println("Nothing new here");
					updateServerStatus(server.getId(), 0);
				}
			}
			requestsToday++;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
			if(server == null)
				Utils.reportError(ex, "Null server returned");
			else
			{
				updateServerStatus(server.getId(), 0);
				Utils.reportError(ex, "Working on server " + server.getId());
				
			}
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
	private Server getServerToScrape() {
		try
		{
		java.sql.Statement s = DBUtils.conn.createStatement();
		s.execute("SELECT KILL_REQ FROM SLAVE WHERE NAME='"+getHostName()+"';");
		ResultSet rs = s.getResultSet();
		rs.next();
		if(rs.getInt(1) == 1)
		{
			rs.close();
			s.executeUpdate("UPDATE SLAVE set LASTSTOP=NOW(), KILL_REQ=0 WHERE NAME='"+getHostName()+"';");
			s.close();
			System.exit(0);
			return null;	
		}
		rs.close();
		
			DBUtils.conn.setAutoCommit(false);
			s.execute("select s.id,s.slug,s.region,s.LAST_AH_MOD from SERVER s" +
					" where s.AH_UPDATING=0 AND s.LAST_AH_MOD < " +(System.currentTimeMillis()/1000 - 3600)+ " AND s.region='us' AND ID > "+lastID+" LIMIT 1 FOR UPDATE;");
			Statement s2 = DBUtils.conn.createStatement();
			rs = s.getResultSet();
			if(rs.next())
			{
				Server t = new Server();
				t.setId(rs.getInt(1));
				t.setSlug(rs.getString(2));
				t.setRegion(rs.getString(3));
				t.setLastUpdated(rs.getLong(4));
//				System.out.println("UPDATE SERVER SET AH_UPDATING=1 WHERE id="+t.getId()+" and AH_UPDATING=0;");
				if(s2.executeUpdate("UPDATE SERVER SET AH_UPDATING=1 WHERE id="+t.getId()+" and AH_UPDATING=0;") == 1)
				{
					DBUtils.conn.commit();
					DBUtils.conn.setAutoCommit(true);
					s2.close();
					rs.close();
					s.close();
					return t;
				}
				else
				{
					System.out.println("No rows updated");
					DBUtils.conn.commit();
					DBUtils.conn.setAutoCommit(true);
					s2.close();
					rs.close();
					s.close();
					return getServerToScrape();
				}
			}			
			return null;
		}
		catch(Exception x)
		{
			Utils.reportError(x);
			return null;
		}
	}
	public static void main(String[] args) {
		HttpClient httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());

		int limit = -1;
		boolean signRequests = false;
		if(args.length == 1)
			limit = Integer.parseInt(args[0]);
		else if(args.length == 2)
		{
			limit = Integer.parseInt(args[0]);
			signRequests = Boolean.parseBoolean(args[1]);
		}
		new AuctionScraper(limit, signRequests).go();
		try {
			DBUtils.conn.close();
		} catch (SQLException e) {
			Utils.reportError(e);
		}
	}
}
