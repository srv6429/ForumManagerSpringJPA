
/**
 *
 *
 * Fonctions javascript utilisées pour répondre aux actions prises sur la page posts.jsp
 * Envoient des requêtes ajax au moyens de méthodes: get, post, put et delete
 * 
 */



jQuery(function(){

	//alert("ready in posts.js");
	//récupérer le context
	//var contextPath = "${pageContext.request.contextPath}";

	//console.log("contextPath: " + contextPath);

	
	//récupérer l'idenitifiant du topic
	//var topicId = "${topicId}";
	//récupérer l'idenitifiant de l'utilisateur courant
	//var userId = "${user.id}";

	//console.log("topicId: " + topicId + "; userId: " + userId);

	//clic sur le bouton "Modifier"
	jQuery(".update_post_button").click(function(e){
		//on ne soumet par le formulaire
		e.preventDefault();

		//on récupère l'idenitifiant du post sélectionné
		var postId = this.id.substr(2);

		console.log("click update post id: " + postId);

		//on remplace le contenu du post par le formulaire d'éition
		
		//rednre invisible l'affichage du commentaire
		jQuery("#post_body_wrapper_"+postId).removeClass("visible");
		jQuery("#post_body_wrapper_"+postId).addClass("hidden");

		//rendre invisible le conteneur de boutons 
		jQuery("#default_button_wrapper_"+postId).removeClass("visible")
		jQuery("#default_button_wrapper_"+postId).addClass("hidden");

		//on affiche le formulaire d'édtition
		jQuery("#update_post_editor_"+postId).removeClass("hidden");
		jQuery("#update_post_editor_"+postId).addClass("visible");
	//	jQuery("#update_button_wrapper_"+postId).removeClass("hidden");
	//	jQuery("#update_button_wrapper_"+postId).addClass("visible");
//
		//désactiver le bouton "Ajouter" en haut à droite 
		jQuery("#add_post").attr("disabled","disabled");

		

	});


	//Clic sur le boton "Annuler"
	jQuery(".cancel_update_button").click(function(e){

		//empêcher la soumission du formulaire
		e.preventDefault();

		//récupérer l'identifiant du post sélectionné
		var postId = this.id.substr(2); //getting the post id

		console.log("click cancel post id: " + postId);
		
		//rendre les boutons visibles de mnuveau
		jQuery("#post_body_wrapper_"+postId).removeClass("hidden");
		jQuery("#post_body_wrapper_"+postId).addClass("visible");
		jQuery("#default_button_wrapper_"+postId).removeClass("hidden");
		jQuery("#default_button_wrapper_"+postId).addClass("visible");

		//cacher le formulaire d'édition du post
		jQuery("#update_post_editor_"+postId).removeClass("visible");
		jQuery("#update_post_editor_"+postId).addClass("hidden");

		//réactiver le bout "Ajouter"
		jQuery("#add_post").removeAttr("disabled");
	});

	//Clic sur "Enregistrer" les modifications apportées au post
	jQuery(".send_update_button").click(function(e){

		//empêcher la soumission du formulaire
		e.preventDefault();
		
		//récupérer l'identifiant du post
		var postId = this.id.substr(2); //getting the post id
		
		console.log("postId: " + postId);

		//récupérer le titre et le commentaires modifié(s) 
		var postTitle =  jQuery("#update_post_title_"+postId).val();
		var postBody =  jQuery("#update_post_text_"+postId).val();

		//remplacer les retours de ligne par '<br />'
		var postTitleEncoded =  postTitle.replace(/\n/g, "<br />");

		//encoder le titre i.e. caracètres spéciaux pour les passer dans une url
		postTitleEncoded = encodeURIComponent(postTitleEncoded);
		console.log("postTitleEncoded: " + postTitleEncoded);

		//encoder le commentaire i.e. caracètres spéciaux pour les passer dans une url
		var postBodyEncoded =  postBody.replace(/\n/g, "<br />");
		postBodyEncoded = encodeURIComponent(postBodyEncoded);
		console.log(postBodyEncoded);
		
		console.log("encoded title: " + postTitleEncoded);
		console.log("encoded body: " + postBodyEncoded);

		//var params = "?postId="+postId+"&postTitle="+postTitleEncoded+"&postBody="+postBodyEncoded;

		var url = contextPath+"/posts/updatePost/"+postId+"/"+postTitleEncoded+"/"+postBodyEncoded;
			
		var method = "put";
		var errorMessage = "Une erreur est survenue lors de la tentative de mise à jour.";

		//envoi de la requête par AJAX
		sendAJAXRequest(url, method, errorMessage);
		
						
	});

	//Clic sur "Retirer" un post
	jQuery(".delete_post_button").click(function(e){

		//empêcher la soumission du formulaire
		e.preventDefault();


		var response = confirm("Désirez-vous vraiment retirer ce commentaire?");
		if(response == false){
			return;	
		}
		

		//récupérer l'identifiant du post sélectionné
		var postId = this.id.substr(2);
		//var topicId = jQuery(".post_topic_id").val();

		console.log("click delete post id: " + postId + "; topic: " + topicId);	

		//var params = "?postId="+postId;

		var url = contextPath+"/posts/deletePost/"+postId;
			
		var method = "delete";
		var errorMessage = "Une erreur est survenue lors de la tentative de mise à jour.";

		//envoi de la requête par AJAX
		sendAJAXRequest(url, method, errorMessage);
								
	});

 
 //*****************************************************************************
	//Clic sur bout "Ajouter" un nouveau post
	jQuery("#add_post").click(function(){

		//var topicId = jQuery(".post_form_topic").val();
		
		console.log("click add post: topicId: " + topicId);
	//	jQuery("#new_post_form").removeClass("hidden");
	//	jQuery("#new_post_form").addClass("visible");
	
		//afficher le formulaire d'Ajout
		jQuery("#new_post_form").show();
		
		//désactiver le bout on d'ajout
		jQuery("#add_post").attr("disabled", "disabled");
	
	});

	//Clic sur le bouton "Annuler" ajout d'un nouveau post
	jQuery("#new_cancel").click(function(){

		//var topicId = jQuery(".post_form_topic").val();

		//console.log("click cancel topic id: " + topicId);
		
	//	jQuery("#new_post_form").removeClass("visible");
	//	jQuery("#new_post_form").addClass("hidden");
	
		//Cacher le formulaire d'ajout
		jQuery("#new_post_form").hide();
		//réactiver le bouton "Ajouter"
		jQuery("#add_post").removeAttr("disabled");
	});

	//Clic sur le bouton "Enregistrer" le nouveua Post
	jQuery("#new_send").click(function(e){

		//empĉher la soumission du formulaire
		e.preventDefault();
	//	var topicId = jQuery(".post_form_topic").val();
	//	var userId = jQuery(".post_form_user_id").val();

		console.log("click new send topic id: " + topicId + "; userId: " + userId);


		//récupérer le titre et le commentaire
		var  postTitle = jQuery("#new_post_title").val();
		var  postBody = jQuery("#new_post_text").val();

		//vérifier que les chaînes ne sont pas vides
		if((!postTitle || postTitle.length == 0  || !postBody || postBody.length == 0)){
			var error = jQuery("#error_send_new");
			
			if(error.length == 0){
				jQuery("#new_post_form").prepend(jQuery("<div>").attr("id", "error_send_new").addClass("error").css({"font-size": "11pt", "margin":"0px 10px"}).html("Le titre et/ou le texte ne peuvent être vides"));
			}
		} else{
			jQuery("#error_send_new").remove();

			//var params = "postTitle="+postTitle+"&postBody="+postBody+"&topicId="+topicId+"&userId="+userId;
			


			console.log("title: " + postTitle + "; body: " + postBody)

	//		var postTitle =  jQuery("#update_post_title_"+postId).val();
	//		var postBody =  jQuery("#update_post_text_"+postId).val();

			//encoder le titre et le commentaire pour la transmission dans une url
			var postTitleEncoded =  postTitle.replace(/\n/g, "<br />");
			postTitleEncoded = encodeURIComponent(postTitleEncoded);
			console.log("postTitleEncoded: " + postTitleEncoded);
			
			var postBodyEncoded =  postBody.replace(/\n/g, "<br />");
			postBodyEncoded = encodeURIComponent(postBodyEncoded);
			console.log(postBodyEncoded);
			
			console.log("encoded title: " + postTitleEncoded);
			console.log("encoded body: " + postBodyEncoded);


			var url = contextPath+"/posts/addPost/"+postTitleEncoded+"/"+postBodyEncoded+"/"+topicId+"/"+userId;
			
			var method = "post";
			var errorMessage = "Une erreur est survenue lors de la tentative d'ajout du nouveau post.";
			
			sendAJAXRequest(url, method, errorMessage);

		}
	});


});	