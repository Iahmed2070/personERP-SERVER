
<#if eventsDetail?has_content>
<div id="slideBox" class="slideBox">
    <div class="bd">
        <ul>
            <#--<li>-->
                <#--<a class="pic" href="#"><img src="../images/ba1.png" /></a>-->
            <#--</li>-->
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


<div class="xj">
    <p><strong>${(eventsDetail.workEffortName)!}</strong></p>
    <#if eventsDetail.actualStartDate?has_content>
    <p>${activityUiLabelMap.ActivityTime}:${(eventsDetail.actualStartDate)!}</p>
    </#if>
    <#if !eventsDetail.actualStartDate?has_content>
    <p>${activityUiLabelMap.ActivityTimeNotFound}</p>
    </#if>
</div>



<div id="wrap">
    <div id="tit">
        <span class="select">${activityUiLabelMap.PersonActivityDetail}</span>
        <span>${activityUiLabelMap.WhoJoinActivity}</span>
    </div>
    <div class="clear"></div>
    <div id="con">
        <div class="con show">
            <h3>${(eventsDetail.locationDesc)!}</h3>
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
                    <#--${(list.lastName)!}${(list.firstName)!}-->
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
        <a href="#">${activityUiLabelMap.ActivityRefused}</a>
    </div>
    <div>
        <a href="#">${activityUiLabelMap.ActivityJoin}</a>
    </div>
</div>

</#if>


<#if !eventsDetail?has_content>
Activity NOT FOUND . You Can Shutdown YourComp
</#if>



<#--<#assign nowTimestamp = Static["org.apache.ofbiz.base.util.UtilDateTime"].nowTimestamp()>-->

