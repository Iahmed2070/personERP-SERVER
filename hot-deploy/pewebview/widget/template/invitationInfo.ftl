<!-- Show Event Block -->
<#if eventsDetail?has_content>
<div id="slideBox" class="slideBox">
    <div class="bd">
        <ul>
            <!-- This Block Wait 'OSS' Logic Code -->
            <li>
                <a class="pic" href="#"><img src="../images/ba2.png" /></a>
            </li>
        </ul>
    </div>

    <div class="hd">
        <ul></ul>
    </div>
</div>

<div class="clear"></div>

<!-- This Block About Show EventName And DateTime -->
<div class="xj">
    <p><strong>${(eventsDetail.workEffortName)!}</strong></p>
    <#if eventsDetail.actualStartDate?has_content>
    <p>${activityUiLabelMap.ActivityTime}:${(eventsDetail.actualStartDate)!}</p>
    </#if>
    <#if !eventsDetail.actualStartDate?has_content>
    <p>${activityUiLabelMap.ActivityTimeNotFound}</p>
    </#if>
</div>


<!-- 2 Tabs ,Chose Select View About Activity Infomation -->
<div id="wrap">
    <div id="tit">
        <span class="select">${activityUiLabelMap.PersonActivityDetail}</span>
        <span>${activityUiLabelMap.WhoJoinActivity}</span>
    </div>
    <div class="clear"></div>
    <div id="con">
        <div class="con show">
            <h3>Address:${(eventsDetail.locationDesc)!} WelCome !</h3>
        </div>
        <div class="con">
            <ul class="ren">
            <#if partyJoinEventsList?has_content>
                <#list partyJoinEventsList as list>
                <li>
                    <div  class="xqy-p">
                        <img src="../images/wangkun.jpg" />
                    </div>
                    <div class="xqy-p1">
                    <#-- What The Fuck Bug In This Line  -->
                        <span style="font-size: 14px;">${(list.nickname)!} ${(list.lastName)!}${(list.firstName)!}</span>
                        <span style="margin-left:1rem; ">-From 'Base Friend'Group</span>
                    </div>
                    <div class="clear"></div>
                </li>
                </#list>
             </#if>
            </ul>
        </div>
    </div>
</div>

<div class="foot">
    <div>
        <a href="javascript:alert('Please !!!');" >${activityUiLabelMap.ActivityRefused}</a>
    </div>
    <div>

    <#if invitationPersonInfo?has_content>
           <a id="join" href="javascript:joinActivity();">${activityUiLabelMap.ActivityJoin}</a>
    </#if>
     <#if !invitationPersonInfo?has_content>
       <a href="javascript:;" name="bounceInA" class="bounceIn">${activityUiLabelMap.ActivityJoin}</a>


     </#if>
    </div>
</div>

</#if>

<!-- Juest Null Info -->
<#if !eventsDetail?has_content>
Activity NOT FOUND.
</#if>
<div id="dialogBg"></div>
<div id="dialog" class="animated">
    <img class="dialogIco" width="50" height="50" src="../images/ico.png" alt="" />
    <div class="dialogTop">
        <a href="javascript:;" class="claseDialogBtn">CLOSE</a>
    </div>
    <form action="" method="post" id="editForm">
        <ul class="editInfos">

            <li><label><font color="#ff0000">* </font>TELE<input type="text"
                                                                 name="" required value="" class="ipt" /></label></li>
            <li><label><font color="#ff0000">* </font>NAME<input type="text"
                                                                 name="" required value="" class="ipt" /></label></li>
            <li><input type="submit" value="JOIN NOW" class="submitBtn" /></li>
        </ul>
    </form>
</div>

<#--<#assign nowTimestamp = Static["org.apache.ofbiz.base.util.UtilDateTime"].nowTimestamp()>-->

