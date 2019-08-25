<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>

<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<script type="text/javascript">
	function viewDetails(modalid) {
		$(modelid).modal('toggle');

	}
</script>

<%-- <c:url value='/previewOilReport-${oilReport.id}' /> --%>


<title>

	Loc Id:${locationSurveyDataModel.locationMaster.locationId},
	SS:${locationSurveyDataModel.locationMaster.substationMaster.stationName},
	Feeder:${locationSurveyDataModel.locationMaster.feederMaster.feederName},
	Boundary:${locationSurveyDataModel.locationMaster.boundaryTypeMaster.boundaryType},
	Meter No:${locationSurveyDataModel.meterMaster.meterSrNo},</title>
</head>

<body onload="myFunction()" style="margin: 0;">
	<%@include file="authheader.jsp"%>
	<%@include file="dataTablesHeader.jsp"%>



	<script type="text/javascript">
		$(document).ready(
				function() {
					$('#dailyTxnTable').DataTable(
							{
								dom : 'Bfrtip',
								"paging" : false,
								"ordering" : false,
								buttons : [ 'copyHtml5', 'excelHtml5',
										'csvHtml5', 'pdfHtml5' ],
								orientation : 'landscape',
								pageSize : 'A4'
							});

					

				});
	</script>
	<div class="sticky-top">
		<nav aria-label="breadcrumb" class="sticky-top">
			<ol class="breadcrumb">
				<li class="breadcrumb-item"><a
					href="javascript:window.location='home'">Home</a></li>

				<li class="breadcrumb-item active" aria-current="page">Energy
					Import Export</li>
			</ol>
		</nav>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {

			$('.modal-content').resizable({
				//alsoResize: ".modal-dialog",
				minHeight : 300,
				minWidth : 600
			});
			$('.modal-dialog').draggable();
		});
	</script>


	<div class="container-fluid">
		<c:if test="${not empty success}">
			<div class="alert alert-success lead">${success}</div>
		</c:if>

	</div>
	<div class="card">
	<sec:authorize
						access="hasAnyRole('ROLE_SLDC_ADMIN')">
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
		</tbody>		</table>
			</div>
		</div>
		<!-- 		<div class="col"> -->
		<!-- 			<div id="tamperTxnTable_wrapper" class="table-responsive "> -->


		<!-- 				table for temper log start  -->
		<!-- 				<table id="tamperTxnTable" -->
		<!-- 					class="table table-striped table-bordered table-hover"> -->

		<!-- 					<thead> -->


		<!-- 						<tr> -->
		<!-- 							<th>Date Time</th> -->
		<!-- 							<th>Import (Watt-hour)</th> -->
		<!-- 							<th>Export(Watt-hour)</th> -->
		<!-- 							<th>Tamper Count</th> -->
		<!-- 							<th>Tamper Duration</th> -->
		<!-- 							<th>Tamper Type</th> -->
		<!-- 							<th>Record No</th> -->
		<!-- 							<th>Record Status</th> -->
		<!-- 						</tr> -->
		<!-- 					</thead> -->

		<%-- 					<c:forEach items="${locationSurveyDataModel.tamperLogTransactions}" --%>
		<%-- 						var="tamperLogForMeter" varStatus="indexStatus"> --%>

		<!-- 						<tr> -->
		<%-- 							<td><fmt:formatDate --%>
		<%-- 									value="${tamperLogForMeter.transactionDate}" --%>
		<%-- 									pattern="dd/MM/yyyy HH:mm:ss" /> <c:if --%>
		<%-- 									test="${ tamperLogForMeter.transactionDate == null}"> --%>
		<!-- 									TOTAL -->
		<%-- 									</c:if></td> --%>
		<%-- 							<td>${tamperLogForMeter.impWh}</td> --%>
		<%-- 							<td>${tamperLogForMeter.expWh}</td> --%>
		<%-- 							<td>${tamperLogForMeter.tamperCount}</td> --%>
		<%-- 							<td>${tamperLogForMeter.tamperDuration}</td> --%>
		<%-- 							<td>${tamperLogForMeter.tamperType}</td> --%>
		<%-- 							<td>${tamperLogForMeter.recordNo}</td> --%>
		<%-- 							<td>${tamperLogForMeter.recordStatus}</td> --%>
		<%-- 					</c:forEach> --%>
		<!-- 				</table> -->
		<!-- 			</div> -->
		<!--table for temper log end  -->
		<!-- 		</div> -->

	</div>



</body>
</html>