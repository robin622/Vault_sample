window.query = {
	qry_save : function(){
		var queryName = document.getElementById("queryName").value;
		if(queryName == ""){
			alert("Please enter Query Name.");
			return;
		}
		var requestName = $('#search_reqName').val();
		var creator = $('#search_creator').val();
		var status = $('#search_status').val() == "" ? "-1" : $('#search_status').val();
		var productId = $('#search_productid').val() == "" ? "-1" : $('#search_productid').val();
		var versionId = $('#search_versionid').val() == "" ? "-1" : $('#search_versionid').val();
		var owner = $('#search_signoff').val();
		var url = "SavequeryServlet";
		$.ajax({
			type: "POST",
			url: url,
			data: "operation=verifyQueryName&queryName="+queryName,
			dataType:'json',
			success: function(rtnData) {
				var queryExist = rtnData[0].queryExist;
				if(queryExist) {
					alert("The queryName has already exist.Please enter another one");
					return;
				}else {
					//save process
					$.ajax({
						type : "POST",
						url : url,
						data : "operation=saveQuery&queryName="+queryName+"&requestName="+requestName+"&creator="+creator
							+ "&owner=" + owner + "&status=" + status + "&productId="+productId+"&versionId="+versionId,
						success : function(rtnData){
							$("#myModal").modal("hide");
							$("#queryName").val("");
							query.qry_getUserQuery();
						}
					});
				}
			}
		});
	},
	
	qry_del : function(squery){
		var r=confirm("Are you sure delete " + squery.queryName + " ???");
		if (r==true){
			var url = "SavequeryServlet";
			$.ajax({
				type: "POST",
				url: url,
				data: "operation=delQuery&queryId=" + squery.queryId,
				dataType:'json',
				success: function(rtnData) {
					alert("Delete " + squery.queryName + " success!!");
					query.qry_getUserQuery();
				}
			});
		}
	},
	
	qry_getUserQuery : function(){
		var url = "SavequeryServlet";
		$.ajax({
			type: "POST",
			url: url,
			data: "operation=getUserQuery",
			dataType:'json',
			success: function(rtnData) {
				$('#query-group').html('');
				var content = '<div class="controls search-vault"><input type="text" class="input-xlarge" id="simple_reqName">   <button type="submit" class="btn" onclick="query.qry_search()">Search</button>';
				$.each(rtnData,function(index,item){
					if(index > 4){
						return;
					}
					content = content + '<a id=query_' + item.queryId + ' href=javascript:query.qry_setQuery(' + escape(JSON.stringify(item)) + ') class=quary>' 
							+ item.queryName + '</a><a href=javascript:query.qry_del(' + escape(JSON.stringify(item)) + ') class=quary-img><img src=images/delete.gif  alt=""></a>';
				});
				if(rtnData.length > 5){
					content = content + '<span class="quary-more"  id="quary-js">';
					for(var i = 5;i<rtnData.length;i++){
						content = content + '<a id=query_' + rtnData[i].queryId + ' href=javascript:query.qry_setQuery(' + escape(JSON.stringify(rtnData[i])) + ') class="quary">' 
								+ rtnData[i].queryName + '</a><a href=javascript:query.qry_del(' + escape(JSON.stringify(rtnData[i])) + ') class="quary-img"><img src="images/delete.gif"  alt=""></a>';
					}
					content = content + '</span><a href="#" class="quary" id="more">more...</a><a href="#" class="quary display-none" id="hide">hide...</a>';
				}
				content = content + '</div>';
				$('#query-group').html(content);
				
				$("#more").click(function(){
					 $("#quary-js").show();
					 $("#more").hide();
					 $("#hide").show();
				});
				$("#hide").click(function(){
					 $("#quary-js").hide();
					 $("#more").show();
					 $("#hide").hide();
				});
			}
		});
	},
	
	qry_initProduct : function(){
		$("#search_selectPro").append("<option value='-1'>------------</option>");
		var url = "SavequeryServlet";
		$.ajax({
			type: "POST",
			url: url,
			data: "operation=getAllProduct",
			dataType:'json',
			success: function(rtnData) {
				$.each(rtnData,function(index,item){
					$("#search_selectPro").append("<option value='" + item.productId + "'>"+ item.productName +"</option>");
				});
				
				$('#search_selectPro').change(function(){ 
					$("#search_selectVersion").html('');
					$("#search_selectVersion").append("<option value='-1'>-----------</option>");
					jQuery('option:selected', this).each(function(){ 
						$('#search_productid').attr('value',this.value);
					});
					var selectproductid = $('#search_productid').attr('value');
					if(selectproductid != '-1'){
						query.qry_initVersion(selectproductid);
					};
				});
				
				$('#search_selectVersion').change(function(){
					jQuery('option:selected', this).each(function(){ 
						$('#search_versionid').attr('value',this.value);
					});
				});
			}
		});
	},
	/**
	 * get versions of the product by id , if the param versionId is not null , make the option whose value is versionId selected
	 * @param productId
	 * @param versionId
	 */
	qry_initVersion : function(productId,versionId){
		var url = "SavequeryServlet";
		$.ajax({
			type: 'POST',
			url: url,
			data: "operation=getProdVersion&productId="+productId,
			dataType:'json',
			success: function(rtnData) {
				$.each(rtnData,function(index,item){
					$("#search_selectVersion").append("<option value='" + item.versionId + "'>" + item.versionValue + "</option>");
				});
				if(versionId){
					$("#search_selectVersion option[value=" + versionId + "]").attr("selected", true);
				}
			}
		});
	},
	
	qry_initStatus : function(){
		$("#search_selectStatus").append("<option value='-1'>------------</option>");
		$("#search_selectStatus").append("<option value='Approved'>Approved</option>");
		$("#search_selectStatus").append("<option value='In progress'>In progress</option>");
		
		$('#search_selectStatus').change(function(){ 
			 jQuery('option:selected', this).each(function(){ 
				 $('#search_status').val(this.value);
			 });
		});
		
	},
	/**
	 * fill the query form when selecting the specific query
	 * @param squery
	 */
	qry_setQuery : function(squery){
		query.qry_clearQuery();
		$('#search_reqName').val(squery.searchName);
		$('#search_creator').val(squery.creator);
		$('#search_signoff').val(squery.owner);
		$('#search_status').val(squery.status);
		$('#search_productid').val(squery.productId);
		$('#search_versionid').val(squery.versionId);
		$("#search_selectStatus option[value=" + squery.status + "]").attr("selected", true);
		$("#search_selectPro option[value=" + squery.productId + "]").attr("selected", true);
		query.qry_initVersion(squery.productId,squery.versionId);
	},
	
	qry_clearQuery : function(){
		$('#search_reqName').val('');
		$('#search_creator').val('');
		$('#search_status').val('');
		$('#search_productid').val('');
		$('#search_versionid').val('');
		$('#search_signoff').val('');
		$("#search_selectVersion").html('');
		$("#search_selectVersion").append("<option value='-1'>-----------</option>");
		$("#search_selectStatus option[value=-1]").attr("selected", true);
		$("#search_selectPro option[value=-1]").attr("selected", true);
		$("#search_selectVersion option[value=-1]").attr("selected", true);
	},
	
	qry_advanceSearch : function(){
		var requestName = $('#search_reqName').val();
    	var creator = $('#search_creator').val();
    	var versionid = $('#search_versionid').val() == "" ? "-1" : $('#search_versionid').val();
    	var status = $('#search_status').val() == "" ? "-1" : $('#search_status').val();
    	var owneremail = $('#search_signoff').val();
    	var productid = $('#search_productid').val() == "" ? "-1" : $('#search_productid').val();
    	var action = "listRequest?operation=Search&requestName="+requestName +"&creator=" + creator + "&versionid=" + versionid+ "&productid=" + productid+ "&status="+status+"&owneremail="+owneremail;
    	window.location.href = action;
	},
	
	qry_search : function(){
		var requestName = $('#simple_reqName').val();
		if(requestName == ""){
			alert("Please enter Search Keyword.");
			return;
		}
    	var action = "listRequest?operation=Search&requestName="+requestName;
    	window.location.href = action;
	}
};
// init
query.qry_clearQuery();
query.qry_getUserQuery();
query.qry_initProduct();
query.qry_initStatus();

