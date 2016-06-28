//加载图片的路径
function getLoadImgPath(filePath){
	return loadImgPath = main.controllers.base.Assets.at(filePath).url;
}

//锚点缓动开始
var ss = {
	fixAllLinks: function() {
	var allLinks = document.getElementsByTagName('a');
		for (var i=0;i<allLinks.length;i++) {
			var lnk = allLinks[i];
			if ((lnk.href && lnk.href.indexOf('#') != -1) &&
			( (lnk.pathname == location.pathname) ||
			('/'+lnk.pathname == location.pathname) ) &&
			(lnk.search == location.search)) {
			ss.addEvent(lnk,'click',ss.smoothScroll);
			}
		}
	},
	smoothScroll: function(e) {
	if (window.event) {
		target = window.event.srcElement;
	} else if (e) {
		target = e.target;
	} else return;
	if (target.nodeName.toLowerCase() != 'a') {
		target = target.parentNode;
	}
	if (target.nodeName.toLowerCase() != 'a') return;
	anchor = target.hash.substr(1);
	var allLinks = document.getElementsByTagName('a');
	var destinationLink = null;
	for (var i=0;i<allLinks.length;i++) {
		var lnk = allLinks[i];
		if (lnk.name && (lnk.name == anchor)) {
		destinationLink = lnk;
		break;
		}
	}
	if (!destinationLink) destinationLink = document.getElementById(anchor);
	if (!destinationLink) return true;
	var destx = destinationLink.offsetLeft;
	var desty = destinationLink.offsetTop;
	var thisNode = destinationLink;
	while (thisNode.offsetParent &&(thisNode.offsetParent != document.body)) {
		thisNode = thisNode.offsetParent;
		destx += thisNode.offsetLeft;
		desty += thisNode.offsetTop;
	}
	clearInterval(ss.INTERVAL);
	cypos = ss.getCurrentYPos();
	ss_stepsize = parseInt((desty-cypos)/ss.STEPS);
	ss.INTERVAL=setInterval('ss.scrollWindow('+ss_stepsize+','+desty+',"'+anchor+'")',10);
	if (window.event) {
		window.event.cancelBubble = true;
		window.event.returnValue = false;
	}
	if (e && e.preventDefault && e.stopPropagation) {
		e.preventDefault();
		e.stopPropagation();
	}
},
scrollWindow: function(scramount,dest,anchor) {
wascypos = ss.getCurrentYPos();
isAbove = (wascypos < dest);
window.scrollTo(0,wascypos + scramount);
iscypos = ss.getCurrentYPos();
isAboveNow = (iscypos < dest);
if ((isAbove != isAboveNow) || (wascypos == iscypos)) {
window.scrollTo(0,dest);
clearInterval(ss.INTERVAL);
location.hash = anchor;
}
},
getCurrentYPos: function() {
if (document.body && document.body.scrollTop)
return document.body.scrollTop;
if (document.documentElement && document.documentElement.scrollTop)
return document.documentElement.scrollTop;
if (window.pageYOffset)
return window.pageYOffset;
return 0;
},
addEvent: function(elm, evType, fn, useCapture) {
if (elm.addEventListener){
elm.addEventListener(evType, fn, useCapture);
return true;
} else if (elm.attachEvent){
var r = elm.attachEvent("on"+evType, fn);
return r;
} else {
alert("Handler could not be removed");
}
}
}
ss.STEPS = 30;  //设置滑动速度
ss.addEvent(window,"load",ss.fixAllLinks);

//锚点缓动结束


//产品详情页随着鼠标滚动选项栏固定不动
function slideInit() {
        var obj = $('#top_side');
        if (obj.length !== 0) {
            var offset = obj.offset();
            var topOffset = offset.top;
            var leftOffset = offset.left;
            var marginTop = obj.css("marginTop");
            var marginLeft = obj.css("marginLeft");

            $(window).scroll(function() {
                var scrollTop = $(window).scrollTop();
                if (scrollTop >= topOffset) {
                    obj.css({
						top: 0,
						left: 0,
                        position: 'fixed',
                        zIndex: 1
                    });	
				
                }
                if (scrollTop < topOffset) {
                    obj.css({
                        position: 'relative'
                    });
                }
            });
        }
    }

function navslideInit() {
		var nav = $('#nav_side');		
		var none = $('.pro_none');
        if (nav.length !== 0) {
			var offset = nav.offset();
            var topOffset = offset.top;
            var leftOffset = offset.left;
            var marginTop = nav.css("marginTop");
            var marginLeft = nav.css("marginLeft");

            $(window).scroll(function() {
                var scrollTop = $(window).scrollTop();
                if (scrollTop >= topOffset) {

					nav.css({
						top: 0,
						left:0 ,
                        position: 'fixed',
                        zIndex: 1 ,
						width: '100%',
						marginLeft:'-159px',
						background: '#fff'
                    });
					none.css({
                        height: '66px'
                    });	
				
                }
                if (scrollTop < topOffset) {

					nav.css({
                        position: 'relative',
						width: 'auto',
						marginLeft:0,
						background: 'none'
                    });
					none.css({
                        height: 'auto'
                    });
                }
            });
        }
    }
	
