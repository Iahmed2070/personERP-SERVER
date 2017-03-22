<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>

<body>
<table style="width:100%">
	<tr>
		<td style="width:20%;text-align:right;">�ӿ�:</td>
		<td>
			<select onchange="showForm(this);">
				<option>��</option>
				<option value="findPerson">��ȡ�Լ�����ϸ��Ϣ</option>
				<option value="findContactInfo">��ȡ��ϵ����ϸ��Ϣ</option>
				<option value="findContacts">��ȡ�Լ�����ϵ���б�</option>
				<option value="findLable">��ѯ�û�ӵ�еı�ǩ</option>
				<option value="findLablePerson">��ѯ��ǩ�ڳ�Ա</option>
				<option value="addContacts">�����ϵ��</option>
				<option value="deleteContacts">ɾ����ϵ��</option>
				<option value="updateContacts">������ϵ����Ϣ</option>
				<option value="deleteLable">ɾ����ǩ</option>
				<option value="createLable">�½���ǩ</option>
				<option value="showPersonAddress">�޸ĵ�ַҳ����</option>
				<option value="editPersonAddress">�޸Ļ������ϵ��Ϣ</option>
				<option value="findMyEvent">�ҵĽ��ڻ�б�</option>
                <option value="createNewEvent">�����</option>
				<option value="findMyEventDetail">�ҵĻ����</option>
                <option value="lableAndRelationContact">ʹ�½��ı�ǩ��������ϵ��</option>

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
			<td><input type="button" value="�ύ" onclick="getJsonForThis('findPerson','findPerson');"/></td>
		</tr>
	</table>
</form>	
<form id="findContactInfo" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">��ϵ��partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="�ύ" onclick="getJsonForThis('findContactInfo','findContactInfo');"/></td>
		</tr>
	</table>
</form>	
<form id="deleteContacts" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">��ϵ��partyId:</td>
			<td><input type="text" name="partyIdFrom"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">�û�partyId:</td>
			<td><input type="text" name="partyIdTo"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="�ύ" onclick="getJsonForThis('deleteContacts','deleteContacts');"/></td>
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
			<td><input type="button" value="�ύ" onclick="getJsonForThis('findContacts','findContacts');"/></td>
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
			<td><input type="button" value="�ύ" onclick="getJsonForThis('findLable','findLable');"/></td>
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
			<td><input type="button" value="�ύ" onclick="getJsonForThis('findLablePerson','findLablePerson');"/></td>
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
			<td style="width:20%;text-align:right;">����:</td>
			<td><input type="text" name="personName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">�Ա�:</td>
			<td><input type="text" name="gender"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">�ֻ�����:</td>
			<td><input type="text" name="contactNumber"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��������:</td>
			<td><input type="text" name="contactEmail"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��ǩ:</td>
			<td><input type="text" name="contactGroup"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��˾:</td>
			<td><input type="text" name="contactCompany"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">ʡ/ֱϽ��:</td>
			<td><input type="text" name="contactGeoName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��:</td>
			<td><input type="text" name="contactCity"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��:</td>
			<td><input type="text" name="contactAddress1"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��ϸ��ַ:</td>
			<td><input type="text" name="contactAddress2"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��������:</td>
			<td><input type="text" name="contactPostalCode"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="�ύ" onclick="getJsonForThis('addContacts','addContacts');"/></td>
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
			<td style="width:20%;text-align:right;">��:</td>
			<td><input type="text" name="firstName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��:</td>
			<td><input type="text" name="lastName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">�Ա�:</td>
			<td><input type="text" name="gender"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">�ֻ�����:</td>
			<td><input type="text" name="contactNumber"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��������:</td>
			<td><input type="text" name="contactEmail"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��ǩ:</td>
			<td><input type="text" name="contactGroup"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��˾:</td>
			<td><input type="text" name="contactCompany"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">ʡ/ֱϽ��:</td>
			<td><input type="text" name="contactGeoName"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��:</td>
			<td><input type="text" name="contactCity"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��:</td>
			<td><input type="text" name="contactAddress1"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��ϸ��ַ:</td>
			<td><input type="text" name="contactAddress2"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��������:</td>
			<td><input type="text" name="contactPostalCode"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="�ύ" onclick="getJsonForThis('updateContacts','updateContacts');"/></td>
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
			<td><input type="button" value="�ύ" onclick="getJsonForThis('deleteLable','deleteLable');"/></td>
		</tr>
	</table>
</form>	
<form id="createLable" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">��ǩ����:</td>
			<td><input type="text" name="lableName"/></td>
		</tr>
		<tr>
            <td style="width:20%;text-align:right;">userLoginId:</td>
            <td><input type="text" name="userLoginId"/></td>

		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="�ύ" onclick="getJsonForThis('createLable','createLable');"/></td>
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
			<td><input type="button" value="�ύ" onclick="getJsonForThis('showPersonAddress','showPersonAddress');"/></td>
		</tr>
	</table>
</form>	
<form id="editPersonAddress" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">ʡ��ID:</td>
			<td><input type="text" name="stateProvinceGeoId"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��ID:</td>
			<td><input type="text" name="geoIdCity"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��ID:</td>
			<td><input type="text" name="geoIdArea"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">��ϸ��ַ:</td>
			<td><input type="text" name="address1"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">�绰����:</td>
			<td><input type="text" name="contactNumber"/>*</td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">�����ַ:</td>
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
			<td style="width:20%;text-align:right;">��������:</td>
			<td><input type="text" name="postalCode"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="�ύ" onclick="getJsonForThis('editPersonAddress','editPersonAddress');"/></td>
		</tr>
	</table>
</form>	


<!--��ѯ�ҵĻ/�¼�-->
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
<!-- ��ʼһ���µĻ-->
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
            <td style="width:20%;text-align:right;">��ǩId:</td>
            <td><input type="text" name="groupId"/></td>
        </tr>
		<tr>
            <td style="width:20%;text-align:right;">��ϵ������:</td>
            <td><input type="text" name="partyArray"/></td>
		</tr>
        <tr>
            <td style="width:20%;text-align:right;">&nbsp;</td>
            <td><input type="button" value="�ύ" onclick="getJsonForThis('lableAndRelationContact','lableAndRelationContact');"/></td>
        </tr>
    </table>
</form>



<!--��ѯ�ҵĻ����-->
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




<!-- ������������ʾjson���ݵ� -->
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