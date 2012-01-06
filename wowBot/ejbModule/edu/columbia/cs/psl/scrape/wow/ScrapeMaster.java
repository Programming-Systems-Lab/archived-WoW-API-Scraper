package edu.columbia.cs.psl.scrape.wow;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import edu.columbia.cs.psl.scrape.wow.entity.Guild;
import edu.columbia.cs.psl.scrape.wow.entity.Profession;
import edu.columbia.cs.psl.scrape.wow.entity.ReputationStanding;
import edu.columbia.cs.psl.scrape.wow.entity.Slave;
import edu.columbia.cs.psl.scrape.wow.entity.Toon;
import edu.columbia.cs.psl.scrape.wow.entity.Title;
import edu.columbia.cs.psl.scrape.wow.entity.Achievement;
import edu.columbia.cs.psl.scrape.wow.entity.AchievementProgress;
import edu.columbia.cs.psl.scrape.wow.entity.Spell;
import edu.columbia.cs.psl.scrape.wow.entity.Raid;
import edu.columbia.cs.psl.scrape.wow.entity.RaidProgress;
import edu.columbia.cs.psl.scrape.wow.entity.RaidBossProgress;
import edu.columbia.cs.psl.scrape.wow.entity.RaidBoss;
import edu.columbia.cs.psl.scrape.wow.entity.Quest;
import edu.columbia.cs.psl.scrape.wow.entity.ReputationSink;
@Stateless
@WebService
@LocalBean
public class ScrapeMaster {
	@PersistenceContext
	EntityManager em;
	
   @Resource
   WebServiceContext wsContex;
   protected long getRemoteIP()
   {
           if(wsContex == null)
                   return 0;

           try
           {
           MessageContext mc = wsContex.getMessageContext();
           HttpServletRequest req = (HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST);
           String[] addrArray = req.getRemoteAddr().split("\\.");

           long num = 0;
           for (int i=0;i<addrArray.length;i++) {
                   int power = 3-i;

                   num += ((Integer.parseInt(addrArray[i])%256 * Math.pow(256,power)));
           }
           return num;
           }
           catch(Exception ex)
           {
                   return 0;
           }
   }
	public int register(String password, String serverName)
	{
		em.getEntityManagerFactory().getCache().evictAll();
		if(!password.equals(Constants.PASSWORD))
			return 3000;
		Slave slave = em.find(Slave.class,getRemoteIP());
		if(slave == null)
		{
			slave = new Slave();
			slave.setIp(getRemoteIP());
			slave.setRequestsToday(0);
			slave.setRequestsTodayDate(new Date());
			em.persist(slave);
		}
		slave.setName(serverName);
		slave.setLastUpdated(new Date());
		em.merge(slave);
		return slave.getRequestsToday();
	}
	
	@Resource
	EJBContext ejbCtx;
	public Guild getGuildToScrape(String password)
	{
		try
		{
			int id  = (new Random()).nextInt(126317);
		Query q = em.createNativeQuery("select * from GUILD where status=0 LIMIT 1;", Guild.class);
		List<Object> r = q.getResultList();
		Guild g = ((Guild) r.get(0));
		g.getServer();
		q = em.createNativeQuery("UPDATE GUILD SET status=1 WHERE status=0 and id="+g.getId()+";");
		if(q.executeUpdate() == 0)
			return getGuildToScrape(password);
		return g;
		}
		catch(NoResultException x)
		{
			return null;
		}
	}
	
	public Toon getToonToScrape(String password)
	{
		try
		{
			int id  = (new Random()).nextInt(126317);
		Query q = em.createNativeQuery("select * from TOON where explored=0 LIMIT 1;", Toon.class);
		List<Object> r = q.getResultList();
		Toon t = ((Toon) r.get(0));
		t.getServer();
		q = em.createNativeQuery("UPDATE TOON SET explored=1 WHERE id="+t.getId()+" and explored=0;");
		if(q.executeUpdate() == 0)
			return getToonToScrape(password);
		return t;
		}
		catch(NoResultException x)
		{
			return null;
		}
	}
	