$(function() {
    slideInit();
	navslideInit();
	init_Ckeditor();
});
	
$(document).ready(function() {
	        $('.flexslider').flexslider({
    	        animation: 'fade',
        	    controlsContainer: '.flexslider'
        	});
	        /*	$("img").lazyload({
                effect : "fadeIn"
            });  */
			$("#menu-btn,.dropdown .btn,.panel-heading").click(function() {
            	$(this).next(".top_menu,.dropdown .menu,.collapse").slideToggle(500);
        	});
			$('#menu_li').hover(function() {
				$(this).children(".sub_menu_ul").fadeToggle(400);
			});
			$(".tab_ul .first").click(function(){
				$(this).addClass("selected").parent('li').siblings().children('a').removeClass("selected");
				$(this).parents('.pro_wrapper').find('#first').show().siblings().hide();
				$(this).parent('li').siblings().children('.youLike').attr("href","#carousel");
			});	
			$(".tab_ul .youLike").click(function(){
				$(this).addClass("selected").parent('li').siblings().children('a').removeClass("selected");
				$(this).parents('.pro_wrapper').find('#first').show().siblings().hide();
				$(this).attr("href","#carousel");
			});	
			$(".tab_ul .second").click(function(){
				$(this).addClass("selected").parent('li').siblings().children('a').removeClass("selected");
				$(this).parents('.pro_wrapper').find('#second').show().siblings().hide();
				$(this).parent('li').siblings().children('.youLike').attr("href","javascript:;");
			});	
			$(".tab_ul .third").click(function(){
				$(this).addClass("selected").parent('li').siblings().children('a').removeClass("selected");
				$(this).parents('.pro_wrapper').find('#third').show().siblings().hide();
				$(this).parent('li').siblings().children('.youLike').attr("href","javascript:;");
			});	
			$(".tab_ul .fiveth").click(function(){
				$(this).addClass("selected").parent('li').siblings().children('a').removeClass("selected");
				$(this).parents('.pro_wrapper').find('#fiveth').show().siblings().hide();
				$(this).parent('li').siblings().children('.youLike').attr("href","javascript:;");
			});	
			$(".list-tab li,.down_center li,.tech_support .tab_ul li").click(function(){
				$(this).addClass("active").siblings().removeClass("active");
				var index = $('.tech_support .tab_ul li').index(this);
				$('.tech_box .supportDesWrapper').eq(index).show().siblings().hide();
			});	
			$(".glyphicon").click(function(){
				$(this).children(".deletePop").toggle();
			});
			$(".btn_yes").click(function(){
				var index = $('.btn_yes').index(this);
				$('#tableBody tr').eq(index).css('display','none');
			});
		
			
});


//首页弹出框
$(document).ready(function(){
	
	$(".pass_btn,.email_btn").click(function(){
		$(this).parents('body').find(".modal-backdrop").show();
		$(this).parents('body').find(".change_modal,.email_modal").fadeIn(800);	
	});
	$(".file_btn").click(function(){
		$("#show_error").html("");
		$(this).parents('body').find(".modal-backdrop").show();
		$(this).parents('body').find(".clickPop").fadeIn(800);	
	});
	$(".close,.closePop").click(function(){
		$(this).parents("#boxModal,.clickPop").css("display","none");
		$(".modal-backdrop").hide();
	});
});


