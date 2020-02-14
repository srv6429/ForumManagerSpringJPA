<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"  />	
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/users.css"/>"  />	
		<script type="text/javascript">

			var contextPath = "${pageContext.request.contextPath}";

		</script>
	</head>
	<body>

		<c:set var="session" value="${pageContext.request.session}" />
		<c:set var="errorMessage" value="${request.getAttribute(\"errorMessage\")}" />	
		
		<c:if test="${user == null || user.username == null}">
			<c:set var="user" value="${session.getAttribute(\"user\")}" />			
		</c:if>			
		<%@include file="menu.jsp" %>

		<div id="main_container">
			<h2>Liste des utilisateurs</h2>
						
			<table id="users_table">
				<tr>
					<th>
						<div class="user_header" id="user_header">
							<span class="user_id">Id</span>
							<span class="user_name">Nom</span>
							<span class="user_email">Courriel</span>
							<span class="user_creation_date">Date de création</span>
							<span class="user_update_date">Dernière mise à jour</span>
							<span class="user_role">Rôle</span>
							<span class="user_active">Actif</span>
							<span class="user_editor_button"></span>
						</div>
					</th>
				</tr>
				<c:forEach items="${userList}" var="userD">
				<tr>
					<td>
						<div class="user_wrapper" id="user_wrapper">
							<div class="topic">
								<span class="user_id"><c:out value="${userD.id}" /></span>
								<span class="user_name">${userD.username}</span>
								<span class="user_email">${userD.email}</span>
								<span class="user_creation_date"><c:out value="${userD.creationDate}" /></span>
								<span class="user_update_date"><c:out value="${userD.updateDate}" /></span>
								<span class="user_role"><c:out value="${userD.role}" /> </span>
								<span class="user_active">${userD.isActive}</span>
								<c:if test="${userD.username != \"admin\"}">
								<span class="button_wrapper right" id="default_button_wrapper_${userD.id}">
									<button class="edit_user_button" id="d_${userD.id}">Activer/Désactiver</button>
								</span>
	
								</c:if>
							</div>							
						</div>
					</td>					
				</tr>
				</c:forEach>
			</table>
		</div>
		<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-3.4.1.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/main.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/users.js" />" ></script>	
	</body>
</html>