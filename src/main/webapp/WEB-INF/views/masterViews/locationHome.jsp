<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="org.pstcl.ea.util.EAUtil"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Location Details</title>
</head>
<body onload="myFunction()" style="margin: 0;">
	<%@include file="../authheader.jsp"%>

	<%@include file="../dataTablesHeader.jsp"%>
	<%@include file="../html/customProgressBarModal.html"%>

	<div class="sticky-top">
		<nav aria-label="breadcrumb" class="sticky-top">
			<ol class="breadcrumb">
				<li class="breadcrumb-item"><a
					href="javascript:window.location='home'">Home</a></li>

				<li class="breadcrumb-item active" aria-current="page">Location
					Home</li>
			</ol>
		</nav>
	</div>
	<script>
		$(document).ready(function() {

			myFunction();
			setDefaultMonth();

		});
		function setDefaultMonth() {
			var newdate = new Date();
			newdate.setDate(new Date().getDate() - 37); // minus the date
			document.getElementById("reportDate").value = newdate.getFullYear()
					+ "-" + ("0" + (newdate.getMonth() + 1)).slice(-2);

		}
		function applyFilters() {
			showProgressBar();

			$('#reportDiv').css('display', 'none');
			$('#loader').css('display', 'block');

			if ($("#reportDate").val() == "") {
				var newdate = new Date();
				newdate.setDate(new Date().getDate() - 15); // minus the date
				document.getElementById("reportDate").value = newdate
						.getFullYear()
						+ "-" + ("0" + (newdate.getMonth() + 1)).slice(-2);

			}

			if ($("#reportDate").val() != "") {
				var reportDateArray = $("#reportDate").val().split("-");
				var month = reportDateArray[1];
				var year = reportDateArray[0];

			} else {
				var month = null;
				var year = null;

			}

			var reportParamsModel = {
				"locationId" : $('#locationId').val(),
				"reportMonth" : month,
				"reportYear" : year,
				"reportType" : $('input[name=reportType]:checked').val()
			};
			return reportParamsModel;

		}

		function loadReport() {
			var reportParamsModel = applyFilters();

			$.ajax({
				url : "locationReports",
				type : "POST",
				data : JSON.stringify(reportParamsModel),
				contentType : 'application/json',
				success : function(response) {
					$("#reportDiv").html(response);

					$('#reportDiv').css('display', 'block');
					hideProgressBar();

					$('#loader').css('display', 'none');

				},
				error : function(data, status, er) {
					alert("OBJECT Error" + data + er);
				}
			});

		}

		function populateTableContainer(data, status) {
			$("#reportDiv").html(data);
		}
	</script>
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

	<script type="text/javascript">
		function getLocationMeterDetails(locationid) {
			$.ajax({
				url : "getLocationMeterDetails",
				type : "GET",
				data : {
					locationid : locationid
				},
				success : function(response) {
					$('#locationContainer').html(response);

				},
				error : function(data, status, er) {
					alert("Error" + data + er);
				}
			});

		}
	</script>
	<script type="text/javascript">
		function getMeterData(locationId, ssCode, meterDiv) {
			$('#' + meterDiv).css('display', 'none');
			$.ajax({
				url : "getMeterDetails",
				type : "GET",
				data : {
					locationId : locationId,
					ssCode : ssCode
				},
				success : function(response) {
					$('#' + meterDiv).replaceWith(response);

				},
				error : function(data, status, er) {
					alert("Error" + data + er);
				}
			});

		}
	</script>

	<script type="text/javascript">
		function viewDetails(url) {
			$('#myModal').modal('toggle');
			document.getElementById('substationIFrame').src = url;

		}
	</script>

	<div class="container">
		<div id="locationContainer">

			<div class="card">
				<div class="card-body">

					<ul class="nav nav-pills" role="tablist">
						<li class="nav-item"><a class="nav-link active"
							data-toggle="pill" href="#locationDetails">Location Details</a></li>
						<li class="nav-item"><a class="nav-link" data-toggle="pill"
							href="#menu1">Meter History</a></li>
						<li class="nav-item"><a class="nav-link" data-toggle="pill"
							href="#menu2">External MF History</a></li>
						<li class="nav-item"><a class="nav-link" data-toggle="pill"
							href="#menu3">Location Reports</a></li>
					</ul>

					<div class="tab-content">
						<div id="locationDetails" class="container tab-pane active">
							<br>
							<h5>${location.locationId}</h5>

							<table class="table table-striped table-bordered table-hover">

								<tr>
									<th>Utility</th>

									<td>${location.utiltiyName}</td>
								</tr>

								<tr>
									<th>Division Name</th>
									<td>${location.substationMaster.divisionMaster.divisionname}</td>
								</tr>

								<tr>
									<th>Station Name</th>

									<td>${location.substationMaster.stationName}</td>
								</tr>


								<tr>
									<th>Device Type</th>
									<td>${location.deviceTypeMaster.deviceType}</td>
								</tr>

								<tr>
									<th>Feeder Name</th>

									<td>${location.feederMaster.feederName}</td>
								</tr>

								<tr>
									<th>Boundary Type</th>

									<td>${location.boundaryTypeMaster.boundaryType}</td>
								</tr>

								<tr>
									<th>Voltage Level</th>

									<td>${location.substationMaster.voltageLevel}</td>
								</tr>

								<tr>
									<th>Status</th>
									<td>${location.location_status}</td>
								</tr>




								<!-- 	<tr> -->
								<!-- 		<th>External MF</th> -->
								<%-- 		<td>${location.externalMF}</td> --%>
								<!-- 	</tr> -->



							</table>
						</div>

						<div id="menu1" class="container tab-pane fade">
							<br>
							<h5>Meter Mappings</h5>
							<input type="hidden" id="locationId"
								value="${location.locationId}" />





							<c:forEach items="${meterLocationMappingList}"
								var="meterLocationMapping" varStatus="indexStatus">

								<div id="accordion">
									<div class="card">
										<div class="card-header" id="headingOne">
											<h5 class="mb-0">
												<button class="btn btn-link" data-toggle="collapse"
													data-target="#collapse${indexStatus.index}"
													aria-expanded="false"
													aria-controls="collapse${indexStatus.index}">
													<table>
														<tr>
															<th>${meterLocationMapping.meterMaster.meterSrNo}</th>
															<th>(<fmt:formatDate
																	value="${meterLocationMapping.startDate}"
																	pattern="dd-MM-yyyy" />)
															</th>
															<th><c:choose>
																	<c:when
																		test="${not empty meterLocationMapping.endDate }">
															to(<fmt:formatDate
																			value="${meterLocationMapping.endDate}"
																			pattern="dd-MM-yyyy" />)
														</c:when>
																	<c:otherwise>till date</c:otherwise>
																</c:choose></th>
														</tr>
													</table>
												</button>
											</h5>
										</div>

										<div id="collapse${indexStatus.index}" class="collapse hide"
											aria-labelledby="headingOne" data-parent="#accordion">
											<div class="card-body">
												<table>
													<tr>
														<th>Meter Serial No.</th>

														<td>${meterLocationMapping.meterMaster.meterSrNo}</td>
													</tr>
													<tr>
														<th>Installation Date at location</th>
														<td><fmt:formatDate
																value="${meterLocationMapping.startDate}"
																pattern="dd-MM-yyyy" /></td>
													</tr>
													<tr>
														<th>Uninstallation Date at location</th>
														<td><c:choose>
																<c:when
																	test="${not empty meterLocationMapping.endDate }">
																	<fmt:formatDate value="${meterLocationMapping.endDate}"
																		pattern="dd-MM-yyyy" />
																</c:when>
																<c:otherwise>Currently installed</c:otherwise>
															</c:choose></td>
													</tr>

													<tr>
														<th>Meter Type</th>

														<td>${meterLocationMapping.meterMaster.meterType}</td>
													</tr>

													<tr>
														<th>Meter Category</th>
														<td>${meterLocationMapping.meterMaster.meterCategory}</td>
													</tr>

													<tr>
														<th>Meter Make</th>
														<td>${meterLocationMapping.meterMaster.meterMake}</td>
													</tr>

													<tr>
														<th>CT Accuracy</th>
														<td>${meterLocationMapping.meterMaster.CTAccuracy}</td>
													</tr>
													<tr>
														<th>PT Accuracy</th>
														<td>${meterLocationMapping.meterMaster.PTAccuracy}</td>
													</tr>
													<tr>
														<th colspan="2"><sec:authorize
																access="hasAnyRole('ROLE_SLDC_USER','ROLE_SLDC_ADMIN')">
																<c:choose>
																	<c:when test="${meterLocationMapping.endDate == null}">
																		<a class="btn btn-primary"
																			href="removeLocationMeter?meterlocationId=${meterLocationMapping.id}">Remove
																			Meter</a>
																	</c:when>
																</c:choose>

															</sec:authorize></th>
													</tr>
												</table>
											</div>
										</div>
									</div>
								</div>

							</c:forEach>
						</div>

						<div id="menu2" class="container tab-pane fade">
							<br>
							<h5>MF Mappings</h5>
							<sec:authorize
								access="hasAnyRole('ROLE_SLDC_USER','ROLE_SLDC_ADMIN')">

								<a class="btn btn-primary"
									href="changeLocationEmf?locationId=${location.locationId}">Edit
									EMF </a>
							</sec:authorize>
							<div class="table-responsive">



								<table class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>External MF</th>
											<th>Sign</th>
											<th>External CT Ratio</th>
											<th>External PT Ratio</th>

											<th>Start Date</th>
											<th>End Date</th>
										</tr>
									</thead>
									<tbody>

										<c:forEach items="${list}" var="locationEMF"
											varStatus="indexStatusSubstationList">
											<tr>

												<td><fmt:formatNumber type="number"
														maxFractionDigits="3" value="${locationEMF.externalMF}" /></td>

												<td><fmt:formatNumber type="number"
														maxFractionDigits="3" value="${locationEMF.netWHSign}" /></td>

												<td>${locationEMF.externalCTRation}</td>
												<td>${locationEMF.externalPTRation}</td>
												<td><fmt:formatDate value="${locationEMF.startDate}"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td><fmt:formatDate value="${locationEMF.endDate}"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>




											</tr>
										</c:forEach>

									</tbody>
								</table>
							</div>

						</div>
						<div id="menu3" class="container tab-pane fade">
							<div class="form-group row">
								<div class="form-control col-md-3">
									Month <input id="reportDate" type="month"
										class="form-control input-sm" />
								</div>
								<div class="form-control col-sm-9 ">
									Boundary
									<div id="intervalDiv" class="btn-group btn-group-toggle"
										data-toggle="buttons">
										<label class="btn btn-primary"> <input type="radio"
											name="reportType" id="option3" value="" autocomplete="off"
											checked="checked">Energy Import Export
										</label> <label class="btn btn-primary"> <input type="radio"
											name="reportType" id="option3"
											value="<%=EAUtil.EA_LOCATION_REPORT_TAMPERS%>" autocomplete="off">
											Tampers
										</label> <label class="btn btn-primary"> <input type="radio"
											name="reportType" id="option3"
											value="<%=EAUtil.EA_LOCATION_REPORT_INSTANT_REGISTERS%>"
											autocomplete="off"> Instant Registers
										</label>

										<p id="demo"></p>
									</div>
								</div>


								<div class="float-right">
									<input type="button" onclick="loadReport()"
										id="loadReportButton" value="Load Report"
										class="btn btn-primary btn-sm" /> <a
										class="btn btn-light btn-sm float-right"
										href="javascript:window.location='lossReportDyn'"><span
										class="glyphicon glyphicon-plus"></span>Clear</a>
								</div>

							</div>
							<div class="card">
								<div class="card-heading">Report</div>
								<div class="card-body">

									<div id="reportDiv"></div>
								</div>
							</div>


						</div>


					</div>

				</div>

				<div class="modal fade" id="myModal">
					<div class="modal-dialog modal-lg">
						<div class="modal-content"
							style="width: 1000px; align-self: center;">

							<!-- Modal Header -->
							<div class="modal-header">
								<h4 class="modal-title">Substation Details</h4>
								<button type="button" class="close" data-dismiss="modal">&times;</button>
							</div>

							<!-- Modal body -->
							<div class="modal-body">
								<div id="myModalDiv"></div>
							</div>


							<!-- Modal footer -->
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">Close</button>
							</div>

						</div>
					</div>
				</div>
			</div>

		</div>
</body>
</html>