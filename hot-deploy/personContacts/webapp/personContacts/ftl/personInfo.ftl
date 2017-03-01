<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>

<body>
<table style="width:100%">
	<tr>
		<td style="width:20%;text-align:right;">接口:</td>
		<td>
			<select onchange="showForm(this);">
				<option>空</option>
				<option value="findPerson">获取自己的详细信息</option>
				<option value="findContactInfo">获取联系人详细信息</option>
				<option value="findContacts">获取自己的联系人列表</option>
				<option value="findLable">查询用户拥有的标签</option>
				<option value="findLablePerson">查询标签内成员</option>
				<option value="addContacts">添加联系人</option>
				<option value="deleteContacts">删除联系人</option>
				<option value="updateContacts">更新联系人信息</option>
				<option value="deleteLable">删除标签</option>
				<option value="createLable">新建标签</option>
				<option value="showPersonAddress">修改地址页数据</option>
				<option value="editPersonAddress">修改或添加联系信息</option>
				<option value="findMyEvent">我的近期活动列表</option>
                <option value="createNewEvent">创建活动</option>
				<option value="findMyEventDetail">我的活动详情</option>
                <option value="lableAndRelationContact">使新建的标签关联到联系人</option>

			</select>
		</td>
	</tr>
</table>
<form id="findPerson" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('findPerson','findPerson');"/></td>
		</tr>
	</table>
</form>	
<form id="findContactInfo" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">联系人partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('findContactInfo','findContactInfo');"/></td>
		</tr>
	</table>
</form>	
<form id="deleteContacts" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">联系人partyId:</td>
			<td><input type="text" name="partyIdFrom"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">用户partyId:</td>
			<td><input type="text" name="partyIdTo"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('deleteContacts','deleteContacts');"/></td>
		</tr>
	</table>
</form>	
<form id="findContacts" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('findContacts','findContacts');"/></td>
		</tr>
	</table>
</form>	
<form id="findLable" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">userLoginId:</td>
			<td><input type="text" name="userLoginId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('findLable','findLable');"/></td>
		</tr>
	</table>
</form>	
<form id="findLablePerson" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('findLablePerson','findLablePerson');"/></td>
		</tr>
	</table>
</form>	
<form id="addContacts" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">姓名:</td>
			<td><input type="text" name="personName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">性别:</td>
			<td><input type="text" name="gender"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">手机号码:</td>
			<td><input type="text" name="contactNumber"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">电子邮箱:</td>
			<td><input type="text" name="contactEmail"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">标签:</td>
			<td><input type="text" name="contactGroup"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">公司:</td>
			<td><input type="text" name="contactCompany"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">省/直辖市:</td>
			<td><input type="text" name="contactGeoName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">市:</td>
			<td><input type="text" name="contactCity"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">区:</td>
			<td><input type="text" name="contactAddress1"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">详细地址:</td>
			<td><input type="text" name="contactAddress2"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">邮政编码:</td>
			<td><input type="text" name="contactPostalCode"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('addContacts','addContacts');"/></td>
		</tr>
	</table>
</form>	
<form id="updateContacts" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">姓:</td>
			<td><input type="text" name="firstName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">名:</td>
			<td><input type="text" name="lastName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">性别:</td>
			<td><input type="text" name="gender"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">手机号码:</td>
			<td><input type="text" name="contactNumber"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">电子邮箱:</td>
			<td><input type="text" name="contactEmail"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">标签:</td>
			<td><input type="text" name="contactGroup"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">公司:</td>
			<td><input type="text" name="contactCompany"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">省/直辖市:</td>
			<td><input type="text" name="contactGeoName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">市:</td>
			<td><input type="text" name="contactCity"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">区:</td>
			<td><input type="text" name="contactAddress1"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">详细地址:</td>
			<td><input type="text" name="contactAddress2"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">邮政编码:</td>
			<td><input type="text" name="contactPostalCode"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('updateContacts','updateContacts');"/></td>
		</tr>
	</table>
</form>	
<form id="deleteLable" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('deleteLable','deleteLable');"/></td>
		</tr>
	</table>
</form>	
<form id="createLable" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">标签名称:</td>
			<td><input type="text" name="lableName"/></td>
		</tr>
		<tr>
            <td style="width:20%;text-align:right;">userLoginId:</td>
            <td><input type="text" name="userLoginId"/></td>

		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('createLable','createLable');"/></td>
		</tr>
	</table>
</form>	
<form id="showPersonAddress" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('showPersonAddress','showPersonAddress');"/></td>
		</tr>
	</table>
