package org.pstcl.ea.dao.impl.meterTxnDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.pstcl.ea.dao.ILoadSurveyTransactionDao;
import org.pstcl.ea.dao.impl.transformer.LossReportEntityTransformer;
import org.pstcl.ea.entity.EAUser;
import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.LossReportEntity;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.SubstationMaster;
import org.pstcl.ea.entity.meterTxnEntity.DailyTransaction;
import org.pstcl.ea.entity.meterTxnEntity.jpa.LoadSurveyTransaction;
import org.pstcl.ea.model.ImportExportModel;
import org.pstcl.ea.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


//CLASS NOT IN USE since AUG 19 2019

//@Repository("loadSurveyTransactionDao")
//@Transactional(value="sldcTxnManager")
public class LoadSurveyTransactionDaoImpl2  implements ILoadSurveyTransactionDao {


	@Transactional(value="sldcTxnManager")
	protected Session getSession(){

		return sessionFactory.getCurrentSession();
	}

	//	@Autowired
	//	public SubstationUtiltiyDaoImpl(EntityManagerFactory factory) {
	//		if(factory.unwrap(SessionFactory.class) == null){
	//			throw new NullPointerException("factory is not a hibernate factory");
	//		}
	//		this.sessionFactory = factory.unwrap(SessionFactory.class);
	//	}

	@Autowired 
	@Qualifier("sldcSessionFactory")
	private SessionFactory sessionFactory;


	static final Logger logger = LoggerFactory.getLogger(LoadSurveyTransactionDaoImpl2.class);

	@Override
	@Transactional(value="sldcTxnManager")
	public LoadSurveyTransaction findById(int id) {


		LoadSurveyTransaction txn = getSession().get(LoadSurveyTransaction.class, id);
		return txn;
	}



