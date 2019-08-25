<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">
	$(document).ready(function() {
		$('#dailyTxnTable').DataTable({
			dom : 'Bfrtip',
			"paging" : false,
			"ordering" : false,
			buttons : [ 'copyHtml5', 'excelHtml5', 'csvHtml5', 'pdfHtml5' ],
			orientation : 'landscape',
			pageSize : 'A4',
			"autoWidth":	true
		});

	});
</script>

<style>
#dailyTxnTable {
  table-layout: fixed;
  width: 100% !important;
}
</style>


<div class="card">
	<sec:authorize access="hasAnyRole('ROLE_SLDC_ADMIN')">
		<a title="${locationSurveyDataModel.locationMaster.locationId}"
			href="javascript:window.location='editPendingLocData-${locationSurveyDataModel.locationMaster.locationId}?month=${month}&year=${year}'">
			Edit Data </a>
		<a title="${locationSurveyDataModel.locationMaster.locationId}"
			href="javascript:window.location='createDailyFromLoadSurveyData-${locationSurveyDataModel.locationMaster.locationId}?month=${month}&year=${year}'">
			Substitute With Load Survey Data </a>
	</sec:authorize>
</div>

<div class="row">
	<div class="col">
		<div id="da111ilyTxnTable_wrapper" class="table-responsive ">
			<div class="table-responsive">


				<table id="dailyTxnTable"
					class="table table-striped table-bordered table-hover">

					<thead>


						<tr>
							<th>Date Time of Reading</th>
							<th>Import (Watt-hour)</th>
							<th>Export(Watt-hour)</th>
							<th>Import (MWH)</th>
							<th>Export(MWH)</th>
							<th>Difference</th>
							<th>Net Sign</th>
							<th>External MF</th>

							<th>Meter Sr. No.</th>
							<th>Remarks</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${locationSurveyDataModel.dailyTransactions}"
							var="dailyTransactionForMeter" varStatus="indexStatus">

							<tr>
								<td><fmt:formatDate
										value="${dailyTransactionForMeter.transactionDate}"
										pattern="dd/MM/yyyy HH:mm:ss" /> <c:if
										test="${ dailyTransactionForMeter.transactionDate == null}">
									TOTAL
									</c:if></td>
								<td>${dailyTransactionForMeter.importWHF}</td>
								<td>${dailyTransactionForMeter.exportWHF}</td>
								<td>${dailyTransactionForMeter.exportBoundaryPtMWH }</td>
								<td>${dailyTransactionForMeter.importBoundaryPtMWH}</td>
								<td>${dailyTransactionForMeter.boundaryPtImportExportDifferenceMWH}</td>

								<td>${dailyTransactionForMeter.netWHSign }</td>
								<td>${dailyTransactionForMeter.externalMF}</td>


								<td>${dailyTransactionForMeter.meter.meterSrNo}</td>

								<td>${dailyTransactionForMeter.remarks}</td>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>


