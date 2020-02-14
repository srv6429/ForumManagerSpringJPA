

/**
 *
 *
 * Fonctions javascript utilisées pour répondre aux actions prises sur la page profile.jsp
 * Récupére les données du formulaire 
 * Envoie une requête ajax au moyens de la méthode put pour mettre à jour un profil 
 * 
 */

jQuery(function(){

	alert("ready in profile.js");
	
	jQuery("#profile_form").submit(function( event ) {

		event.preventDefault();

		var valid =  false;
		var message = "";
		var username = jQuery("#username").val();
		var email = jQuery("#email").val();
		var password = jQuery("#password").val();

		

		console.log("id: " + userId+ "; username: " + username + "; password: " + password + "; email: " + email);

		if(!username || username.length  < 4 || username.length > 15 ){
			message +=  "Le pseudonyme doit contenir entre 4 et 15 caract&egrave;res <br />";
		}
		if(!password || password.length  < 4 || password.length  > 15){
			message +=  "Le mot de passe doit contenir entre 4 et 15 caract&egrave;res <br />";
		}

//		console.log("message: " + message);
		//Afficher le message d'erreur
		if(message.length > 0){
			jQuery("#message_error").remove();
			jQuery("#header").after(jQuery("<div>").attr("id", "message_error").addClass("error").html(message));

			jQuery("#profile_form")[0].reset();

			jQuery("#username").html(username);
			jQuery("#email").html(email);
			jquery("#password").html("");

			
		} else{
			console.log("ok");
			//jQuery("#message").remove();
//						console.log(jQuery("#register").attr("action"));

			valid =  true;
		//	$("#register").submit(function(){
		//		return;
		//	});
		}
		console.log("valid ---------> " + valid);
		if(valid){	
//					    return;
			var messageDiv = jQuery("<div>").attr("id", "message");

			var params =  "/"+userId+"/"+username+"/"+email+"/"+password;

			console.log("params: " + params);

			var url =  contextPath+"/profile"+params;

			var method = "put";
			var errorMessage = "Un problème est survenu lors de la mise à jour de votre profil.";

						sendAJAXRequest(url, method, errorMessage);
 
		}
	});
});