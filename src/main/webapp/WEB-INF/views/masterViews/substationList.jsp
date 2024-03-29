<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>

<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<script type="text/javascript">
	function getLocationMeterDetails(locationid, meterDiv) {
		$('#' + meterDiv).css('display', 'none');
		$.ajax({
			url : "getLocationMeterDetails",
			type : "GET",
			data : {
				locationid : locationid
			},
			success : function(response) {
				$('#myModalDiv').html(response);

				$('#myModal').modal('toggle');
				
			},
			error : function(data, status, er) {
				alert("Error" + data + er);
			}
		});

	}
</script>
<script type="text/javascript">
	function getMeterData(locationId, ssCode,meterDiv) {
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
	
function submitform() {
// 		document.forms['filterLocations'].action = 'substationMaster';
// 		document.forms['filterLocations'].submit();
		$.post("substationMaster", {
		circleSelected : $("#circleDD").val(),
		divisionSelected : $("#divisionDD").val(),
		substationSelected : $("#substationDD").val(),
		locationSelected : null
	}, viewList).done(function() {
		//alert( "Employee Added" );
	}).fail(function(data, status, er) {
		alert("Couldn't load location information !" + data + er);
	});

	}
	
	
function viewList(data, status) {
	 $('#tableContainer').html(data);
	$('#tableContainer').css('display', 'block');
}
</script>

<%-- <c:url value='/previewOilReport-${oilReport.id}' /> --%>


<title>Sub Station Master</title>
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

	<div class="container-fluid">
		<c:if test="${not empty success}">
			<div class="alert alert-success lead">${success}</div>
		</c:if>
		<span class="lead">Energy Meters Master Data</span>

		<form:form method="POST"  id="filterLocations">
			<%@include file="../filters/locationFilter.jsp"%>
			<input type="button" class="btn btn-info" onclick="submitform()"
				value="Load Meters">

		</form:form>

<div id="tableContainer">
</div>
	</div>


</body>
</html>