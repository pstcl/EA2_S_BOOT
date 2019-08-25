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

	<script type="text/javascript">
		function submitform() {

			document.forms['changeDetails'].action = 'changeLocationEmf';
			document.forms['changeDetails'].submit();

		}
	</script>



	<%@include file="../dataTablesHeader.jsp"%>


	<script type="text/javascript">
		function showDiv(divId, element) {
			document.getElementById(divId).style.display = element.value == "yes" ? 'block'
					: 'none';
		}
	</script>

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

	<div class="container">
		<div class="row">
			<div class="alert alert-success lead">${error}</div>
		</div>
		<div class="table-responsive">



			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>Location Id</th>

						<th>External MF</th>
						<th>Net WH Sign</th>
						<th>Start Date</th>
						<th>End Date</th>
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
							<td><fmt:formatDate value="${location.endDate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>

						</tr>


					</c:forEach>
				</tbody>
			</table>
		</div>
		<form:form method="POST" modelAttribute="locationEMFModel"
			id="changeDetails">





			<form:hidden path="locationMaster"
				value="${locationEMFModel.locationMaster.locationId}"></form:hidden>
			<div class="row">
				<div class="form-control ">
					<label class="col-lg-4"><b>Location Id</b></label> <b>${locationEMFModel.locationMaster.locationId}</b>
				</div>
			</div>



			<c:choose>
				<c:when test="${locationEMFModel.oldLocationEmf != null}">
					<form:hidden path="oldLocationEmf"
						value="${locationEMFModel.oldLocationEmf.id}"></form:hidden>

					<div class="row">
						<div class="col form-control ">

							<label><b>Current External MF:
									${locationEMFModel.oldLocationEmf.externalMF}</b></label>
						</div>
					</div>
					<div class="row">
						<div class="col form-control ">
							<label><b>Current Net WH Sign</b> <b>${locationEMFModel.oldLocationEmf.netWHSign}</b></label>
						</div>
					</div>

				</c:when>

				<c:when test="${locationEMFModel.oldLocationEmf == null}">
					<div class="row">
						<div class="alert alert-info lead">Currently No EMF mapping in use</div>

					</div>
				</c:when>
			</c:choose>


			<div class="row">
				<div class="col form-control">
					Enter New Emf for Location:
					<form:input type="text" path="externalMF"
						class="form-control input-sm"></form:input>
				</div>
			</div>

<div class="row">
				<div class="col form-control">
					External CT Ratio:
					<form:input type="text" path="externalCTRation"
						class="form-control input-sm"></form:input>
				</div>
			</div>
			<div class="row">
				<div class="col form-control">
					External PT Ratio:
					<form:input type="text" path="externalPTRation"
						class="form-control input-sm"></form:input>
				</div>
			</div>
			<div class="row">
				<div class="col form-control">
					Select Net WH Sign for Location
					<form:select path="netWHSign">
						<form:option value="">Select Sign</form:option>
						<form:option value='-1'>-1</form:option>
						<form:option value='1'>1</form:option>
					</form:select>
				</div>
			</div>
			<div class="row">
				<div class="col form-control">
					<label>Effective Date for change in External Multiplying
						Factor</label>
					<form:input type="text" cssClass="date-picker" path="effectiveDate"
						class="form-control input-sm" required="true" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-lg-offset-2 col-lg-10">

					<input type="button" class="btn btn-info" onclick="submitform()"
						value="Save Details">
				</div>
			</div>


		</form:form>
	</div>
</body>
</html>