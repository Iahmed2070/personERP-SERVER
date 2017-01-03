<table style="width:100%">
	<tr>
		<td style="width:20%;text-align:right;">接口:</td>
		<td>
			<select onchange="showForm(this);">
				<option>空</option>
				<option value="findPerson">获取自己的详细信息</option>
				<option value="findContects">获取自己的联系人列表</option>
				<option value="findLable">查询用户拥有的标签</option>
				<option value="findLablePerson">查询标签内成员</option>
				<option value="addContects">添加联系人</option>
				<option value="updateContects">更新联系人信息</option>
				<option value="deleteLable">删除标签</option>
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
<form id="findContects" style="display:none;" class="showOrHid">
	<table width="100%">
		<tr>
			<td style="width:20%;text-align:right;">partyId:</td>
			<td><input type="text" name="partyId"/></td>
		</tr>
		<tr>
			<td style="width:20%;text-align:right;">&nbsp;</td>
			<td><input type="button" value="提交" onclick="getJsonForThis('findContects','findContects');"/></td>
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
<form id="addContects" style="display:none;" class="showOrHid">
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
			<td><input type="button" value="提交" onclick="getJsonForThis('addContects','addContects');"/></td>
		</tr>
	</table>
</form>	
<form id="updateContects" style="display:none;" class="showOrHid">
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
			<td><input type="button" value="提交" onclick="getJsonForThis('updateContects','updateContects');"/></td>
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