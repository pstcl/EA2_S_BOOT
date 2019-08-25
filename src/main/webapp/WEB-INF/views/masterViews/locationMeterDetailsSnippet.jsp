<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>




<table class="table table-striped table-bordered table-hover">

	<tr>
		<th>Location Id</th>
		<th>Meter Sr. No.</th>
		<th>Meter Type/Make</th>
		<th>Feeder</th>
		<th>Boundary Type</th>
		<th>Start Date</th>
		<th>End Date</th>
		<sec:authorize access="hasAnyRole('ROLE_SLDC_USER','ROLE_SLDC_ADMIN')">
			<th>Action</th>
		</sec:authorize>
	</tr>

	<c:forEach items="${meterLocationMappingList}"
		var="meterLocationMapping" varStatus="indexStatus">


		<tr>
			<td>${meterLocationMapping.locationMaster.locationId }</td>
			<td>${meterLocationMapping.meterMaster.meterSrNo}</td>
			<td>${meterLocationMapping.meterMaster.meterType}</td>
			<td>${meterLocationMapping.locationMaster.feederMaster.feederName}</td>
			<td>${meterLocationMapping.locationMaster.boundaryTypeMaster.boundaryType}</td>
			<td><fmt:formatDate value="${meterLocationMapping.startDate}"
					pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td><fmt:formatDate value="${meterLocationMapping.endDate}"
					pattern="yyyy-MM-dd HH:mm:ss" /></td>

			<td><sec:authorize
					access="hasAnyRole('ROLE_SLDC_USER','ROLE_SLDC_ADMIN')">
					<c:choose>
						<c:when test="${meterLocationMapping.endDate == null}">
							<a class="btn btn-primary"
								href="removeLocationMeter?meterlocationId=${meterLocationMapping.id}">Remove
								Meter</a>
						</c:when>
					</c:choose>

					<c:if test="${empty list}">
						<a class="btn btn-primary"
							href="changeLocationEmf?locationId=${meterLocationMapping.locationMaster.locationId}">Edit
							EMF Mapping</a>

					</c:if>
				</sec:authorize></td>
		</tr>

	</c:forEach>
</table>

<div class="table-responsive">



	<table class="table table-striped table-bordered table-hover">
		<thead>
			<tr>
				<th>Location Id</th>
				<th>External MF</th>
				<th>Net Sign</th>
				<th>Start Date</th>
				<th>End Date</th>
				<sec:authorize
					access="hasAnyRole('ROLE_SLDC_USER','ROLE_SLDC_ADMIN')">
					<th>Action</th>
				</sec:authorize>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${list}" var="location"
				varStatus="indexStatusSubstationList">
				<tr>
					<td>${location.locationMaster.locationId}</td>
					<td><fmt:formatNumber type="number" maxFractionDigits="3"
							value="${location.externalMF}" /></td>
					<td>${location.netWHSign}</td>
					<td><fmt:formatDate value="${location.startDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><c:choose>
							<c:when test="${not empty location.endDate }">
			to<fmt:formatDate value="${location.endDate}" pattern="yyyy-MM-dd" />
							</c:when>
							<c:otherwise>
till date</c:otherwise>
						</c:choose></td>



					<sec:authorize
						access="hasAnyRole('ROLE_SLDC_USER','ROLE_SLDC_ADMIN')">

						<td><a class="btn btn-primary"
							href="changeLocationEmf?locationId=${location.locationMaster.locationId}">Edit
								EMF Mapping</a></td>


					</sec:authorize>
				</tr>
			</c:forEach>

		</tbody>
	</table>
</div>