//返回顶部按钮
var scrolltotop={
	//startline: Integer. Number of pixels from top of doc scrollbar is scrolled before showing control
	//scrollto: Keyword (Integer, or "Scroll_to_Element_ID"). How far to scroll document up when control is clicked on (0=top).
	setting: {startline:1, scrollto: 0, scrollduration:1000, fadeduration:[500, 100]},
	controlHTML: '<img src="' + getLoadImgPath("images/gotop.png") + '"style="width:50px; height:50px; z-index:1" />', //HTML for control, which is auto wrapped in DIV w/ ID="topcontrol"
	controlattrs: {offsetx:25, offsety:25}, //offset of control relative to right/ bottom of window corner
	anchorkeyword: '#top', //Enter href value of HTML anchors on the page that should also act as "Scroll Up" links

	state: {isvisible:false, shouldvisible:false},

	scrollup:function(){
		if (!this.cssfixedsupport) //if control is positioned using JavaScript
			this.$control.css({opacity:0}) //hide control immediately after clicking it
		var dest=isNaN(this.setting.scrollto)? this.setting.scrollto : parseInt(this.setting.scrollto)
		if (typeof dest=="string" && jQuery('#'+dest).length==1) //check element set by string exists
			dest=jQuery('#'+dest).offset().top
		else
			dest=0
		this.$body.animate({scrollTop: dest}, this.setting.scrollduration);
	},

	keepfixed:function(){
		var $window=jQuery(window)
		var controlx=$window.scrollLeft() + $window.width() - this.$control.width() - this.controlattrs.offsetx
		var controly=$window.scrollTop() + $window.height() - this.$control.height() - this.controlattrs.offsety
		this.$control.css({left:controlx+'px', top:controly+'px'})
	},

	togglecontrol:function(){
		var scrolltop=jQuery(window).scrollTop()
		if (!this.cssfixedsupport)
			this.keepfixed()
		this.state.shouldvisible=(scrolltop>=this.setting.startline)? true : false
		if (this.state.shouldvisible && !this.state.isvisible){
			this.$control.stop().animate({opacity:1}, this.setting.fadeduration[0])
			this.state.isvisible=true
		}
		else if (this.state.shouldvisible==false && this.state.isvisible){
			this.$control.stop().animate({opacity:0}, this.setting.fadeduration[1])
			this.state.isvisible=false
		}
	},
	
	init:function(){
		jQuery(document).ready(function($){
			var mainobj=scrolltotop
			var iebrws=document.all
			mainobj.cssfixedsupport=!iebrws || iebrws && document.compatMode=="CSS1Compat" && window.XMLHttpRequest //not IE or IE7+ browsers in standards mode
			mainobj.$body=(window.opera)? (document.compatMode=="CSS1Compat"? $('html') : $('body')) : $('html,body')
			mainobj.$control=$('<div id="topcontrol">'+mainobj.controlHTML+'</div>')
				.css({position:mainobj.cssfixedsupport? 'fixed' : 'absolute', bottom:mainobj.controlattrs.offsety, right:mainobj.controlattrs.offsetx, opacity:0, cursor:'pointer'})
				.attr({title:'Scroll Back to Top'})
				.click(function(){mainobj.scrollup(); return false})
				.appendTo('body')
			if (document.all && !window.XMLHttpRequest && mainobj.$control.text()!='') //loose check for IE6 and below, plus whether control contains any text
				mainobj.$control.css({width:mainobj.$control.width()}) //IE6- seems to require an explicit width on a DIV containing text
			mainobj.togglecontrol()
			$('a[href="' + mainobj.anchorkeyword +'"]').click(function(){
				mainobj.scrollup()
				return false
			})
			$(window).bind('scroll resize', function(e){
				mainobj.togglecontrol()
			})
		})
	}
}

scrolltotop.init()


//搜索页面不断加载
/*
$(document).ready(function(){

	$(window).bind("scroll", function(){
		$(".search_pro_list").each(function(){
			var fold = $(window).height() + $(window).scrollTop();
			if(fold >= $('#loadingbox').offset().top){
				var t=this;
				$.get("search.html",function(html){
					$(html).insertBefore("#loadingbox")
				});
			}
		});
	});
	
});
*/

$(document).ready(function(){
	$(".color-bg").mouseenter(function(){
		var imgurl = $(this).data('imgurl');
		var url = $(this).data('url');
		var title = $(this).data('title');
		$(this).parent().prev(".pro_img").children(".pro-info-a").attr("href", url);
		$(this).parent().prev(".pro_img").children(".pro-info-a").children(".pro-info-img").attr("src", imgurl);
		$(this).parent().siblings(".pro_info").children(".pro-info-title").attr("href", url);
		$(this).parent().siblings(".pro_info").children(".pro-info-title").text(title);
	});
});

$(function(){
	  var i = 5;  //已知显示的<a>元素的个数
	  var m = 5;  //用于计算的变量
      var $content = $(".special_lt .showArea");
	  var count = $content.find("a").length;//总共的<a>元素的个数
	  //下一张
	  $(".next").live("click",function(){
			var $scrollableDiv = $(this).siblings(".items").find(".showArea");
			if( !$scrollableDiv.is(":animated")){  //判断元素是否正处于动画，如果不处于动画状态，则追加动画。
				if(m<count){  //判断 i 是否小于总的个数
					m++;
					$scrollableDiv.animate({left: "-=80px"}, 600);
				}
			}
			return false;
	  });
	   //上一张
	  $(".prev").live("click",function(){
			var $scrollableDiv = $(this).siblings(".items").find(".showArea");
			if( !$scrollableDiv.is(":animated")){
				if(m>i){ //判断 i 是否小于总的个数
					m--;
					$scrollableDiv.animate({left: "+=80px"}, 600);
				}
			}
			return false;
	  });
	});	
	
//初始化编辑框
function init_Ckeditor(){
	var hash = location.hash;
	var hashArray = ['#dp', '#faq', '#manuals', '#see_also', '#reviews'];
	if(hash.length > 1 && $.inArray(hash, hashArray) > -1){
		hash = hash.slice(1);
	}
    try {
       $("textarea[name=textarea_content]").ckeditor(
         {
            toolbar: [
                {name: 'font', items : [ 'FontSize', 'TextColor', 'BGColor', 'Bold', 'Italic' ]},
                {name: 'align', items : [ 'JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock' ]}
            ]
        });
    } catch(e) {
        console.log(e);
    }
}

