<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="resources/css/style.css"/>"  />
		<script type="text/javascript" src="<c:url value="resources/scripts/jquery-3.4.1.min.js" />" ></script>	
		
		<style>
			.error{
				color: #cc0000;
				font-style: italic;
			}
		</style>
		
		<script type="text/javascript">
		//	jQuery(function(){
		//		alert("ready");
		//	});
		</script>
	</head>
	<body>

	<%@include file="menu.jsp" %>
				
	    <div class="main_container login_container">
	    
	    	<c:if test="${user.messageExists()}">
				<div class="error"><c:out value="${user.message}" /></div>
			</c:if>

			<h3>Login</h3>
			<div>Veuillez vous connecter ou <a href="${pageContext.request.contextPath}/register">vous enregistrer</a></div>
			<form id="login_form" method="post">
				<div>
					<input type="text" id="username" name="username" required="required" value=""/>	
					<label class="label" id="pseudo_label" for="username">Pseudonyme ou courriel</label>
				</div>
				<div>
					<input type="password" id="password" name="password" value="" />
					<label class="label" for="password">Mot de passe</label>
				</div> 				
				<div>
					<input id="submit" type="submit" value="Connecter" />
				</div>
			</form>
		</div>
		
	</body>
</html>