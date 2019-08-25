<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<style>
#tamperTxnTable {
  table-layout: fixed;
  width: 100% !important;
}
</style>


<script type="text/javascript">
		$(document).ready(
				function() {
					

					$('#tamperTxnTable').DataTable(
							{
								dom : 'Bfrtip',
								"paging" : false,
								"ordering" : true,
								buttons : [ 'copyHtml5', 'excelHtml5',
										'csvHtml5', 'pdfHtml5' ],
								orientation : 'landscape',
								pageSize : 'A4',
								"autoWidth":	true
							});

				});
	</script>

<div class="row">
	<div class="col">
		<div id="tamperTxnTable_wrapper" class="table-responsive ">


			<!--table for temper log start  -->
			<table id="tamperTxnTable"
				class="table table-striped table-bordered table-hover">

				<thead>


					<tr>
						<th>Date Time</th>
						<th>Import (Watt-hour)</th>
						<th>Export(Watt-hour)</th>
						<th>Tamper Count</th>
						<th>Tamper Duration</th>
						<th>Tamper Type</th>
						<th>Record No</th>
						<th>Record Status</th>
						<th>Circle</th>
						<th>Division</th>
						<th>Substation</th>
						<th>Meter Details</th>
					</tr>
				</thead>

				<c:forEach items="${locationSurveyDataModel.tamperLogTransactions}"
					var="tamperLogForMeter" varStatus="indexStatus">

					<tr>
						<td><fmt:formatDate
								value="${tamperLogForMeter.transactionDate}"
								pattern="dd/MM/yyyy HH:mm:ss" /> <c:if
								test="${ tamperLogForMeter.transactionDate == null}">
									TOTAL
									</c:if></td>
						<td>${tamperLogForMeter.impWh}</td>
						<td>${tamperLogForMeter.expWh}</td>
						<td>${tamperLogForMeter.tamperCount}</td>
						<td>${tamperLogForMeter.tamperDuration}</td>
						<td>${tamperLogForMeter.tamperType}</td>
						<td>${tamperLogForMeter.recordNo}</td>
						<td>${tamperLogForMeter.recordStatus}</td>
						<td>${tamperLogForMeter.location.circleMaster.circleName}</td>
						<td>${tamperLogForMeter.location.substationMaster.divisionMaster.divisionname}</td>
						<td>${tamperLogForMeter.location.substationMaster.stationName}</td>
						<td>${tamperLogForMeter.meter.meterSrNo}(${tamperLogForMeter.meter.meterType})


						</td>
				</c:forEach>
			</table>
			<!--table for temper log end  -->
		</div>

	</div>
</div>


