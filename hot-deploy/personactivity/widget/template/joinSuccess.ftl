<!DOCTYPE html>
<html class=" js-no-overflow-scrolling">
<head>
    <meta charset="utf-8">
    <meta http-equiv="cache-control" content="max-age=0" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <title>DON'T SPLIT PEAR</title>

    <style type="text/css">
        .H5FullscreenPage .H5FullscreenPage-wrap {
            width: 100%;
            height: 100%;
            overflow: hidden;
            position: relative;
        }
        .H5FullscreenPage .item {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            transition: all 700ms cubic-bezier(0.550, 0.085, 0.000, 0.990);
            -webkit-transition: all 700ms cubic-bezier(0.550, 0.085, 0.000, 0.990);
            /*transition: all 300ms ease-in-out;
            -webkit-transition: all 300ms ease-in-out;*/
            background-position: center;
        }
        .H5FullscreenPage .item:not(:first-child ) {
            transform: translate3d(0,100%,0);
            -webkit-transform: translate3d(0,100%,0);
        }

        .H5FullscreenPage .no-animation {
            transition: none;
            -webkit-transition: none;
        }
        .H5FullscreenPage .hide {
            display: none;
        }
        .H5FullscreenPage .overlay {
            display: none;
            position: absolute;
            top: 0;
            left: 0;
            bottom: 0;
            right: 0;
            z-index: 99;
        }
        .H5FullscreenPage .zindex {
            z-index: 99;
        }
        .H5FullscreenPage .arrow {
            display: block;
            width: 25px;
            height: 25px;
            position: absolute;
            bottom: 18px;
            left: 50%;
            margin-left: -10px;
            border-top: 2px solid #5a584e;
            border-left: 2px solid #5a584e;
            z-index: 9999;
            -webkit-tap-highlight-color: rgba(255,255,255,0);
            animation: fadeInUp 1.5s .2s ease infinite;
            -webkit-animation: fadeInUp 1.5s .2s infinite;
        }
        .H5FullscreenPage .parallax.part {
            transition: all .5s;
            transition-timing-function: ease-out;
        }
        .H5FullscreenPage .parallax-item {
            transition-property: background-position, transform;
            transition-timing-function: ease-out;
            transition-duration: 500ms;

        }
        .H5FullscreenPage .music {
            position: absolute;
            right: 10px;
            top: 10px;
            width: 25px;
            height: 25px;
            background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADcAAAA3CAMAAACfBSJ0AAAAh1BMVEUAAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////9qkf8RAAAALHRSTlMA6j0E4S2vQO/nFWs2Dgga+VMo7M25h3ZG02Vbmnww87NNP96oI8CMb5Lg1UAzAj8AAAKLSURBVEjHndbbkqMgEADQRpSAGsX7XeMmMcmO//99WzqzEgWMNf2QB6tOEZqGblCFHaSkQHzkqLikgQ2Hws9iemmC1saA7TZoLjTO/I/KJVF9wqtP+FRHxN1VYXItPcV3r7wmoVZ5KXMwqAM7LPU0ixVkLwU2KZRL5siB/XBQLn+smAGfwkCVxEwLPjvKNjA3wwMMnaz1X70j9xCbfu5vBxCXBxmAE4vjSMke8PI0Nmc2B0mXg2Pac8NGM3Dz5owLA5v9T0XiqI2bJR26lSHAeVwYgJP8lLKJZWQ9L5SSp/WT7lEwwOZ3Eom0XHBDXZK5eElJNK7qZk6HT6V6/fsyJiNYs3Iene5jVsM2TOs9MzXnbOWgzgAgPqmcyAzlj/C8dqcYwI+wwonMVFMmNw4jGwICsgvKn8zMxy05IAGkjcLxoTGwKC7JNQ8gvcK1q5qUXX+BwlI4SzClswpAtsYJJjufAccaJ5jszh2MoHGCyQ5GzXqCadbT7U8w3OZ5L+1Pk0/Byoher1cpn+rzqxZWoVx5fsp6YfS0XKleXS+q+jTGcnkgKYCyPrf3oa9vA+fLnkP0nYdOug8QGyDixh51V9PFYerOyRnWfyie7vtLfHCKs4H+vN/37OsO4Eb5yr0ymC6ueF+KfmLvDr+6Ie7KFfMif/Oe8X5iws2pKatNaThk+35GdGIeV9SQ/H6K99rgybynYb/tJpv+YKDArFsrpa3WiP4g+tGUEv9GebLLgKSr/jezDyH6n+i3B5mL7qv+zughZqF8O4ZYB1hoVr+ZX1xW/WpeinLNfObvKF8znwF4j+ipnQef0cMDXbQ782f7Yd6linmXEvfwfO2fAfTztX6eZ3Sk7Es3z/8D0fgrl5NXHdYAAAAASUVORK5CYII=');

            background-size: 25px;
            z-index: 99;
        }
        .H5FullscreenPage .music.play {
            background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADcAAAA3CAMAAACfBSJ0AAAAilBMVEUAAAD////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////2N2iNAAAALXRSTlMA6y3h5gVsDgg2snYaPe/UFqtTKPK4r4Z9ZUxFMAP3WpokE97NiyzOv2mjksf96K+aAAACWUlEQVRIx52W6a6CMBCFp9pi2cq+L4LiLu//ejdgIkJL4XJ+GGnypfQM0zkg0smNHAu1bYss7eKeYJUMvfG12M1tAsTO3VjzG91YpNQQVSYZLRGzQqEqpbzgnFDgRZNz4M1SNMIPAmKRB47ozGZWaMO87NASbpkhBeRSUMYvHrEJSzLxkceusKzrFMywB2u0G79qjVRYJxPVPwVoElgrpRnKETkgEc2iNx4ew+hbODxbN2LG9xI/lfanjl8rAnHhClUPfOQ8aoDDDwdKAL3UPREYp2iMacqu+z/hyP5jYsht5zrID3S1AAABB0rY9xujU27/GrcSTUccZV0/6hVw3G7qzIiDSgeAxhRzgzNJfRhzZgNgo0LM9c6EH2cmXIFO4GrAc+5jcEbAgeZCFAu48h4Pzgi4+AJOKuDy7lfCpRpYOwG3W+CuFiB7A2dgKMkG7uBDCxs4aLfut3w+kmdZyp1v0c8EsfP5zPvppFLuiDJx/aJYyu3Tme/FDWVczYqZ79MY90NaOY5TfjkPfXzwuX6A22//PfFF13X25QhTe3Pu4/67df3+GhYU6zQ5n24ZAOpkEL307h3QcL9Y6dSX4lXe334ywigy+vvs8V0pT3zd6+RoT656bXp/4h6gpfj+Hu7P6X1dPfszveWTJeDmg72v8mvk5zLMxh4/j4wnKwMxxs8joLd/zL8b3TJvVVRvnO9b8oSHj1vyi8pjAOmKvJTO5DNDQhmONeMBvSBlNg8q6EJhTrkkf+byo2tMkHeZpq7J16zL18YBij5fs5skX/N5Hvutj2fz/B/LOymzX/xZaQAAAABJRU5ErkJggg==');
            -webkit-animation: rotate 3.82s linear infinite;
            animation: rotate 3.82s linear infinite;
        }
        @keyframes fadeInUp{0%{opacity:0;transform:translateY(-20px) rotate(-135deg)}100%{opacity:1;transform:translateY(0px) rotate(-135deg)}}@-webkit-keyframes fadeInUp{0%{opacity:0;-webkit-transform:translateY(-20px) rotate(-135deg)}100%{opacity:1;-webkit-transform:translateY(0px) rotate(-135deg)}}
        @keyframes rotate{0%{-webkit-transform:rotate(0deg)}100%{-webkit-transform:rotate(360deg)}}@-webkit-keyframes rotate{0%{-webkit-transform:rotate(0deg)}100%{-webkit-transform:rotate(360deg)}}
        .fadeIn {
            animation-name: fadeIn;
            -webkit-animation-name: fadeIn;

            animation-duration: 1.5s;
            -webkit-animation-duration: 1.5s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;


        }

        @keyframes fadeIn {
            0% {
                transform: scale(0);
                opacity: 0.0;
            }
            60% {
                transform: scale(1.1);
            }
            80% {
                transform: scale(0.9);
                opacity: 1;
            }
            100% {
                transform: scale(1);
                opacity: 1;
            }
        }

        @-webkit-keyframes fadeIn {
            0% {
                -webkit-transform: scale(0);
                opacity: 0.0;
            }
            60% {
                -webkit-transform: scale(1.1);
            }
            80% {
                -webkit-transform: scale(0.9);
                opacity: 1;
            }
            100% {
                -webkit-transform: scale(1);
                opacity: 1;
            }
        }
        .slideLeft{
            animation-name: slideLeft;
            -webkit-animation-name: slideLeft;

            animation-duration: 1s;
            -webkit-animation-duration: 1s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;


        }

        @keyframes slideLeft {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(-100%, 0, 0);
                transform: translate3d(-100%, 0, 0);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }

        @-webkit-keyframes slideLeft {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(-100%, 0, 0);
                transform: translate3d(-100%, 0, 0);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }
        .slideRight{
            animation-name: slideRight;
            -webkit-animation-name: slideRight;

            animation-duration: 1s;
            -webkit-animation-duration: 1s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;


        }

        @keyframes slideRight {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(100%, 0, 0);
                transform: translate3d(100%, 0, 0);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }

        @-webkit-keyframes slideRight {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(100%, 0, 0);
                transform: translate3d(100%, 0, 0);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }
        .slideUp {
            animation-name: slideUp;
            -webkit-animation-name: slideUp;

            animation-duration: 1s;
            -webkit-animation-duration: 1s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;


        }

        @keyframes slideUp {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(0, -100%, 0);
                transform: translate3d(0, -100%, 0);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }

        @-webkit-keyframes slideUp {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(0, -100%, 0);
                transform: translate3d(0, -100%, 0);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }
        .slideDown {
            animation-name: slideDown;
            -webkit-animation-name: slideDown;

            animation-duration: 1s;
            -webkit-animation-duration: 1s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;


        }

        @keyframes slideDown {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(0, 100%, 0);
                transform: translate3d(0, 100%, 0);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }

        @-webkit-keyframes slideDown {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(0, 100%, 0);
                transform: translate3d(0, 100%, 0);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }
        .rotateIn {
            animation-name: rotateIn;
            -webkit-animation-name: rotateIn;

            animation-duration: 1s;
            -webkit-animation-duration: 1s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;


        }

        @keyframes rotateIn {
            0% {
                -webkit-transform-origin: center;
                transform-origin: center;
                -webkit-transform: rotate3d(0, 0, 1, -200deg);
                transform: rotate3d(0, 0, 1, -200deg);
                opacity: 0;
            }

            100% {
                -webkit-transform-origin: center;
                transform-origin: center;
                -webkit-transform: none;
                transform: none;
                opacity: 1;
            }
        }

        @-webkit-keyframes rotateIn {
            0% {
                -webkit-transform-origin: center;
                transform-origin: center;
                -webkit-transform: rotate3d(0, 0, 1, -200deg);
                transform: rotate3d(0, 0, 1, -200deg);
                opacity: 0;
            }

            100% {
                -webkit-transform-origin: center;
                transform-origin: center;
                -webkit-transform: none;
                transform: none;
                opacity: 1;
            }
        }
        .zoomIn {
            animation-name: zoomIn;
            -webkit-animation-name: zoomIn;

            animation-duration: 1s;
            -webkit-animation-duration: 1s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;


        }
        @-webkit-keyframes zoomIn {
            0% {
                opacity: 0;
                -webkit-transform: scale3d(.3, .3, .3);
                transform: scale3d(.3, .3, .3);
            }

            50% {
                opacity: 1;
            }
        }

        @keyframes zoomIn {
            0% {
                opacity: 0;
                -webkit-transform: scale3d(.3, .3, .3);
                transform: scale3d(.3, .3, .3);
            }

            50% {
                opacity: 1;
            }
        }
        .heartBeat{
            animation-name: heartBeat;
            -webkit-animation-name: heartBeat;

            animation-duration: 1.5s;
            -webkit-animation-duration: 1.5s;

            animation-iteration-count: infinite;
            -webkit-animation-iteration-count: infinite;
        }

        @keyframes heartBeat {
            0% {
                transform: scale(0.9);
                opacity: 0.7;
            }
            50% {
                transform: scale(1);
                opacity: 1;
            }
            100% {
                transform: scale(0.9);
                opacity: 0.7;
            }
        }

        @-webkit-keyframes heartBeat {
            0% {
                -webkit-transform: scale(0.95);
                opacity: 0.7;
            }
            50% {
                -webkit-transform: scale(1);
                opacity: 1;
            }
            100% {
                -webkit-transform: scale(0.95);
                opacity: 0.7;
            }
        }
        .rollInLeft {
            animation-name: rollInLeft;
            -webkit-animation-name: rollInLeft;

            animation-duration: 1s;
            -webkit-animation-duration: 1s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;
        }
        @-webkit-keyframes rollInLeft {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(-100%, 0, 0) rotate3d(0, 0, 1, -120deg);
                transform: translate3d(-100%, 0, 0) rotate3d(0, 0, 1, -120deg);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }

        @keyframes rollInLeft {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(-100%, 0, 0) rotate3d(0, 0, 1, -120deg);
                transform: translate3d(-100%, 0, 0) rotate3d(0, 0, 1, -120deg);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }
        .rollInRight {
            animation-name: rollInRight;
            -webkit-animation-name: rollInRight;

            animation-duration: 1s;
            -webkit-animation-duration: 1s;

            animation-timing-function: ease-in-out;
            -webkit-animation-timing-function: ease-in-out;
        }
        @-webkit-keyframes rollInRight {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(100%, 0, 0) rotate3d(0, 0, 1, 120deg);
                transform: translate3d(100%, 0, 0) rotate3d(0, 0, 1, 120deg);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }

        @keyframes rollInRight {
            0% {
                opacity: 0;
                -webkit-transform: translate3d(100%, 0, 0) rotate3d(0, 0, 1, 120deg);
                transform: translate3d(100%, 0, 0) rotate3d(0, 0, 1, 120deg);
            }

            100% {
                opacity: 1;
                -webkit-transform: none;
                transform: none;
            }
        }
        html, body {
            margin: 0;
            width: 100%;
            height: 100%;
            font-family: arial;
            text-align: center;
        }

        * {
            margin:0;
            padding: 0;
        }
        ul {
            list-style: none;
        }

        .item2 {
            background-color: #0067A6;
        }
        .item1 {
            background-color: white;
        }
        .item3 {
            background-color: #008972;
        }
        .item4 {
            background-color: #F5C564;
        }
        .item5 {
            background-color: #F2572D;
        }
        .item6 {
            background-color: #0067A6;
        }

        .item1 .part1 {
            position: absolute;
            top: 100px;
            left: 19%;
            text-align: center;
            width: 100px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item1 .part2 {
            position: absolute;
            bottom: 100px;
            right: 19%;
            text-align: center;
            width: 100px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item3 .part1 {
            position: absolute;
            top: 100px;
            left: 19%;
            text-align: center;
            width: 100px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item3 .part2 {
            position: absolute;
            bottom: 100px;
            right: 19%;
            text-align: center;
            width: 100px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item2 .part1 {
            position: absolute;
            top: 100px;
            right: 19%;
            text-align: center;
            width: 100px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item2 .part3 {
            position: absolute;
            bottom: 60px;
            left: 19%;
            text-align: center;
            width: 150px;
            height: 150px;
            background-color: rgb(116, 178, 9);
        }
        .item2 .part2 {
            position: absolute;
            top: 100px;
            left: 19%;
            text-align: center;
            width: 100px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item4 .part1 {
            position: absolute;
            top: 100px;
            left: 19%;
            text-align: center;
            width: 100px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item4 .part2 {
            position: absolute;
            bottom: 100px;
            right: 19%;
            text-align: center;
            width: 150px;
            height: 150px;
            background-color: rgb(116, 178, 9);
        }
        .item5 .part1 {
            position: absolute;
            bottom: 100px;
            right: 19%;
            text-align: center;
            width: 150px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item5 .part2 {
            position: absolute;
            top: 100px;
            left: 19%;
            text-align: center;
            width: 150px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }

        .item6 .part2 {
            position: absolute;
            top: 100px;
            left: 19%;
            text-align: center;
            width: 150px;
            height: 100px;
            background-color: rgb(116, 178, 9);
        }
        .item6 .part1 {
            position: absolute;
            bottom: 100px;
            left: 19%;
            text-align: center;
            width: 250px;
            height: 250px;
            background-color: rgb(116, 178, 9);
        }
        /*.item6 {
            background-image: url(7634.jpg);
            background-position: center;
        }*/
    </style>
    <script>
        function setCookie(name,value)
        {
            var Days = 30;
            var exp = new Date();
            exp.setTime(exp.getTime() + Days*24*60*60*1000);
            document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
        }


        (function(){
            setCookie("join","success");
            var opt = {
                'type' : 1,
                'pageShow' : function(){},
                'pageHide' : function(){},
                'useShakeDevice' : {
                    'speed' : 30,
                    'callback' : function(){}
                },
                'useParallax' : true,
                'useArrow' : true,
                'useAnimation' : true,
                'useMusic' : {
                    'autoPlay' : true,
                    'loopPlay' : true,
                    'src' : 'http://mat1.gtimg.com/news/2015/love/FadeAway.mp3'
                }
            };
            window.H5FullscreenPage = {
                'init' : function(option){
                    $.extend(opt,option);
                    initDom(opt);
                    initEvent(opt);
                }
            };

            var obj = {
                '1' : {
                    'upDrag' : function(percentage, item){
                        var translateY = 1 - 0.7*percentage;//位置系数，可以微调
                        item.next().css('-webkit-transform', 'translate3d(0,'+translateY*100+'%,0)'); //下一个item上移动
                    },
                    'downDrag' : function(percentage, item){
                        var translateY = -(0.7*percentage);
                        item.prev().css('-webkit-transform', 'translate3d(0,'+(translateY*100 - 100)+'%,0)');
                        item.css('-webkit-transform', 'translate3d(0,'+translateY*100+'%,0)');//当前item下移动
                    },
                    'nextSlide' : function(item){
                        item.css('-webkit-transform', 'translate3d(0,-100%,0)');
                        item.next().css('-webkit-transform', 'translate3d(0,0,0)');
                    },
                    'prevSlide' : function(item){
                        item.prev().css('-webkit-transform', 'scale(1)');
                        item.css('-webkit-transform', 'translate3d(0,100%,0)');
                    },
                    'showSlide' : function(item){
                        item.css('-webkit-transform', 'scale(1)');
                        item.next().css('-webkit-transform', 'translate3d(0,100%,0)');
                    }
                },
                '2' : {
                    'upDrag' : function(percentage, item){
                        var scale = 1 - 0.2*percentage;//缩放系数，可以微调
                        var translateY = 1 - 0.7*percentage;//位置系数，可以微调
                        item.css('-webkit-transform', 'scale('+scale+')');//当前item缩小
                        item.next().css('-webkit-transform', 'translate3d(0,'+translateY*100+'%,0)'); //下一个item上移动
                    },
                    'downDrag' : function(percentage, item){
                        var scale = 0.8 - 0.2*percentage;
                        var translateY = -(0.7*percentage);
                        item.css('-webkit-transform', 'translate3d(0,'+translateY*100+'%,0)');//当前item下移动
                        item.prev().css('-webkit-transform', 'scale('+scale+')');//前一个item放大
                    },
                    'nextSlide' : function(item){
                        item.css('-webkit-transform', 'scale(.8)');
                        item.next().css('-webkit-transform', 'translate3d(0,0,0)');
                    },
                    'prevSlide' : function(item){
                        item.prev().css('-webkit-transform', 'scale(1)');
                        item.css('-webkit-transform', 'translate3d(0,100%,0)');
                    },
                    'showSlide' : function(item){
                        item.css('-webkit-transform', 'scale(1)');
                        item.next().css('-webkit-transform', 'translate3d(0,100%,0)');
                    }
                },
                '3' : {
                    'upDrag' : function(percentage, item){
                        var translateY = 1 - 0.4*percentage;//位置系数，可以微调
                        item.css('-webkit-transform', 'translate3d(0,'+(translateY*100 - 100)+'%,0)');
                        item.next().css('-webkit-transform', 'translate3d(0,'+translateY*100+'%,0)'); //下一个item上移动
                    },
                    'downDrag' : function(percentage, item){
                        var translateY = -(0.4*percentage);
                        item.prev().css('-webkit-transform', 'translate3d(0,'+(translateY*100 - 100)+'%,0)');
                        item.css('-webkit-transform', 'translate3d(0,'+translateY*100+'%,0)');//当前item下移动
                    },
                    'nextSlide' : function(item){
                        item.css('-webkit-transform', 'translate3d(0,-100%,0)');
                        item.next().css('-webkit-transform', 'translate3d(0,0,0)');
                    },
                    'prevSlide' : function(item){
                        item.prev().css('-webkit-transform', 'scale(1)');
                        item.css('-webkit-transform', 'translate3d(0,100%,0)');
                    },
                    'showSlide' : function(item){
                        item.css('-webkit-transform', 'scale(1)');
                        item.next().css('-webkit-transform', 'translate3d(0,100%,0)');
                    }
                },
                '4' : {
                    'upDrag' : function(percentage, item){
                        var translateY = 1 - 0.4*percentage;//位置系数，可以微调
                        item.css('-webkit-transform', 'translate3d(0,'+(translateY*100 - 100)+'%,0)');
                        item.next().css('-webkit-transform', 'translate3d(0,'+translateY*100+'%,0)'); //下一个item上移动
                    },
                    'downDrag' : function(percentage, item){
                        var translateY = -(0.4*percentage);
                        item.prev().css('-webkit-transform', 'translate3d(0,'+(translateY*100 - 100)+'%,0)');
                        item.css('-webkit-transform', 'translate3d(0,'+translateY*100+'%,0)');//当前item下移动
                    },
                    'nextSlide' : function(item){
                        item.addClass('zindex');
                        setTimeout(function(){
                            item.removeClass('no-animation').css('-webkit-transform', 'translate3d(0,-100%,0)');
                            item.next().removeClass('zindex').addClass('no-animation').css('-webkit-transform', 'translate3d(0,0,0)');
                        },100);

                    },
                    'prevSlide' : function(item){

                        item.prev().css('-webkit-transform', 'translate3d(0,0,0)');
                        item.next().css('-webkit-transform', 'translate3d(0,100%,0)');
                        item.removeClass('zindex');
                    },
                    'showSlide' : function(item){
                        item.css('-webkit-transform', 'scale(1)');
                        item.next().css('-webkit-transform', 'translate3d(0,100%,0)');
                    }
                }
            };
            var dragThreshold = 0.15; //临界值
            var dragStart = null;//开始抓取标志位
            var percentage = 0;//拖动量的百分比
            var currentItem;

            function touchStart(event) {
                if (dragStart !== null) return;
                var item = $(event.target).closest('.item');
                if (!item.length) {
                    $('.overlay').hide();
                    return;
                }
                if (event.touches) {
                    event = event.touches[0];
                }
                //抓取时的所在位置
                dragStart = event.clientY;

                //分别关闭item的动画效果,动画效果只在松开抓取时出现
                item.addClass('no-animation');
                item.next().addClass('no-animation');
                item.prev().addClass('no-animation');

            }

            function touchMove (event) {
                //console.log(33);
                //防止ios拖动事件
                event.preventDefault();

                if (dragStart === null) return;
                var item = $(event.target).closest('.item');
                if (!item.length) {
                    $('.overlay').hide();
                    return;
                }
                if (event.touches) {
                    event = event.touches[0];
                }
                //得到抓取开始时于进行中的差值的百分比
                percentage = (dragStart - event.clientY) / window.screen.height;//

                if (percentage > 0) {
                    // //向上拖动
                    var scale = 1 - 0.5*percentage;//缩放系数，可以微调
                    // var translateY = 1 - 0.4*percentage;//位置系数，可以微调
                    // $(event.target).css('-webkit-transform', 'scale('+scale+')');//当前item缩小
                    // $(event.target).next().css('-webkit-transform', 'translateY('+translateY*100+'%)'); //下一个item上移动
                    //$(event.target).css('opacity', ''+scale+'');//当前item缩小
                    obj[opt.type].upDrag(percentage, item);

                } else if (item.prev()) {
                    //向下拖动
                    // var scale = 0.8 - 0.2*percentage;
                    // var translateY = -(0.4*percentage);
                    // $(event.target).css('-webkit-transform', 'translateY('+translateY*100+'%)');//当前item下移动
                    // $(event.target).prev().css('-webkit-transform', 'scale('+scale+')');//前一个item放大
                    obj[opt.type].downDrag(percentage, item);
                }

            }

            function touchEnd (event) {
                //防止多次滚动，故增加一个覆盖层
                $('.overlay').show();
                dragStart = null;
                var item = $(event.target).closest('.item');
                if (!item.length) {
                    $('.overlay').hide();
                    return;
                }
                item.removeClass('no-animation');
                item.next().removeClass('no-animation');
                item.prev().removeClass('no-animation');

                //抓取停止后，根据临界值做相应判断
                if (percentage >= dragThreshold) {
                    nextSlide(item);
                } else if ( Math.abs(percentage) >= dragThreshold ) {
                    prevSlide(item);
                } else {
                    showSlide(item);
                }
                //重置percentage
                percentage = 0;

            }
            function swipeUp(event){
                var item = $(event.target).closest('.item');
                if (!item.length) {
                    return;
                }
                nextSlide(item);
                //$(event.target).css('-webkit-transform', 'translateY(-101%)');
                //$(event.target).next().css('-webkit-transform', 'translateY(0)');
            }
            function swipeDown(event){
                var item = $(event.target).closest('.item');
                if (!item.length) {
                    return;
                }
                prevSlide(item);
                //$(event.target).css('-webkit-transform', 'translateY(101%)');
                //$(event.target).prev().css('-webkit-transform', 'translateY(0)');
            }
            function nextSlide(item){
                //$(event.target).removeClass('parallax-item');
                //恢复到原样，或者展示下一item
                if (item.next().length) {
                    item.attr('state','prev');
                    item.siblings('.item').removeAttr('state');

                    currentItem = item.next();
                    currentItem.attr('state','next');

                    orderPart(item.next());
                    obj[opt.type].nextSlide(item);
                } else {
                    obj[opt.type].showSlide(item);
                }

            }
            function prevSlide(item){
                //$(event.target).removeClass('parallax-item');
                if (item.prev().length) {

                    item.attr('state','prev');
                    item.siblings('.item').removeAttr('state');
                    currentItem = item.prev();
                    currentItem.attr('state','next');
                    obj[opt.type].prevSlide(item);
                } else {
                    obj[opt.type].showSlide(item);
                }

            }
            function showSlide(item){
                //$(event.target).removeClass('parallax-item');
                obj[opt.type].showSlide(item);
            }
            function initDom(opt){
                $('body').addClass('H5FullscreenPage');
                currentItem = $('.item').first();
                currentItem.attr('state','next');
                if (opt.useAnimation) {
                    var items = $('.item');
                    items.find('.part').addClass('hide');
                    orderPart(items.first());
                }
                $('body').append('<div class="overlay"></div>');
                if (opt.useArrow) {
                    $('.item').slice(0,$('.item').length-1).append('<span class="arrow"></span>');
                }
                if (opt.useMusic) {
                    var autoplay = opt.useMusic.autoPlay ? 'autoplay="autoplay"' : '';
                    var loopPlay = opt.useMusic.loopPlay ? 'loop="loop"' : '';
                    var src = opt.useMusic.src;
                    $('body').append('<span class="music play"><audio id="audio" src='+src+' '+autoplay+' '+loopPlay+'></audio></span>');
                }
            }
            function orderPart(dom){
                var parts = $(dom).find('.part');
                parts.forEach(function(item){
                    var time = $(item).attr('data-delay') || 100;
                    setTimeout(function(){
                        $(item).removeClass('hide');
                    },time);
                });
            }
            function initEvent(opt){
                if (opt.useParallax) {

                    var orginData = {x:0,y:0};
                    window.addEventListener('deviceorientation',function(event){
                        var gamma = event.gamma;
                        var beta = event.beta;

                        var x = -gamma;
                        var y = -beta;

                        if (Math.abs(Math.abs(x) - Math.abs(orginData.x)) < 0.1 || Math.abs(Math.abs(y) - Math.abs(orginData.y)) < 0.1) {
                            orginData.x = x;
                            orginData.y = y;
                            return;
                        } else {
                            orginData.x = x;
                            orginData.y = y;
                        }

                        var halfWidth = window.innerWidth / 2;
                        var halfHeight = window.innerHeight / 2;


                        var max = 5;
                        var items = $('.parallax');
                        items.forEach(function(item){
                            var dx = (item.getBoundingClientRect().width/max)*(x / halfWidth);
                            var dy = (item.getBoundingClientRect().width/max)*(y / halfHeight);

                            if ($(item).hasClass('item')) {
                                //$(item).addClass('parallax-item');
                                dx = -dx/1 + 50;
                                dy = -dy/1 + 50;
                                item.style['background-position'] = ''+dx+'% '+dy+'%';
                                //$(item).removeClass('parallax-item');
                            } else {
                                item.style['transform'] = item.style['-webkit-transform'] = 'translate3d('+dx+'px,'+dy+'px,0)';
                            }


                        });


                    }, false);
                }
                if (opt.useShakeDevice && opt.useShakeDevice.speed) {
                    var x = y = z = lastX = lastY = lastZ = 0;
                    if (window.DeviceMotionEvent) {
                        window.addEventListener('devicemotion',function(eventData){
                            var acceleration =event.accelerationIncludingGravity;
                            x = acceleration.x;
                            y = acceleration.y;
                            z = acceleration.z;
                            if(Math.abs(x-lastX) > opt.useShakeDevicespeed || Math.abs(y-lastY) > opt.useShakeDevicespeed || Math.abs(z-lastZ) > opt.useShakeDevicespeed) {
                                //shake
                                opt.useShakeDevice.callback && opt.useShakeDevice.callback(currentItem);

                            }
                            lastX = x;
                            lastY = y;
                            lastZ = z;
                        }, false);
                    }
                }
                $('.music').on('tap',function(){
                    $(this).toggleClass('play');
                    var audio = document.getElementById('audio');
                    if (audio.paused) {
                        audio.play();
                    } else {
                        audio.pause();
                    }
                });
                // 绑定事件
                $(document).on('touchmove', function(e){
                    e.preventDefault();
                });
                if (opt.type > 4) {
                    opt.type = opt.type - 4;
                    $('.item').on({
                        'swipeUp': swipeUp,
                        'swipeDown': swipeDown
                    });
                } else {
                    $('.item').on({
                        'touchstart': touchStart,
                        'touchmove': touchMove,
                        'touchend': touchEnd,
                        'touchcancel': touchEnd
                    });
                }

                $('.item').on('tap', function(){
                    //覆盖层隐藏
                    $('.overlay').hide();
                });
                $('.item').on('transitionend webkitTransitionEnd', function(event){
                    //覆盖层隐藏
                    $('.overlay').hide();
                    //console.log($(event.target).attr('state'));
                    if ($(event.target).attr('state') == 'next') {
                        opt.pageShow(event.target);
                    } else {
                        opt.pageHide(event.target);
                    }
                    // opt.pageComplete(event.target);
                    // debugger;
                });
                $('.overlay').on('tap', function(){
                    //覆盖层隐藏
                    $('.overlay').hide();
                });
            }

        })();
    </script>
</head>
<body class="H5FullscreenPage">
<div class="H5FullscreenPage-wrap">
    <div class="item item1">
        <br/>
        <br/>
        <br/> /
        <h1>(_)</h1><br/><br/><br/><br/><br/>
        <h4 style="font-family: '微软雅黑'"><span style="font-size:34px;">${uiLabel.Congratulations}</span><br/><br/><br/><h3>${uiLabel.JoinSuccess}</h3>
    </div>

    <div class="item item6" style="background-image:url('../images/background.jpg')">
        <br/>
        <h2 style="color: salmon;">${uiLabel.InstallApp}</h2><br/>
        <br/><h3>${uiLabel.AppDesc}</h3>
        <img class="part part1 rollInRight parallax" src="../images/1489557679.png"/>

    </div>
</div>
<script src="http://cdn.bootcss.com/zepto/1.0rc1/zepto.min.js"></script>

<script type="text/javascript">

    H5FullscreenPage.init({'type':2,'pageShow' : function(dom){

    },'pageHide' : function(dom){
        //console.log(dom);
    }});

</script>
</body>
</html>