package org.pstcl.ea.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.pstcl.ea.dao.ILocationEMFDao;
import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.mapping.LocationMFMap;
import org.pstcl.ea.entity.mapping.MeterLocationMap;
import org.pstcl.ea.entity.meterTxnEntity.InstantRegisters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("locationEMFRepository")
@org.springframework.transaction.annotation.Transactional(value="sldcTxnManager")
public class LocationEMFDaoImpl implements ILocationEMFDao {
	//emf

	@Transactional(value="sldcTxnManager")
	protected Session getSession(){

		return sessionFactory.getCurrentSession();
	}

	@Autowired 
	@Qualifier("sldcSessionFactory")
	private SessionFactory sessionFactory;


	static final Logger logger = LoggerFactory.getLogger(LocationEMFDaoImpl.class);


	@Transactional(value="sldcTxnManager")
	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(LocationMFMap.class);
	}

	@Override
	@Transactional(value="sldcTxnManager")
	public LocationMFMap findById(int id) {


		LocationMFMap txn = getSession().get(LocationMFMap.class, id);
		return txn;
	}


	@Override
	public void deleteById(Integer id) {
		LocationMFMap txn = getSession().get(LocationMFMap.class, id);
		getSession().delete(txn);
	}


	@Override
	public void save(LocationMFMap txn,EAUser user) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		session.persist(txn);
		transaction.commit();
		session.close();
	}

	@Override
	public void update(LocationMFMap txn,EAUser user) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		session.update(txn);
		transaction.commit();
		session.close();
	}

	@Override
	public void save(List<LocationMFMap> locationEMFs, EAUser loggedInUser) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		for (LocationMFMap locationEMF : locationEMFs) {
			try
			{
				if (null != locationEMF) {
					session.save(locationEMF);

				}

			}
			catch(ConstraintViolationException dupExp)
			{
				transaction.rollback();
				session.close();
				saveOrUpdateInCaseDuplicateException(locationEMFs, loggedInUser);
				return;
			}
			catch (Exception e) {
				transaction.rollback();
				session.close();
				saveOrUpdateInCaseDuplicateException(locationEMFs, loggedInUser);
				return;
			}
		}
		transaction.commit();
		session.close();
	}


	private void saveOrUpdateInCaseDuplicateException(List<LocationMFMap> locationEMFs, EAUser loggedInUser) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		for (LocationMFMap locationEMF : locationEMFs) {
			try
			{
				Criteria crit = session.createCriteria(InstantRegisters.class);
				crit.add(Restrictions.eq("location.locationId", locationEMF.getLocationMaster().getLocationId()));
				//crit.add(Restrictions.eq("transactionDate",instantRegister.getTransactionDate()));
				LocationMFMap entity =(LocationMFMap) crit.uniqueResult();
				if (null != entity) {
					//	entity.updateValues(locationEMF);
					session.update(entity);
				}
				else
				{
					session.save(locationEMF);
				}

			}
			catch(ConstraintViolationException dupExp)
			{
				System.out.println(dupExp.getClass());
				transaction.rollback();
				session.close();
				return;
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
		transaction.commit();
		session.close();

	}


	@Override
	@Transactional(value="sldcTxnManager")
	public List<LocationMFMap> findLocationEmfByDate(String locationId,Date startDateOftheMonth) {




		Criteria crit = createEntityCriteria();

		if(locationId!=null) {
			crit.add((Restrictions.eq("locationMaster.locationId", locationId)));
		}
		if(startDateOftheMonth!=null) {
			Criterion rest4= Restrictions.ge("endDate",startDateOftheMonth);
			Criterion rest5= Restrictions.isNull("endDate");
			crit.add(Restrictions.or( rest4,rest5));
		}
		return (List<LocationMFMap>) crit.list();

	}

	@Override
	@Transactional(value="sldcTxnManager")
	public List<LocationMFMap> findLocationEmfByLocAndDate(List<MeterLocationMap> meterLocationMapList,Date startDateOftheMonth) {




		Criteria crit = createEntityCriteria();
		List<String> locationIds=new ArrayList<>();
		for (MeterLocationMap meterLocationMap : meterLocationMapList) {
			locationIds.add(meterLocationMap.getLocationMaster().getLocationId());
		}

		if(locationIds.size()>0) {
			crit.add((Restrictions.in("locationMaster.locationId", locationIds)));
		}
		Criterion rest4= Restrictions.gt("endDate",startDateOftheMonth);
		Criterion rest5= Restrictions.isNull("endDate");
		crit.add(Restrictions.or( rest4,rest5));
		return (List<LocationMFMap>) crit.list();

	}
	
	
	
	@Override
	public LocationMFMap findLocationRecentEmf(String locationId) {
		Criteria crit = createEntityCriteria();
		crit.add((Restrictions.eq("locationMaster.locationId", locationId)));
		crit.add(Restrictions.isNull("endDate"));
		return (LocationMFMap) crit.uniqueResult();
	}


//	@Override
//	@Transactional(value="sldcTxnManager")
//	public List<LocationMFMap> find(LocationMFMap newEmf) {
//		Criteria crit = createEntityCriteria();
//		crit.add((Restrictions.eq("locationMaster.locationId", newEmf.getLocationMaster().getLocationId())));
//		crit.add((Restrictions.eq("startDate", newEmf.getStartDate())));
//		crit.add((Restrictions.eq("externalMF", newEmf.getExternalMF())));
//		crit.add((Restrictions.eq("netWHSign", newEmf.getNetWHSign())));
//		List <LocationMFMap> list = (List<LocationMFMap>) crit.list();
//		return list;
//
//	}

	@Override
	@Transactional(value="sldcTxnManager")
	public LocationMFMap findSimiliarEMF(LocationMFMap newEmf) {
		Criteria crit = createEntityCriteria();
		crit.add((Restrictions.eq("locationMaster.locationId", newEmf.getLocationMaster().getLocationId())));
		crit.add((Restrictions.eq("startDate", newEmf.getStartDate())));
		crit.add((Restrictions.eq("endDate", newEmf.getEndDate())));
		crit.add((Restrictions.eq("externalMF", newEmf.getExternalMF())));
		crit.add((Restrictions.eq("netWHSign", newEmf.getNetWHSign())));
		//List <LocationMFMap> list = (List<LocationMFMap>) crit.list();
		return (LocationMFMap) crit.uniqueResult();

	}
}


