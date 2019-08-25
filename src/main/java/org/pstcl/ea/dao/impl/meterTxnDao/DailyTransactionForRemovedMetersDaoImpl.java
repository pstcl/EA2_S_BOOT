package org.pstcl.ea.dao.impl.meterTxnDao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.pstcl.ea.dao.IDailyTransactionRemovedDao;
import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransactionForRemovedMeters;
import org.pstcl.ea.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;





@Repository("dailyTransactionForRemovedMeterDao")
//@org.springframework.transaction.annotation.Transactional(value="sldcTxnManager")
public class DailyTransactionForRemovedMetersDaoImpl  implements IDailyTransactionRemovedDao {



	@Autowired 
	@Qualifier("sldcSessionFactory")
	private SessionFactory sessionFactory;


	@Transactional(value="sldcTxnManager")
	protected Session getSession(){

		return sessionFactory.getCurrentSession();
	}

	static final Logger logger = LoggerFactory.getLogger(DailyTransactionForRemovedMeters.class);

	@Override
	public DailyTransactionForRemovedMeters findById(int id) {
		DailyTransactionForRemovedMeters txn = getSession().get(DailyTransactionForRemovedMeters.class,id);
		return txn;
	}



	@Transactional(value="sldcTxnManager")

	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(DailyTransactionForRemovedMeters.class);
	}




	@Override
	public void deleteById(Integer id) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("txnId", id));
		DailyTransactionForRemovedMeters txn = (DailyTransactionForRemovedMeters)crit.uniqueResult();
		getSession().delete(txn);
	}


	

	@Override
	@Transactional(value="sldcTxnManager")
	public DailyTransactionForRemovedMeters findByLocationDateFilter(DailyTransactionForRemovedMeters dailyTransaction) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("location.locationId", dailyTransaction.getLocation().getLocationId()));
		crit.add(Restrictions.eq("transactionDate",dailyTransaction.getTransactionDate()));
		return (DailyTransactionForRemovedMeters) crit.uniqueResult();
	}






	@Override
	@Transactional(value="sldcTxnManager")
	public DailyTransactionForRemovedMeters findByLocationDateCombo(LocationMaster meter, Date txnDate) {
		Criteria crit = createEntityCriteria();
		java.sql.Date date=new java.sql.Date(txnDate.getTime());
		crit.add(Restrictions.eq("location.locationId", meter.getLocationId()));
		crit.add(Restrictions.eq("transactionDate",date));
		return (DailyTransactionForRemovedMeters) crit.uniqueResult();
	}







	@Override
	public void saveOrUpdate(DailyTransactionForRemovedMeters dailyTransaction, EAUser loggedInUser) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
			try
			{
				if (null != dailyTransaction) {
					session.save(dailyTransaction);
				}

			}
			catch(ConstraintViolationException dupExp)
			{
				transaction.rollback();
				session.close();
				saveOrUpdateInCaseDuplicateException(dailyTransaction, loggedInUser);
				return;
			}
			catch (Exception e) {
				transaction.rollback();
				session.close();
				saveOrUpdateInCaseDuplicateException(dailyTransaction, loggedInUser);
				return;
			}
		
		transaction.commit();
		session.close();
	}

	//Called only in case of exception on duplicate entries for date,location combo. Saves the records only if not found
	private void saveOrUpdateInCaseDuplicateException(DailyTransactionForRemovedMeters dailyTransaction, EAUser loggedInUser) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
			try
			{
				Criteria crit = session.createCriteria(DailyTransactionForRemovedMeters.class);
				System.out.println(dailyTransaction);
				crit.add(Restrictions.eq("meter.meterSrNo", dailyTransaction.getLocation().getLocationId()));
				crit.add(Restrictions.eq("transactionDate",dailyTransaction.getTransactionDate()));
				DailyTransactionForRemovedMeters entity =(DailyTransactionForRemovedMeters) crit.uniqueResult();
				if (null != entity) {
					entity.updateValues(dailyTransaction);
					session.update(entity);
				}
				else
				{
					session.save(dailyTransaction);
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



	@Transactional(value="sldcTxnManager")
	@Override
	public List<DailyTransactionForRemovedMeters> getDailyTransactionForRemovedMeterssByLocGreaterThanDate(LocationMaster locationMaster,Date startDate) {
		Criteria criteria = createEntityCriteria();
		if(null!=locationMaster)
		{
			criteria.add(Restrictions.eq("location.locationId", locationMaster.getLocationId()));
		}
		else
		{
			return null;
		}
		criteria.add(Restrictions.ge("transactionDate",startDate));
		List<DailyTransactionForRemovedMeters> results=criteria.list();


	
		return results;
	}

	@Transactional(value="sldcTxnManager")
	@Override
	public List<DailyTransactionForRemovedMeters> getDailyTransactionForRemovedMeterssByMeterGreaterThanDate(MeterMaster meterMaster,Date startDate) {
		Criteria criteria = createEntityCriteria();
		if(null!=meterMaster)
		{
			criteria.add(Restrictions.eq("meter.meterSrNo", meterMaster.getMeterSrNo()));
		}
		else
		{
			return null;
		}
		criteria.add(Restrictions.ge("transactionDate",startDate));
		List<DailyTransactionForRemovedMeters> results=criteria.list();


		@SuppressWarnings("unchecked")
		List<DailyTransactionForRemovedMeters> totalAll = criteria
		.setProjection(Projections.projectionList()
				.add(Projections.sum("exportWHF"), "exportWHF")
				.add(Projections.sum("importWHF"), "importWHF"))
		.setResultTransformer(Transformers.aliasToBean(DailyTransactionForRemovedMeters.class)).list();
		results.addAll(totalAll);

		return results;
	}

	@Transactional(value="sldcTxnManager")
	@Override
	public List<DailyTransactionForRemovedMeters> getDailyTransactionForRemovedMeterssByMonth(LocationMaster locationMaster,Integer month,Integer year) {


		Criteria criteria = createEntityCriteria();
		if(null!=locationMaster)
		{
			criteria.add(Restrictions.eq("location.locationId", locationMaster.getLocationId()));
		}
		else
		{
			return null;
		}
		Date startDate=DateUtil.startDateTimeForDailySurveyRecs(month, year);
		Date endDate=DateUtil.endDateTimeForDailySurveyRecs(month, year);



		criteria.add(Restrictions.ge("transactionDate",startDate));
		criteria.add(Restrictions.le("transactionDate",endDate));


		List<DailyTransactionForRemovedMeters> results=criteria.list();


		@SuppressWarnings("unchecked")
		List<DailyTransactionForRemovedMeters> totalAll = criteria
		.setProjection(Projections.projectionList()
				.add(Projections.sum("exportWHF"), "exportWHF")
				.add(Projections.sum("importWHF"), "importWHF"))
		.setResultTransformer(Transformers.aliasToBean(DailyTransactionForRemovedMeters.class)).list();
		results.addAll(totalAll);

		return results;
	}


	@Transactional(value="sldcTxnManager")
	@Override
	public List<DailyTransactionForRemovedMeters> getDailyTransactionForRemovedMeterssByMonth(MeterMaster meterMaster,Integer month,Integer year) {


		Criteria criteria = createEntityCriteria();
		if(null!=meterMaster)
		{
			criteria.add(Restrictions.eq("meter.meterSrNo", meterMaster.getMeterSrNo()));
		}
		else
		{
			return null;
		}
		Date startDate=DateUtil.startDateTimeForDailySurveyRecs(month, year);
		Date endDate=DateUtil.endDateTimeForDailySurveyRecs(month, year);



		criteria.add(Restrictions.ge("transactionDate",startDate));
		criteria.add(Restrictions.le("transactionDate",endDate));


		List<DailyTransactionForRemovedMeters> results=criteria.list();


		@SuppressWarnings("unchecked")
		List<DailyTransactionForRemovedMeters> totalAll = criteria
		.setProjection(Projections.projectionList()
				.add(Projections.sum("exportWHF"), "exportWHF")
				.add(Projections.sum("importWHF"), "importWHF"))
		.setResultTransformer(Transformers.aliasToBean(DailyTransactionForRemovedMeters.class)).list();
		results.addAll(totalAll);

		return results;
	}


}
