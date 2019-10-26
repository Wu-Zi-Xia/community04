
// 回复按钮
function post(e) {
    var data=e;
    console.log(data);
    if("btn-question"==e.getAttribute("id")){
        var parent_id=$("#parent_id").val();
        var comment_content =$("#comment_content").val();
        if(!comment_content)
        {
            alert("不能回复空的内容~~~~~~");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/comment",
            data: JSON.stringify({
                "parentId":parent_id,
                "description":comment_content,
                "type":1
            }),
            // 请求成功之后返回的方法
            success: function (response) {
                if(response.code==200)
                {
                    $("#comment_section").hide();
                    // window.open("http://localhost:8886/question?id="+parent_id);重定向
                    window.location.reload();
                }
                if(response.code==2003){
                    var isLogin=confirm(response.message);
                    if(isLogin)
                    {
                        window.open("https://github.com/login/oauth/authorize?client_id=6709a76aab7b2b0068ba&http://localhost:8886/callback&scope=user&state=1");
                        // 主要是在没有进行登录的情况下进行评论需要先登录，这时登录之后主页面没有消除，设置这个变量是想让登录页面消除掉，在登录页面加载时，用js判断是否有这个变量
                        window.localStorage.setItem("closeable","yes");
                    }
                }
                else {
                    alert(response.message);
                }
                console.log(response);
            },
            //返回的内容的格式
            dataType: "json",
            //请求的内容的格式
            contentType:"application/json"
        });
        console.log(parent_id);
        console.log(comment_content);
    }else {
        var parent_comment_id=e.getAttribute("data-id");
        var comment_comment_content=$("#comment_comment_content").val();
        alert(parent_comment_id);
        console.log(comment_comment_content);
        if (!comment_comment_content){
            alert("评论不能为空内容哦~~~");
            return;
        }
        $.ajax({
            type:"post",
            url:"/comment",
            data:JSON.stringify({
                "parentId":parent_comment_id,
                "description":comment_comment_content,
                "type":2
            }),
            success:function(response){
                alert(response.message);
            },
            dataType:"json",
            contentType:"application/json"

        });
    }

}
// 显示二级评论的按钮方法
function collapseComments(e){
    console.log(e);
    this.e=e;
    var data=e.getAttribute("data-id");
    var comments=$("#comment-"+data);//获取到那个div
    comments.addClass("subComment");
    // 给获取到的页面元素添加一个样式

    if(e.getAttribute("data-collapse"))
    {
        // 消除二级评论
        comments.removeClass("in");
        // 消除二级评论展开标志
        e.removeAttribute("data-collapse");
        e.classList.remove("blueColor");
    }else {

        if(comments.children().length!=1){//判断当前的元素有多少子元素，在那个里面已经有了一个评论的回复框的元素，主要是不用再发一次请求
            comments.addClass("in");
            // 标记二级评论是展开的
            e.setAttribute("data-collapse", "in");
            e.classList.add("blueColor");
        }else{
            $.getJSON("/comment/" + data, function (data1) {
                console.log(data1);
                if(data1.data==null)//证明当前评论没有子评论
                {
                    comments.addClass("in");
                    // 标记二级评论是展开的
                    e.setAttribute("data-collapse", "in");
                    e.classList.add("blueColor");
                }
                else{
                    //  var subCommentContainer = $("#comment-",data);
                    $.each(data1.data.reverse(), function (index, comment) {//数组顺序倒序
                        console.log(comment);

                        var avatarUrlElement = $("<img/>", {
                            "class": "media-object img-rounded",
                            src:comment.user.avatarUrl
                        });

                        var mediaLeftElement = $("<div/>", {
                            "class": "media-left",
                        }).append(avatarUrlElement);

                        var mediaBodyElement = $("<div/>", {
                            "class": "media-body",
                        }).append($("<h5/>",{
                            "class":"media-heading",
                            html:comment.user.name
                        })).append($("<div/>",{
                            html:comment.description
                        })).append($("<span/>"),{
                            "class":"pull-right",
                            html:moment(comment.gmtCreate).format('YYYY-MM-DD')//我这里传过来的时间是空的，后台哪里出了问题
                        });

                        var mediaElement = $("<div/>", {
                            "class": "media",
                        }).append(mediaLeftElement).
                        append(mediaBodyElement);

                        var commentElement = $("<div/>", {
                            "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                        }).append(mediaElement);
                        comments.prepend(commentElement);
                        //append往元素里面追加子元素
                    });
                    comments.addClass("in");
                    // 标记二级评论是展开的
                    e.setAttribute("data-collapse", "in");
                    e.classList.add("blueColor");
                }

            });
        }

    }
}

function selectTag(e) {
     var value=e.getAttribute("data")+'，';
    var previous=$("#tag").val();
    if(previous.indexOf(value)!=-1){//判断输入框内的内容是否存在当前点击之后传过来的内容，包含的话就不再显示
        return;
    }
    else {
        if(previous){
            $("#tag").val(previous+value);
        }else {
            $("#tag").val(value);
        }
    }
}


function showSelectTag(){
    $("#selectTags").show();
}