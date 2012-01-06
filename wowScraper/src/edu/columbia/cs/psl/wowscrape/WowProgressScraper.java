package edu.columbia.cs.psl.wowscrape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

import com.mysql.jdbc.Connection;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WowProgressScraper {
	
	public WowProgressScraper()
	{
		DBUtils.initDB();
	}
	public static void main(String[] args) {
		new WowProgressScraper().go();
	}

	   HttpClient httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());
	private void getSingleFile(String baseURL, String file)
	{
		File outFile = new File("wowProgressData/"+file);
		if(outFile.exists())
			return;
        HttpGet httpget = new HttpGet(baseURL + file);
		try {
			HttpResponse resp = httpclient.execute(httpget);
			InputStream in = resp.getEntity().getContent();
			FileOutputStream out = new FileOutputStream(outFile);
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
			out.flush();
			out.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private String fetchURL(String url)
	{
		HttpGet get = new HttpGet(url);
		try
		{
			HttpResponse resp = httpclient.execute(get);
			return EntityUtils.toString(resp.getEntity());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	private Semaphore downloadRestriction = new Semaphore(20);
	public void downloadFiles(final String url)
	{
        HttpGet httpget = new HttpGet(url);
        try {
			HttpResponse resp = httpclient.execute(httpget);
			Scanner s = new Scanner(resp.getEntity().getContent());
			Pattern p = Pattern.compile("<tr><td class=\"n\"><a href=\"([^\"]*)\">(.*)");
			System.out.println(p);
			while(s.hasNextLine())
			{
				String line = s.nextLine();
				final Matcher m = p.matcher(line);
				if(m.matches())
				{
					downloadRestriction.acquire();
					Thread t  = new Thread(new Runnable() {
						
						@Override
						public void run() {
							System.out.println(downloadRestriction.availablePermits());
							System.out.println(m.group(1));
							if(m.group(1).length() > 3)
								getSingleFile(url, m.group(1));
							downloadRestriction.release();
						}
					});
					t.start();
					
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        resp.get
 catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void processWowProgressFiles()
	{
		File f = new File("wowProgressData");
		for(File i : f.listFiles())
		{
			parseFile(i.getPath());			
		}
		int nguilds = 0;
		int servers = guilds.size();
		for(String s : guilds.keySet())
		{
			nguilds += guilds.get(s).size();
		}
		System.out.println("Servers: " + servers);
		System.out.println("Guilds: " + nguilds);
	}
	HashMap<String, HashSet<String>> guilds = new HashMap<String, HashSet<String>>();
	public void go()
	{
//		downloadFiles("http://www.wowprogress.com/export/ranks/");
//		processWowProgressFiles();
		getGuildMembers("Foundation", "lightnings-blade", "us");
//		checkServerSlugs("tw");
	}
	private void checkServerSlugs(String region)
	{
		HashMap<String, String> slugToName = new HashMap<String, String>();
		String servers = fetchURL("http://"+region+".battle.net/api/wow/realm/status");
		JSONObject o = JSONObject.fromObject(servers);
		JSONArray s = o.getJSONArray("realms");
		for(int i = 0; i< s.size(); i++)
		{
			JSONObject server = s.getJSONObject(i);
			String fullName = server.getString("name");
			String slug = server.getString("slug");
			slugToName.put(slug, fullName);
		}
		try
		{
			Statement st = DBUtils.conn.createStatement();
			st.execute("SELECT slug, id from server where region='"+region+"'");
			ResultSet rs = st.getResultSet();
			while(rs.next())
			{
				String slugName = rs.getString(1);
				int id = rs.getInt(2);
				if(!slugToName.containsKey(slugName))
				{
					slugName = slugName.replaceFirst("-", "");
					if(!slugToName.containsKey(slugName))
						System.out.println("Missing: " + slugName);
				}
//				PreparedStatement st2 = DBUtils.conn.prepareStatement("UPDATE server set name=?, slug=? where id=?");
//				st2.setString(1, slugToName.get(slugName));
//				st2.setString(2, slugName);
//				st2.setInt(3, id);
//				st2.executeUpdate();
			}
		}
		catch(Exception x)
		{
			
		}
	}
	public void parseFile(String filename)
	{
		String server = filename.substring(16);
		server=server.replace("_rs_", "_");
		server=server.replace("_tier", "");
		server=server.replace("_ach", "");
		server=server.substring(0, server.indexOf(".json"));
		server = URLDecoder.decode(server);
		while(server.charAt(server.length()-1) <= '9')
			server = server.substring(0,server.length() - 1);
		if(server.endsWith("_"))
			server = server.substring(0,server.length() - 1);
		while(server.charAt(server.length()-1) <= '9')
			server = server.substring(0,server.length() - 1);
		if(server.endsWith("_"))
			server = server.substring(0,server.length() - 1);
		while(server.charAt(server.length()-1) <= '9')
			server = server.substring(0,server.length() - 1);
		if(server.endsWith("_"))
			server = server.substring(0,server.length() - 1);
		
		if(!guilds.containsKey(server))
		{
			guilds.put(server, new HashSet<String>());
			insertServer(server);
		}
		Scanner s = null;
		try {
			File f = new File(filename);
			GZIPInputStream is = new GZIPInputStream(new FileInputStream(f));
			s = new Scanner(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(filename);
			e.printStackTrace();
		}
		String data ="";
		while(s.hasNextLine())
			data += s.nextLine();

		JSONArray json = JSONArray.fromObject(data);
		for(int i = 0; i<json.size();i++)
		{
			MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(json.getJSONObject(i));
			String guildName = (String) bean.get("name");
			if(!guilds.get(server).contains(bean.get("name")))
			{
				insertGuild(guildName, serverIDKeys.get(server));
				guilds.get(server).add((String) bean.get("name"));
			}
		}
	}
	private HashMap<String, Integer> serverIDKeys = new HashMap<String, Integer>();
	private void insertServer(String server)
	{
		try
		{
			PreparedStatement s = DBUtils.conn.prepareStatement("INSERT INTO server (name,source) VALUES (?,1)",Statement.RETURN_GENERATED_KEYS);
			System.out.println(server);
			s.setString(1, server);
			s.executeUpdate();
			ResultSet keys = s.getGeneratedKeys();
			keys.next();
			int key = keys.getInt(1);
			serverIDKeys.put(server, key);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	private void insertGuild(String guild, int server)
	{
		try
		{
			PreparedStatement s = DBUtils.conn.prepareStatement("INSERT INTO guild (name,server,source) VALUES (?,?,1)");
			s.setString(1, guild);
			s.setInt(2, server);
			s.executeUpdate();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	private int getGuildId(String guild, String realm, String region)
	{
		try
		{
			PreparedStatement p = DBUtils.conn.prepareStatement("select g.id from guild g inner join server s on s.id=g.server where s.slug=? and s.region=? and g.name=?");
			p.setString(1, realm);
			p.setString(2, region);
			p.setString(3, guild);
			p.execute();
			
			ResultSet rs = p.getResultSet();
			rs.next();
			return rs.getInt(1);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return 0;
	}
	private int getRealmId(String realm, String region)
	{
		try
		{
			PreparedStatement p = DBUtils.conn.prepareStatement("select s.id from server s where s.slug=? and s.region=?");
			p.setString(1, realm);
			p.setString(2, region);
			p.execute();
			
			ResultSet rs = p.getResultSet();
			rs.next();
			return rs.getInt(1);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return 0;
	}
	private void getGuildMembers(String guildName, String realm, String region)
	{
		HashMap<String, String> slugToName = new HashMap<String, String>();
		String info = fetchURL("http://"+region+".battle.net/api/wow/guild/"+realm+"/"+guildName+"?fields=members");
		
		JSONObject o = JSONObject.fromObject(info);
		JSONArray s = o.getJSONArray("members");
		int serverId = getRealmId(realm, region);
		int guildId = getGuildId(guildName, realm, region);
		for(int i = 0; i< s.size(); i++)
		{
			JSONObject character = s.getJSONObject(i);
			String name = character.getJSONObject("character").getString("name");
			int race = character.getJSONObject("character").getInt("race");
			int gender = character.getJSONObject("character").getInt("gender");
			int level = character.getJSONObject("character").getInt("level");
			int rank = character.getInt("rank");
			try
			{
				PreparedStatement st2 = DBUtils.conn.prepareStatement("INSERT INTO `character` (name,updated,server_id,guild_id,race_id,gender,level,guild_rank,source) " +
						" values (?,NOW(),?,?,?,?,?,?,?);");
				st2.setString(1, name);
				st2.setInt(2, serverId);
				st2.setInt(3, guildId);
				st2.setInt(4, race);
				st2.setInt(5, gender);
				st2.setInt(6, level);
				st2.setInt(7, rank);
				st2.setInt(8, 1);
				st2.executeUpdate();
			}
			catch(Exception x)
			{
//				x.printStackTrace();
			}
		}
	}
}
