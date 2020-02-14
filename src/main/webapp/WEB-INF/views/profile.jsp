<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Profile</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"  />	
		
		<script type="text/javascript">

			var contextPath = "${pageContext.request.contextPath}";
			var userId =  "${user.id}";
			
			console.log("context path: " + contextPath);
			console.log("userId: " + userId);

		</script>
	</head>
	<body>
    	<c:set var="session" value="${pageContext.request.session}"/>			
		<c:if test="${user == null || user.username == null}">
			<c:set var="user" value="${session.getAttribute(\"user\")}" />			
		</c:if>	
		
			
		<%@include file="menu.jsp" %>
	
    	<div class="main_container message_container">


			<h2>Profil</h2>

			<div id="header">Modifier votre profil. Veuillez compl&eacute;ter tous les champs</div>
			<form id="profile_form" method="post">
				<div>
					<input type="text" id="username" name="username" maxlength="12" value="${user.username}" />
					<label id="pseudo_label" for="username">Pseudonyme: </label>
				</div>
				<div>
					<input type="email" id="email" name="email" maxlength="20" value="${user.email}" />
					<label for="email">Courriel: </label>
				</div>
				<div>
					<input type="password" id="password" name="password" maxlength="12" />
					<label for="password">Mot de passe: </label>
				</div> 
				<div>
					<input id="submit" type="submit" value="Enregistrer" />
				</div>
				<input type="hidden" name="op" value="update"/>
			</form>
		</div>
		<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-3.4.1.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/main.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/profile.js" />" ></script>	
	</body>
</html>