
<div class="topnavbar" id="topnavbar">
		<c:choose>
			<c:when test="${user != null && user.username != null}"> 
				<!-- Menu pour un utilisateur connecté -->
				<span class="button left" id="topnavbutton_welcome" title="Bienvenue" >Bienvenue ${user.username}</span>
				<a class="button right" id="topnavbutton_logout" title="Déconnexion" href="${pageContext.request.contextPath}/logout">Déconnexion</a>
				<c:if test="${user.role == \"A\"}">
				<!-- Option pour une administrateur -->
					<a class="button right" id="topnavbutton_discussions" title="Utilisateurs" href="${pageContext.request.contextPath}/users">Utilisateurs</a>
				</c:if>
				<a class="button right" id="topnavbutton_profile" title="Profil" href="${pageContext.request.contextPath}/profile">Profil</a>
				<a class="button right" id="topnavbutton_discussions" title="Discussions" href="${pageContext.request.contextPath}/topics">Discussions</a>	
			</c:when>
			<c:otherwise>
				<!-- Menu pour un utilisateur non connecté -->
               	<a class="button right" id="topnavbutton_login" title="Connexion" href="${pageContext.request.contextPath}/login">Connexion</a>	
               	<a class="button right" id="topnavbutton_register" title="Inscription" href="${pageContext.request.contextPath}/register">Inscription</a>		
			</c:otherwise>
		</c:choose>
</div>