package edu.columbia.cs.psl.scrape.wow.client;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

import ca.wowapi.utils.Base64Converter;


import edu.columbia.cs.psl.scrape.wow.Constants;
import edu.columbia.cs.psl.scrape.wow.DBUtils;
import edu.columbia.cs.psl.scrape.wow.Guild;
import edu.columbia.cs.psl.scrape.wow.ScrapeMaster;
import edu.columbia.cs.psl.scrape.wow.ScrapeMasterService;
import edu.columbia.cs.psl.scrape.wow.Server;
import edu.columbia.cs.psl.scrape.wow.Toon;
import edu.columbia.cs.psl.scrape.wow.Utils;

public class GuildRescraper {
	private int requestsToday = 0;
	private Calendar requestsTomorrowsDate;
	public GuildRescraper() {

	}
	private String getHostName()
	{
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return null;
	}


	public void go()
	{
		String hostname = getHostName();
		DBUtils.initDB();
		while(true)
		{
			try
			{
				doGuildScrape();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	boolean skipSign = false;
	public void doGuildScrape() throws Exception
	{
		Statement s = DBUtils.conn.createStatement();
		s.execute("select t.id,t.name,s.slug,s.region,s.id from GUILD t " +
				"INNER JOIN SERVER s on t.SERVER_ID=s.ID where t.rescraped is null and t.status=1 limit 1;");
		ResultSet rs = s.getResultSet();
		boolean mightBeOK = false;
		String guildName="";
		String slug="";
		String region ="";
		int guildId=0;
		int serverId=0;
		if(rs.next())
		{
			guildId=rs.getInt(1);
			guildName=rs.getString(2);
			slug=rs.getString(3);
			region = rs.getString(4);
			serverId = rs.getInt(5);
		}
		else
		{
			System.out.println("No more");
			System.exit(0);
		}
		rs.close();
		s.close();
		System.out.println(guildId);
		try
		{
			SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
			fmt.setTimeZone(TimeZone.getTimeZone("GMT"));

				String date = fmt.format(new Date());
				HttpGet httpget = new HttpGet("http://"+region+
						".battle.net/api/wow/guild/"+slug+"/"+URLEncoder.encode(guildName,"utf-8").replace("+", "%20")+"?fields=members");
				String signature = getSignature("/api/wow/guild/"+URLEncoder.encode(slug,"utf-8")+"/"+URLEncoder.encode(guildName,"utf-8").replace("+", "%20"),date);
//				System.out.println((guildId +"\n"+"http://"+region+".battle.net/api/wow/guild/"+URLEncoder.encode(slug,"utf-8")+"/"+URLEncoder.encode(guildName,"utf-8").replace("+", "%20")));
				httpget.addHeader("Host",region+".battle.net");
				httpget.addHeader("Date",date);
//				httpget.addHeader("User-Agent","Jonathan Bell (jbell@cs.columbia.edu)");
				if(skipSign)
					skipSign = false;
				else
					httpget.addHeader("Authorization",signature);
				HttpResponse resp = httpclient.execute(httpget);
				String data = EntityUtils.toString(resp.getEntity());
//					System.out.println(data);
//				System.exit(0);
//				System.out.println(data);
			JSONObject ob = JSONObject.fromObject(data.replaceAll(",\"class\":", ",\"pclass\":"));
//			System.out.println(data);
			if(ob.containsKey("status"))
			{
				System.out.println(ob);
				if(ob.getString("reason").equals("Guild not found."))
				{
					Statement s2 = DBUtils.conn.createStatement();
					s2.executeUpdate("UPDATE GUILD set rescraped=-1 where id="+guildId);
					s2.close();
					return;
				}
				else if(ob.getString("reason").equals("Realm not found."))
				{
					Statement s2 = DBUtils.conn.createStatement();
					s2.executeUpdate("UPDATE GUILD set rescraped=-1 where id="+guildId);
					s2.close();
					return;
				}
				else if(ob.getString("reason").equals("Invalid application signature."))
				{
					skipSign = true;
					Statement s2 = DBUtils.conn.createStatement();
					s2.executeUpdate("UPDATE GUILD set rescraped=null where id="+guildId);
					s2.close();
					return;
				}
				else if(ob.getString("reason").equals("Internal server error."))
				{
					Statement s2 = DBUtils.conn.createStatement();
					s2.executeUpdate("UPDATE GUILD set rescraped=-1 where id="+guildId);
					s2.close();
					return;
				}
				Statement s2 = DBUtils.conn.createStatement();
				s2.executeUpdate("UPDATE GUILD set rescraped=null where id="+guildId);
				s2.close();
				System.exit(-1);
			}
			else
			{
				JSONArray ar = ob.getJSONArray("members");
				ArrayList<Toon> toons = new ArrayList<Toon>();
				String query = "INSERT INTO toon_gender_class (toon_name,server_id,class_gender) VALUES ";

				for(int i=0;i<ar.size();i++)
				{
					JSONObject toond= ar.getJSONObject(i).getJSONObject("character");
					int pclass = 0;
					if(toond.containsKey("pclass"))
						pclass = toond.getInt("pclass");
					int gender = toond.getInt("gender");
					PreparedStatement ps = DBUtils.conn.prepareStatement("INSERT INTO toon_gender_class (toon_name,server_id,class,gender) VALUES (?,?,?,?);");
					ps.setString(1, toond.getString("name"));
					ps.setInt(2, serverId);
					ps.setInt(3, pclass);
					ps.setInt(4, gender);
					ps.executeUpdate();
					ps.close();
				}
				Statement s2 = DBUtils.conn.createStatement();
				s2.executeUpdate("UPDATE GUILD set rescraped=1 where id="+guildId);
				s2.close();
			}
			requestsToday++;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Statement s2 = DBUtils.conn.createStatement();
			s2.executeUpdate("UPDATE GUILD set rescraped=-2 where id="+guildId);
			s2.close();
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
	public static void main(String[] args) {
		
//		System.setProperty("socksProxyHost", "127.0.0.1");
//		System.setProperty("socksProxyPort", "9000");
		
		new GuildRescraper().go();
		
		
	}
}
