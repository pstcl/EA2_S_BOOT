<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	function submitform() {

		document.forms['changeDetails'].action = 'mapMeterEffect';
		document.forms['changeDetails'].submit();

	}
</script>
</head>
<body onload="myFunction()" style="margin: 0;">
	<%@include file="../authheader.jsp"%>
	<div class="sticky-top">
		<nav aria-label="breadcrumb" class="sticky-top">
			<ol class="breadcrumb">
				<li class="breadcrumb-item"><a
					href="javascript:window.location='home'">Home</a></li>


				<li class="breadcrumb-item "><a
					href="javascript:window.location='substationMaster'">Sub
						Station Master</a></li>

				<li class="breadcrumb-item active" aria-current="page">Successfully
					Changed mapping</li>
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


	<%@include file="../dataTablesHeader.jsp"%>


	<script>
		$(document).ready(function() {
			function disableBack() {
				window.history.forward()
			}

			window.onload = disableBack();
			window.onpageshow = function(evt) {
				if (evt.persisted)
					disableBack()
			}
		});
	</script>


	<div class="table-responsive">



		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>Location Id</th>
					<th>Substation</th>
					<th>Meter Sr. No.</th>
					<th>Meter Type/Make</th>
					<th>Feeder</th>
					<th>Boundary Type</th>
					<th>Start Date</th>
					<th>End Date</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${locationMeterMappingList}"
					var="meterLocationMapping" varStatus="indexStatusSubstationList">
					<tr>

						<td>${meterLocationMapping.locationMaster.locationId }</td>
						<td>${meterLocationMapping.locationMaster.substationMaster.stationName}</td>

						<td>${meterLocationMapping.meterMaster.meterSrNo}</td>
						<td>${meterLocationMapping.meterMaster.meterType}</td>
						<td>${meterLocationMapping.locationMaster.feederMaster.feederName}</td>
						<td>${meterLocationMapping.locationMaster.boundaryTypeMaster.boundaryType}</td>
						<td><fmt:formatDate value="${location.startDate}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td><fmt:formatDate value="${location.endDate}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>

					</tr>


				</c:forEach>
			</tbody>
		</table>
		<div class="row">
			<c:if test="${success}">


				<div class="row">
					<form:form method="POST" modelAttribute="changeMeterSnippet"
						id="changeDetails">
						<form:hidden path="meterMaster"></form:hidden>
						<form:hidden path="startDate"></form:hidden>
						<div class="form-group">
							<div class="col-lg-offset-2 col-lg-10">
								<input type="button" class="btn btn-info" onclick="submitform()"
									value="View Mapping Changes">
							</div>
						</div>

					</form:form>
				</div>

			</c:if>
		</div>
	</div>

</body>
</html>