	public void updateGuildStatus(int guildId,int status)
	{
		Guild g = em.find(Guild.class, guildId);
		g.setStatus(status);
		em.merge(g);
	}
	public long getMyIP()
	{
		return getRemoteIP();
	}
	public void updateToonStatus(int toonId,int status)
	{
		em.createNativeQuery("INSERT INTO SLAVE_HISTORY (IP,time,toon_id,status) VALUES ("+getRemoteIP()+",NOW(),"+toonId+","+status+");").executeUpdate();
	}
	public void updateSlaveCount(int newCount)
	{
		Slave slave = em.find(Slave.class,getRemoteIP());
		slave.setRequestsToday(newCount);
		slave.setLastUpdated(new Date());
		em.merge(slave);
	}
	public String getToonName(int id)
	{
		Toon t = em.find(Toon.class, id);
		return t.getName();
	}
	private Profession findProfession(Profession p)
	{
		if(p== null)
			return null;
		Profession ret = em.find(Profession.class, p.getId());
		if(ret == null)
		{
			em.persist(p);
			ret = em.find(Profession.class, p.getId());
		}
		return ret;
	}
	private Title findTitle(Title p)
	{
		if(p== null)
			return null;
		Title ret = em.find(Title.class, p.getId());
		if(ret == null)
		{
			em.persist(p);
			ret = em.find(Title.class, p.getId());
		}
		return ret;
	}
	private Achievement findAchievement(Achievement p)
	{
		if(p== null)
			return null;
		Achievement ret = em.find(Achievement.class, p.getId());
		if(ret == null)
		{
			em.persist(p);
			ret = em.find(Achievement.class, p.getId());
		}
		return ret;
	}
	private Spell findSpell(Spell p)
	{
		if(p== null)
			return null;
		Spell ret = em.find(Spell.class, p.getId());
		if(ret == null)
		{
			em.persist(p);
			ret = em.find(Spell.class, p.getId());
		}
		return ret;
	}
	private Raid findRaid(Raid p)
	{
		if(p== null)
			return null;
		Raid ret = em.find(Raid.class, p.getId());
		if(ret == null)
		{
			em.persist(p);
			ret = em.find(Raid.class, p.getId());
		}
		return ret;
	}
	private RaidBoss findRaidBoss(RaidBoss p)
	{
		if(p== null)
			return null;
		RaidBoss ret = em.find(RaidBoss.class, p.getId());
		if(ret == null)
		{
			p.setRaid(findRaid(p.getRaid()));
			em.persist(p);
			ret = em.find(RaidBoss.class, p.getId());
		}
		return ret;
	}
	private Quest findQuest(Quest p)
	{
		if(p== null)
			return null;
		Quest ret = em.find(Quest.class, p.getId());
		if(ret == null)
		{
			em.persist(p);
			ret = em.find(Quest.class, p.getId());
		}
		return ret;
	}
	private ReputationSink findReputationSink(ReputationSink p)
	{
		if(p== null)
			return null;
		ReputationSink ret = em.find(ReputationSink.class, p.getId());
		if(ret == null)
		{
			em.persist(p);
			ret = em.find(ReputationSink.class, p.getId());
		}
		return ret;
	}
	private String padInt(int n)
	{
		if(n < 10)
			return "0"+n;
		else
			return ""+n;
	}
	public void processToon(Toon toon)
	{
		//Merge professions
		toon.setProf1(findProfession(toon.getProf1()));
		toon.setProf2(findProfession(toon.getProf2()));
		
		//Merge titles
		if(toon.getTitles() != null && toon.getTitles().size() > 0)
		{
			String query = "INSERT INTO TOON_TITLE (Toon_ID,titles_ID) VALUES ";
			for(int i=0;i<toon.getTitles().size();i++)
			{
				query += "("+toon.getId()+","+toon.getTitles().get(i).getId()+"), ";
			}
			query = query.substring(0,query.length()-2)+";";
			Query q=em.createNativeQuery(query);
			q.executeUpdate();
		}
//		toon.setSelectedTitle(findTitle(toon.getSelectedTitle()));

		//Merge achievements & criteria
		if(toon.getProgress().size() > 0)
		{
			String query = "INSERT INTO ACHIEVEMENTPROGRESS (COMPLETED,UPDATED,PROGRESS,ACHIEVEMENT_ID,TOON_ID) VALUES ";
			for(int i=0;i<toon.getProgress().size();i++)
			{
	//			toon.getProgress().get(i).setAchievement(findAchievement(toon.getProgress().get(i).getAchievement()));
	//			toon.getProgress().get(i).setToon(toon);
				Calendar updated = new GregorianCalendar();
				updated.setTime(toon.getProgress().get(i).getUpdated());
				query += "("+(toon.getProgress().get(i).isCompleted() ? 1 : 0) +", '"+ 
				updated.get(Calendar.YEAR)+"-"+
						padInt(updated.get(Calendar.MONTH))+"-"+
						padInt(updated.get(Calendar.DAY_OF_MONTH))+" "+
						padInt(updated.get(Calendar.HOUR_OF_DAY))+":"+
						padInt(updated.get(Calendar.MINUTE))+":"+
						padInt(updated.get(Calendar.SECOND))+"',"+
				toon.getProgress().get(i).getProgress()+","+toon.getProgress().get(i).getAchievement().getId()+","+toon.getId()+"), ";
			}
			query = query.substring(0,query.length()-2)+";";
			Query q=em.createNativeQuery(query);
			q.executeUpdate();
		}
		//Merge mounts
		if(toon.getMounts() != null && toon.getMounts().size() > 0)
		{
			String query = "INSERT INTO TOON_SPELL (Toon_ID,mounts_ID) VALUES ";
			for(int i=0;i<toon.getMounts().size();i++)
			{
//				toon.getMounts().set(i, findSpell(toon.getMounts().get(i)));
				query+="("+toon.getId()+","+toon.getMounts().get(i).getId()+"), ";
			}
			query = query.substring(0,query.length()-2)+";";
			Query q=em.createNativeQuery(query);
			q.executeUpdate();
		}
		//merge companions
		if(toon.getCompanions() != null && toon.getCompanions().size() > 0)
		{
			String query = "INSERT INTO TOON_SPELL (Toon_ID,companions_ID) VALUES ";
			for(int i=0;i<toon.getCompanions().size();i++)
			{
//				toon.getCompanions().set(i, findSpell(toon.getCompanions().get(i)));
				query+="("+toon.getId()+","+toon.getCompanions().get(i).getId()+"), ";
			}
			query = query.substring(0,query.length()-2)+";";
			Query q=em.createNativeQuery(query);
			q.executeUpdate();
		}

		//merge raid progressions
		if(toon.getRaidProgress().size() > 0)
		{
			String query = "INSERT INTO RAIDPROGRESS (HEROIC,NORMAL,RAID_ID,TOON_ID) VALUES ";
			for(RaidProgress p : toon.getRaidProgress())
			{
//				p.setRaid(findRaid(p.getRaid()));
//				p.setToon(toon);
				query += "("+p.getHeroic()+","+p.getNormal()+","+p.getRaid().getId()+","+toon.getId()+"), ";
			}
			query = query.substring(0,query.length()-2)+";";
			Query q=em.createNativeQuery(query);
			q.executeUpdate();
		}
		if(toon.getRaidBossProgress().size() > 0)
		{
			String query = "INSERT INTO RAIDBOSSPROGRESS (HEROIC,NORMAL,RAIDBOSS_ID,TOON_ID) VALUES ";
			for(RaidBossProgress p : toon.getRaidBossProgress())
			{
//				p.setRaidBoss(findRaidBoss(p.getRaidBoss()));
//				p.setToon(toon);
				query += "("+p.getHeroic()+","+p.getNormal()+","+p.getRaidBoss().getId()+","+toon.getId()+"), ";
			}
			query = query.substring(0,query.length()-2)+";";
			Query q=em.createNativeQuery(query);
			q.executeUpdate();
		}

		//merge quests
		if(toon.getQuestsDone().size() > 0)
		{
			String query = "INSERT INTO TOON_QUEST (Toon_ID,questsDone_ID) VALUES ";
			for(int i =0;i<toon.getQuestsDone().size();i++)
			{
//				toon.getQuestsDone().set(i, findQuest(toon.getQuestsDone().get(i)));
				query += "("+toon.getId()+","+toon.getQuestsDone().get(i).getId()+"), ";
			}
			query = query.substring(0,query.length()-2)+";";
			Query q=em.createNativeQuery(query);
			q.executeUpdate();
		}
		//merge reputation stuff
		if(toon.getReputation().size() > 0)
		{
			String query = "INSERT INTO REPUTATIONSTANDING (MAX,STANDING,VALUE,TOON_ID,SINK_ID) VALUES ";
			for(ReputationStanding rs : toon.getReputation())
			{
//				rs.setSink(findReputationSink(rs.getSink()));
//				rs.setToon(toon);
				query += "("+rs.getMax()+","+rs.getStanding()+","+rs.getValue()+","+toon.getId()+","+rs.getSink().getId()+"), ";
			}
			query = query.substring(0,query.length()-2)+";";
			Query q=em.createNativeQuery(query);
			q.executeUpdate();
		}
		
		Query q = em.createNativeQuery("UPDATE TOON SET EXPLORED=2, ARCHAELOGOYLVEL="+toon.getArchaelogyLevel()+"," +
				" ACHIEVEMENTPOINTS=" +toon.getAchievementPoints()+", "+
				"COOKINGLEVEL="+toon.getCookingLevel()+", " +
				"FIRSTAIDLEVEL="+toon.getFirstAidLevel()+"," +
				"FISHINGLEVEL="+toon.getFishingLevel()+", " +
				"PROF1_ID="+(toon.getProf1() == null ? "null" : toon.getProf1().getId())+", " +
				"PROF2_ID="+(toon.getProf2() == null ? "null" : toon.getProf2().getId())+", " +
				"PROF1LEVEL="+toon.getProf1Level()+", " +
				"PROF2LEVEL="+toon.getProf2Level()+", " +
				"SELECTEDTITLE_ID="+(toon.getSelectedTitle() == null ? "null" : toon.getSelectedTitle().getId())+" " +
				"WHERE ID="+toon.getId()+";");
//		System.out.println("6");
//		em.merge(toon);
//		System.out.println("7");
	}
	public void processGuild(List<Toon> toons ,int guildId)
	{
		Guild g = em.find(Guild.class, guildId);
		for(Toon t : toons)
		{
			t.setGuild(g);
			t.setServer(g.getServer());
			t.setSource(g.getSource());
			em.persist(t);
		}
	}
}
