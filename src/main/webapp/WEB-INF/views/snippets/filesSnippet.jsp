<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<table class="table">
	<tr>
		<th>File Name</th>
		<th>File Generation Date Time</th>
		<th>Date of Uploading</th>
		<th>No. of Daily(24 hr) Records</th>
		<th>No. of Load Survey(15 min) Records in File</th>
		<th>Uploaded By</th>
		<th>Approved By</th>
		<th>ProcessingStatus</th>
		<th>Action</th>


	</tr>
	<c:forEach items="${locDetails.fileMasters}" var="fileDetails"
		varStatus="indexStatus1">
		<%@include file="../substationHomeFileToolkit.jsp"%>
	</c:forEach>
</table>