</form>	
<form id="editPersonAddress" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">省份ID:</td>
			<td><input type="text" name="stateProvinceGeoId"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">市ID:</td>
			<td><input type="text" name="geoIdCity"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">区ID:</td>
			<td><input type="text" name="geoIdArea"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">详细地址:</td>
			<td><input type="text" name="address1"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">电话号码:</td>
			<td><input type="text" name="contactNumber"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">邮箱地址:</td>
			<td><input type="text" name="email"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">toName:</td>
			<td><input type="text" name="toName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">attnName:</td>
			<td><input type="text" name="attnName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">邮政编码:</td>
			<td><input type="text" name="postalCode"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('editPersonAddress','editPersonAddress');"/></td>
		</tr>
	</table>
</form>	


<!--查询我的活动/事件-->
<form id="findMyEvent" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>

		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="Commit" onclick="getJsonForThis('findMyEvent','findMyEvent');"/></td>
		</tr>
	</table>
</form>
<!-- 开始一个新的活动-->
<form id="createNewEvent" style="display:none;" enctype="multipart/form-data" method="post" action="<@ofbizUrl>createNewEvent</@ofbizUrl>"  class="showOrHid">
    <table width="100%">
        <tr>
            <td style="width:20%;text-align:right;">partyId:</td>
            <td><input type="text" name="partyId"/></td>
            <td style="width:20%;text-align:right;">eventName:</td>
            <td><input type="text" name="workEffortName"/></td>
        </tr>
		<tr>
            <td style="width:20%;text-align:right;">scopeEnumId:</td>
            <td><input type="text" name="scopeEnumId"/></td>
		</tr>
		<tr>
            <td style="width:20%;text-align:right;">description:</td>
            <td><input type="text" name="description"/></td>
		</tr>
        <tr>
            <td style="width:20%;text-align:right;">uploadFile:</td>
            <td><input type="file" name="file_upload" size="25"/></td>
            <td style="width:20%;text-align:right;">address:</td>
            <td><input type="text" name="convertedLeads"/></td>
        </tr>

		<tr>
            <td style="width:20%;text-align:right;">Time:</td>
            <td><span class="view-calendar"><@htmlTemplate.renderDateTimeField name="actualStartDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="25" maxlength="30" id="shipByDate_0" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/></span>
            </td>
		</tr>
        <tr>
            <td style="width:20%;text-align:right;">&nbsp;</td>
            <#--<td><input type="button" value="Commit" onclick="getJsonForThis('createNewEvent','createNewEvent');"/></td>-->
			<input type="submit" value="sub"/>
        </tr>
    </table>
</form>
<form id="lableAndRelationContact" style="display:none;" class="showOrHid">
    <table width="100%">
        <tr>
            <td style="width:20%;text-align:right;">标签Id:</td>
            <td><input type="text" name="groupId"/></td>
        </tr>
		<tr>
            <td style="width:20%;text-align:right;">联系人数组:</td>
            <td><input type="text" name="partyArray"/></td>
		</tr>
        <tr>
            <td style="width:20%;text-align:right;">&nbsp;</td>
            <td><input type="button" value="提交" onclick="getJsonForThis('lableAndRelationContact','lableAndRelationContact');"/></td>
        </tr>
    </table>
</form>



<!--查询我的活动详情-->
<form id="findMyEventDetail" style="display:none;" class="showOrHid">
    <table width="100%">
        <tr>
            <td style="width:20%;text-align:right;">partyId:</td>
            <td><input type="text" name="workEffortId"/></td>

        </tr>
        <tr>
            <td style="width:20%;text-align:right;">&nbsp;</td>
            <td><input type="button" value="Commit" onclick="getJsonForThis('findMyEventDetail','findMyEventDetail');"/></td>
        </tr>
    </table>
</form>




<!-- 这里是用来显示json数据的 -->
<div style="width: 100%;">
    <textarea style="width: 70%;height: 200px;" readonly id="showJson"></textarea>
</div>
<script type="text/javascript">
	function showForm(values){
        $("#showJson").val("");
        $(".showOrHid").css("display","none");
        $("input[type='text']").val("");
        var id = $(values).val();
        $("#"+id).css("display","block");
    }

    function getJsonForThis(values,values2){
        //alert($("#"+values).serialize());
        $.post(
                values,
                $("#"+values2).serialize(),

                function(result){
                    if(result.resultMap!=null){
                        var jsonStr = JSON.stringify(result, null, "\t")
                        $("#showJson").val(jsonStr);
                    }else{
                        alert(result);
                    }
                }
        );
    }
</script>
</body>
</html>