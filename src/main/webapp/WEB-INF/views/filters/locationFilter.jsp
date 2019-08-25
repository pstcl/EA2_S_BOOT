<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link
	href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/css/select2.min.css"
	rel="stylesheet" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/js/select2.min.js"></script>

<script type="text/javascript">
	function initLocations() {
		$('#circleDD').find('option').remove().end();
		$('#divisionDD').find('option').remove().end();
		$('#substationDD').find('option').remove().end();
		$('#locationDD').find('option').remove().end();
		$.post("getLocationsModel", {
			circleSelected : null,
			divisionSelected : null,
			substationSelected : null,
			locationSelected : null,
		}, populate).done(function() {
			//alert( "Employee Added" );
		}).fail(function(data, status, er) {
			alert("Couldn't load location information !" + data + er);
		});

	}

	function addEmpty() {
		addEmptyCircle();
		addEmptyDivision();
		addEmptySubstation();
		addEmptyLocation()
	}
	function addEmptyCircle() {
		$('#circleDD').append(
				$("<option></option>").attr("value", "").text("Select Circle"));

	}
	function addEmptyDivision() {

		$('#divisionDD').append(
				$("<option></option>").attr("value", "")
						.text("Select Division"));

	}
	function addEmptySubstation() {

		$('#substationDD').append(
				$("<option></option>").attr("value", "").text(
						"Select Sub-Station"));
	}

	function addEmptyLocation() {
		$('#locationDD').append(
				$("<option></option>").attr("value", "")
						.text("Select Location"));

	}
	function populate(data, status) {

		addEmpty();

		$.each(data.circleList, function(idx, obj) {
			//alert(obj.circleName);
			$('#circleDD').append(
					$("<option></option>").attr("value", obj.crCode).text(
							obj.circleName));
		});
		$.each(data.divisionList, function(idx, obj) {
			//alert(obj.divisionname);
			$('#divisionDD').append(
					$("<option></option>").attr("value", obj.divCode).text(
							obj.divisionname));
		});
		$.each(data.substationList, function(idx, obj) {
			//alert(obj.stationName);
			$('#substationDD').append(
					$("<option></option>").attr("value", obj.ssCode).text(
							obj.stationName));
		});

		$.each(data.locationList, function(idx, obj) {
			//alert(obj.stationName);
			$('#locationDD').append(
					$("<option></option>").attr("value", obj.locationId).text(
							obj.locationId));
		});
	}

	function submitCircle() {

		$('#divisionDD').find('option').remove().end();
		$('#substationDD').find('option').remove().end();
		$('#locationDD').find('option').remove().end();
		$.post("getLocationsModel", {
			circleSelected : $("#circleDD").val(),
			divisionSelected : null,
			substationSelected : null,
			locationSelected : null
		}, circleSelected).done(function() {
			//alert( "Employee Added" );
		}).fail(function(data, status, er) {
			alert("Couldn't load location information !" + data + er);
		});

	}
	function circleSelected(data, status) {

		addEmptyDivision();
		addEmptySubstation();
		addEmptyLocation();
		$.each(data.divisionList, function(idx, obj) {
			//alert(obj.divisionname);
			$('#divisionDD').append(
					$("<option></option>").attr("value", obj.divCode).text(
							obj.divisionname));
		});
		$.each(data.substationList, function(idx, obj) {
			//alert(obj.stationName);
			$('#substationDD').append(
					$("<option></option>").attr("value", obj.ssCode).text(
							obj.stationName));
		});
		$.each(data.locationList, function(idx, obj) {
			//alert(obj.stationName);
			console.log(obj.locationId);
			$('#locationDD').append(
					$("<option></option>").attr("value", obj.locationId).text(
							obj.locationId));
		});

	}

	function submitDivision() {

		$('#substationDD').find('option').remove().end();
		$('#locationDD').find('option').remove().end();
		$.post("getLocationsModel", {
			circleSelected : $("#circleDD").val(),
			divisionSelected : $("#divisionDD").val(),
			substationSelected : null,
			locationSelected : null
		}, divSelected).done(function() {
			//alert( "Employee Added" );
		}).fail(function(data, status, er) {
			alert("Couldn't load location information !" + data + er);
		});

	}
	function divSelected(data, status) {

		addEmptySubstation();
		addEmptyLocation();
		$.each(data.substationList, function(idx, obj) {
			//alert(obj.stationName);
			$('#substationDD').append(
					$("<option></option>").attr("value", obj.ssCode).text(
							obj.stationName));
		});
		$.each(data.locationList, function(idx, obj) {
			//alert(obj.stationName);
			$('#locationDD').append(
					$("<option></option>").attr("value", obj.locationId).text(
							obj.locationId));
		});

	}

	function submitSS() {

		$('#locationDD').find('option').remove().end();
		$.post("getLocationsModel", {
			circleSelected : $("#circleDD").val(),
			divisionSelected : $("#divisionDD").val(),
			substationSelected : $("#substationDD").val(),
			locationSelected : null
		}, ssSelected).done(function() {
			//alert( "Employee Added" );
		}).fail(function(data, status, er) {
			alert("Couldn't load location information !" + data + er);
		});

	}
	function ssSelected(data, status) {

		addEmptyLocation();
		$.each(data.locationList, function(idx, obj) {
			//alert(obj.stationName);
			$('#locationDD').append(
					$("<option></option>").attr("value", obj.locationId).text(
							obj.locationId));
		});

	}
</script>
<script>
	$(document).ready(function() {
		initLocations();
		$('.js-example-basic-single').select2();
	});
</script>

<div class="row">

	<div class="form-control col-md-4">


		Circle <select id="circleDD"
			class="js-example-basic-single form-control input-sm"
			onchange="submitCircle()">

		</select>



	</div>
	<div class="form-control col-md-4">

		Division <select id="divisionDD"
			class="js-example-basic-single form-control input-sm"
			onchange="submitDivision()">
		</select>

	</div>


	<div class="form-control col-md-4">

		Substation <select id="substationDD"
			class="js-example-basic-single form-control input-sm"
			onchange="submitSS()">
		</select>

	</div>
</div>
