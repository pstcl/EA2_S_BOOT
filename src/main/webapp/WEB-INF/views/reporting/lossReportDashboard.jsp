
<%@page import="org.pstcl.ea.util.EAUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>



<script type="text/javascript">
	
	
	function applyFilters() {
		showProgressBar();

		$('#lossReportDiv').css('display', 'none');
		$('#loader').css('display', 'block');

		if ($("#reportDate").val() == "") {
			var newdate = new Date();
			newdate.setDate(new Date().getDate() - 15); // minus the date
			document.getElementById("reportDate").value = newdate.getFullYear()
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
			"reportMonth" : month,
			"reportYear" : year,
			"reportType" : $('input[name=reportType]:checked').val()
		};
		return reportParamsModel;
			
		}
	

	function post(path, params, method='post') {

		  // The rest of this code assumes you are not using a library.
		  // It can be made less wordy if you use one.
		  const form = document.createElement('form');
		  form.method = method;
		  form.action = path;

		  for (const key in params) {
		    if (params.hasOwnProperty(key)) {
		      const hiddenField = document.createElement('input');
		      hiddenField.type = 'hidden';
		      hiddenField.name = key;
		      hiddenField.value = params[key];

		      form.appendChild(hiddenField);
		    }
		  }

		  document.body.appendChild(form);
		  form.submit();
		}

	function downloadExlReport()
	{

		var reportParamsModel = applyFilters();
		post("lossReportExl",reportParamsModel);
		
		var millisecondsToWait = 2000;
		setTimeout(function() {
			hideProgressBar();
		}, millisecondsToWait);
// 	$.ajax({
// 			url : "lossReportExl",
// 			type : "POST",
// 			data : JSON.stringify(reportParamsModel),
// 			contentType : 'application/json',
// 			success : function(response) {
// 				$("#lossReportDiv").html(response);
// 				initDataTables();
// 				$('#lossReportDiv').css('display', 'block');
// 				hideProgressBar();
				
// 				$('#loader').css('display', 'none');

// 			},
// 			error : function(data, status, er) {
// 				alert("OBJECT Error" + data + er);
// 			}
// 		});

	}
		
	function loadReport()
	{
		var reportParamsModel = applyFilters();
		
	$.ajax({
			url : "lossReportDyn",
			type : "POST",
			data : JSON.stringify(reportParamsModel),
			contentType : 'application/json',
			success : function(response) {
				$("#lossReportDiv").html(response);
				initDataTables();
				$('#lossReportDiv').css('display', 'block');
				hideProgressBar();
				
				$('#loader').css('display', 'none');

			},
			error : function(data, status, er) {
				alert("OBJECT Error" + data + er);
			}
		});

	}

	function populateTableContainer(data, status) {
		$("#lossReportDiv").html(data);
	}
</script>

<style>
.btn-primary:not (:disabled ):not (.disabled ).active, .btn-primary:not
	 (:disabled ):not (.disabled ):active, .show>.btn-primary.dropdown-toggle
	{
	color: #fff;
	background-color: #6d2814;
	border-color: #add02e;
}

.btn-primary {
	color: #fff;
	background-color: #007bff;
	border-color: #20f116;
}

.form-group {
	padding: 15px 15px 15px 15px;
}
</style>


</head>
<body>
	<%@include file="../authheader.jsp"%>
	<%@include file="../dataTablesHeader.jsp"%>
	<%@include file="../html/customProgressBarModal.html"%>
	<script type="text/javascript">
		$(document).ready(function() {
			initDataTables();
			myFunction();
			setDefaultMonth();
			$('#loadReportButton').click();
		});
		
		function initDataTables()
		{
			$('#dailyTxnTable').DataTable({
				dom : 'Bfrtip',
				"paging" : false,
				"ordering" : false,
				buttons : [ 'copyHtml5', 'excelHtml5', 'csvHtml5', {
					extend : 'pdfHtml5',
					orientation : 'landscape',
					pageSize : 'LEGAL'
				} ],
				orientation : 'landscape',
				pageSize : 'A4'
			});
		}
		function setDefaultMonth()
		{
			var newdate = new Date();
			newdate.setDate(new Date().getDate() - 37); // minus the date
			document.getElementById("reportDate").value = newdate.getFullYear()
					+ "-" + ("0" + (newdate.getMonth() + 1)).slice(-2);
		
		}
	</script>
	<script type="text/javascript">
		$(document).ready(
				function() {
					$('div .checkbox').click(
							function() {
								checkedState = $(this).attr('checked');
								$(this).parent('div').children(
										'.checkbox:checked').each(function() {
									$(this).attr('checked', false);
								});
								$(this).attr('checked', checkedState);
							});
				});
	</script>
	<script type="text/javascript">
		var customModal;
		customModal = customModal
				|| (function() {
					var pleaseWaitDiv = $('<div class="modal hide" id="pleaseWaitDialog" data-backdrop="static" data-keyboard="false"><div class="modal-header"><h1>Processing...</h1></div><div class="modal-body"><div class="progress progress-striped active"><div class="bar" style="width: 100%;"></div></div></div></div>');
					return {
						showPleaseWait : function() {
							pleaseWaitDiv.modal();
						},
						hidePleaseWait : function() {
							pleaseWaitDiv.modal('hide');
						},

					};
				})();
	</script>
	<div>

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
						checked="checked">Consolidated Loss Report
					</label> <label class="btn btn-primary"> <input type="radio"
						name="reportType" id="option3"
						value="<%=EAUtil.LOSS_REPORT_CRITERIA_G_T%>" autocomplete="off">
						(G-T)
					</label> <label class="btn btn-primary"> <input type="radio"
						name="reportType" id="option3"
						value="<%=EAUtil.LOSS_REPORT_CRITERIA_I_T_%>" autocomplete="off">
						(I-T)
					</label> <label class="btn btn-primary"> <input type="radio"
						name="reportType" id="option3"
						value="<%=EAUtil.LOSS_REPORT_CRITERIA_T_D_220_66_%>"
						autocomplete="off"> (T-D)(220/66)
					</label> <label class="btn btn-primary"> <input type="radio"
						name="reportType" id="option3"
						value="<%=EAUtil.LOSS_REPORT_CRITERIA_T_D_132_11_%>"
						autocomplete="off"> (T-D)(132/11)
					</label> <label class="btn btn-primary"> <input type="radio"
						name="reportType" id="option3"
						value="<%=EAUtil.LOSS_REPORT_CRITERIA_T_D_132_33_%>"
						autocomplete="off"> (T-D)(132/33)
					</label> <label class="btn btn-primary"> <input type="radio"
						name="reportType" id="option3"
						value="<%=EAUtil.LOSS_REPORT_CRITERIA_T_D_132_66%>"
						autocomplete="off"> (T-D)(132/66)
					</label> <label class="btn btn-primary"> <input type="radio"
						name="reportType" id="option3"
						value="<%=EAUtil.LOSS_REPORT_CRITERIA_INDEPENDENT_%>"
						autocomplete="off"> (T-D)(Independent)
					</label>


					<p id="demo"></p>
				</div>
			</div>


			<div class="float-right">
				<input type="button" onclick="loadReport()" id="loadReportButton"
					value="Load Report" class="btn btn-primary btn-sm" /> <input
					type="button" onclick="downloadExlReport()"
					value="Download as Excel" class="btn btn-primary btn-sm" /> <a
					class="btn btn-light btn-sm float-right"
					href="javascript:window.location='lossReportDyn'"><span
					class="glyphicon glyphicon-plus"></span>Clear</a>
			</div>

		</div>

		<div id="lossReportDiv"></div>
	</div>
</body>
</html>