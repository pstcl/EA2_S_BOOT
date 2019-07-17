<%@page import="org.pstcl.ea.util.EAUtil"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Mappings</title>
</head>
<body onload="myFunction()" style="margin: 0;">

	<%@include file="../authheader.jsp"%>

	<script>
		$(document).ready(function() {
			$("[data-toggle=popover]").popover({
				html : true
			});

			$(function() {
				$('[data-toggle="tooltip"]').tooltip()
			})
		});
	</script>

	<div class="container">

		<div class="card">

			<div class="card-heading">Meters With no Location Mapping</div>
			<div class="card-body">
				<div class="table-responsive">


					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<td>#</td>
								<th>Meter Sr No</th>
								<th>Meter Type</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${notMappedMeters}" var="meter"
								varStatus="indexStatus">


								<tr>
									<td>${indexStatus.index+1}</td>

									<td>${meter.meterSrNo}</td>
									<td>${meter.meterType}</td>
									<td><a
										href="map-meter?meterId=${meter.meterSrNo}">Assign
											Location</a></td>
								</tr>

							</c:forEach>
						</tbody>
					</table>
				</div>


			</div>
		</div>
		<div class="card">

			<div class="card-heading">Locations without Meter</div>
			<div class="card-body">
				<div class="table-responsive">


					<table class="table table-striped table-bordered table-hover">

						<tr>
							<th>Sr. No.</th>
							<th>Circle</th>
							<th>Division</th>
							<th>Substation</th>
							<th>Location Id</th>
							<th>Feeder</th>
							<th>Boundary Type</th>


						</tr>

						<c:forEach items="${notMappedLocations}" var="location"
							varStatus="indexStatus">


							<tr>
								<td>${indexStatus.index+1}</td>
								<td>${location.substationMaster.circleMaster.circleName}</td>
								<td>${location.substationMaster.divisionMaster.divisionname}</td>
								<td>${location.substationMaster.stationName}</td>

								<td>${location.locationId }</td>
								<td>${location.feederMaster.feederName}</td>
								<td>${location.boundaryTypeMaster.boundaryType}</td>
									<td><a class="btn btn-primary"
						href="changeLocationEmf?locationId=${location.locationId}">Edit
							EMF Mapping</a></td>
								
							</tr>

						</c:forEach>
					</table>
				</div>


			</div>
		</div>
	</div>
</body>
</html>