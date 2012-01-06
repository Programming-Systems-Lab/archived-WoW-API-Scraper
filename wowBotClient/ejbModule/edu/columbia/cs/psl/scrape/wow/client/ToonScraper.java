package edu.columbia.cs.psl.scrape.wow.client;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
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
import edu.columbia.cs.psl.scrape.wow.Server;
import edu.columbia.cs.psl.scrape.wow.Spell;
import edu.columbia.cs.psl.scrape.wow.Title;
import edu.columbia.cs.psl.scrape.wow.Toon;
import edu.columbia.cs.psl.scrape.wow.Utils;

public class ToonScraper {
	private int requestsToday = 0;
	private Calendar requestsTomorrowsDate;
	private boolean signRequests =false;
	private int groupNumber = 0;
	private int limit = 3000;
	private long IP;
	private boolean daemon;
	SimpleDateFormat fmt;
	public ToonScraper(int limit, boolean useKey, boolean daemon) {
		fmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		
		this.limit = limit;
		this.signRequests = useKey;
		this.daemon = daemon;
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
	private Queue<Toon> toonsToScrape;
	public void go()
	{
		ScrapeMaster master = new ScrapeMasterService().getScrapeMasterPort();
		DBUtils.initDB();
		String hostname = getHostName();
		requestsToday = master.register(Constants.PASSWORD,hostname);
		IP = master.getMyIP();
		
		try{
		PreparedStatement s = DBUtils.conn.prepareStatement("SELECT minorGroup FROM SLAVE where NAME=?");
		s.setString(1, getHostName());
		ResultSet rs = s.executeQuery();
		rs.next();
		groupNumber=rs.getInt(1);
		rs.close();
		s.close();
		}
		catch(Exception ex)
		{
			Utils.reportError(ex);
		}
		Timer t = new Timer(true);
		
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				updateSlaveCount(requestsToday);
			}
		}, 5000, 5000);

		while(limit < 0 || requestsToday < limit)
		{
			doGuildScrape();
		}
	}
	public String getSignature(String request, String date)
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
		catch(SignatureException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	public static String calculateRFC2104HMAC(String data, String key)
			throws java.security.SignatureException
			{
			String result;
			try {
				
			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("utf-8"), HMAC_SHA1_ALGORITHM);

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());

			// base64-encode the hmac
			result = Base64.encodeBase64String(rawHmac);
			result = result.substring(0,result.length()-2);
			} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
			}
			return result;
			}
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public void doGuildScrape()
	{
		Toon toon = null;
		try{
		toon = getToonToScrape(Constants.PASSWORD);
		}
		catch(Exception ex)
		{
			Utils.reportError(ex);
		}
		if(toon == null)
		{
			System.out.println("Got a null toon!");
			System.exit(0);
		}
//		System.out.println("Starting on toon " + toon.getId());
		try
		{
			HttpClient httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager());
			HttpGet httpget = new HttpGet("http://"+toon.getServer().getRegion()+
					".battle.net/api/wow/character/"+toon.getServer().getSlug()+
					"/"+URLEncoder.encode(toon.getName(),"utf-8").replace("+", "%20")+
					"?fields=achievements,quests,professions,progression,companions,mounts,titles,reputation");
//			httpget.addHeader("User-Agent","Jonathan Bell (jbell@cs.columbia.edu)");
//			httpget.addHeader("Last-Modified","Wed, 15 Nov 1995 04:58:08 GMT");
			
			if(signRequests)
			{
				String signature = getSignature("/api/wow/character/"+toon.getServer().getSlug()+
					"/"+URLEncoder.encode(toon.getName(),"utf-8").replace("+", "%20")+
					"?fields=achievements,quests,professions,progression,companions,mounts,titles,reputation","");
			}
			HttpResponse resp = httpclient.execute(httpget);
			String data = EntityUtils.toString(resp.getEntity());
			JSONObject ob = JSONObject.fromObject(data);
			if(ob.containsKey("status"))
			{
				if(ob.getString("reason").equals("Character not found."))
					updateToonStatus(toon.getId(), -1);
				else
				{
					System.out.println(ob);
					Statement s = DBUtils.conn.createStatement();
					updateToonStatus(toon.getId(), 0);
					for(Toon t : toonsToScrape)
						s.executeUpdate("UPDATE TOON_JOB SET is_Free=0 WHERE id="+t.getId()+";");
					s.close();
					if(daemon)
					{
						System.out.println("Going to sleep for 12 hours");
						Thread.sleep(12*60*60*1000);//12 hours
						s = DBUtils.conn.createStatement();
						s.executeUpdate("UPDATE SLAVE set LASTSTART=NOW(), REQUESTSTODAY=0 WHERE NAME='"+getHostName()+"';");
						s.close();
					}
					else
					{
							System.out.println("Stopping, no more regions to go");
							s = DBUtils.conn.createStatement();
							s.executeUpdate("UPDATE SLAVE set LASTSTOP=NOW(), KILL_REQ=0 WHERE NAME='"+getHostName()+"';");
							s.close();
							System.exit(0);
						
						return;
					}
				}
//				System.out.println(toon.getName() +" " +ob);
			}
			else
			{
				int achievementPoints = ob.getInt("achievementPoints");
				toon.setAchievementPoints(achievementPoints);
				
				//Professions
				try
				{
					JSONArray primaryProfs = ob.getJSONObject("professions").getJSONArray("primary");
					Profession p1 = new Profession();
					p1.setId(primaryProfs.getJSONObject(0).getInt("id"));
					p1.setName(primaryProfs.getJSONObject(0).getString("name"));
					toon.setProf1(p1);
					toon.setProf1Level(primaryProfs.getJSONObject(0).getInt("rank"));
					Profession p2 = new Profession();
					p2.setId(primaryProfs.getJSONObject(1).getInt("id"));
					p2.setName(primaryProfs.getJSONObject(1).getString("name"));
					toon.setProf2(p2);
					toon.setProf2Level(primaryProfs.getJSONObject(1).getInt("rank"));
				}
				catch(Exception ex)
				{
					
				}
				try
				{
					JSONArray moreProfs = ob.getJSONObject("professions").getJSONArray("secondary");
					toon.setCookingLevel(0);
					toon.setFishingLevel(0);
					toon.setFirstAidLevel(0);
					toon.setArchaelogyLevel(0);
					for(int i=0;i<moreProfs.size();i++)
					{
						JSONObject pr = moreProfs.getJSONObject(i);
						if(pr.getInt("id") == 129) //First Aid
							toon.setFirstAidLevel(pr.getInt("rank"));
						else if(pr.getInt("id") == 794) //Arcahelogy
							toon.setArchaelogyLevel(pr.getInt("rank"));
						else if(pr.getInt("id") == 356) //Fishing
							toon.setFishingLevel(pr.getInt("rank"));
						else if(pr.getInt("id") == 185) //Cooking
							toon.setCookingLevel(pr.getInt("rank"));
					}
				}
				catch(Exception ex)
				{
					
				}
				
				//Titles
				JSONArray titles = ob.getJSONArray("titles");
				for(int i=0;i<titles.size();i++)
				{
					Title t = new Title();
					t.setId(titles.getJSONObject(i).getInt("id"));
					t.setName(titles.getJSONObject(i).getString("name"));
					toon.getTitles().add(t);
					if(titles.getJSONObject(i).containsKey("selected") && titles.getJSONObject(i).getBoolean("selected"))
						toon.setSelectedTitle(t);
				}
				HashSet<Integer> completedAchievements = new HashSet<Integer>();
				//Achievements
				JSONObject ach = ob.getJSONObject("achievements");
				for(int i=0;i<ach.getJSONArray("achievementsCompleted").size();i++)
				{
					int achId = ach.getJSONArray("achievementsCompleted").getInt(i);
					completedAchievements.add(achId);
					long achTs = ach.getJSONArray("achievementsCompletedTimestamp").getLong(i);
					AchievementProgress ap = new AchievementProgress();
					ap.setAchievement(new Achievement());
					ap.getAchievement().setId(achId);
					ap.setCompleted(true);
					GregorianCalendar time = new GregorianCalendar();
					time.setTimeInMillis(achTs);
					ap.setUpdated(DatatypeFactory.newInstance().newXMLGregorianCalendar(time));
					ap.setCreated(ap.getUpdated());
					toon.getProgress().add(ap);
				}

				//Criteria
				//Achievements
//				for(int i=0;i<ach.getJSONArray("criteria").size();i++)
//				{
//					int achId = ach.getJSONArray("criteria").getInt(i);
//					if(completedAchievements.contains(achId))
//						continue;
//					int quantity = ach.getJSONArray("criteriaQuantity").getInt(i);
//					long achTs = ach.getJSONArray("criteriaTimestamp").getLong(i);
//					AchievementProgress ap = new AchievementProgress();
//					ap.setAchievement(new Achievement());
//					ap.getAchievement().setId(achId);
//					ap.setCompleted(false);
//					ap.setProgress(quantity);
//					GregorianCalendar time = new GregorianCalendar();
//					time.setTimeInMillis(achTs);
//					ap.setUpdated(DatatypeFactory.newInstance().newXMLGregorianCalendar(time));
//					ap.setCreated(ap.getUpdated());
//					toon.getProgress().add(ap);
//				}
				
				//Mounts
				if(ob.containsKey("mounts"))
					for(int i=0;i<ob.getJSONArray("mounts").size();i++)
					{
						Spell sp = new Spell();
						sp.setId(ob.getJSONArray("mounts").getInt(i));
						sp.setMount(true);
						toon.getMounts().add(sp);
					}
				
				//Companions
				if(ob.containsKey("companions"))
					for(int i=0;i<ob.getJSONArray("companions").size();i++)
					{
						Spell sp = new Spell();
						sp.setId(ob.getJSONArray("companions").getInt(i));
						sp.setCompanion(true);
						toon.getCompanions().add(sp);
					}
				
				//Progression
				JSONArray raids = ob.getJSONObject("progression").getJSONArray("raids");
				for(int i =0;i<raids.size();i++)
				{
					JSONObject r = raids.getJSONObject(i);
					Raid rd = new Raid();
					rd.setId(r.getInt("id"));
					rd.setName(r.getString("name"));
					RaidProgress rp = new RaidProgress();
					rp.setRaid(rd);
					rp.setNormal(r.getInt("normal"));
					rp.setHeroic(r.getInt("heroic"));
					toon.getRaidProgress().add(rp);
					if(r.containsKey("bosses"))
						for(int j=0;j<r.getJSONArray("bosses").size();j++)
						{
							RaidBoss rb = new RaidBoss();
							rb.setId(r.getJSONArray("bosses").getJSONObject(j).getInt("id"));
							rb.setName(r.getJSONArray("bosses").getJSONObject(j).getString("name"));
							rb.setRaid(rd);
							RaidBossProgress rbp = new RaidBossProgress();
							rbp.setRaidBoss(rb);
							rbp.setNormal(r.getJSONArray("bosses").getJSONObject(j).getInt("normalKills"));
							rbp.setHeroic(r.getJSONArray("bosses").getJSONObject(j).getInt("heroicKills"));
							toon.getRaidBossProgress().add(rbp);
						}
				}
				//Quests
				for(int i=0;i<ob.getJSONArray("quests").size();i++)
				{
					Quest q = new Quest();
					q.setId(ob.getJSONArray("quests").getInt(i));
					toon.getQuestsDone().add(q);
				}
				//Reputation
				JSONArray rep = ob.getJSONArray("reputation");
				for(int i =0;i<rep.size();i++)
				{
					ReputationSink rs = new ReputationSink();
					rs.setId(rep.getJSONObject(i).getInt("id"));
					rs.setName(rep.getJSONObject(i).getString("name"));
					ReputationStanding st = new ReputationStanding();
					st.setSink(rs);
					st.setStanding(rep.getJSONObject(i).getInt("standing"));
					st.setValue(rep.getJSONObject(i).getInt("value"));
					st.setMax(rep.getJSONObject(i).getInt("max"));
					toon.getReputation().add(st);
				}
				toon.setExplored(2);
//				System.out.println("Sending to server");
				processToon(toon);
//				System.out.println("Done");
			}
			requestsToday++;
		}
		catch(Exception ex)
		{
			if(toon == null)
				Utils.reportError(ex, "Null toon returned");
			else
			{
				updateToonStatus(toon.getId(), -2);
				Utils.reportError(ex, "Working on char " + toon.getId());
				
			}
		}
	}

	private void updateToonStatus(int id, int i) {
		PreparedStatement s;
		try {
			s = DBUtils.conn.prepareStatement("UPDATE TOON set EXPLORED=? WHERE id=?");
			s.setInt(1, i);
			s.setInt(2, id);
			s.executeUpdate();
			s.executeUpdate("INSERT INTO SLAVE_HISTORY (IP,time,toon_id,status) VALUES ("+IP+",NOW(),"+id+","+i+");");
			s.close();

		} catch (SQLException e) {
			Utils.reportError(e);
		}
	}
	private boolean shouldDie = false;
	private void processToon(Toon toon) throws SQLException {
		// TODO Auto-generated method stub
		//Merge titles
		Statement s = DBUtils.conn.createStatement();
				if(toon.getTitles() != null && toon.getTitles().size() > 0)
				{
					String query = "INSERT INTO TOON_TITLE (Toon_ID,titles_ID) VALUES ";
					for(int i=0;i<toon.getTitles().size();i++)
					{
						query += "("+toon.getId()+","+findTitle(toon.getTitles().get(i))+"), ";
					}
					query = query.substring(0,query.length()-2)+";";
					s.executeUpdate(query);
				}
//				toon.setSelectedTitle(findTitle(toon.getSelectedTitle()));

				//Merge achievements & criteria
				if(toon.getProgress().size() > 0)
				{
					String query = "INSERT INTO ACHIEVEMENTPROGRESS (CREATED,ACHIEVEMENT_ID,TOON_ID) VALUES ";
					for(int i=0;i<toon.getProgress().size();i++)
					{
						XMLGregorianCalendar updated = toon.getProgress().get(i).getUpdated();

						query += "('"+ 
						updated.getYear()+"-"+
								padInt(updated.getMonth())+"-"+
								padInt(updated.getDay())+" "+
								padInt(updated.getHour())+":"+
								padInt(updated.getMinute())+":"+
								padInt(updated.getSecond())+"',"+
								toon.getProgress().get(i).getAchievement().getId()+","+toon.getId()+"), ";
					}
					query = query.substring(0,query.length()-2)+";";
					s.executeUpdate(query);
				}
				//Merge mounts
				if(toon.getMounts() != null && toon.getMounts().size() > 0)
				{
					String query = "INSERT INTO TOON_SPELL (Toon_ID,Spell_ID) VALUES ";
					for(int i=0;i<toon.getMounts().size();i++)
					{
//						toon.getMounts().set(i, findSpell(toon.getMounts().get(i)));
						query+="("+toon.getId()+","+findMount(toon.getMounts().get(i).getId())+"), ";
					}
					query = query.substring(0,query.length()-2)+";";
					s.executeUpdate(query);
				}
				//merge companions
				if(toon.getCompanions() != null && toon.getCompanions().size() > 0)
				{
					String query = "INSERT INTO TOON_SPELL (Toon_ID,Spell_ID) VALUES ";
					for(int i=0;i<toon.getCompanions().size();i++)
					{
//						toon.getCompanions().set(i, findSpell(toon.getCompanions().get(i)));
						query+="("+toon.getId()+","+findCompanion(toon.getCompanions().get(i).getId())+"), ";
					}
					query = query.substring(0,query.length()-2)+";";
					s.executeUpdate(query);
				}

				//merge raid progressions
				if(toon.getRaidProgress().size() > 0)
				{
					String query = "INSERT INTO RAIDPROGRESS (HEROIC,NORMAL,RAID_ID,TOON_ID) VALUES ";
					for(RaidProgress p : toon.getRaidProgress())
					{
//						p.setRaid(findRaid(p.getRaid()));
//						p.setToon(toon);
						query += "("+p.getHeroic()+","+p.getNormal()+","+p.getRaid().getId()+","+toon.getId()+"), ";
					}
					query = query.substring(0,query.length()-2)+";";
					s.executeUpdate(query);
				}
				if(toon.getRaidBossProgress().size() > 0)
				{
					String query = "INSERT INTO RAIDBOSSPROGRESS (HEROIC,NORMAL,RAIDBOSS_ID,TOON_ID) VALUES ";
					for(RaidBossProgress p : toon.getRaidBossProgress())
					{
//						p.setRaidBoss(findRaidBoss(p.getRaidBoss()));
//						p.setToon(toon);
						query += "("+p.getHeroic()+","+p.getNormal()+","+p.getRaidBoss().getId()+","+toon.getId()+"), ";
					}
					query = query.substring(0,query.length()-2)+";";
					s.executeUpdate(query);
				}

				//merge quests
				if(toon.getQuestsDone().size() > 0)
				{
					String query = "INSERT INTO TOON_QUEST (Toon_ID,questsDone_ID) VALUES ";
					for(int i =0;i<toon.getQuestsDone().size();i++)
					{
						query += "("+toon.getId()+","+findQuest(toon.getQuestsDone().get(i).getId())+"), ";
					}
					query = query.substring(0,query.length()-2)+";";
					s.executeUpdate(query);
				}
				//merge reputation stuff
				if(toon.getReputation().size() > 0)
				{
					String query = "INSERT INTO REPUTATIONSTANDING (MAX,STANDING,VALUE,TOON_ID,SINK_ID) VALUES ";
					for(ReputationStanding rs : toon.getReputation())
					{
//						rs.setSink(findReputationSink(rs.getSink()));
//						rs.setToon(toon);
						query += "("+rs.getMax()+","+rs.getStanding()+","+rs.getValue()+","+toon.getId()+","+rs.getSink().getId()+"), ";
					}
					query = query.substring(0,query.length()-2)+";";
					s.executeUpdate(query);
				}
				
				s.executeUpdate("UPDATE TOON SET EXPLORED=2, ARCHAELOGYLEVEL="+toon.getArchaelogyLevel()+"," +
						" ACHIEVEMENTPOINTS=" +toon.getAchievementPoints()+", "+
						"COOKINGLEVEL="+toon.getCookingLevel()+", " +
						"FIRSTAIDLEVEL="+toon.getFirstAidLevel()+"," +
						"FISHINGLEVEL="+toon.getFishingLevel()+", " +
						"PROF1_ID="+(toon.getProf1() == null ? "null" : toon.getProf1().getId())+", " +
						"PROF2_ID="+(toon.getProf2() == null ? "null" : toon.getProf2().getId())+", " +
						"PROF1LEVEL="+toon.getProf1Level()+", " +
						"PROF2LEVEL="+toon.getProf2Level()+", " +
						"SELECTEDTITLE_ID="+(toon.getSelectedTitle() == null ? "null" : findTitle(toon.getSelectedTitle()))+" " +
						"WHERE ID="+toon.getId()+";");
				s.executeUpdate("INSERT INTO SLAVE_HISTORY (IP,time,toon_id,status) VALUES ("+IP+",NOW(),"+toon.getId()+",2);");
				s.close();
//				System.out.println("6");
//				em.merge(toon);
//				System.out.println("7");

	}
	
	private HashMap<Integer, Integer> titles = new HashMap<Integer, Integer>();
	private int findTitle(Title title) {
		if(titles.containsKey(title.getId()))
			return titles.get(title.getId());
		Statement s = null;
		try
		{
			s= DBUtils.conn.createStatement();
			s.execute("SELECT ID FROM TITLE WHERE ID="+title.getId());
			ResultSet rs = s.getResultSet();
			if(rs.next())
			{
				int intid = rs.getInt(1);
				titles.put(title.getId(), intid);
				rs.close();
				s.close();
				return intid;
			}
			else
			{
				rs.close();
				s.close();
				PreparedStatement ps = DBUtils.conn.prepareStatement("INSERT INTO TITLE (ID,NAME) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);	
				ps.setInt(1, title.getId());
				ps.setString(2, title.getName());
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				int intid = rs.getInt(1);
				titles.put(title.getId(), intid);
				return intid;
			}
		}
		catch(Exception ex)
		{
			Utils.reportError(ex);
		}
		
		return 0;
	}
	
	private HashMap<Integer, Integer> quests = new HashMap<Integer, Integer>();
	private int findQuest(int id) {
		if(quests.containsKey(id))
			return quests.get(id);
		Statement s = null;
		try
		{
			s= DBUtils.conn.createStatement();
			s.execute("SELECT ID FROM QUEST WHERE BNET_ID="+id);
			ResultSet rs = s.getResultSet();
			if(rs.next())
			{
				int intid = rs.getInt(1);
				quests.put(id, intid);
				rs.close();
				s.close();
				return intid;
			}
			else
			{
				rs.close();
				s.close();
				PreparedStatement ps = DBUtils.conn.prepareStatement("INSERT INTO QUEST (BNET_ID) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, id);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				int intid = rs.getInt(1);
				quests.put(id, intid);
				return intid;
			}
		}
		catch(Exception ex)
		{
			Utils.reportError(ex);
		}
		
		return 0;
	}
	
	private HashMap<Integer, Integer> spells = new HashMap<Integer, Integer>();
	private int findMount(int id)
	{
		return findSpell(id, true);
	}
	private int findCompanion(int id)
	{
		return findSpell(id, false);
	}
	private int findSpell(int id,boolean mount) {
		if(spells.containsKey(id))
			return spells.get(id);
		Statement s = null;
		try
		{
			s= DBUtils.conn.createStatement();
			s.execute("SELECT ID FROM SPELL WHERE BNET_ID="+id);
			ResultSet rs = s.getResultSet();
			if(rs.next())
			{
				int intid = rs.getInt(1);
				spells.put(id, intid);
				rs.close();
				s.close();
				return intid;
			}
			else
			{
				rs.close();
				s.executeUpdate("INSERT INTO SPELL (BNET_ID, COMPANION, MOUNT) VALUES ("+id+","+(mount? 0:1)+","+(mount? 1:0)+");",Statement.RETURN_GENERATED_KEYS);
				rs = s.getGeneratedKeys();
				int intid = rs.getInt(1);
				spells.put(id, intid);
				rs.close();
				s.close();
				return intid;
			}
		}
		catch(Exception ex)
		{
			Utils.reportError(ex);
		}
		
		return 0;
	}
	Random rand = new Random();
	private Toon getToonToScrape(String password) {
		try
		{
		java.sql.Statement s = DBUtils.conn.createStatement();
		s.execute("SELECT KILL_REQ FROM SLAVE WHERE NAME='"+getHostName()+"';");
		ResultSet rs = s.getResultSet();
		rs.next();
		if(rs.getInt(1) == 1)
		{
			rs.close();
//			System.out.println("Resetting current queue:");
//			System.out.println(toonsToScrape);
			if(toonsToScrape != null)
			for(Toon t : toonsToScrape)
				s.executeUpdate("UPDATE TOON_JOB SET is_Free=0 WHERE id="+t.getId()+";");
			s.executeUpdate("UPDATE SLAVE set LASTSTOP=NOW(), KILL_REQ=0 WHERE NAME='"+getHostName()+"';");
			s.close();
			System.out.println("No toons left to scrape");
			System.exit(0);
			return null;	
		}
		rs.close();
		
		if(toonsToScrape == null || toonsToScrape.isEmpty())
		{
//			System.out.println("Filling queue");
			toonsToScrape = new LinkedBlockingQueue<Toon>();
			
			DBUtils.conn.setAutoCommit(false);
			s.execute("select t.id,t.name,s.slug,s.region from TOON_JOB t " +
					"INNER JOIN SERVER s on t.SERVER_ID=s.ID where t.is_Free=0 LIMIT 30 FOR UPDATE;");
			Statement s2 = DBUtils.conn.createStatement();
			rs = s.getResultSet();
			boolean mightBeOK = false;
			while(rs.next())
			{
				Toon t = new Toon();
				t.setId(rs.getInt(1));
				t.setName(rs.getString(2));
				t.setServer(new Server());
				t.getServer().setSlug(rs.getString(3));
				t.getServer().setRegion(rs.getString(4));
				if(s2.executeUpdate("UPDATE TOON_JOB SET is_Free=1 WHERE id="+t.getId()+" and IS_FREE=0;") == 0)
					mightBeOK = true;
				else
					toonsToScrape.add(t);
			}
			DBUtils.conn.commit();
			DBUtils.conn.setAutoCommit(true);
			s2.close();
//			System.out.println("Filled queue");
			if(toonsToScrape.isEmpty() && mightBeOK)
			{
//				System.out.println("Search miss");
				Thread.sleep(1000 + rand.nextInt(2000));
				return getToonToScrape(password);
			}
			else if(toonsToScrape.isEmpty())
			{
				rs.close();
				s.close();
				return null;
			}
//			System.out.println("Queue filled");
			rs.close();
			s.close();
		}
//		System.out.println("Popping");
		return toonsToScrape.poll();
		}
		catch(Exception x)
		{
			Utils.reportError(x);
			return null;
		}
	}
	public static void main(String[] args) {
		int limit = -1;
		boolean signRequests = false;
		boolean daemon = false;
		if(args.length == 1)
		{
			if(args[0].equals("daemon"))
				daemon = true;
			else
				limit = Integer.parseInt(args[0]);
		}
		else if(args.length == 2)
		{
			limit = Integer.parseInt(args[0]);
			signRequests = Boolean.parseBoolean(args[1]);
		}
		new ToonScraper(limit, signRequests,daemon).go();
		try {
			DBUtils.conn.close();
		} catch (SQLException e) {
			Utils.reportError(e);
		}
	}
}
