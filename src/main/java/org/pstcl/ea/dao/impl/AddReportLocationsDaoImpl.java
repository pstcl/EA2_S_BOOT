package org.pstcl.ea.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.pstcl.ea.dao.IAddReportLocationsDao;
import org.pstcl.ea.model.entity.EAUser;
import org.pstcl.ea.model.entity.LocationMaster;
import org.pstcl.ea.model.mapping.ReportLocationsMonthMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("addReportLocationsDao")
public class AddReportLocationsDaoImpl implements IAddReportLocationsDao {

	static final Logger logger = LoggerFactory.getLogger(AddReportLocationsDaoImpl.class);

	@Transactional(value = "sldcTxnManager")
	protected Session getSession() {

		return sessionFactory.getCurrentSession();
	}

	@Autowired
	@Qualifier("sldcSessionFactory")
	private SessionFactory sessionFactory;

	@Transactional(value = "sldcTxnManager")
	protected Criteria createEntityCriteria() {
		return getSession().createCriteria(ReportLocationsMonthMap.class);
	}

	@Override
	@Transactional(value = "sldcTxnManager")
	public ReportLocationsMonthMap findById(int id) {

		ReportLocationsMonthMap txn = getSession().get(ReportLocationsMonthMap.class, id);
		return txn;
	}

	@Override
	@Transactional(value = "sldcTxnManager")
	public void deleteById(int id) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("id", id));
		ReportLocationsMonthMap txn = (ReportLocationsMonthMap) crit.uniqueResult();
		getSession().delete(txn);
	}

	@Override
	@Transactional(value = "sldcTxnManager")
	public void delete(int month, int year, List<LocationMaster> locationMaster) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("month", month));
		crit.add(Restrictions.eq("year", year));
		crit.add(Restrictions.in("locationMaster1", locationMaster));
		List<ReportLocationsMonthMap> txns = (List<ReportLocationsMonthMap>) crit.list();
		System.out.println(txns.size());
		for (ReportLocationsMonthMap txn : txns)
			getSession().delete(txn);
	}

	@Override
	@Transactional(value = "sldcTxnManager")
	public void save(ReportLocationsMonthMap txn, EAUser user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.persist(txn);
		transaction.commit();
		session.close();
	}

	@Override
	@Transactional(value = "sldcTxnManager")
	public void update(ReportLocationsMonthMap txn, EAUser user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(txn);
		transaction.commit();
		session.close();
	}

	@Override
	@Transactional(value = "sldcTxnManager")
	public List<LocationMaster> findByMonthAndYear(int month, int year) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("month", month));
		crit.add(Restrictions.eq("year", year));
		crit.setProjection(Projections.property("locationMaster1"));
		return crit.list();
	}

}
