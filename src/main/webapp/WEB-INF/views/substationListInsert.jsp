<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="tableContainer1">

	<div class="table-responsive">



		<table class="table table-striped table-bordered table-hover">
			<col width="20">
			<col width="60">
			<col width="60">
			<col width="60">
			<col width="800">

			<tr>
				<th>Sr. No.</th>
				<th>Circle</th>
				<th>Division</th>
				<th>Substation</th>
				<th>Meter Details</th>


			</tr>
			<c:forEach items="${substationList}" var="substation"
				varStatus="indexStatusSubstationList">

				<tr>

					<td>${indexStatusSubstationList.index+1}</td>
					<td>${substation.substationMaster.circleMaster.circleName}</td>
					<td>${substation.substationMaster.divisionMaster.divisionname}</td>
					<td>${substation.substationMaster.stationName}</td>

					<td>
						<table class="table table-striped table-bordered table-hover">

							<tr>
								<th>Location Id</th>
								<th>Meter Sr. No.</th>
								<th>Meter Type/Make</th>
								<th>Location</th>
								<th>Boundary Type</th>
								<th>Start Date</th>
								<th>End Date</th>

							</tr>

							<c:forEach items="${substation.mtrLocMap}" var="location"
								varStatus="indexStatus">


								<tr>
									<td>${location.locationMaster.locationId }</td>
									<td>${location.meterMaster.meterSrNo}</td>
									<td>${location.meterMaster.meterType}</td>
									<td>${location.locationMaster.feederMaster.feederName}</td>
									<td>${location.locationMaster.boundaryTypeMaster.boundaryType}</td>
									<td><fmt:formatDate value="${location.startDate}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${location.endDate}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><a class="btn btn-primary"
										href="changeLocationEmf?locationId=${location.locationMaster.locationId}">Edit
											EMF Mapping</a></td>
									<td><button class="btn btn-primary" data-toggle="collapse"
											data-target="#demo${indexStatus.index+1 }"
											onclick="getMeterData('${location.id}','${substation.substationMaster.ssCode}','demo${indexStatusSubstationList.index}_${indexStatus.index+1 }')">Meter
											Details</button></td>
									<c:choose>
										<c:when test="${location.endDate == null}">
											<td><a class="btn btn-primary"
												href="changeMeterLocation?meterlocationId=${location.id}">Change
													Meter Location</a></td>
										</c:when>
									</c:choose>
								</tr>
								<tr>
									<td><div
											id="demo${indexStatusSubstationList.index}_${indexStatus.index+1 }"
											class="collapse in"></div></td>
								</tr>

							</c:forEach>
						</table>
			</c:forEach>
			<tr>
				<td></td>
				<td>Meters With No Mapping</td>
				<td></td>
				<td></td>
				<td>
					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th>Meter Sr No</th>
								<th>Meter Type</th>
								<th>Add Mapping</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${notMappedMeters}" var="meter"
								varStatus="indexStatusSubstationList">


								<tr>

									<td>${meter.meterSrNo}</td>
									<td>${meter.meterType}</td>
									<td><a
										href="map-meter?meterId=${meter.meterSrNo}">Change
											Details</a></td>
								</tr>

							</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>

		</table>
		<div class="modal fade" id="myModal">
			<div class="modal-dialog modal-lg">
				<div class="modal-content" style="width: 600px; align-self: center;">

					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">Substation Details</h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>

					<!-- Modal body -->
					<div class="modal-body">
						<iframe id="substationIFrame" width="100%" height="100%"></iframe>
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
