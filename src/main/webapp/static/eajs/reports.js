/**
 * 
 */
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