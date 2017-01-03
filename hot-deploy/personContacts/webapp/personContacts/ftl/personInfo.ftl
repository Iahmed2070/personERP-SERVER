<table style="width:100%">
	<tr>
		<td style="width:20%;text-align:right;">接口:</td>
		<td>
			<select onchange="showForm(this);">
				<option>空</option>
				<option value="findPerson">获取自己的详细信息</option>
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
                    	$("#showJson").val(result);
                    }
                }
        );
    }
</script>