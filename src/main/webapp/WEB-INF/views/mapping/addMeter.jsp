<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Meter Details</title>
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
		function initAllList() {
			$('meterMake').find('option').remove().end();
			$('meterType').find('option').remove().end();
			$('meterCategory').find('option').remove().end();

			$.post("getMeterListModel", {

			}, populateAllList).done(function() {
				//alert( "Employee Added" );
			}).fail(function(data, status, er) {
				alert("Couldn't load location information !" + data + er);
			});

		}

		function populateAllList(data, status) {
			$.each(data.meterMake, function(idx, obj) {
				//alert(obj.circleName);
				$('#meterMake').append(
						$("<option></option>").attr("value", obj).text(obj));
			});
			$.each(data.meterCategory, function(idx, obj) {
				//alert(obj.circleName);
				$('#meterCategory').append(
						$("<option></option>").attr("value", obj).text(obj));
			});
			$.each(data.meterType, function(idx, obj) {
				//alert(obj.circleName);
				$('#meterType').append(
						$("<option></option>").attr("value", obj).text(obj));
			});
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


	<script>
		$(document).ready(function() {

			initAllList();
		});
	</script>


	<%@include file="../dataTablesHeader.jsp"%>


	<script type="text/javascript">
		function submitform(action) {

			document.forms['changeDetails'].action = action;
			document.forms['changeDetails'].submit();

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


	<%@include file="../dataTablesHeader.jsp"%>
	<c:if test="${not empty msg}">

		<div class="row">
			<div class="alert alert-success lead">${msg}</div>
		</div>

	</c:if>
	<form:form method="POST" modelAttribute="meter" id="changeDetails">
		
		<div class="container">
			<div class="row">





				<div class="form-control col-md-6">

					<div class="form-control ">
						<label class="form-control-label">Meter Sr No</label>
						<form:input path="meterSrNo" />
					</div>
					<div class="has-error">
						<form:errors path="meterSrNo" class="alert alert-danger" />
					</div>
				</div>
				<div class="form-control col-md-6">
					<div class="form-control ">
						<label class="form-control-label">Meter Type</label>

						<form:select id="meterType" path="meterType">
						</form:select>
					</div>
					<div class="has-error">
						<form:errors path="meterType" class="alert alert-danger" />
					</div>
				</div>
			</div>
			<div class="row">



				<div class="form-control col-md-6">
					<div class="form-control ">
						<label class="form-control-label">Meter Make</label>

						<form:select id="meterMake" path="meterMake">
						</form:select>
					</div>
					<div class="has-error">
						<form:errors path="meterMake" class="alert alert-danger" />
					</div>
				</div>

				<div class="form-control col-md-6">
					<div class="form-control ">
						<label class="form-control-label">Meter Category</label>

						<form:select id="meterCategory" path="meterCategory">
						</form:select>
					</div>
					<div class="has-error">
						<form:errors path="meterCategory" class="alert alert-danger" />
					</div>
				</div>

			</div>
			<div class="row">


				<div class="form-control col-md-6">
					<div class="form-control ">
						<label class="form-control-label">Grid Loss Factor</label>
						<form:input path="gridLossFactor" type="number" />
					</div>
					<div class="has-error">
						<form:errors path="gridLossFactor" class="alert alert-danger" />
					</div>
				</div>



				<div class="form-control col-md-6">
					<div class="form-control ">
						<label class="form-control-label">Installed Date</label>
						<form:input type="text" cssClass="date-picker"
							path="installedDate" name="installedDate"
							class="form-control input-sm col-md-8" />
					</div>
					<div class="has-error">
						<form:errors path="installedDate" class="alert alert-danger" />
					</div>
				</div>
			</div>
			<div class="row">

				<div class="form-control col-md-6">

					<div class="form-control ">
						<label class="form-control-label">Internal MF</label>
						<form:input path="internalMF" type="number" />
					</div>
					<div class="has-error">
						<form:errors path="internalMF" class="alert alert-danger" />
					</div>
				</div>


				<div class="form-control col-md-6">
					<div class="form-control ">
						<label class="form-control-label">CT Accuracy</label>
						<form:input path="CTAccuracy" type="text" />
					</div>
					<div class="has-error">
						<form:errors path="CTAccuracy" class="alert alert-danger" />
					</div>
				</div>

			</div>
			<div class="row">

				<div class="form-control col-md-6">
					<div class="form-control row">
						<label class="form-control-label">PT Accuracy</label>
						<form:input path="PTAccuracy" type="text" />
						<div class="has-error">
							<form:errors path="PTAccuracy" class="alert alert-danger" />
						</div>
					</div>
					<div class="has-error">
						<form:errors path="PTAccuracy" class="alert alert-danger" />
					</div>
				</div>

				<div class="form-control col-md-6">

					<div class="form-control ">
						<label class="form-control-label">Internal CT Ratio</label>
						<form:input path="internalCTRatio" type="text" />
					</div>
					<div class="has-error">
						<form:errors path="internalCTRatio" class="alert alert-danger" />
					</div>
				</div>

			</div>
			<div class="row">

				<div class="form-control col-md-6">
					<div class="form-control ">
						<label class="form-control-label">Internal PT Ratio</label>
						<form:input path="internalPTRatio" type="text" />
					</div>
					<div class="has-error">
						<form:errors path="internalPTRatio" class="alert alert-danger" />
					</div>
				</div>

			</div>
			<div class="row">
			
			<div class="form-actions">
				<c:choose>
					<c:when test="${edit}">
						<input type="button" value="Update" class="btn btn-primary btn-sm"
							onclick="submitform('updateMeter')" /> or <a href="<c:url value='/home' />">Cancel</a>
					</c:when>
					<c:otherwise>
						<input type="button" value="Save" onclick="submitform('addMeter')"
							class="btn btn-primary btn-sm" /> or <a
							href="<c:url value='/home' />">Cancel</a>
					</c:otherwise>
				</c:choose>
			</div>

			</div>
		</div>
	</form:form>
</body>
</html>