<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>


</head>

<body onload="myFunction()" style="margin: 0;">
	<%@include file="../authheader.jsp"%>
	<div class="sticky-top">
		<nav aria-label="breadcrumb" class="sticky-top">
			<ol class="breadcrumb">
				<li class="breadcrumb-item"><a
					href="javascript:window.location='home'">Home</a></li>

				<li class="breadcrumb-item active" aria-current="page">Sub
					Station Master</li>
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
	<script>
		$(document).ready(function() {

			$('#locationDD').find('option').remove().end();
			$.get("getUnmappedLocations", populate).done(function() {
				//alert( "Employee Added" );
			}).fail(function(data, status, er) {
				alert("Couldn't load location information !" + data + er);
			});

		});
	</script>

	<script type="text/javascript">
		function submitform() {

			document.forms['changeDetails'].action = 'removeLocationMeter';
			document.forms['changeDetails'].submit();

		}

		function addEmpty() {
			addEmptyLocation()
		}

		function addEmptyLocation() {
			$('#locationDD').append(
					$("<option></option>").attr("value", "").text(
							"Select Location"));

		}
		function populate(data, status) {

			addEmpty();
			$.each(data, function(idx, obj) {

				$('#locationDD').append(
						$("<option></option>").attr("value", obj.locationId)
								.text(obj.locationId));
			});
		}
	</script>


	<%@include file="../dataTablesHeader.jsp"%>


	<script type="text/javascript">
		$(function() {
			$('.date-picker').datepicker({
				yearRange : '1950:2100',
				changeMonth : true,
				changeYear : true,
				showButtonPanel : false,
				dateFormat : "dd/mm/yy"

			});
		});
	</script>


	<script type="text/javascript">
		function showDiv(divId) {
			document.getElementById(divId).style.display = 'block';
		}
	</script>
	<div class="container">

		<div class="table-responsive row">
			<div class="card">

				<div class="card-heading">History</div>
				<div class="card-body">

					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th>Location Id</th>
								<th>Start Date</th>
								<th>End Date</th>
							</tr>
						</thead>
						<tbody>

							<c:forEach items="${locationMeterMappingList}" var="location"
								varStatus="indexStatusSubstationList">
								<tr>
									<td>${location.locationMaster.locationId}</td>
									<td><fmt:formatDate value="${location.startDate}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${location.endDate}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>

								</tr>


							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<c:if test="${not empty error}">

			<div class="row">
				<div class="alert alert-success lead">${error}</div>
			</div>

		</c:if>

		<div class="row">
			<form:form method="POST" modelAttribute="changeMeterSnippet"
				id="changeDetails">

				<div class="row">
					<div class="form-control ">
						<label class="col-lg-2">Meter Sr No</label> <b>${changeMeterSnippet.meterMaster.meterSrNo}</b>
					</div>
				</div>
				<form:hidden path="meterMaster"
					value="${changeMeterSnippet.meterMaster.meterSrNo}"></form:hidden>

				<c:choose>
					<c:when test="${changeMeterSnippet.oldMeterLocationMap != null}">
						<form:hidden path="oldMeterLocationMap"
							value="${changeMeterSnippet.oldMeterLocationMap.id}"></form:hidden>
						<div class="row">
							<div class="form-control ">
								End Date
								<form:input type="text" cssClass="date-picker" path="endDate"
									class="form-control input-sm" required="true" />
							</div>
						</div>


					</c:when>
				</c:choose>
				

				<div class="form-group">
					<div class="col-lg-offset-2 col-lg-10">

						<input type="button" class="btn btn-info" onclick="submitform()"
							value="Save">
					</div>
				</div>

			</form:form>
		</div>
	</div>
</body>
</html>