	@Transactional(value="sldcTxnManager")
	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(LoadSurveyTransaction.class);
	}



	@Override
	public void deleteById(String id) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("id", id));
		LoadSurveyTransaction txn = (LoadSurveyTransaction)crit.uniqueResult();
		getSession().delete(txn);
	}

	@Override
	public void save(LoadSurveyTransaction txn,EAUser user) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		getSession().persist(txn);
		transaction.commit();
		session.close();
	}

	@Override
	public void update(LoadSurveyTransaction txn,EAUser user) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		getSession().update(txn);
		transaction.commit();
		session.close();
	}

	//	private List <String> getLocationMeters(FilterModel entity)
	//
	//	{
	//		Criteria critLocationList = getSession().createCriteria(LocationMaster.class);
	//
	//		if(null!=entity)
	//		{
	//			if(null!=entity.getSubstation())
	//			{
	//				critLocationList.add(Restrictions.eq("substationMaster.ssCode", entity.getSubstation().getSsCode()));
	//			}
	//		}
	//		critLocationList.setProjection(Projections.property("meterSrNo"));
	//		return critLocationList.list();
	//	}





	//	@Override
	//	public LoadSurveyTransaction findByMeter(MeterMaster meter, Date txnDate) {
	//
	//
	//		Criteria crit = createEntityCriteria();
	//		crit.add(Restrictions.eq("meterMaster.meterSrNo", meter.getMeterSrNo()));
	//
	//		crit.add(Restrictions.eq("transactionDate",txnDate));
	//
	//		return (LoadSurveyTransaction) crit.uniqueResult();
	//	}

	@Override
	@Transactional(value="sldcTxnManager")
	public List<LoadSurveyTransaction> findLoadSurveyByDayAndLocation(LocationMaster location,int dayOfMonth,int monthOfYear,int year) {
		Criteria crit = createEntityCriteria();
		crit.add((Restrictions.in("location.locationId", location.getLocationId())));
		crit.add(Restrictions.eq("monthOfYear",monthOfYear));
		crit.add(Restrictions.eq("dayOfMonth",dayOfMonth));
		crit.add(Restrictions.eq("year",year));
		return (List<LoadSurveyTransaction>) crit.list();
	}

	@Override
	@Transactional(value="sldcTxnManager")
	public List<LoadSurveyTransaction> findLoadSurveyByDayAndLocation(LocationMaster location,Date transactionDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(transactionDate);
		int year = cal.get(Calendar.YEAR);
		int monthOfYear = cal.get(Calendar.MONTH);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);


		Criteria crit = createEntityCriteria();
		crit.add((Restrictions.in("location.locationId", location.getLocationId())));
		crit.add(Restrictions.eq("monthOfYear",monthOfYear));
		crit.add(Restrictions.eq("dayOfMonth",dayOfMonth));
		crit.add(Restrictions.eq("year",year));
		return (List<LoadSurveyTransaction>) crit.list();
	}

	@Override
	@Transactional(value="sldcTxnManager")
	public DailyTransaction sumLoadSurveyByDayAndLocation(LocationMaster location,Date transactionDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(transactionDate);
		int year = cal.get(Calendar.YEAR);
		int monthOfYear = cal.get(Calendar.MONTH);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);


		Criteria crit = createEntityCriteria();
		crit.add((Restrictions.in("location.locationId", location.getLocationId())));
		crit.add(Restrictions.eq("monthOfYear",monthOfYear));
		crit.add(Restrictions.eq("dayOfMonth",dayOfMonth));
		crit.add(Restrictions.eq("year",year));


		@SuppressWarnings("unchecked")
		DailyTransaction sumEntity = (DailyTransaction) crit
		.setProjection(Projections.projectionList().add(Projections.sum("exportWhFundTotal"), "exportWHF")
				.add(Projections.sum("importWhFundTotal"), "importWHF")
				.add(Projections.groupProperty("location"), "location")
				)
		.setResultTransformer(Transformers.aliasToBean(DailyTransaction.class)).uniqueResult();


		return sumEntity;
	}
	@Override
	@Transactional(value="sldcTxnManager")
	public DailyTransaction sumLoadSurveyByDayAndMeter(MeterMaster meterMaster,Date startDate,Date endDate) {


		Criteria crit = createEntityCriteria();
		crit.add((Restrictions.in("meter.meterSrNo", meterMaster.getMeterSrNo())));
		crit.add(Restrictions.ge("transactionDate",startDate));
		crit.add(Restrictions.le("transactionDate",endDate));



		@SuppressWarnings("unchecked")
		DailyTransaction sumEntity = (DailyTransaction) crit
		.setProjection(Projections.projectionList().add(Projections.sum("exportWhFundTotal"), "exportWHF")
				.add(Projections.sum("importWhFundTotal"), "importWHF")
				.add(Projections.groupProperty("location"), "location")
				)
		.setResultTransformer(Transformers.aliasToBean(DailyTransaction.class)).uniqueResult();


		return sumEntity;
	}


	@Override
	@Transactional(value="sldcTxnManager")
	public List<LoadSurveyTransaction> findLoadSurveyTxnsBySubstation(SubstationMaster location,int dayOfMonth,int monthOfYear,int year) {

		Criteria critLocationList = getSession().createCriteria(LocationMaster.class);

		if(null!=location)
		{
			critLocationList.add(Restrictions.eq("substationMaster.ssCode", location.getSsCode()));
		}

		critLocationList.setProjection(Projections.property("locationId"));
		List <String> locationIdList=critLocationList.list();

		Criteria crit = createEntityCriteria();

		if(locationIdList!=null){
			if(locationIdList.size()>0)
			{
				crit.add((Restrictions.in("location.locationId", locationIdList)));
			}
		}
		crit.add(Restrictions.eq("dayOfMonth",dayOfMonth));
		crit.add(Restrictions.eq("monthOfYear",monthOfYear));
		crit.add(Restrictions.eq("year",year));

		return (List<LoadSurveyTransaction>) crit.list();
	}






	@Override
	@Transactional(value="sldcTxnManager")
	public List<LoadSurveyTransaction> findLoadSurveyTxnByLocationList(ImportExportModel filter) {


		Criteria critLocationList = getSession().createCriteria(LocationMaster.class);
		if(null!=filter)
		{
			if(null!=filter.getSubstation())
			{
				critLocationList.add(Restrictions.eq("substationMaster.ssCode", filter.getSubstation().getSsCode()));
			}
			if(null!=filter.getDivision())
			{
				critLocationList.add(Restrictions.eq("divisionMaster.divCode", filter.getDivision().getDivCode()));
			}
			if(null!=filter.getCircle())
			{
				critLocationList.add(Restrictions.eq("circleMaster.crCode", filter.getCircle().getCrCode()));
			}
		}

		critLocationList.setProjection(Projections.property("locationId"));


		List <String> locationIdList=critLocationList.list();

		Criteria crit = createEntityCriteria();

		if(locationIdList!=null){
			if(locationIdList.size()>0)
			{
				crit.add((Restrictions.in("location.locationId", locationIdList)));
			}
		}
		crit.add(Restrictions.eq("transactionDate",filter.getTransactionDate()));

		return (List<LoadSurveyTransaction>) crit.list();
	}



	@Override
	@Transactional(value="sldcTxnManager")
	public LoadSurveyTransaction findByLocationDateCombo(LocationMaster meter, Date txnDate) {

		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("location.locationId", meter.getLocationId()));




		//crit.add(Restrictions.between("transactionDate",StringUtil.getDayBeforeDate(StringUtil.getDayBeforeDate(StringUtil.getToday())),StringUtil.getToday()));

		//crit.add(Expression.le("transactionDate",StringUtil.getToday()));

		crit.add(Restrictions.eq("transactionDate",txnDate));

		return (LoadSurveyTransaction) crit.uniqueResult();
	}







	@Override
	public void save(List<LoadSurveyTransaction> loadSurveyList, EAUser loggedInUser) {

		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		for (LoadSurveyTransaction loadSurveyTransaction : loadSurveyList) {
			try

			{
				if (null != loadSurveyTransaction) {

					session.save(loadSurveyTransaction);
				}	
			}
			catch(ConstraintViolationException dupExp)
			{

				transaction.rollback();
				session.close();
				saveAfterDeletingDuplicates(loadSurveyList, loggedInUser);;
				return;
			}
			catch (Exception e) {
				transaction.rollback();
				session.close();
				saveAfterDeletingDuplicates(loadSurveyList, loggedInUser);;
				return;

			}

		}
		transaction.commit();

		session.close();
	}
	private void deleteDuplicates(List<LoadSurveyTransaction> loadSurveyList, EAUser loggedInUser)
	{
		List<Integer> txnToDelete = findTxnIdsForLoadSurveyTxns(loadSurveyList);

		Session session=sessionFactory.openSession();
		Transaction transaction=null;
		try {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaDelete<LoadSurveyTransaction> criteriaDelete = cb.createCriteriaDelete(LoadSurveyTransaction.class);
			Root<LoadSurveyTransaction> root = criteriaDelete.from(LoadSurveyTransaction.class);
			criteriaDelete.where((root.get("txnId").in(txnToDelete)));
			transaction = session.beginTransaction();
			session.createQuery(criteriaDelete).executeUpdate();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			transaction.rollback();
			session.close();
			return;
		}
		if(null!=transaction)
		{
			transaction.commit();
		}
		session.close();

	}



	@Transactional(value="sldcTxnManager")
	private List<Integer> findTxnIdsForLoadSurveyTxns(List<LoadSurveyTransaction> loadSurveyList) {
		List <Integer> txnToDelete=null;
		List<String> meterSrNo=null;
		Date smallestDate=DateUtil.nextDay(DateUtil.getToday());
		Date largestDate = DateUtil.substractYear(DateUtil.getToday());
		//delete load survey transactions with same date range and meter sr no. 
		for (LoadSurveyTransaction loadSurveyTransaction : loadSurveyList) {
			if(loadSurveyTransaction.getTransactionDate()!=null)
			{
				if(null==meterSrNo)
				{
					meterSrNo=new ArrayList<>();
					meterSrNo.add(loadSurveyTransaction.getMeter().getMeterSrNo());
				}
				else if(!meterSrNo.get(0).equalsIgnoreCase(loadSurveyTransaction.getMeter().getMeterSrNo()))
				{
					meterSrNo.add(loadSurveyTransaction.getMeter().getMeterSrNo());

				}
				if(loadSurveyTransaction.getTransactionDate().compareTo(smallestDate)<0)
				{
					smallestDate=loadSurveyTransaction.getTransactionDate();
				}
				if(loadSurveyTransaction.getTransactionDate().compareTo(largestDate)>=0)
				{
					largestDate=loadSurveyTransaction.getTransactionDate();
				}

			}

		}
		if(CollectionUtils.isNotEmpty(meterSrNo))
		{
			if(meterSrNo.size()==1)
			{
				Session session=sessionFactory.openSession();
				Criteria crit = session.createCriteria(LoadSurveyTransaction.class);
				crit.add(Restrictions.eq("meter.meterSrNo", meterSrNo.get(0)));
				crit.add(Restrictions.le("transactionDate",largestDate));
				crit.add(Restrictions.ge("transactionDate",smallestDate));
				crit.setProjection(Projections.property("txnId"));
				txnToDelete=crit.list();
			}
		}
		return txnToDelete;
	}


	private void deleteDuplicates1(List<LoadSurveyTransaction> loadSurveyList, EAUser loggedInUser)
	{
		List<LoadSurveyTransaction> toDeleteLoadSurveyList=null;
		Boolean atleastOneRecordDeleted=false;
		List<String> meterSrNo=null;
		Date smallestDate=DateUtil.nextDay(DateUtil.getToday());
		Date largestDate = DateUtil.substractYear(DateUtil.getToday());
		//delete load survey transactions with same date range and meter sr no. 
		for (LoadSurveyTransaction loadSurveyTransaction : loadSurveyList) {
			if(loadSurveyTransaction.getTransactionDate()!=null)
			{
				if(null==meterSrNo)
				{
					meterSrNo=new ArrayList<>();
					meterSrNo.add(loadSurveyTransaction.getMeter().getMeterSrNo());
				}
				else if(!meterSrNo.get(0).equalsIgnoreCase(loadSurveyTransaction.getMeter().getMeterSrNo()))
				{
					meterSrNo.add(loadSurveyTransaction.getMeter().getMeterSrNo());

				}
				if(loadSurveyTransaction.getTransactionDate().compareTo(smallestDate)<0)
				{
					smallestDate=loadSurveyTransaction.getTransactionDate();
				}
				if(loadSurveyTransaction.getTransactionDate().compareTo(largestDate)>=0)
				{
					largestDate=loadSurveyTransaction.getTransactionDate();
				}

			}

		}
		if(CollectionUtils.isNotEmpty(meterSrNo))
		{
			if(meterSrNo.size()==1)
			{
				Criteria crit = getSession().createCriteria(LoadSurveyTransaction.class);
				crit.add(Restrictions.eq("meter.meterSrNo", meterSrNo.get(0)));
				crit.add(Restrictions.le("transactionDate",largestDate));
				crit.add(Restrictions.ge("transactionDate",smallestDate));
				toDeleteLoadSurveyList= crit.list();

			}
		}

		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		try {
			for (LoadSurveyTransaction toDeleteLoadSurveyTransaction : toDeleteLoadSurveyList) {
				session.delete(toDeleteLoadSurveyTransaction);
				atleastOneRecordDeleted=true;
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			transaction.rollback();
			session.close();
			return;
		}
		if(atleastOneRecordDeleted)
		{
			transaction.commit();
		}
		//	session.flush();
		session.close();

	}


	private void saveAfterDeletingDuplicates(List<LoadSurveyTransaction> loadSurveyList, EAUser loggedInUser) {

		//Delete transactions with same date and meter sr no
		try
		{
			deleteDuplicates(loadSurveyList, loggedInUser);
		}		catch (Exception e) {
			e.printStackTrace();
		}

		//
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		for (LoadSurveyTransaction loadSurveyTransaction : loadSurveyList) {
			try
			{
				if (null != loadSurveyTransaction) {

					session.save(loadSurveyTransaction);
				}	
			}
			catch(ConstraintViolationException dupExp)
			{

				transaction.rollback();
				session.close();
				saveOrUpdateInException(loadSurveyList, loggedInUser);;
				return;
			}
			catch (Exception e) {
				transaction.rollback();
				session.close();
				saveOrUpdateInException(loadSurveyList, loggedInUser);;
				return;

			}

		}
		transaction.commit();
		session.close();
	}


	private void saveOrUpdateInException(List<LoadSurveyTransaction> loadSurveyList, EAUser loggedInUser) {
		Session session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		//save all again

		for (LoadSurveyTransaction loadSurveyTransaction : loadSurveyList) {
			try

			{
				Criteria crit = session.createCriteria(LoadSurveyTransaction.class);
				crit.add(Restrictions.eq("meter.meterSrNo", loadSurveyTransaction.getMeter().getMeterSrNo()));
				crit.add(Restrictions.eq("transactionDate",loadSurveyTransaction.getTransactionDate()));
				LoadSurveyTransaction entity =(LoadSurveyTransaction) crit.uniqueResult();

				if (null != entity) {
					entity.updateValues(loadSurveyTransaction);
					session.update(entity);
				}
				else
				{
					session.save(loadSurveyTransaction);
				}

			}
			catch(ConstraintViolationException dupExp)
			{

				System.out.println(loadSurveyTransaction);
				System.out.println(dupExp.getClass());
				transaction.rollback();
				session.close();
				return;
			}
			catch (Exception e) {
				System.out.println(loadSurveyTransaction);
				e.printStackTrace();
				transaction.rollback();
				session.close();

			}

		}
		transaction.commit();
		session.close();
	}
}
