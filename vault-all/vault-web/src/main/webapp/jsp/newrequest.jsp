<form action="Saverequest" method="POST" name="newrequest_form">	
<!-- hidden fields -->
<input name="selectproductid" id="selectproductid" type="hidden" value="-1"/>
<input name="selectversionid" id="selectversionid" type="hidden" value="-1"/>
<input type="hidden" name="ownercount" id="ownercount" value="0"/>
<input type="hidden" name="maxownercount" id="maxownercount" value="0"/>
<input type="hidden" name="owner" id="owner" value=""/>
<input type="hidden" name="notifyOption" id="notifyOption" value=""/>
<input type="hidden" name="cccount" id="cccount" value="0"/>
<input type="hidden" name="maxcccount" id="maxcccount" value="0"/>
<input type="hidden" name="cc" id="cc" value=""/>
<input type="hidden" name="newrequestid" id="newrequestid" value=""/>
<input type="hidden" name="userName" id="userName"/>
<input type="hidden" name="maxchildcount" id="maxchildcount" value="0"/>
<input type="hidden" name="parentStr" id="parentStr" value=""/>
<input type="hidden" name="childrenStr" id="childrenStr" value=""/>
<input type="hidden" name="detailStr" id="detailStr" value=""/>

		<table class="eso-table new-request" id="newrequest_tbl">
  <thead>
  	<th colspan="2">Create Request</th>
<tbody>
    <tr>
    <td width="9%" ><span> Name:</span><span class="red">*</span></td>
    <td ><input type="text" class="input-xlarge" name="requestname" id="requestname"></td>
    </tr>
    <tr>
    <td >Product:</td>
    <td ><select id="request_selectPro" data-placeholder="" class="chzn-select creat-request-select" tabindex="6">
      
      </select></td>
   
    </tr>
    <tr>
    <td ><span>Version:</span></td>
    <td ><select id="request_selectVersion" data-placeholder="" class="chzn-select creat-request-select" tabindex="6">
      </select></td>
   
    </tr>
    <tr>
      <td ><span>Sign off by:</span><span class="red">*</span></td>
      <td >
      	<a title="1. Reset and require new sign off: Any change to the Vault request will reset the sign off state to 'Waiting' and require a new sign off. 2. Send email notification: Any change to the Vault request generates an email to the signatory. The sign off state is not changed. 3. Do not notify: Changes to the Vault request produce no notifications." href="javascript:avoid(0);">For any change,</a>
      	<select id="notifyoption0" data-placeholder=""  class="chzn-select creat-request-select" tabindex="6">
        <option value="1">Reset and require new sign off</option>
        <option value="2" selected= "selected">Send email notification</option>
        <option value="3">Do not notify</option>
      </select>
      <input type="text" class="input-xlarge add-width" id="input_owner_0" value="">
      <span class="delate-table"></span>
      You can input multiple E-mail and separated by commas.
      </br>
      <input type="button" id="sign_addchild_btn" class ="btn off-margin span-top" onclick="javascript:request.req_addOwner()" value="Add More">
      </td>
    </tr>
    <tr>
      <td ><span>CC:</span></td>
      <td ><input type="text" class="input-xlarge sign add-width" id="input_cc"></td>
      
    </tr>
    <tr>
    <td ><span>Due date:</span></td>
    <td ><input type="text" class="input-xlarge" id="requesttime" name="requesttime"><input type="text" class="input-xlarge times" name="requestdatetime" id="requestdatetime"> GMT+0800 (CST)</td>
   
    </tr>
    <tr>
    <td ><span>Parent:</span></td>
    <td ><input type="text" class="input-xlarge" id="input_parent"></td>
   
    </tr>
    <tr>
    <td ><span>Child:</span></td>
    <td >
    <input type="text" class="input-xlarge" id="input_child_0"><span class="delate-table"></span></br>
    <input type="button" id="addchild_btn" class ="btn off-margin span-top" onclick="javascript:request.req_addChild()" value="Add More">
    </td>
   
    </tr>
    <tr>
    <td ><span>Description:</span><span class="red">*</span></td>
    <td ><textarea class="input-xlarge" name="detail" id="detail" rows="3"></textarea></td>
   
    </tr>
    <tr>
    <td ><span>Attachment:</span></td>
    <td ><div class="control-group">
            <label class="control-label" for="fileInput">File input</label>
            <div id="Attachment_div_request">
				<div style="display: none"></div>
			</div>
            <div class="controls">
              <input class="input-file" id="requestAttachment" name="requestAttachment" type="file">
            </div>
          </div> allowable file types: image, openoffice, pdf, text, plain-text, package(gz,tgz,bz2,z,rar,zip,tar) </td>
   
    </tr>
    <tr>
    <td ><span>Public:</span></td>
    <td ><input type="checkbox" value="option2" name="is_public" id="is_public" checked="checked"> (If you want to share this request flow to all users, please check box.)</td>
   
    </tr>
    
    <tr>
    <td colspan="2" ><input type="button" class="btn btn-primary quary" value="Create" onclick="javascript:request.req_commit()"><input type="button" value="Cancel" class="btn quary"></td>
   
    </tr>


    </tbody>
</table>
</form>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/request.js"></script>