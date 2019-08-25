package org.pstcl.ea.entity.meterTxnEntity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pstcl.ea.entity.LocationMaster;
import org.pstcl.ea.entity.MeterMaster;
import org.pstcl.ea.entity.mapping.LocationMFMap;
import org.pstcl.ea.entity.mapping.MeterLocationMap;



@Entity
@Table(name = "DAILY_TRANSACTION",uniqueConstraints={@UniqueConstraint(columnNames={"LOC_ID", "transactionDate"})})
public class DailyTransaction extends DailyTransactionMappedSuper{


	public DailyTransaction()
	{
	super();	
	}

	public DailyTransaction(DailyTransactionForRemovedMeters superInstance) {
		super(superInstance);
		
	}

}
