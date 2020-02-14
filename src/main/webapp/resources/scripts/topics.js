
/**
 *
 *
 * Fonctions javascript utilisées pour répondre aux actions prises sur la page topics.jsp
 * Envoient des requêtes ajax au moyens de méthodes: get, post, put et delete
 * 
 */


jQuery(function(){
	//alert("ready in topic.js");

	//Clic sur le bouton "Modifier" un topic
	jQuery(".update_button").click(function(){

		//récupérer l'identifiant du topic sélectionné
		var topicId = this.id.substr(7);

		console.log("update_button click: topicId: " + topicId);

		//rendre invisibles les boutons d'édition
		jQuery("#default_button_wrapper_"+topicId).removeClass("visible")
		jQuery("#default_button_wrapper_"+topicId).addClass("hidden");


		//afficher le formulaire d'édition du topic
		jQuery("#update_topic_form_"+topicId).removeClass("hidden");
		jQuery("#update_topic_form_"+topicId).addClass("visible");
//				jQuery("#update_button_wrapper_"+topicId).removeClass("hidden");
//				jQuery("#update_button_wrapper_"+topicId).addClass("visible");

		//désactiver le bounton "Ajouter" un topic
		jQuery("#add_topic").attr("disabled","disabled");

	});


	//Clic sur le bouton "Annuler" la modification
	jQuery(".cancel_update").click(function(){

		//récupérer l'identifiant du topic
		var topicId = this.id.substr(7); //getting the post id

		console.log("click cancel post id: " + topicId);

		//cacher le formulaire d'édition
		jQuery("#update_topic_form_"+topicId).removeClass("visible");
		jQuery("#update_topic_form_"+topicId).addClass("hidden");
//			jQuery("#update_button_wrapper_"+topicId).removeClass("visible");
//			jQuery("#update_button_wrapper_"+topicId).addClass("hidden");

		//afficher les boutons d'édition
		jQuery("#default_button_wrapper_"+topicId).removeClass("hidden");
		jQuery("#default_button_wrapper_"+topicId).addClass("visible");

		//réactiver le bouton "Ajouter"
		jQuery("#add_topic").removeAttr("disabled");
	});

	//Clic sur le bouton "Envoyer" le topic modifié
	jQuery(".send_update").click(function(e){

		//empĉher la soumission du formulaire
		e.preventDefault();

		//récupérer l'identifiant du topic
		var topicId = this.id.substr(5); //getting the post id

		//récupérer le titre modifié
		var topicTitle =  jQuery("#update_topic_title_"+topicId).val();

		console.log("topicId id: " + topicId + "; topicTitle: " + topicTitle);


		//encoder le titre pour le transmettre dans l'url
		var topicTitleEncoded =  topicTitle.replace(/\n/, "<br />");
		topicTitleEncoded =  encodeURIComponent(topicTitleEncoded);

		console.log("title encoded: " + topicTitleEncoded);
		//var url = contextPath +"/topics/updateTopic/"+topicId + "/"+topicTitleEncoded;
		//console.log("url: " + url);

		var url = contextPath +"/topics/updateTopic/"+topicId + "/"+topicTitleEncoded;
		var method = "put";
		var errorMessage = "Une erreur est survenue lors de la tentative de mise à jour.";
		
		//envoi de la requête par AJAX
		sendAJAXRequest(url, method, errorMessage);

						
	});

	//Clis sur le bouton "Retirer" le topic
	jQuery(".delete_button").click(function(){


		var response = confirm("Désirez-vous vraiment retirer cette discussion?");

		if(response == false){
			return;	
		}
		
		//récupérer l'identifiant du topic
		var topicId = this.id.substr(7);

		console.log("click delete topic id: " + topicId );

		var url = contextPath+"/topics/deleteTopic?topicId="+ topicId;
		var method = "delete";
		var errorMessage = "Une erreur est survenue lors de la tentative de mise à jour.";
		
		//envoi de la requête par AJAX
		sendAJAXRequest(url, method, errorMessage);

		
	});


	//Clic sur "Ajouter" un topic
	jQuery("#add_topic").click(function(){

		
		console.log("click add topic ");
	//	jQuery("#new_post_form").removeClass("hidden");
	//	jQuery("#new_post_form").addClass("visible");
	
		//afficher le formulaire
		jQuery("#new_topic_form").show();

		//désactiver le bouton "Ajouter"
		jQuery("#add_topic").attr("disabled", "disabled");
		//document.location.href="#topic_wrapper";
	
	});

	//Clic sur "Annuler" ajout de topic
	jQuery("#new_cancel").click(function(){

		console.log("click cancel new topic");
		
	//	jQuery("#new_post_form").removeClass("visible");
	//	jQuery("#new_post_form").addClass("hidden");
	
		//Cacher le formulaire
		jQuery("#new_topic_form").hide();

		//réactiver le nouton "Ajouter"
		jQuery("#add_topic").removeAttr("disabled");
		
		document.location.href="#top";
	});


	//Clic sur "Envoyer" nouveau topic/post				
	jQuery("#new_send").click(function(){

		//var userId = jQuery("#new_topic_user_id").val();

		//récupérer les valeurs de schamps
		var topicTitle = jQuery("#new_topic_title").val();
		var postTitle = jQuery("#new_post_title").val();
		var postBody = jQuery("#new_post_text").val();

		console.log("click new send " +"; userId: " + userId + "; topicTitle: " + topicTitle + 
				"; postTitle: " + postTitle + "; postBody: " + postBody);
		
		//vérifier que les champs ne sont pas vides
		if((!userId || (!topicTitle || topicTitle.length == 0) || (!postTitle || postTitle.length == 0) || (!postBody || postBody.length == 0))){

			if(error.length == 0){
				jQuery("#new_topic_form").prepend(jQuery("<div>").attr("id", "error_send_new").addClass("error").css({"font-size": "11pt", "margin":"0px 10px"}).html("Le titre et/ou le texte ne peuvent être vides"));
			}
		} else {


			//encoder le titre du topic
			var topicTitleEncoded =  topicTitle.replace(/\n/, "<br />");
			topicTitleEncoded =  encodeURIComponent(topicTitleEncoded);
			console.log("topic title encoded: " + topicTitleEncoded);

			//encoder le titre du post
			var postTitleEncoded =  postTitle.replace(/\n/, "<br />");
			postTitleEncoded =  encodeURIComponent(postTitleEncoded);
			console.log("post title encoded: " + postTitleEncoded);

			//encoder le commentaire du post
			var postBodyEncoded =  postBody.replace(/\n/, "<br />");
			postBodyEncoded =  encodeURIComponent(postBodyEncoded);
			console.log("post body encoded: " + postBodyEncoded);
			
			jQuery("#error_send_new").remove();
			console.log("before sending request");


			var url = contextPath+"/topics/addTopic/"+topicTitleEncoded+"/"+postTitleEncoded+"/"+postBodyEncoded+"/"+userId;
			var method = "post";
			var errorMessage = "Une erreur est survenue lors de la tentative de création du topic.";
			
			//envoi de la requête par AJAX
			sendAJAXRequest(url, method, errorMessage);
		}
	});
	

	
});