package org.pstcl.ea.dao.impl.meterTxnDao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.pstcl.ea.dao.IInstantRegistersDao;
import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.entity.meterTxnEntity.InstantRegisters;
import org.pstcl.ea.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("instantRegistersDao")
public class InstantRegistersDaoImpl implements IInstantRegistersDao{

	@Transactional(value="sldcTxnManager")
	protected Session getSession(){

		boolean falseSession = true;
		Session s = null;
		while(falseSession) {
			try {
				s = sessionFactory.getCurrentSession();
				falseSession=false;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	@Autowired 
	@Qualifier("sldcSessionFactory")
	private SessionFactory sessionFactory;


	static final Logger logger = LoggerFactory.getLogger(InstantRegistersDaoImpl.class);



	@Transactional(value="sldcTxnManager")
	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(InstantRegisters.class);
	}

	@Override
	@Transactional(value="sldcTxnManager")
	public InstantRegisters findById(int id) {


		InstantRegisters txn = getSession().get(InstantRegisters.class, id);
		return txn;
	}

	@Override
	public void deleteById(String id) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("id", id));
		InstantRegisters txn = (InstantRegisters)crit.uniqueResult();
		getSession().delete(txn);
	}


	//	@Override
	//	public void save(InstantRegisters txn,EAUser user) {
	//		Session session=sessionFactory.openSession();
	//		Transaction transaction=session.beginTransaction();
	//		session.persist(txn);
	//		transaction.commit();
	//		session.close();
	//	}

	@Override
	public void save(InstantRegisters instantRegister, EAUser loggedInUser) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		try
		{
			if (null != instantRegister) {
				session.save(instantRegister);
			}

		}
		catch(ConstraintViolationException dupExp)
		{
			transaction.rollback();
			session.close();
			saveOrUpdateInCaseDuplicateException(instantRegister, loggedInUser);
			return;
		}
		catch (Exception e) {
			transaction.rollback();
			session.close();
			saveOrUpdateInCaseDuplicateException(instantRegister, loggedInUser);
			return;
		}

		transaction.commit();
		session.close();
	}


	private void saveOrUpdateInCaseDuplicateException(InstantRegisters instantRegister, EAUser loggedInUser) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		try
		{
			Criteria crit = session.createCriteria(InstantRegisters.class);
			crit.add(Restrictions.eq("location.locationId", instantRegister.getLocation().getLocationId()));
			crit.add(Restrictions.eq("transactionDate",instantRegister.getTransactionDate()));
			InstantRegisters entity =(InstantRegisters) crit.uniqueResult();
			if (null != entity) {
				entity.updateValues(instantRegister);
				session.update(entity);
			}
			else
			{
				session.save(instantRegister);
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

		transaction.commit();
		session.close();

	}


	@Override
	public void update(InstantRegisters txn,EAUser user) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		session.update(txn);
		transaction.commit();
		session.close();
	}

	@Override
	public void save(List<InstantRegisters> instantRegisters, EAUser loggedInUser) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		for (InstantRegisters instantRegister : instantRegisters) {
			try
			{
				if (null != instantRegister) {
					session.save(instantRegister);
				}

			}
			catch(ConstraintViolationException dupExp)
			{
				transaction.rollback();
				session.close();
				saveOrUpdateInCaseDuplicateException(instantRegisters, loggedInUser);
				return;
			}
			catch (Exception e) {
				transaction.rollback();
				session.close();
				saveOrUpdateInCaseDuplicateException(instantRegisters, loggedInUser);
				return;
			}
		}
		transaction.commit();
		session.close();
	}


	private void saveOrUpdateInCaseDuplicateException(List<InstantRegisters> instantRegisters, EAUser loggedInUser) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		for (InstantRegisters instantRegister : instantRegisters) {
			try
			{
				Criteria crit = session.createCriteria(InstantRegisters.class);
				crit.add(Restrictions.eq("location.locationId", instantRegister.getLocation().getLocationId()));
				crit.add(Restrictions.eq("transactionDate",instantRegister.getTransactionDate()));
				InstantRegisters entity =(InstantRegisters) crit.uniqueResult();
				if (null != entity) {
					entity.updateValues(instantRegister);
					session.update(entity);
				}
				else
				{
					session.save(instantRegister);
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



	@Transactional(value="sldcTxnManager")
	@Override
	public InstantRegisters findInstantRegistersByMeterAndMonth(MeterMaster meterMaster,Integer month,Integer year) {



		InstantRegisters ir=null;
		try {
			Date startDate=DateUtil.startDateTimeForDailySurveyRecs(month, year);
			Date endDate=DateUtil.endDateTimeForDailySurveyRecs(month, year);
			Criteria crit = createEntityCriteria();

			if(null!=meterMaster)
			{
				crit.add(Restrictions.eq("meter.meterSrNo", meterMaster.getMeterSrNo()));
			}
			else
			{
				return null;
			}

			crit.add(Restrictions.ge("transactionDate",startDate));
			crit.add(Restrictions.le("transactionDate",endDate));
			ir =  (InstantRegisters) crit.uniqueResult();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ir;
	}
	@Override
		@Transactional(value="sldcTxnManager")
		public InstantRegisters findInstantRegistersByDayAndLocation(String locationid, Integer month,Integer year) {
			InstantRegisters ir=null;
			try {
				Date startDate=DateUtil.startDateTimeForDailySurveyRecs(month, year);
				Date endDate=DateUtil.endDateTimeForDailySurveyRecs(month, year);
				Criteria crit = createEntityCriteria();

				if(locationid!=null) {
					crit.add((Restrictions.eq("location.locationId", locationid)));
				}

				crit.add(Restrictions.ge("transactionDate",startDate));
				crit.add(Restrictions.le("transactionDate",endDate));
				ir =  (InstantRegisters) crit.uniqueResult();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return ir;
		}

		@Override
		@Transactional(value="sldcTxnManager")
		public List<InstantRegisters> findAllByMonthAndLocation(Integer month,Integer year) {
			List<InstantRegisters> irList=null;
			try {
				Date startDate=DateUtil.startDateTimeForDailySurveyRecs(month, year);
				Date endDate=DateUtil.endDateTimeForDailySurveyRecs(month, year);
				Criteria crit = createEntityCriteria();


				crit.add(Restrictions.ge("transactionDate",startDate));
				crit.add(Restrictions.le("transactionDate",endDate));
				irList =  (List<InstantRegisters>) crit.list();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return irList;
		}
	}
