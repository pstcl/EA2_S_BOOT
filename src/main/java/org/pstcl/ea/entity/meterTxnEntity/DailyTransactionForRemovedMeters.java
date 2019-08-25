package org.pstcl.ea.entity.meterTxnEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@Entity
@Table(name = "DAILY_TRANSACTION_REMOVED_METERS",uniqueConstraints={@UniqueConstraint(columnNames={"METER_ID", "transactionDate"})})
public class DailyTransactionForRemovedMeters extends DailyTransactionMappedSuper{


	public DailyTransactionForRemovedMeters()
	{
	super();	
	}

	public DailyTransactionForRemovedMeters(DailyTransaction superInstance) {
		super(superInstance);
		
	}
}
