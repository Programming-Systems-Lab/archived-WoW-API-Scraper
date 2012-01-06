package edu.columbia.cs.psl.scrape.wow;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.columbia.cs.psl.scrape.wow.entity.Achievement;

/**
 * Session Bean implementation class StaticDataLoader
 */
@Stateless
@LocalBean
public class StaticDataLoader //implements StaticDataLoaderRemote 
{

	@PersistenceContext
	EntityManager em;
    public StaticDataLoader() {
        // TODO Auto-generated constructor stub
    }

//	@Override
	public void loadAchievement(Achievement a) {
		em.persist(a);
	}
    
}
