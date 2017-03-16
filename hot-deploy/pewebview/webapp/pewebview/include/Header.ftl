<html>
<head>
    <title>
    ${(eventsDetail.workEffortName)!}
    </title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <script language="javascript" type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.7.2/jquery.min.js"></script>
    <script>
        //Check Mobile Function
        function checkMobile(sMobile){
            if(!(/^1\d{10}$/.test(sMobile))){
                var message = $("#message").val();
                return false;
            }
            return true;
        }
        //Check Person Nick Name
        function checkPersonNickName(nickName){
            if(nickName == null || nickName=="" || nickName === "" ){

                var message = $("#messageForNickName").val();

                return false;
            }
            return true;
        }


             /**
             * PersonInfo Input Form
             */
            var w,h,className;
            function getSrceenWH(){
                w = $(window).width();
                h = $(window).height();
                $('#dialogBg').width(w).height(h);
            }

            window.onresize = function(){
                getSrceenWH();
            }
            $(window).resize();
            /**
             * 黑暗录入Form
             */


            $(
                    function(){
                        //Form Commit Check
                        $("form[name='mobileform']").submit(
                                function(){

                                    var isPersonRight = true;
                                    //Do Check Phone Number
                                    if(!checkMobile(this.tel.value)){
                                        isPersonRight = false;
                                    }

                                    //Do Check Person NickName IS Empty
                                    if(!checkPersonNickName(this.nickName.value)){
                                        isPersonRight = false;
                                    }


                                    return isPersonRight;
                                }
                        );

                        //Show Dialog
                        $("a[name='bounceInA']").click(function(){
                            className = $(this).attr('class');
                            $('#dialogBg').fadeIn(300);
                            $('#dialog').removeAttr('class').addClass('animated '+className+'').fadeIn();
                        });

                        //Close Window
                        $('.claseDialogBtn').click(function(){
                            $('#dialogBg').fadeOut(300,function(){
                                $('#dialog').addClass('bounceOutUp').fadeOut();
                            });
                        });



                        /**
                         * Submit Form Demo Function
                         * Author:S.Y.L
                         */
                        $("a[id='join']").click(
                                function(){
                                    var userJoin = {
                                        workEffortId:$("#workEffortId").val(),
                                        tel:$("#tel").val(),
                                        nickName:$("#nickName").val()
                                    };
                                    var url = "newPartyJoinActivity";
                                    $.ajax({
                                        type: 'POST',
                                        url: url,
                                        data:userJoin,
                                        success : function(data) {
                                            alert("Join Success!");
                                        },
                                        error:function(data){
                                            alert("ERROR :"+data.status);
                                        }
                                    });
                                }
                        );
                        $('#tit span').click(function() {
                            var i = $(this).index();//�±��һ��д��
                            $(this).addClass('select').siblings().removeClass('select');
                            $('.con').eq(i).show().siblings().hide();
                        });
                        var TouchSlide=function(a){a=a||{};var b={slideCell:a.slideCell||"#touchSlide",titCell:a.titCell||".hd li",mainCell:a.mainCell||".bd",effect:a.effect||"left",autoPlay:a.autoPlay||!1,delayTime:a.delayTime||200,interTime:a.interTime||2500,defaultIndex:a.defaultIndex||0,titOnClassName:a.titOnClassName||"on",autoPage:a.autoPage||!1,prevCell:a.prevCell||".prev",nextCell:a.nextCell||".next",pageStateCell:a.pageStateCell||".pageState",pnLoop:"undefined "==a.pnLoop?!0:a.pnLoop,startFun:a.startFun||null,endFun:a.endFun||null,switchLoad:a.switchLoad||null},c=document.getElementById(b.slideCell.replace("#",""));if(!c)return!1;var d=function(a,b){a=a.split(" ");var c=[];b=b||document;var d=[b];for(var e in a)0!=a[e].length&&c.push(a[e]);for(var e in c){if(0==d.length)return!1;var f=[];for(var g in d)if("#"==c[e][0])f.push(document.getElementById(c[e].replace("#","")));else if("."==c[e][0])for(var h=d[g].getElementsByTagName("*"),i=0;i<h.length;i++){var j=h[i].className;j&&-1!=j.search(new RegExp("\\b"+c[e].replace(".","")+"\\b"))&&f.push(h[i])}else for(var h=d[g].getElementsByTagName(c[e]),i=0;i<h.length;i++)f.push(h[i]);d=f}return 0==d.length||d[0]==b?!1:d},e=function(a,b){var c=document.createElement("div");c.innerHTML=b,c=c.children[0];var d=a.cloneNode(!0);return c.appendChild(d),a.parentNode.replaceChild(c,a),m=d,c},g=function(a,b){!a||!b||a.className&&-1!=a.className.search(new RegExp("\\b"+b+"\\b"))||(a.className+=(a.className?" ":"")+b)},h=function(a,b){!a||!b||a.className&&-1==a.className.search(new RegExp("\\b"+b+"\\b"))||(a.className=a.className.replace(new RegExp("\\s*\\b"+b+"\\b","g"),""))},i=b.effect,j=d(b.prevCell,c)[0],k=d(b.nextCell,c)[0],l=d(b.pageStateCell)[0],m=d(b.mainCell,c)[0];if(!m)return!1;var N,O,n=m.children.length,o=d(b.titCell,c),p=o?o.length:n,q=b.switchLoad,r=parseInt(b.defaultIndex),s=parseInt(b.delayTime),t=parseInt(b.interTime),u="false"==b.autoPlay||0==b.autoPlay?!1:!0,v="false"==b.autoPage||0==b.autoPage?!1:!0,w="false"==b.pnLoop||0==b.pnLoop?!1:!0,x=r,y=null,z=null,A=null,B=0,C=0,D=0,E=0,G=/hp-tablet/gi.test(navigator.appVersion),H="ontouchstart"in window&&!G,I=H?"touchstart":"mousedown",J=H?"touchmove":"",K=H?"touchend":"mouseup",M=m.parentNode.clientWidth,P=n;if(0==p&&(p=n),v){p=n,o=o[0],o.innerHTML="";var Q="";if(1==b.autoPage||"true"==b.autoPage)for(var R=0;p>R;R++)Q+="<li>"+(R+1)+"</li>";else for(var R=0;p>R;R++)Q+=b.autoPage.replace("$",R+1);o.innerHTML=Q,o=o.children}"leftLoop"==i&&(P+=2,m.appendChild(m.children[0].cloneNode(!0)),m.insertBefore(m.children[n-1].cloneNode(!0),m.children[0])),N=e(m,'<div class="tempWrap" style="overflow:hidden; position:relative;"></div>'),m.style.cssText="width:"+P*M+"px;"+"position:relative;overflow:hidden;padding:0;margin:0;";for(var R=0;P>R;R++)m.children[R].style.cssText="display:table-cell;vertical-align:top;width:"+M+"px";var S=function(){"function"==typeof b.startFun&&b.startFun(r,p)},T=function(){"function"==typeof b.endFun&&b.endFun(r,p)},U=function(a){var b=("leftLoop"==i?r+1:r)+a,c=function(a){for(var b=m.children[a].getElementsByTagName("img"),c=0;c<b.length;c++)b[c].getAttribute(q)&&(b[c].setAttribute("src",b[c].getAttribute(q)),b[c].removeAttribute(q))};if(c(b),"leftLoop"==i)switch(b){case 0:c(n);break;case 1:c(n+1);break;case n:c(0);break;case n+1:c(1)}},V=function(){M=N.clientWidth,m.style.width=P*M+"px";for(var a=0;P>a;a++)m.children[a].style.width=M+"px";var b="leftLoop"==i?r+1:r;W(-b*M,0)};window.addEventListener("resize",V,!1);var W=function(a,b,c){c=c?c.style:m.style,c.webkitTransitionDuration=c.MozTransitionDuration=c.msTransitionDuration=c.OTransitionDuration=c.transitionDuration=b+"ms",c.webkitTransform="translate("+a+"px,0)"+"translateZ(0)",c.msTransform=c.MozTransform=c.OTransform="translateX("+a+"px)"},X=function(a){switch(i){case"left":r>=p?r=a?r-1:0:0>r&&(r=a?0:p-1),null!=q&&U(0),W(-r*M,s),x=r;break;case"leftLoop":null!=q&&U(0),W(-(r+1)*M,s),-1==r?(z=setTimeout(function(){W(-p*M,0)},s),r=p-1):r==p&&(z=setTimeout(function(){W(-M,0)},s),r=0),x=r}S(),A=setTimeout(function(){T()},s);for(var c=0;p>c;c++)h(o[c],b.titOnClassName),c==r&&g(o[c],b.titOnClassName);0==w&&(h(k,"nextStop"),h(j,"prevStop"),0==r?g(j,"prevStop"):r==p-1&&g(k,"nextStop")),l&&(l.innerHTML="<span>"+(r+1)+"</span>/"+p)};if(X(),u&&(y=setInterval(function(){r++,X()},t)),o)for(var R=0;p>R;R++)!function(){var a=R;o[a].addEventListener("click",function(){clearTimeout(z),clearTimeout(A),r=a,X()})}();k&&k.addEventListener("click",function(){(1==w||r!=p-1)&&(clearTimeout(z),clearTimeout(A),r++,X())}),j&&j.addEventListener("click",function(){(1==w||0!=r)&&(clearTimeout(z),clearTimeout(A),r--,X())});var Y=function(a){clearTimeout(z),clearTimeout(A),O=void 0,D=0;var b=H?a.touches[0]:a;B=b.pageX,C=b.pageY,m.addEventListener(J,Z,!1),m.addEventListener(K,$,!1)},Z=function(a){if(!H||!(a.touches.length>1||a.scale&&1!==a.scale)){var b=H?a.touches[0]:a;if(D=b.pageX-B,E=b.pageY-C,"undefined"==typeof O&&(O=!!(O||Math.abs(D)<Math.abs(E))),!O){switch(a.preventDefault(),u&&clearInterval(y),i){case"left":(0==r&&D>0||r>=p-1&&0>D)&&(D=.4*D),W(-r*M+D,0);break;case"leftLoop":W(-(r+1)*M+D,0)}null!=q&&Math.abs(D)>M/3&&U(D>-0?-1:1)}}},$=function(a){0!=D&&(a.preventDefault(),O||(Math.abs(D)>M/10&&(D>0?r--:r++),X(!0),u&&(y=setInterval(function(){r++,X()},t))),m.removeEventListener(J,Z,!1),m.removeEventListener(K,$,!1))};m.addEventListener(I,Y,!1)};

                        TouchSlide({
                            slideCell:"#slideBox",
                            titCell:".hd ul",
                            mainCell:".bd ul",
                            effect:"leftLoop",
                            autoPage:true,
                            autoPlay:true
                        });




                    }
            );

    </script>
    <style>
        /*------------------- 华丽分割线 -----------------------*/

        #dialogBg{width:100%;height:100%;background-color:#000000;opacity:.8;filter:alpha(opacity=60);position:fixed;top:0;left:0;z-index:9999;display:none;}
        #dialog{ width: 300px; height: 240px; margin: 0 auto; display: none; background-color: #ffffff; position: fixed; top: 50%; left: 50%; margin: -120px 0 0 -150px; z-index: 10000; border: 1px solid #ccc; border-radius: 10px; -webkit-border-radius: 10px; box-shadow: 3px 2px 4px rgba(0,0,0,0.2); -webkit-box-shadow: 3px 2px 4px rgba(0,0,0,0.2); }
        .dialogTop{width:90%;margin:0 auto;border-bottom:1px dotted #ccc;letter-spacing:1px;padding:10px 0;text-align:right;}
        .dialogIco{width:50px;height:50px;position:absolute;top:-25px;left:50%;margin-left:-25px;}
        .editInfos{padding:15px 0;}
        .editInfos li{width:90%;margin:8px auto auto;text-align: center;}
        .ipt{border:1px solid #ccc;padding:5px;border-radius:3px;-webkit-border-radius:3px;box-shadow:0 0 3px #ccc inset;-webkit-box-shadow:0 0 3px #ccc inset;margin-left:5px;}
        /*.ipt:focus{outline:none;border-color:#66afe9;box-shadow:0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(102, 175, 233, 0.6);-webkit-box-shadow:0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(102, 175, 233, 0.6);}*/
        .submitBtn{width:90px;height:30px;line-height:30px;font-family:"微软雅黑","microsoft yahei";cursor:pointer;margin-top:10px;display:inline-block;border-radius:5px;-webkit-border-radius:5px;text-align:center;background-color:#428bca;color:#fff;box-shadow: 0 -3px 0 #2a6496 inset;-webkit-box-shadow: 0 -3px 0 #2a6496 inset;}






        *{box-sizing: border-box;}
        ul,li,dl,dt,dd{list-style:none;}
        body{font-size:12px; font-family:Gotham, "Helvetica Neue", Helvetica, Arial, sans-serif; background:#FFF;}
        a{text-decoration:none;color:#000;margin:0;padding:0; color:#000;}
        a:link{}
        a:visited{}
        a:hover{}
        a:active{}
        a img{ border:0;}
        div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,blockquote,p,select,img,table,a,body{padding:0; margin:0;}
        .clear {clear:both; height:0; overflow:hidden;}



        .t { width:100%;}
        .t img { width:100%; display:block;}
        .cn { width:100%;  background-color:#feb62c;}
        .cn1 { width:94%; background-color:#ffed7d; margin:0 auto;}
        .hp { padding:.5rem;}
        .hpb { width:50%;float:left;}
        .hpb-t { width:3.5rem; height:3.5rem; background:url(../images/hb.png) no-repeat center; background-size:100%; text-align:center; float:left}
        .hpb-t img {margin-top: .15rem; width:90%;border-radius: 2rem;}
        .hpb p{ font-size:.7rem; color:#0295b7; float:left; margin:1rem 0 0 .5rem}
        .hpb a{ display:block; padding-left: 1rem;}
        .hpb-t1 { width:1.5rem; float:left}
        .hpb-t1 img {margin-top: .85rem; width:100%;}


        .gz { display:block; float:right; color:#fc2748;}

        .lie ul{ display:block; padding:0 .2rem;}
        .lie ul li { width:100%; background-color:#fef9dd; margin-bottom:10px;}
        .lie ul li img { float:left; width:5rem;}
        .jia { float:left;}
        .in { float:left; }
        .jia { margin:0 .5rem; width:8.6rem;}
        .jiage1{ font-size:.7rem; color:#333333; margin-top:.2rem; overflow:hidden;white-space:nowrap;text-overflow:ellipsis}
        .jiage p{ font-size:.4rem; color:#999; margin-top:.1rem;}

        .te { font-size:.6rem; color:#ed304e;}
        .in { width:2.2rem; height:1rem; border:1px solid #ed304e; border-radius:7px; text-align:center; line-height:1rem; color:#ed304e; font-size:.6rem; float:left; margin:1rem 0 0 1rem;}


        .slideBox{ position:relative; overflow:hidden; margin-bottom:10px;width:100%; }
        .slideBox .hd{ position:absolute; height:28px; line-height:28px; bottom:0; z-index:1;width: 100%;text-align: center; }
        .slideBox .hd li{ display:inline-block; width:.4rem; height:.4rem; border-radius:15px; background:#fff; text-indent:-9999px; overflow:hidden; margin:0 6px;   }
        .slideBox .hd li.on{ background:#fe798e;  }
        .slideBox .bd{ position:relative; z-index:0; }
        .slideBox .bd li{ position:relative; text-align:center;  }
        .slideBox .bd li img{ vertical-align:top; width:100%;}
        .slideBox .bd li a{ -webkit-tap-highlight-color:rgba(0,0,0,0);  }
        .slideBox .bd li .tit{ display:block; width:100%;  position:absolute; bottom:0; text-indent:10px; height:28px; line-height:28px; background:url(images/focusBg.png) repeat-x; color:#fff;  text-align:left;  }


        .xj { width:100%; text-align:center; padding-bottom:.5rem; border-bottom:10px solid #E5E5E5;}
        .xj p:nth-child(1) { font-size:.7rem; color:#333333;}
        .xj p:nth-child(2) { font-size:1rem; color:#fe798e;}
        .xj p:nth-child(2) span { font-size:.5rem; color:#999;text-decoration:line-through;}



        #wrap {width:100%; overflow: hidden; margin-bottom:3rem;}
        #tit { width:100%;}
        #tit span {float: left; height:2rem; line-height: 2rem; width: 50%; font-size: .7rem; text-align: center; color: #666;background:#fff;border-bottom: 1px solid #e5e5e5;}
        .con{display: none; width: 100%;}
        #tit span.select {background:#fe798e; color: #fff;}
        #con div.show {display: block;}


        .ren { width:100%; padding:0 .5rem;}
        .ren li { padding:.5rem 0; border-bottom: 1px solid #e5e5e5;}
        .xqy-p { float:left; width:2rem;}
        .xqy-p  img { width:100%; border-radius:2rem;}
        .xqy-p1 { float:left; margin-left:.5rem; margin-top: .5rem; font-size:.6rem; color:#999999;}



        .foot{ width:100%; border-top:1px solid #E5E5E5; height:3rem;position: fixed;bottom: 0; padding:.3rem; background-color:#FFFFFF;}
        .foot div:nth-child(1) { float:left; width:48%; border:1px solid #fe798e; text-align:center; font-size:.8rem; height:100%; border-radius:.5rem;}
        .foot div:nth-child(1) a { font-size:.8rem; color:#fe798e; display:block;padding: .6rem 0;}
        .foot div:nth-child(2) { float:right; width:48%; text-align:center; font-size:.8rem; height:100%; border-radius:.5rem; background-color:#fe798e}
        .foot div:nth-child(2) a { font-size:.8rem; color:#fff; display:block;padding: .6rem 0;}

        .cd-popup {
            position: fixed;
            left: 0;
            top: 0;
            height: 100%;
            width: 100%;
            background-color: rgba(94, 110, 141, 0.9);
            opacity: 0;
            visibility: hidden;
            -webkit-transition: opacity 0.3s 0s, visibility 0s 0.3s;
            -moz-transition: opacity 0.3s 0s, visibility 0s 0.3s;
            transition: opacity 0.3s 0s, visibility 0s 0.3s;
        }
        .cd-popup.is-visible {
            opacity: 1;
            visibility: visible;
            -webkit-transition: opacity 0.3s 0s, visibility 0s 0s;
            -moz-transition: opacity 0.3s 0s, visibility 0s 0s;
            transition: opacity 0.3s 0s, visibility 0s 0s;
        }

        .cd-popup-container {
            position: relative;
            width: 90%;
            max-width: 400px;
            margin: 4em auto;
            background: #FFF;
            border-radius: .25em .25em .4em .4em;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
            -webkit-transform: translateY(-40px);
            -moz-transform: translateY(-40px);
            -ms-transform: translateY(-40px);
            -o-transform: translateY(-40px);
            transform: translateY(-40px);
            /* Force Hardware Acceleration in WebKit */
            -webkit-backface-visibility: hidden;
            -webkit-transition-property: -webkit-transform;
            -moz-transition-property: -moz-transform;
            transition-property: transform;
            -webkit-transition-duration: 0.3s;
            -moz-transition-duration: 0.3s;
            transition-duration: 0.3s;
            padding: 0 1em 3em 1em;
        }
        .cd-popup-container p:nth-child(1) { text-align:center; font-size:16px; color:#333333;padding-top: 30px;}
        .cd-popup-container p:nth-child(2) {  font-size:12px; color:#666;padding-top: 5px;}
        .nei {  font-size:12px; color:#666; margin-left:20px}
        .cd-popup-container .cd-buttons:after {
            content: "";
            display: table;
            clear: both;
        }
        .cd-popup-container .cd-buttons li {
            float: left;
            width: 50%;
        }
        .cd-popup-container .cd-buttons a {
            display: block;
            height: 60px;
            line-height: 60px;
            text-transform: uppercase;
            color: #FFF;
            -webkit-transition: background-color 0.2s;
            -moz-transition: background-color 0.2s;
            transition: background-color 0.2s;
        }
        .cd-popup-container .cd-buttons li:first-child a {
            background: #fc7169;
            border-radius: 0 0 0 .25em;
        }
        .no-touch .cd-popup-container .cd-buttons li:first-child a:hover {
            background-color: #fc8982;
        }
        .cd-popup-container .cd-buttons li:last-child a {
            background: #b6bece;
            border-radius: 0 0 .25em 0;
        }
        .no-touch .cd-popup-container .cd-buttons li:last-child a:hover {
            background-color: #c5ccd8;
        }
        .cd-popup-container .cd-popup-close {
            position: absolute;
            top: 8px;
            right: 8px;
            width: 30px;
            height: 30px;
        }
        .cd-popup-container .cd-popup-close::before, .cd-popup-container .cd-popup-close::after {
            content: '';
            position: absolute;
            top: 12px;
            width: 14px;
            height: 3px;
            background-color: #8f9cb5;
        }
        .cd-popup-container .cd-popup-close::before {
            -webkit-transform: rotate(45deg);
            -moz-transform: rotate(45deg);
            -ms-transform: rotate(45deg);
            -o-transform: rotate(45deg);
            transform: rotate(45deg);
            left: 8px;
        }
        .cd-popup-container .cd-popup-close::after {
            -webkit-transform: rotate(-45deg);
            -moz-transform: rotate(-45deg);
            -ms-transform: rotate(-45deg);
            -o-transform: rotate(-45deg);
            transform: rotate(-45deg);
            right: 8px;
        }
        .is-visible .cd-popup-container {
            -webkit-transform: translateY(0);
            -moz-transform: translateY(0);
            -ms-transform: translateY(0);
            -o-transform: translateY(0);
            transform: translateY(0);
        }
        @media only screen and (min-width: 1170px) {
            .cd-popup-container {
                margin: 8em auto;
            }
        }



    </style>
</head>
<body>
<input type="hidden" id="message" value="${activityUiLabelMap.PhoneNumberExcetion}"/>
<input type="hidden" id="messageForNickName" value="${activityUiLabelMap.NickNameExcetion}"/>

<form name="newPartyJoinActivity" id="2017newPartyJoinActivity.do" action="2017newPartyAsJoinActivity.do">
    <input type="hidden" id="workEffortId" name="workEffortId" value="${(eventsDetail.workEffortId)!}" />
    <input type="hidden" id="tel" name="tel" value="${(invitationPersonInfo.tel)!}" />
    <input type="hidden" id="nickName" name="nickName" value="${(invitationPersonInfo.custName)!}" />
</form>