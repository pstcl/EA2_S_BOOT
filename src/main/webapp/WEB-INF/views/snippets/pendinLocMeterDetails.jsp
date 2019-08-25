<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<table>
	<c:forEach items="${locDetails.meterLocationMaps}"
		var="meterLocationMapping" varStatus="indexStatus">


		<tr>
			<td>${meterLocationMapping.meterMaster.meterSrNo}</td>
			<td><fmt:formatDate value="${meterLocationMapping.startDate}"
					pattern="yyyy-MM-dd" /></td>
			<td><c:choose>
					<c:when test="${not empty meterLocationMapping.endDate }">
			to<fmt:formatDate value="${meterLocationMapping.endDate}"
							pattern="yyyy-MM-dd" />
					</c:when>
					<c:otherwise>
till date</c:otherwise>
				</c:choose></td>

		</tr>

	</c:forEach>
</table>