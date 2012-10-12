<div id="query-area">
		<input name="searchproductid" id="search_productid" type="hidden" value="-1"/>
		<input name="searchversionid" id="search_versionid" type="hidden" value="-1"/>
		<input name="searchStatus" id="search_status" type="hidden" value="-1"/>
		<!-- <form class="form-horizontal form-horizontal-vault "> -->
            <div id="query-group" class="control-group">
               
            </div>
        <!-- </form> -->
        <div class="clear"></div>
	</div>
    <fieldset id="advance_set" class="fieldset-vault fieldset-search">
    <legend class="legend-vault img2" id="search1">Advanced Search </legend>
    <legend class="legend-vault img1 display-none" id="search2" >Advanced Search </legend>
    </fieldset>
	<div id="advance_query" class="form-area display-none">
    <div class="form-area-left">
        <form class="form-horizontal form-horizontal-vault">
            <div class="control-group">
                <label class="control-label" for="input01">Request Name:</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="search_reqName">
                </div>
            </div>
        </form>
         <form class="form-horizontal form-horizontal-vault">
            <div class="control-group">
                <label class="control-label" for="input01">Product:</label>
                <div class="controls">
                <select id="search_selectPro" data-placeholder="Your Favorite Football Team"  class="chzn-select" tabindex="6">
             
           </select>
                </div>
            </div>
        </form>
        <form class="form-horizontal form-horizontal-vault">
            <div class="control-group">
                <label class="control-label" for="input01">Creator:</label>
                  <div class="controls">
             
           <input type="text" class="input-xlarge" id="search_creator">
           </div>
            </div>
        </form>
       
    </div>
    <div class="form-area-right">
        <form class="form-horizontal">
              <div class="control-group">
                <label class="control-label">Status: </label>
                <div class="controls">
                <select id="search_selectStatus" data-placeholder="RHEL6"  class="chzn-select select-width" tabindex="6">
                </select>
                </div>
            </div>
            
            <div class="control-group">
                <label class="control-label" for="input01">Version:</label>
                <div class="controls">
                <select id="search_selectVersion" data-placeholder="Your Favorite Football Team"  class="chzn-select" tabindex="6">
           </select>
                </div>
            </div>
            
            <div class="control-group">
                <label class="control-label">Sign off by: </label>
                <div class="controls">
               <input type="text" class="input-xlarge" id="search_signoff">
                </div>
            </div>
          
        
        </form>
    </div>
    <div class="bottom">
    <button type="submit" class="btn btn-primary" onclick="query.qry_advanceSearch()">Search</button>
    <a class="btn" data-toggle="modal" href="#myModal" >Save Query</a>
  
	<div class="page-header">
    </div>
    <div class="row-fluid">
    	<div class="span8">
            <div class="modal hide fade" id="myModal">
                <div class="modal-header">
                  <button data-dismiss="modal" class="close">x</button>
                  <h3>Please input query name</h3>
                </div>
                <div class="modal-body">
                	<p><input type="text" class="input-xlarge" id="queryName"></p>
                </div>
                <div class="modal-footer">
                  <a data-dismiss="modal" class="btn" href="#">Close</a>
                  <a class="btn btn-primary" href="javascript:query.qry_save()">Save</a>
                </div>
          </div>
           
      </div>
     </div>
    </div>
    <div class="clear"></div>  
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/query.js"></script>
