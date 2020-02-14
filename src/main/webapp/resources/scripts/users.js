


jQuery(function(){

	//alert("ready in user.js");

	//Clic sur le bouton "Activer/Désactiver"
	jQuery(".edit_user_button").click(function(e){

		//Empêcher la soumission du formualire
		e.preventDefault();

		//récupérer l'identifiant de l'utilisateur sélectionné
		var userId = this.id.substr(2);
		//var topicId = jQuery(".post_topic_id").val();

		console.log("click toggle user id: " + userId);	

		//var params = "?postId="+postId+"&userId="+userId;
		
		
		var url =  contextPath+"/toggleUser/"+ userId;

		var method = "put";
		var errorMessage = "Une erreur est survenue lors de la tentative de mise à jour.";

		sendAJAXRequest(url, method, errorMessage);
		
	});
});